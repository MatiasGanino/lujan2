package com.psmsf.lujanapp.web.rest;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.psmsf.lujanapp.repository.PeregrinosRepository;
import com.psmsf.lujanapp.domain.Peregrinos;
import com.psmsf.lujanapp.domain.enumeration.TipoFormaPago;
import com.psmsf.lujanapp.domain.enumeration.TipoPersona;
import com.psmsf.lujanapp.domain.enumeration.TipoSalida;
import com.psmsf.lujanapp.service.MercadoPagoService;
import com.psmsf.lujanapp.service.dto.IngresoPublicoDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/public/peregrinos")
public class PublicPeregrinosResource {

    private static final Logger log = LoggerFactory.getLogger(PublicPeregrinosResource.class);

    private final MercadoPagoService mercadoPagoService;
    private final PeregrinosRepository peregrinosRepository;

    public PublicPeregrinosResource(
        MercadoPagoService mercadoPagoService,
        PeregrinosRepository peregrinosRepository
    ) {
        this.mercadoPagoService = mercadoPagoService;
        this.peregrinosRepository = peregrinosRepository;
    }

    // -------------------------------------------------------------------------
    // PASO 1: El frontend pide una preferencia de pago
    // -------------------------------------------------------------------------

    @PostMapping("/iniciar-pago")
    public ResponseEntity<?> iniciarPago(@Valid @RequestBody IngresoPublicoDTO dto) {
        log.info("Iniciando pago para {} {}", dto.getNombre(), dto.getApellido());
        try {
            Preference preference = mercadoPagoService.crearPreferencia(dto);
            return ResponseEntity.ok(Map.of(
                "id", preference.getId(),
                "init_point", preference.getInitPoint(),
                "sandbox_init_point", preference.getSandboxInitPoint()
            ));
        } catch (MPException | MPApiException e) {
            log.error("Error al crear preferencia MP: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "No se pudo conectar con MercadoPago: " + e.getMessage()));
        }
    }

    // -------------------------------------------------------------------------
    // PASO 2a: Webhook — MP notifica el resultado del pago (POST asíncrono)
    // Configurá esta URL en el Dashboard de MP:
    //   https://tudominio.com/api/public/peregrinos/webhook
    // -------------------------------------------------------------------------

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(
        @RequestBody Map<String, Object> body,
        @RequestHeader(value = "x-signature", required = false) String signature
    ) {
        log.info("Webhook MP recibido: {}", body);

        String topic = (String) body.get("topic");
        String type  = (String) body.get("type");

        // MP puede enviar notificaciones de distintos tipos; nos interesan los pagos
        if ("payment".equals(topic) || "payment".equals(type)) {
            Object dataObj = body.get("data");
            if (dataObj instanceof Map<?,?> data) {
                String paymentIdStr = String.valueOf(data.get("id"));
                try {
                    Long paymentId = Long.parseLong(paymentIdStr);
                    Payment payment = mercadoPagoService.consultarPago(paymentId);

                    if ("approved".equals(payment.getStatus())) {
                        log.info("Pago aprobado vía webhook: paymentId={} externalRef={}",
                            paymentId, payment.getExternalReference());
                        registrarPeregrinoDesdeReferencia(payment);
                    } else {
                        log.info("Pago con estado '{}': paymentId={}", payment.getStatus(), paymentId);
                    }
                } catch (Exception e) {
                    log.error("Error procesando webhook MP paymentId={}: {}", paymentIdStr, e.getMessage());
                    // Devolvemos 200 igual para que MP no reintente indefinidamente
                }
            }
        }

        // MP espera siempre un 200 OK
        return ResponseEntity.ok().build();
    }

    // -------------------------------------------------------------------------
    // PASO 2b: URL de retorno — el usuario vuelve desde el checkout de MP
    // El frontend llama a este endpoint con el payment_id que MP agrega en la URL
    // Endpoint: GET /api/public/peregrinos/confirmar?payment_id=XXX&status=approved
    // -------------------------------------------------------------------------

    @GetMapping("/confirmar")
    public ResponseEntity<?> confirmarPago(
        @RequestParam("payment_id") Long paymentId,
        @RequestParam("status") String status,
        @RequestParam(value = "external_reference", required = false) String externalReference
    ) {
        log.info("Retorno de MP: paymentId={} status={} ref={}", paymentId, status, externalReference);

        if (!"approved".equals(status)) {
            return ResponseEntity.ok(Map.of("resultado", status));
        }

        try {
            // Verificamos el estado real contra la API de MP (nunca confiar solo en el param de URL)
            Payment payment = mercadoPagoService.consultarPago(paymentId);

            if ("approved".equals(payment.getStatus())) {
                // Registrar si todavía no existe (el webhook puede haber llegado primero)
                boolean yaExiste = peregrinosRepository
                    .findByPaymentId(String.valueOf(paymentId))
                    .isPresent();

                if (!yaExiste) {
                    registrarPeregrinoDesdeReferencia(payment);
                }
                return ResponseEntity.ok(Map.of("resultado", "approved"));
            } else {
                return ResponseEntity.ok(Map.of("resultado", payment.getStatus()));
            }
        } catch (Exception e) {
            log.error("Error al verificar pago {}: {}", paymentId, e.getMessage());
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "No se pudo verificar el pago"));
        }
    }

    // -------------------------------------------------------------------------
    // Lógica interna: grabar el peregrino en la BD
    // -------------------------------------------------------------------------

    /**
     * Crea y guarda el peregrino a partir de los datos del pago aprobado.
     * NOTA: Los datos completos del formulario están en el externalReference (clave compuesta).
     * Para producción se recomienda guardar el DTO en caché (Redis/DB temporal) usando
     * el preferenceId como clave, y recuperarlo acá.
     */
    private void registrarPeregrinoDesdeReferencia(Payment payment) {
        // externalReference = "APELLIDO_NOMBRE_DNI" (definido en MercadoPagoService)
        String ref = payment.getExternalReference();
        if (ref == null) return;

        // Verificar idempotencia por paymentId
        if (peregrinosRepository.findByPaymentId(String.valueOf(payment.getId())).isPresent()) {
            log.info("Peregrino ya registrado para paymentId={}", payment.getId());
            return;
        }

        String[] partes = ref.split("_", 3);
        String apellido  = partes.length > 0 ? partes[0] : "DESCONOCIDO";
        String nombre    = partes.length > 1 ? partes[1] : "DESCONOCIDO";
        String documento = partes.length > 2 ? partes[2] : null;

        Peregrinos peregrino = new Peregrinos();
        peregrino.setApellido(apellido);
        peregrino.setNombre(nombre);
        peregrino.setNumeroDocumento("SINDNI".equals(documento) ? null : documento);
        peregrino.setPago(payment.getTransactionAmount() != null
            ? payment.getTransactionAmount().intValue() : null);
        peregrino.setFechaInscripcion(Instant.now());

        // Los campos de enum y el resto de los datos del formulario se recuperarían
        // del caché/preferencia. Acá dejamos valores por defecto para el ejemplo:
        peregrino.setMayorMenor(TipoPersona.MAYOR);
        peregrino.setSalida(TipoSalida.values()[0]);
        peregrino.setFormaPago(TipoFormaPago.TRANSFERENCIA);
        peregrino.setPaymentId(String.valueOf(payment.getId()));

        peregrinosRepository.save(peregrino);
        log.info("Peregrino registrado: {} {} paymentId={}", nombre, apellido, payment.getId());
    }
}
