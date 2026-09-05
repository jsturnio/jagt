package absl.domain;

import absl.domain.enumeration.TipoNomenclador;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Nomenclador.
 */
@Entity
@Table(name = "nomenclador")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Nomenclador implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 12)
    @Column(name = "nombre", length = 12, nullable = false)
    private String nombre;

    @Size(max = 100)
    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoNomenclador tipo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "nomenclador")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "nomenclador" }, allowSetters = true)
    private Set<Prestacion> prestacions = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "nomencladors", "plans" }, allowSetters = true)
    private Mutual mutual;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Nomenclador id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Nomenclador nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Nomenclador descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoNomenclador getTipo() {
        return this.tipo;
    }

    public Nomenclador tipo(TipoNomenclador tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(TipoNomenclador tipo) {
        this.tipo = tipo;
    }

    public Set<Prestacion> getPrestacions() {
        return this.prestacions;
    }

    public void setPrestacions(Set<Prestacion> prestacions) {
        if (this.prestacions != null) {
            this.prestacions.forEach(i -> i.setNomenclador(null));
        }
        if (prestacions != null) {
            prestacions.forEach(i -> i.setNomenclador(this));
        }
        this.prestacions = prestacions;
    }

    public Nomenclador prestacions(Set<Prestacion> prestacions) {
        this.setPrestacions(prestacions);
        return this;
    }

    public Nomenclador addPrestacion(Prestacion prestacion) {
        this.prestacions.add(prestacion);
        prestacion.setNomenclador(this);
        return this;
    }

    public Nomenclador removePrestacion(Prestacion prestacion) {
        this.prestacions.remove(prestacion);
        prestacion.setNomenclador(null);
        return this;
    }

    public Mutual getMutual() {
        return this.mutual;
    }

    public void setMutual(Mutual mutual) {
        this.mutual = mutual;
    }

    public Nomenclador mutual(Mutual mutual) {
        this.setMutual(mutual);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Nomenclador)) {
            return false;
        }
        return getId() != null && getId().equals(((Nomenclador) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Nomenclador{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", tipo='" + getTipo() + "'" +
            "}";
    }
}
