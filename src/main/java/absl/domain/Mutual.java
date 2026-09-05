package absl.domain;

import absl.domain.enumeration.TipoIva;
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
 * A Mutual.
 */
@Entity
@Table(name = "mutual")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Mutual implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 7)
    @Column(name = "nombre", length = 7, nullable = false)
    private String nombre;

    @Size(max = 100)
    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Size(max = 20)
    @Column(name = "cuit", length = 20)
    private String cuit;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_iva", nullable = false)
    private TipoIva tipoIva;

    @Size(max = 50)
    @Column(name = "domicilio", length = 50)
    private String domicilio;

    @Pattern(regexp = "^(\\d{3}[- .]?){2}\\\\d{4}$")
    @Column(name = "telefono")
    private String telefono;

    @Pattern(regexp = "^(.+)@(\\\\S+)$")
    @Column(name = "email")
    private String email;

    @Column(name = "habilitada")
    private Boolean habilitada;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mutual")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "prestacions", "mutual" }, allowSetters = true)
    private Set<Nomenclador> nomencladors = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mutual")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paquetes", "mutual" }, allowSetters = true)
    private Set<PlanMutual> plans = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Mutual id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Mutual nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Mutual descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCuit() {
        return this.cuit;
    }

    public Mutual cuit(String cuit) {
        this.setCuit(cuit);
        return this;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public TipoIva getTipoIva() {
        return this.tipoIva;
    }

    public Mutual tipoIva(TipoIva tipoIva) {
        this.setTipoIva(tipoIva);
        return this;
    }

    public void setTipoIva(TipoIva tipoIva) {
        this.tipoIva = tipoIva;
    }

    public String getDomicilio() {
        return this.domicilio;
    }

    public Mutual domicilio(String domicilio) {
        this.setDomicilio(domicilio);
        return this;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Mutual telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return this.email;
    }

    public Mutual email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getHabilitada() {
        return this.habilitada;
    }

    public Mutual habilitada(Boolean habilitada) {
        this.setHabilitada(habilitada);
        return this;
    }

    public void setHabilitada(Boolean habilitada) {
        this.habilitada = habilitada;
    }

    public Set<Nomenclador> getNomencladors() {
        return this.nomencladors;
    }

    public void setNomencladors(Set<Nomenclador> nomencladors) {
        if (this.nomencladors != null) {
            this.nomencladors.forEach(i -> i.setMutual(null));
        }
        if (nomencladors != null) {
            nomencladors.forEach(i -> i.setMutual(this));
        }
        this.nomencladors = nomencladors;
    }

    public Mutual nomencladors(Set<Nomenclador> nomencladors) {
        this.setNomencladors(nomencladors);
        return this;
    }

    public Mutual addNomenclador(Nomenclador nomenclador) {
        this.nomencladors.add(nomenclador);
        nomenclador.setMutual(this);
        return this;
    }

    public Mutual removeNomenclador(Nomenclador nomenclador) {
        this.nomencladors.remove(nomenclador);
        nomenclador.setMutual(null);
        return this;
    }

    public Set<PlanMutual> getPlans() {
        return this.plans;
    }

    public void setPlans(Set<PlanMutual> planMutuals) {
        if (this.plans != null) {
            this.plans.forEach(i -> i.setMutual(null));
        }
        if (planMutuals != null) {
            planMutuals.forEach(i -> i.setMutual(this));
        }
        this.plans = planMutuals;
    }

    public Mutual plans(Set<PlanMutual> planMutuals) {
        this.setPlans(planMutuals);
        return this;
    }

    public Mutual addPlan(PlanMutual planMutual) {
        this.plans.add(planMutual);
        planMutual.setMutual(this);
        return this;
    }

    public Mutual removePlan(PlanMutual planMutual) {
        this.plans.remove(planMutual);
        planMutual.setMutual(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mutual)) {
            return false;
        }
        return getId() != null && getId().equals(((Mutual) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mutual{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", cuit='" + getCuit() + "'" +
            ", tipoIva='" + getTipoIva() + "'" +
            ", domicilio='" + getDomicilio() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", email='" + getEmail() + "'" +
            ", habilitada='" + getHabilitada() + "'" +
            "}";
    }
}
