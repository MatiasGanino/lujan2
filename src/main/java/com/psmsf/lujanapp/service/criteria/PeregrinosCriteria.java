package com.psmsf.lujanapp.service.criteria;

import com.psmsf.lujanapp.domain.enumeration.TipoFormaPago;
import com.psmsf.lujanapp.domain.enumeration.TipoPersona;
import com.psmsf.lujanapp.domain.enumeration.TipoSalida;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.psmsf.lujanapp.domain.Peregrinos} entity. This class is used
 * in {@link com.psmsf.lujanapp.web.rest.PeregrinosResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /peregrinos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PeregrinosCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoPersona
     */
    public static class TipoPersonaFilter extends Filter<TipoPersona> {

        public TipoPersonaFilter() {}

        public TipoPersonaFilter(TipoPersonaFilter filter) {
            super(filter);
        }

        @Override
        public TipoPersonaFilter copy() {
            return new TipoPersonaFilter(this);
        }
    }

    /**
     * Class for filtering TipoSalida
     */
    public static class TipoSalidaFilter extends Filter<TipoSalida> {

        public TipoSalidaFilter() {}

        public TipoSalidaFilter(TipoSalidaFilter filter) {
            super(filter);
        }

        @Override
        public TipoSalidaFilter copy() {
            return new TipoSalidaFilter(this);
        }
    }

    /**
     * Class for filtering TipoFormaPago
     */
    public static class TipoFormaPagoFilter extends Filter<TipoFormaPago> {

        public TipoFormaPagoFilter() {}

        public TipoFormaPagoFilter(TipoFormaPagoFilter filter) {
            super(filter);
        }

        @Override
        public TipoFormaPagoFilter copy() {
            return new TipoFormaPagoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter numeroEspecial;

    private StringFilter apellido;

    private StringFilter nombre;

    private StringFilter numeroDocumento;

    private StringFilter telefono;

    private TipoPersonaFilter mayorMenor;

    private TipoSalidaFilter salida;

    private IntegerFilter pago;

    private TipoFormaPagoFilter formaPago;

    private StringFilter aclaraciones;

    private BooleanFilter completoFormulario;

    private Boolean distinct;

    public PeregrinosCriteria() {}

    public PeregrinosCriteria(PeregrinosCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numeroEspecial = other.optionalNumeroEspecial().map(IntegerFilter::copy).orElse(null);
        this.apellido = other.optionalApellido().map(StringFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.numeroDocumento = other.optionalNumeroDocumento().map(StringFilter::copy).orElse(null);
        this.telefono = other.optionalTelefono().map(StringFilter::copy).orElse(null);
        this.mayorMenor = other.optionalMayorMenor().map(TipoPersonaFilter::copy).orElse(null);
        this.salida = other.optionalSalida().map(TipoSalidaFilter::copy).orElse(null);
        this.pago = other.optionalPago().map(IntegerFilter::copy).orElse(null);
        this.formaPago = other.optionalFormaPago().map(TipoFormaPagoFilter::copy).orElse(null);
        this.aclaraciones = other.optionalAclaraciones().map(StringFilter::copy).orElse(null);
        this.completoFormulario = other.optionalCompletoFormulario().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PeregrinosCriteria copy() {
        return new PeregrinosCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getNumeroEspecial() {
        return numeroEspecial;
    }

    public Optional<IntegerFilter> optionalNumeroEspecial() {
        return Optional.ofNullable(numeroEspecial);
    }

    public IntegerFilter numeroEspecial() {
        if (numeroEspecial == null) {
            setNumeroEspecial(new IntegerFilter());
        }
        return numeroEspecial;
    }

    public void setNumeroEspecial(IntegerFilter numeroEspecial) {
        this.numeroEspecial = numeroEspecial;
    }

    public StringFilter getApellido() {
        return apellido;
    }

    public Optional<StringFilter> optionalApellido() {
        return Optional.ofNullable(apellido);
    }

    public StringFilter apellido() {
        if (apellido == null) {
            setApellido(new StringFilter());
        }
        return apellido;
    }

    public void setApellido(StringFilter apellido) {
        this.apellido = apellido;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public Optional<StringFilter> optionalNombre() {
        return Optional.ofNullable(nombre);
    }

    public StringFilter nombre() {
        if (nombre == null) {
            setNombre(new StringFilter());
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getNumeroDocumento() {
        return numeroDocumento;
    }

    public Optional<StringFilter> optionalNumeroDocumento() {
        return Optional.ofNullable(numeroDocumento);
    }

    public StringFilter numeroDocumento() {
        if (numeroDocumento == null) {
            setNumeroDocumento(new StringFilter());
        }
        return numeroDocumento;
    }

    public void setNumeroDocumento(StringFilter numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public StringFilter getTelefono() {
        return telefono;
    }

    public Optional<StringFilter> optionalTelefono() {
        return Optional.ofNullable(telefono);
    }

    public StringFilter telefono() {
        if (telefono == null) {
            setTelefono(new StringFilter());
        }
        return telefono;
    }

    public void setTelefono(StringFilter telefono) {
        this.telefono = telefono;
    }

    public TipoPersonaFilter getMayorMenor() {
        return mayorMenor;
    }

    public Optional<TipoPersonaFilter> optionalMayorMenor() {
        return Optional.ofNullable(mayorMenor);
    }

    public TipoPersonaFilter mayorMenor() {
        if (mayorMenor == null) {
            setMayorMenor(new TipoPersonaFilter());
        }
        return mayorMenor;
    }

    public void setMayorMenor(TipoPersonaFilter mayorMenor) {
        this.mayorMenor = mayorMenor;
    }

    public TipoSalidaFilter getSalida() {
        return salida;
    }

    public Optional<TipoSalidaFilter> optionalSalida() {
        return Optional.ofNullable(salida);
    }

    public TipoSalidaFilter salida() {
        if (salida == null) {
            setSalida(new TipoSalidaFilter());
        }
        return salida;
    }

    public void setSalida(TipoSalidaFilter salida) {
        this.salida = salida;
    }

    public IntegerFilter getPago() {
        return pago;
    }

    public Optional<IntegerFilter> optionalPago() {
        return Optional.ofNullable(pago);
    }

    public IntegerFilter pago() {
        if (pago == null) {
            setPago(new IntegerFilter());
        }
        return pago;
    }

    public void setPago(IntegerFilter pago) {
        this.pago = pago;
    }

    public TipoFormaPagoFilter getFormaPago() {
        return formaPago;
    }

    public Optional<TipoFormaPagoFilter> optionalFormaPago() {
        return Optional.ofNullable(formaPago);
    }

    public TipoFormaPagoFilter formaPago() {
        if (formaPago == null) {
            setFormaPago(new TipoFormaPagoFilter());
        }
        return formaPago;
    }

    public void setFormaPago(TipoFormaPagoFilter formaPago) {
        this.formaPago = formaPago;
    }

    public StringFilter getAclaraciones() {
        return aclaraciones;
    }

    public Optional<StringFilter> optionalAclaraciones() {
        return Optional.ofNullable(aclaraciones);
    }

    public StringFilter aclaraciones() {
        if (aclaraciones == null) {
            setAclaraciones(new StringFilter());
        }
        return aclaraciones;
    }

    public void setAclaraciones(StringFilter aclaraciones) {
        this.aclaraciones = aclaraciones;
    }

    public BooleanFilter getCompletoFormulario() {
        return completoFormulario;
    }

    public Optional<BooleanFilter> optionalCompletoFormulario() {
        return Optional.ofNullable(completoFormulario);
    }

    public BooleanFilter completoFormulario() {
        if (completoFormulario == null) {
            setCompletoFormulario(new BooleanFilter());
        }
        return completoFormulario;
    }

    public void setCompletoFormulario(BooleanFilter completoFormulario) {
        this.completoFormulario = completoFormulario;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PeregrinosCriteria that = (PeregrinosCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numeroEspecial, that.numeroEspecial) &&
            Objects.equals(apellido, that.apellido) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(numeroDocumento, that.numeroDocumento) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(mayorMenor, that.mayorMenor) &&
            Objects.equals(salida, that.salida) &&
            Objects.equals(pago, that.pago) &&
            Objects.equals(formaPago, that.formaPago) &&
            Objects.equals(aclaraciones, that.aclaraciones) &&
            Objects.equals(completoFormulario, that.completoFormulario) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            numeroEspecial,
            apellido,
            nombre,
            numeroDocumento,
            telefono,
            mayorMenor,
            salida,
            pago,
            formaPago,
            aclaraciones,
            completoFormulario,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeregrinosCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumeroEspecial().map(f -> "numeroEspecial=" + f + ", ").orElse("") +
            optionalApellido().map(f -> "apellido=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalNumeroDocumento().map(f -> "numeroDocumento=" + f + ", ").orElse("") +
            optionalTelefono().map(f -> "telefono=" + f + ", ").orElse("") +
            optionalMayorMenor().map(f -> "mayorMenor=" + f + ", ").orElse("") +
            optionalSalida().map(f -> "salida=" + f + ", ").orElse("") +
            optionalPago().map(f -> "pago=" + f + ", ").orElse("") +
            optionalFormaPago().map(f -> "formaPago=" + f + ", ").orElse("") +
            optionalAclaraciones().map(f -> "aclaraciones=" + f + ", ").orElse("") +
            optionalCompletoFormulario().map(f -> "completoFormulario=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
