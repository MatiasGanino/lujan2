package com.psmsf.lujanapp.service.dto;

import com.psmsf.lujanapp.domain.enumeration.TipoFormaPago;
import com.psmsf.lujanapp.domain.enumeration.TipoPersona;
import com.psmsf.lujanapp.domain.enumeration.TipoSalida;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.psmsf.lujanapp.domain.Peregrinos} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PeregrinosDTO implements Serializable {

    private Long id;

    private Integer numeroEspecial;

    @NotNull
    private String apellido;

    @NotNull
    private String nombre;

    private String numeroDocumento;

    private String telefono;

    @NotNull
    private TipoPersona mayorMenor;

    @NotNull
    private TipoSalida salida;

    private Integer pago;

    @NotNull
    private TipoFormaPago formaPago;

    private String aclaraciones;

    private Boolean completoFormulario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroEspecial() {
        return numeroEspecial;
    }

    public void setNumeroEspecial(Integer numeroEspecial) {
        this.numeroEspecial = numeroEspecial;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public TipoPersona getMayorMenor() {
        return mayorMenor;
    }

    public void setMayorMenor(TipoPersona mayorMenor) {
        this.mayorMenor = mayorMenor;
    }

    public TipoSalida getSalida() {
        return salida;
    }

    public void setSalida(TipoSalida salida) {
        this.salida = salida;
    }

    public Integer getPago() {
        return pago;
    }

    public void setPago(Integer pago) {
        this.pago = pago;
    }

    public TipoFormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(TipoFormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public String getAclaraciones() {
        return aclaraciones;
    }

    public void setAclaraciones(String aclaraciones) {
        this.aclaraciones = aclaraciones;
    }

    public Boolean getCompletoFormulario() {
        return completoFormulario;
    }

    public void setCompletoFormulario(Boolean completoFormulario) {
        this.completoFormulario = completoFormulario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeregrinosDTO)) {
            return false;
        }

        PeregrinosDTO peregrinosDTO = (PeregrinosDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, peregrinosDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeregrinosDTO{" +
            "id=" + getId() +
            ", numeroEspecial=" + getNumeroEspecial() +
            ", apellido='" + getApellido() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", numeroDocumento='" + getNumeroDocumento() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", mayorMenor='" + getMayorMenor() + "'" +
            ", salida='" + getSalida() + "'" +
            ", pago=" + getPago() +
            ", formaPago='" + getFormaPago() + "'" +
            ", aclaraciones='" + getAclaraciones() + "'" +
            ", completoFormulario='" + getCompletoFormulario() + "'" +
            "}";
    }
}
