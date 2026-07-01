package com.psmsf.lujanapp.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class IngresoPublicoDTO {

    @NotBlank
    private String apellido;

    @NotBlank
    private String nombre;

    private String numeroDocumento;
    private String telefono;
    private String email;

    @NotNull
    private String mayorMenor;   // valor del enum TipoPersona

    @NotNull
    private String salida;       // valor del enum TipoSalida

    @NotNull
    private String formaPago;    // valor del enum TipoFormaPago

    private String aclaraciones;
    private String contactoEmergencia;
    private String telefonoEmergencia;

    // Getters y setters

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMayorMenor() { return mayorMenor; }
    public void setMayorMenor(String mayorMenor) { this.mayorMenor = mayorMenor; }

    public String getSalida() { return salida; }
    public void setSalida(String salida) { this.salida = salida; }

    public String getFormaPago() { return formaPago; }
    public void setFormaPago(String formaPago) { this.formaPago = formaPago; }

    public String getAclaraciones() { return aclaraciones; }
    public void setAclaraciones(String aclaraciones) { this.aclaraciones = aclaraciones; }

    public String getContactoEmergencia() { return contactoEmergencia; }
    public void setContactoEmergencia(String contactoEmergencia) { this.contactoEmergencia = contactoEmergencia; }

    public String getTelefonoEmergencia() { return telefonoEmergencia; }
    public void setTelefonoEmergencia(String telefonoEmergencia) { this.telefonoEmergencia = telefonoEmergencia; }
}
