package absl.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link absl.domain.PlanMutual} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlanMutualDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 7)
    private String categoria;

    @NotNull
    @Size(max = 7)
    private String etiquetaReporte;

    private Boolean prestacionesImportadas;

    @NotNull
    private MutualDTO mutual;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEtiquetaReporte() {
        return etiquetaReporte;
    }

    public void setEtiquetaReporte(String etiquetaReporte) {
        this.etiquetaReporte = etiquetaReporte;
    }

    public Boolean getPrestacionesImportadas() {
        return prestacionesImportadas;
    }

    public void setPrestacionesImportadas(Boolean prestacionesImportadas) {
        this.prestacionesImportadas = prestacionesImportadas;
    }

    public MutualDTO getMutual() {
        return mutual;
    }

    public void setMutual(MutualDTO mutual) {
        this.mutual = mutual;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanMutualDTO)) {
            return false;
        }

        PlanMutualDTO planMutualDTO = (PlanMutualDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, planMutualDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanMutualDTO{" +
            "id=" + getId() +
            ", categoria='" + getCategoria() + "'" +
            ", etiquetaReporte='" + getEtiquetaReporte() + "'" +
            ", prestacionesImportadas='" + getPrestacionesImportadas() + "'" +
            ", mutual=" + getMutual() +
            "}";
    }
}
