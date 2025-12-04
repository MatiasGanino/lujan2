package com.psmsf.lujanapp.domain;

import com.psmsf.lujanapp.domain.enumeration.TipoFormaPago;
import com.psmsf.lujanapp.domain.enumeration.TipoPersona;
import com.psmsf.lujanapp.domain.enumeration.TipoSalida;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Peregrinos.
 */
@Entity
@Table(name = "peregrinos")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Peregrinos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "numero_especial")
    private Integer numeroEspecial;

    @NotNull
    @Column(name = "apellido", nullable = false)
    private String apellido;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "numero_documento")
    private String numeroDocumento;

    @Column(name = "telefono")
    private String telefono;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "mayor_menor", nullable = false)
    private TipoPersona mayorMenor;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "salida", nullable = false)
    private TipoSalida salida;

    @Column(name = "pago")
    private Integer pago;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pago", nullable = false)
    private TipoFormaPago formaPago;

    @Column(name = "aclaraciones")
    private String aclaraciones;

    @Column(name = "completo_formulario")
    private Boolean completoFormulario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Peregrinos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroEspecial() {
        return this.numeroEspecial;
    }

    public Peregrinos numeroEspecial(Integer numeroEspecial) {
        this.setNumeroEspecial(numeroEspecial);
        return this;
    }

    public void setNumeroEspecial(Integer numeroEspecial) {
        this.numeroEspecial = numeroEspecial;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Peregrinos apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Peregrinos nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroDocumento() {
        return this.numeroDocumento;
    }

    public Peregrinos numeroDocumento(String numeroDocumento) {
        this.setNumeroDocumento(numeroDocumento);
        return this;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Peregrinos telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public TipoPersona getMayorMenor() {
        return this.mayorMenor;
    }

    public Peregrinos mayorMenor(TipoPersona mayorMenor) {
        this.setMayorMenor(mayorMenor);
        return this;
    }

    public void setMayorMenor(TipoPersona mayorMenor) {
        this.mayorMenor = mayorMenor;
    }

    public TipoSalida getSalida() {
        return this.salida;
    }

    public Peregrinos salida(TipoSalida salida) {
        this.setSalida(salida);
        return this;
    }

    public void setSalida(TipoSalida salida) {
        this.salida = salida;
    }

    public Integer getPago() {
        return this.pago;
    }

    public Peregrinos pago(Integer pago) {
        this.setPago(pago);
        return this;
    }

    public void setPago(Integer pago) {
        this.pago = pago;
    }

    public TipoFormaPago getFormaPago() {
        return this.formaPago;
    }

    public Peregrinos formaPago(TipoFormaPago formaPago) {
        this.setFormaPago(formaPago);
        return this;
    }

    public void setFormaPago(TipoFormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public String getAclaraciones() {
        return this.aclaraciones;
    }

    public Peregrinos aclaraciones(String aclaraciones) {
        this.setAclaraciones(aclaraciones);
        return this;
    }

    public void setAclaraciones(String aclaraciones) {
        this.aclaraciones = aclaraciones;
    }

    public Boolean getCompletoFormulario() {
        return this.completoFormulario;
    }

    public Peregrinos completoFormulario(Boolean completoFormulario) {
        this.setCompletoFormulario(completoFormulario);
        return this;
    }

    public void setCompletoFormulario(Boolean completoFormulario) {
        this.completoFormulario = completoFormulario;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Peregrinos)) {
            return false;
        }
        return getId() != null && getId().equals(((Peregrinos) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Peregrinos{" +
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
