package absl.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Practica.
 */
@Entity
@Table(name = "practica")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Practica implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "pvalor", precision = 21, scale = 2)
    private BigDecimal pvalor;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "nomenclador" }, allowSetters = true)
    private Prestacion prestacion;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "practicases", "paquete", "usuario", "bioquimico" }, allowSetters = true)
    private Orden orden;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Practica id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public Practica cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPvalor() {
        return this.pvalor;
    }

    public Practica pvalor(BigDecimal pvalor) {
        this.setPvalor(pvalor);
        return this;
    }

    public void setPvalor(BigDecimal pvalor) {
        this.pvalor = pvalor;
    }

    public Prestacion getPrestacion() {
        return this.prestacion;
    }

    public void setPrestacion(Prestacion prestacion) {
        this.prestacion = prestacion;
    }

    public Practica prestacion(Prestacion prestacion) {
        this.setPrestacion(prestacion);
        return this;
    }

    public Orden getOrden() {
        return this.orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public Practica orden(Orden orden) {
        this.setOrden(orden);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Practica)) {
            return false;
        }
        return getId() != null && getId().equals(((Practica) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Practica{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", pvalor=" + getPvalor() +
            "}";
    }
}
