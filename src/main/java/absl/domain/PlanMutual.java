package absl.domain;

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
 * A PlanMutual.
 */
@Entity
@Table(name = "plan_mutual")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlanMutual implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 7)
    @Column(name = "categoria", length = 7, nullable = false)
    private String categoria;

    @NotNull
    @Size(max = 7)
    @Column(name = "etiqueta_reporte", length = 7, nullable = false)
    private String etiquetaReporte;

    @Column(name = "prestaciones_importadas")
    private Boolean prestacionesImportadas;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "plan")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ordeneses", "plan" }, allowSetters = true)
    private Set<Paquete> paquetes = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "nomencladors", "plans" }, allowSetters = true)
    private Mutual mutual;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PlanMutual id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoria() {
        return this.categoria;
    }

    public PlanMutual categoria(String categoria) {
        this.setCategoria(categoria);
        return this;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEtiquetaReporte() {
        return this.etiquetaReporte;
    }

    public PlanMutual etiquetaReporte(String etiquetaReporte) {
        this.setEtiquetaReporte(etiquetaReporte);
        return this;
    }

    public void setEtiquetaReporte(String etiquetaReporte) {
        this.etiquetaReporte = etiquetaReporte;
    }

    public Boolean getPrestacionesImportadas() {
        return this.prestacionesImportadas;
    }

    public PlanMutual prestacionesImportadas(Boolean prestacionesImportadas) {
        this.setPrestacionesImportadas(prestacionesImportadas);
        return this;
    }

    public void setPrestacionesImportadas(Boolean prestacionesImportadas) {
        this.prestacionesImportadas = prestacionesImportadas;
    }

    public Set<Paquete> getPaquetes() {
        return this.paquetes;
    }

    public void setPaquetes(Set<Paquete> paquetes) {
        if (this.paquetes != null) {
            this.paquetes.forEach(i -> i.setPlan(null));
        }
        if (paquetes != null) {
            paquetes.forEach(i -> i.setPlan(this));
        }
        this.paquetes = paquetes;
    }

    public PlanMutual paquetes(Set<Paquete> paquetes) {
        this.setPaquetes(paquetes);
        return this;
    }

    public PlanMutual addPaquete(Paquete paquete) {
        this.paquetes.add(paquete);
        paquete.setPlan(this);
        return this;
    }

    public PlanMutual removePaquete(Paquete paquete) {
        this.paquetes.remove(paquete);
        paquete.setPlan(null);
        return this;
    }

    public Mutual getMutual() {
        return this.mutual;
    }

    public void setMutual(Mutual mutual) {
        this.mutual = mutual;
    }

    public PlanMutual mutual(Mutual mutual) {
        this.setMutual(mutual);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanMutual)) {
            return false;
        }
        return getId() != null && getId().equals(((PlanMutual) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanMutual{" +
            "id=" + getId() +
            ", categoria='" + getCategoria() + "'" +
            ", etiquetaReporte='" + getEtiquetaReporte() + "'" +
            ", prestacionesImportadas='" + getPrestacionesImportadas() + "'" +
            "}";
    }
}
