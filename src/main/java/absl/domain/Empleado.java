package absl.domain;

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
 * A Empleado.
 */
@Entity
@Table(name = "empleado")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Empleado implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "cuit")
    private String cuit;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_iva", nullable = false)
    private TipoIva tipoIva;

    @Column(name = "domicilio")
    private String domicilio;

    @NotNull
    @Pattern(regexp = "^(.+)@(\\S+)$")
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    @Pattern(regexp = "^(\\d{3}[- .]?){2}\\d{3}$")
    @Column(name = "telefono")
    private String telefono;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "genero", nullable = false)
    private Gender genero;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User usuario;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "practicases", "paquete", "usuario", "bioquimico" }, allowSetters = true)
    private Set<Orden> ordens = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Empleado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Empleado nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Empleado apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCuit() {
        return this.cuit;
    }

    public Empleado cuit(String cuit) {
        this.setCuit(cuit);
        return this;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public TipoIva getTipoIva() {
        return this.tipoIva;
    }

    public Empleado tipoIva(TipoIva tipoIva) {
        this.setTipoIva(tipoIva);
        return this;
    }

    public void setTipoIva(TipoIva tipoIva) {
        this.tipoIva = tipoIva;
    }

    public String getDomicilio() {
        return this.domicilio;
    }

    public Empleado domicilio(String domicilio) {
        this.setDomicilio(domicilio);
        return this;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getEmail() {
        return this.email;
    }

    public Empleado email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaIngreso() {
        return this.fechaIngreso;
    }

    public Empleado fechaIngreso(LocalDate fechaIngreso) {
        this.setFechaIngreso(fechaIngreso);
        return this;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Empleado telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Gender getGenero() {
        return this.genero;
    }

    public Empleado genero(Gender genero) {
        this.setGenero(genero);
        return this;
    }

    public void setGenero(Gender genero) {
        this.genero = genero;
    }

    public User getUsuario() {
        return this.usuario;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public Empleado usuario(User user) {
        this.setUsuario(user);
        return this;
    }

    public Set<Orden> getOrdens() {
        return this.ordens;
    }

    public void setOrdens(Set<Orden> ordens) {
        if (this.ordens != null) {
            this.ordens.forEach(i -> i.setUsuario(null));
        }
        if (ordens != null) {
            ordens.forEach(i -> i.setUsuario(this));
        }
        this.ordens = ordens;
    }

    public Empleado ordens(Set<Orden> ordens) {
        this.setOrdens(ordens);
        return this;
    }

    public Empleado addOrden(Orden orden) {
        this.ordens.add(orden);
        orden.setUsuario(this);
        return this;
    }

    public Empleado removeOrden(Orden orden) {
        this.ordens.remove(orden);
        orden.setUsuario(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Empleado)) {
            return false;
        }
        return getId() != null && getId().equals(((Empleado) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Empleado{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", cuit='" + getCuit() + "'" +
            ", tipoIva='" + getTipoIva() + "'" +
            ", domicilio='" + getDomicilio() + "'" +
            ", email='" + getEmail() + "'" +
            ", fechaIngreso='" + getFechaIngreso() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", genero='" + getGenero() + "'" +
            "}";
    }
}
