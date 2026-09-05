package absl.service.dto;

import absl.domain.enumeration.Gender;
import absl.domain.enumeration.TipoIva;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link absl.domain.Empleado} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmpleadoDTO implements Serializable {

    private Long id;

    private String nombre;

    private String apellido;

    private String cuit;

    @NotNull
    private TipoIva tipoIva;

    private String domicilio;

    @NotNull
    @Pattern(regexp = "^(.+)@(\\S+)$")
    private String email;

    private LocalDate fechaIngreso;

    @Pattern(regexp = "^(\\d{3}[- .]?){2}\\d{3}$")
    private String telefono;

    @NotNull
    private Gender genero;

    @NotNull
    private UserDTO usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public TipoIva getTipoIva() {
        return tipoIva;
    }

    public void setTipoIva(TipoIva tipoIva) {
        this.tipoIva = tipoIva;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Gender getGenero() {
        return genero;
    }

    public void setGenero(Gender genero) {
        this.genero = genero;
    }

    public UserDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmpleadoDTO)) {
            return false;
        }

        EmpleadoDTO empleadoDTO = (EmpleadoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, empleadoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmpleadoDTO{" +
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
            ", usuario=" + getUsuario() +
            "}";
    }
}
