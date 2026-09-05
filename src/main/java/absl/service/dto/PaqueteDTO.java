package absl.service.dto;

import absl.domain.enumeration.EstadoPaquete;
import absl.domain.enumeration.TipoIva;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link absl.domain.Paquete} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaqueteDTO implements Serializable {

    private Long id;

    private String paqDescrip;

    private String nombre;

    @NotNull
    private LocalDate periodo;

    private EstadoPaquete estado;

    @Size(max = 60)
    private String descripcion;

    private TipoIva tipoIva;

    @NotNull
    private PlanMutualDTO plan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaqDescrip() {
        return paqDescrip;
    }

    public void setPaqDescrip(String paqDescrip) {
        this.paqDescrip = paqDescrip;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getPeriodo() {
        return periodo;
    }

    public void setPeriodo(LocalDate periodo) {
        this.periodo = periodo;
    }

    public EstadoPaquete getEstado() {
        return estado;
    }

    public void setEstado(EstadoPaquete estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoIva getTipoIva() {
        return tipoIva;
    }

    public void setTipoIva(TipoIva tipoIva) {
        this.tipoIva = tipoIva;
    }

    public PlanMutualDTO getPlan() {
        return plan;
    }

    public void setPlan(PlanMutualDTO plan) {
        this.plan = plan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaqueteDTO)) {
            return false;
        }

        PaqueteDTO paqueteDTO = (PaqueteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paqueteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaqueteDTO{" +
            "id=" + getId() +
            ", paqDescrip='" + getPaqDescrip() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", periodo='" + getPeriodo() + "'" +
            ", estado='" + getEstado() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", tipoIva='" + getTipoIva() + "'" +
            ", plan=" + getPlan() +
            "}";
    }
}
