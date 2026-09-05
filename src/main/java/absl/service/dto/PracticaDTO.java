package absl.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link absl.domain.Practica} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PracticaDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    private Integer cantidad;

    private BigDecimal pvalor;

    @NotNull
    private PrestacionDTO prestacion;

    @NotNull
    private OrdenDTO orden;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPvalor() {
        return pvalor;
    }

    public void setPvalor(BigDecimal pvalor) {
        this.pvalor = pvalor;
    }

    public PrestacionDTO getPrestacion() {
        return prestacion;
    }

    public void setPrestacion(PrestacionDTO prestacion) {
        this.prestacion = prestacion;
    }

    public OrdenDTO getOrden() {
        return orden;
    }

    public void setOrden(OrdenDTO orden) {
        this.orden = orden;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PracticaDTO)) {
            return false;
        }

        PracticaDTO practicaDTO = (PracticaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, practicaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PracticaDTO{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", pvalor=" + getPvalor() +
            ", prestacion=" + getPrestacion() +
            ", orden=" + getOrden() +
            "}";
    }
}
