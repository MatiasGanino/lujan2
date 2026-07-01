package com.psmsf.lujanapp.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.psmsf.lujanapp.config.MercadoPagoProperties;
import com.psmsf.lujanapp.service.dto.IngresoPublicoDTO;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MercadoPagoService {

    private static final Logger log = LoggerFactory.getLogger(MercadoPagoService.class);

    // Monto de la inscripción — podés moverlo a properties si varía
    private static final BigDecimal MONTO_INSCRIPCION = new BigDecimal("5000");
    private static final String TITULO_ITEM = "Inscripción Peregrinación";

    private final MercadoPagoProperties mpProperties;

    public MercadoPagoService(MercadoPagoProperties mpProperties) {
        this.mpProperties = mpProperties;
    }

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(mpProperties.getAccessToken());
    }

    /**
     * Crea una preferencia de pago en MercadoPago para el peregrino.
     * Devuelve la Preference con su id y los init_point (URLs de checkout).
     */
    public Preference crearPreferencia(IngresoPublicoDTO dto) throws MPException, MPApiException {

        PreferenceItemRequest item = PreferenceItemRequest.builder()
            .title(TITULO_ITEM)
            .quantity(1)
            .unitPrice(MONTO_INSCRIPCION)
            // id externo para poder asociar el pago al peregrino en el webhook
            .id("PEREGRINO-" + dto.getApellido().toUpperCase() + "-" + dto.getNumeroDocumento())
            .build();

        PreferencePayerRequest payer = PreferencePayerRequest.builder()
            .name(dto.getNombre())
            .surname(dto.getApellido())
            .email(dto.getEmail())
            .build();

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
            .success(mpProperties.getBackUrlSuccess())
            .failure(mpProperties.getBackUrlFailure())
            .pending(mpProperties.getBackUrlPending())
            .build();

        PreferenceRequest request = PreferenceRequest.builder()
            .items(List.of(item))
            .payer(payer)
            .backUrls(backUrls)
            .autoReturn("approved")          // redirige automáticamente al aprobar
            // externalReference: se devuelve en el webhook y en la URL de retorno
            .externalReference(buildExternalReference(dto))
            .notificationUrl(null)           // se configura en MP Dashboard o acá si tenés URL pública
            .build();

        PreferenceClient client = new PreferenceClient();
        try {
            Preference preference = client.create(request);
            log.info("Preferencia MP creada: id={} para {} {}", preference.getId(), dto.getNombre(), dto.getApellido());
            return preference;
        } catch (MPApiException e) {
            log.error("MP API error - status: {} body: {}",
                e.getApiResponse().getStatusCode(),
                e.getApiResponse().getContent());   // <-- acá está el mensaje real
            throw e;
        } catch (MPException e) {
            log.error("MP error: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Consulta el estado de un pago por su payment_id (devuelto por MP en la URL de retorno).
     */
    public Payment consultarPago(Long paymentId) throws MPException, MPApiException {
        PaymentClient client = new PaymentClient();
        return client.get(paymentId);
    }

    /**
     * Clave externa que identifica al peregrino en el flujo de pago.
     * Se recibe tanto en el webhook como en la URL de retorno.
     */
    private String buildExternalReference(IngresoPublicoDTO dto) {
        return dto.getApellido().toUpperCase()
            + "_" + dto.getNombre().toUpperCase()
            + "_" + (dto.getNumeroDocumento() != null ? dto.getNumeroDocumento() : "SINDNI");
    }
}
