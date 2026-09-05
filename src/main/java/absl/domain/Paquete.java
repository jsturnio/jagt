package absl.domain;

import absl.domain.enumeration.EstadoPaquete;
import absl.domain.enumeration.TipoIva;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Paquete.
 */
@Entity
@Table(name = "paquete")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Paquete implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @NotNull
    @Column(name = "periodo", nullable = false)
    private LocalDate periodo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoPaquete estado;

    @Size(max = 60)
    @Column(name = "descripcion", length = 60)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_iva")
    private TipoIva tipoIva;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "paquete")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "practicases", "paquete", "usuario", "bioquimico" }, allowSetters = true)
    private Set<Orden> ordeneses = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "paquetes", "mutual" }, allowSetters = true)
    private PlanMutual plan;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Paquete id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Paquete nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getPeriodo() {
        return this.periodo;
    }

    public Paquete periodo(LocalDate periodo) {
        this.setPeriodo(periodo);
        return this;
    }

    public void setPeriodo(LocalDate periodo) {
        this.periodo = periodo;
    }

    public EstadoPaquete getEstado() {
        return this.estado;
    }

    public Paquete estado(EstadoPaquete estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoPaquete estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Paquete descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoIva getTipoIva() {
        return this.tipoIva;
    }

    public Paquete tipoIva(TipoIva tipoIva) {
        this.setTipoIva(tipoIva);
        return this;
    }

    public void setTipoIva(TipoIva tipoIva) {
        this.tipoIva = tipoIva;
    }

    public Set<Orden> getOrdeneses() {
        return this.ordeneses;
    }

    public void setOrdeneses(Set<Orden> ordens) {
        if (this.ordeneses != null) {
            this.ordeneses.forEach(i -> i.setPaquete(null));
        }
        if (ordens != null) {
            ordens.forEach(i -> i.setPaquete(this));
        }
        this.ordeneses = ordens;
    }

    public Paquete ordeneses(Set<Orden> ordens) {
        this.setOrdeneses(ordens);
        return this;
    }

    public Paquete addOrdenes(Orden orden) {
        this.ordeneses.add(orden);
        orden.setPaquete(this);
        return this;
    }

    public Paquete removeOrdenes(Orden orden) {
        this.ordeneses.remove(orden);
        orden.setPaquete(null);
        return this;
    }

    public PlanMutual getPlan() {
        return this.plan;
    }

    public void setPlan(PlanMutual planMutual) {
        this.plan = planMutual;
    }

    public Paquete plan(PlanMutual planMutual) {
        this.setPlan(planMutual);
        return this;
    }

    /**
     * Campo calculado generado automáticamente por Blueprint (@dtoDisplay)
     */
    @jakarta.persistence.Transient
    public String getPaqDescrip() {
        return "(" + getId() + ") " + getPeriodo() + "-" + getNombre();
    }

    public void setPaqDescrip(String paqDescrip) {
        // No hacer nada: campo calculado
    }

    public Paquete paqDescrip(String paqDescrip) {
        // No hacer nada: campo calculado
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paquete)) {
            return false;
        }
        return getId() != null && getId().equals(((Paquete) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paquete{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", periodo='" + getPeriodo() + "'" +
            ", estado='" + getEstado() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", tipoIva='" + getTipoIva() + "'" +
            "}";
    }
}
