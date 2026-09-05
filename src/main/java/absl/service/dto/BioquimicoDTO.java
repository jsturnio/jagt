package absl.service.dto;

import absl.domain.enumeration.EstadoSocio;
import absl.domain.enumeration.Gender;
import absl.domain.enumeration.TipoIva;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link absl.domain.Bioquimico} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BioquimicoDTO implements Serializable {

    private Long id;

    private Long matricula;

    private String cuit;

    @NotNull
    private TipoIva tipoIva;

    private String domicilioProfesional;

    private EstadoSocio estado;

    @NotNull
    @Pattern(regexp = "^(.+)@(\\S+)$")
    private String email;

    private LocalDate fechaIngreso;

    @Pattern(regexp = "^(\\d{3}[- .]?){2}\\d{3}$")
    private String telefono;

    private String nombreEnDosep;

    private Integer nroPrestadorOsde;

    @Size(max = 20)
    private String ingBrutos;

    private String nroJubilacion;

    private String nroLaboratorio;

    private String nombreCompleto;

    @NotNull
    private Gender genero;

    @NotNull
    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMatricula() {
        return matricula;
    }

    public void setMatricula(Long matricula) {
        this.matricula = matricula;
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

    public String getDomicilioProfesional() {
        return domicilioProfesional;
    }

    public void setDomicilioProfesional(String domicilioProfesional) {
        this.domicilioProfesional = domicilioProfesional;
    }

    public EstadoSocio getEstado() {
        return estado;
    }

    public void setEstado(EstadoSocio estado) {
        this.estado = estado;
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

    public String getNombreEnDosep() {
        return nombreEnDosep;
    }

    public void setNombreEnDosep(String nombreEnDosep) {
        this.nombreEnDosep = nombreEnDosep;
    }

    public Integer getNroPrestadorOsde() {
        return nroPrestadorOsde;
    }

    public void setNroPrestadorOsde(Integer nroPrestadorOsde) {
        this.nroPrestadorOsde = nroPrestadorOsde;
    }

    public String getIngBrutos() {
        return ingBrutos;
    }

    public void setIngBrutos(String ingBrutos) {
        this.ingBrutos = ingBrutos;
    }

    public String getNroJubilacion() {
        return nroJubilacion;
    }

    public void setNroJubilacion(String nroJubilacion) {
        this.nroJubilacion = nroJubilacion;
    }

    public String getNroLaboratorio() {
        return nroLaboratorio;
    }

    public void setNroLaboratorio(String nroLaboratorio) {
        this.nroLaboratorio = nroLaboratorio;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Gender getGenero() {
        return genero;
    }

    public void setGenero(Gender genero) {
        this.genero = genero;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BioquimicoDTO)) {
            return false;
        }

        BioquimicoDTO bioquimicoDTO = (BioquimicoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bioquimicoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BioquimicoDTO{" +
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
            ", nombreCompleto='" + getNombreCompleto() + "'" +
            ", genero='" + getGenero() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
