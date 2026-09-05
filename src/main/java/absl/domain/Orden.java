package absl.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Orden.
 */
@Entity
@Table(name = "orden")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Orden implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "afiliado")
    private Long afiliado;

    @Column(name = "protocolo")
    private Long protocolo;

    @Column(name = "fecha_orden")
    private LocalDate fechaOrden;

    @Column(name = "fecha_prescripcion")
    private LocalDate fechaPrescripcion;

    @Column(name = "total_orden", precision = 21, scale = 2)
    private BigDecimal totalOrden;

    @Column(name = "suma_ub", precision = 21, scale = 2)
    private BigDecimal sumaUb;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    /**
     * @showCount
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orden")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "prestacion", "orden" }, allowSetters = true)
    private Set<Practica> practicases = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "ordeneses", "plan" }, allowSetters = true)
    private Paquete paquete;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "usuario", "ordens" }, allowSetters = true)
    private Empleado usuario;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "ordeneses" }, allowSetters = true)
    private Bioquimico bioquimico;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Orden id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAfiliado() {
        return this.afiliado;
    }

    public Orden afiliado(Long afiliado) {
        this.setAfiliado(afiliado);
        return this;
    }

    public void setAfiliado(Long afiliado) {
        this.afiliado = afiliado;
    }

    public Long getProtocolo() {
        return this.protocolo;
    }

    public Orden protocolo(Long protocolo) {
        this.setProtocolo(protocolo);
        return this;
    }

    public void setProtocolo(Long protocolo) {
        this.protocolo = protocolo;
    }

    public LocalDate getFechaOrden() {
        return this.fechaOrden;
    }

    public Orden fechaOrden(LocalDate fechaOrden) {
        this.setFechaOrden(fechaOrden);
        return this;
    }

    public void setFechaOrden(LocalDate fechaOrden) {
        this.fechaOrden = fechaOrden;
    }

    public LocalDate getFechaPrescripcion() {
        return this.fechaPrescripcion;
    }

    public Orden fechaPrescripcion(LocalDate fechaPrescripcion) {
        this.setFechaPrescripcion(fechaPrescripcion);
        return this;
    }

    public void setFechaPrescripcion(LocalDate fechaPrescripcion) {
        this.fechaPrescripcion = fechaPrescripcion;
    }

    public BigDecimal getTotalOrden() {
        return this.totalOrden;
    }

    public Orden totalOrden(BigDecimal totalOrden) {
        this.setTotalOrden(totalOrden);
        return this;
    }

    public void setTotalOrden(BigDecimal totalOrden) {
        this.totalOrden = totalOrden;
    }

    public BigDecimal getSumaUb() {
        return this.sumaUb;
    }

    public Orden sumaUb(BigDecimal sumaUb) {
        this.setSumaUb(sumaUb);
        return this;
    }

    public void setSumaUb(BigDecimal sumaUb) {
        this.sumaUb = sumaUb;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Orden fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Set<Practica> getPracticases() {
        return this.practicases;
    }

    public void setPracticases(Set<Practica> practicas) {
        if (this.practicases != null) {
            this.practicases.forEach(i -> i.setOrden(null));
        }
        if (practicas != null) {
            practicas.forEach(i -> i.setOrden(this));
        }
        this.practicases = practicas;
    }

    public Orden practicases(Set<Practica> practicas) {
        this.setPracticases(practicas);
        return this;
    }

    public Orden addPracticas(Practica practica) {
        this.practicases.add(practica);
        practica.setOrden(this);
        return this;
    }

    public Orden removePracticas(Practica practica) {
        this.practicases.remove(practica);
        practica.setOrden(null);
        return this;
    }

    public Paquete getPaquete() {
        return this.paquete;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
    }

    public Orden paquete(Paquete paquete) {
        this.setPaquete(paquete);
        return this;
    }

    public Empleado getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Empleado empleado) {
        this.usuario = empleado;
    }

    public Orden usuario(Empleado empleado) {
        this.setUsuario(empleado);
        return this;
    }

    public Bioquimico getBioquimico() {
        return this.bioquimico;
    }

    public void setBioquimico(Bioquimico bioquimico) {
        this.bioquimico = bioquimico;
    }

    public Orden bioquimico(Bioquimico bioquimico) {
        this.setBioquimico(bioquimico);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Orden)) {
            return false;
        }
        return getId() != null && getId().equals(((Orden) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Orden{" +
            "id=" + getId() +
            ", afiliado=" + getAfiliado() +
            ", protocolo=" + getProtocolo() +
            ", fechaOrden='" + getFechaOrden() + "'" +
            ", fechaPrescripcion='" + getFechaPrescripcion() + "'" +
            ", totalOrden=" + getTotalOrden() +
            ", sumaUb=" + getSumaUb() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            "}";
    }
}
