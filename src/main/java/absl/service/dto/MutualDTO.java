package absl.service.dto;

import absl.domain.enumeration.TipoIva;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link absl.domain.Mutual} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MutualDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 7)
    private String nombre;

    @Size(max = 100)
    private String descripcion;

    @Size(max = 20)
    private String cuit;

    @NotNull
    private TipoIva tipoIva;

    @Size(max = 50)
    private String domicilio;

    @Pattern(regexp = "^(\\d{3}[- .]?){2}\\\\d{4}$")
    private String telefono;

    @Pattern(regexp = "^(.+)@(\\\\S+)$")
    private String email;

    private Boolean habilitada;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getHabilitada() {
        return habilitada;
    }

    public void setHabilitada(Boolean habilitada) {
        this.habilitada = habilitada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MutualDTO)) {
            return false;
        }

        MutualDTO mutualDTO = (MutualDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mutualDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MutualDTO{" +
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
