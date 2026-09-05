package absl.domain;

import absl.domain.enumeration.EstadoSocio;
import absl.domain.enumeration.Gender;
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
 * A Bioquimico.
 */
@Entity
@Table(name = "bioquimico")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Bioquimico implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "matricula")
    private Long matricula;

    @Column(name = "cuit")
    private String cuit;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_iva", nullable = false)
    private TipoIva tipoIva;

    @Column(name = "domicilio_profesional")
    private String domicilioProfesional;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoSocio estado;

    @NotNull
    @Pattern(regexp = "^(.+)@(\\S+)$")
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    @Pattern(regexp = "^(\\d{3}[- .]?){2}\\d{3}$")
    @Column(name = "telefono")
    private String telefono;

    @Column(name = "nombre_en_dosep")
    private String nombreEnDosep;

    @Column(name = "nro_prestador_osde")
    private Integer nroPrestadorOsde;

    @Size(max = 20)
    @Column(name = "ing_brutos", length = 20)
    private String ingBrutos;

    @Column(name = "nro_jubilacion")
    private String nroJubilacion;

    @Column(name = "nro_laboratorio")
    private String nroLaboratorio;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "genero", nullable = false)
    private Gender genero;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bioquimico")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "practicases", "paquete", "usuario", "bioquimico" }, allowSetters = true)
    private Set<Orden> ordeneses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bioquimico id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMatricula() {
        return this.matricula;
    }

    public Bioquimico matricula(Long matricula) {
        this.setMatricula(matricula);
        return this;
    }

    public void setMatricula(Long matricula) {
        this.matricula = matricula;
    }

    public String getCuit() {
        return this.cuit;
    }

    public Bioquimico cuit(String cuit) {
        this.setCuit(cuit);
        return this;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public TipoIva getTipoIva() {
        return this.tipoIva;
    }

    public Bioquimico tipoIva(TipoIva tipoIva) {
        this.setTipoIva(tipoIva);
        return this;
    }

    public void setTipoIva(TipoIva tipoIva) {
        this.tipoIva = tipoIva;
    }

    public String getDomicilioProfesional() {
        return this.domicilioProfesional;
    }

    public Bioquimico domicilioProfesional(String domicilioProfesional) {
        this.setDomicilioProfesional(domicilioProfesional);
        return this;
    }

    public void setDomicilioProfesional(String domicilioProfesional) {
        this.domicilioProfesional = domicilioProfesional;
    }

    public EstadoSocio getEstado() {
        return this.estado;
    }

    public Bioquimico estado(EstadoSocio estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoSocio estado) {
        this.estado = estado;
    }

    public String getEmail() {
        return this.email;
    }

    public Bioquimico email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaIngreso() {
        return this.fechaIngreso;
    }

    public Bioquimico fechaIngreso(LocalDate fechaIngreso) {
        this.setFechaIngreso(fechaIngreso);
        return this;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Bioquimico telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombreEnDosep() {
        return this.nombreEnDosep;
    }

    public Bioquimico nombreEnDosep(String nombreEnDosep) {
        this.setNombreEnDosep(nombreEnDosep);
        return this;
    }

    public void setNombreEnDosep(String nombreEnDosep) {
        this.nombreEnDosep = nombreEnDosep;
    }

    public Integer getNroPrestadorOsde() {
        return this.nroPrestadorOsde;
    }

    public Bioquimico nroPrestadorOsde(Integer nroPrestadorOsde) {
        this.setNroPrestadorOsde(nroPrestadorOsde);
        return this;
    }

    public void setNroPrestadorOsde(Integer nroPrestadorOsde) {
        this.nroPrestadorOsde = nroPrestadorOsde;
    }

    public String getIngBrutos() {
        return this.ingBrutos;
    }

    public Bioquimico ingBrutos(String ingBrutos) {
        this.setIngBrutos(ingBrutos);
        return this;
    }

    public void setIngBrutos(String ingBrutos) {
        this.ingBrutos = ingBrutos;
    }

    public String getNroJubilacion() {
        return this.nroJubilacion;
    }

    public Bioquimico nroJubilacion(String nroJubilacion) {
        this.setNroJubilacion(nroJubilacion);
        return this;
    }

    public void setNroJubilacion(String nroJubilacion) {
        this.nroJubilacion = nroJubilacion;
    }

    public String getNroLaboratorio() {
        return this.nroLaboratorio;
    }

    public Bioquimico nroLaboratorio(String nroLaboratorio) {
        this.setNroLaboratorio(nroLaboratorio);
        return this;
    }

    public void setNroLaboratorio(String nroLaboratorio) {
        this.nroLaboratorio = nroLaboratorio;
    }

    public Gender getGenero() {
        return this.genero;
    }

    public Bioquimico genero(Gender genero) {
        this.setGenero(genero);
        return this;
    }

    public void setGenero(Gender genero) {
        this.genero = genero;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Bioquimico user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Orden> getOrdeneses() {
        return this.ordeneses;
    }

    public void setOrdeneses(Set<Orden> ordens) {
        if (this.ordeneses != null) {
            this.ordeneses.forEach(i -> i.setBioquimico(null));
        }
        if (ordens != null) {
            ordens.forEach(i -> i.setBioquimico(this));
        }
        this.ordeneses = ordens;
    }

    public Bioquimico ordeneses(Set<Orden> ordens) {
        this.setOrdeneses(ordens);
        return this;
    }

    public Bioquimico addOrdenes(Orden orden) {
        this.ordeneses.add(orden);
        orden.setBioquimico(this);
        return this;
    }

    public Bioquimico removeOrdenes(Orden orden) {
        this.ordeneses.remove(orden);
        orden.setBioquimico(null);
        return this;
    }

    /**
     * Campo calculado generado automáticamente por Blueprint (@dtoDisplay)
     */
    @jakarta.persistence.Transient
    public String getNombreCompleto() {
        if (getUser() == null) {
            return "";
        }
        return user.getLastName() + ", " + user.getFirstName() + " (" + getId() + ")";
    }

    public void setNombreCompleto(String nombreCompleto) {
        // No hacer nada: campo calculado
    }

    public Bioquimico nombreCompleto(String nombreCompleto) {
        // No hacer nada: campo calculado
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bioquimico)) {
            return false;
        }
        return getId() != null && getId().equals(((Bioquimico) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bioquimico{" +
            "id=" + getId() +
            ", matricula=" + getMatricula() +
            ", cuit='" + getCuit() + "'" +
            ", tipoIva='" + getTipoIva() + "'" +
            ", domicilioProfesional='" + getDomicilioProfesional() + "'" +
            ", estado='" + getEstado() + "'" +
            ", email='" + getEmail() + "'" +
            ", fechaIngreso='" + getFechaIngreso() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", nombreEnDosep='" + getNombreEnDosep() + "'" +
            ", nroPrestadorOsde=" + getNroPrestadorOsde() +
            ", ingBrutos='" + getIngBrutos() + "'" +
            ", nroJubilacion='" + getNroJubilacion() + "'" +
            ", nroLaboratorio='" + getNroLaboratorio() + "'" +
            ", genero='" + getGenero() + "'" +
            "}";
    }
}
