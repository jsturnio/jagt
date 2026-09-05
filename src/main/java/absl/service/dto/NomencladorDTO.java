package absl.service.dto;

import absl.domain.enumeration.TipoNomenclador;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link absl.domain.Nomenclador} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NomencladorDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 12)
    private String nombre;

    @Size(max = 100)
    private String descripcion;

    @NotNull
    private TipoNomenclador tipo;

    @NotNull
    private MutualDTO mutual;

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

    public TipoNomenclador getTipo() {
        return tipo;
    }

    public void setTipo(TipoNomenclador tipo) {
        this.tipo = tipo;
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
        if (!(o instanceof NomencladorDTO)) {
            return false;
        }

        NomencladorDTO nomencladorDTO = (NomencladorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nomencladorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NomencladorDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", mutual=" + getMutual() +
            "}";
    }
}
