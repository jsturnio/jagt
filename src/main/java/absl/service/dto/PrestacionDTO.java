package absl.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link absl.domain.Prestacion} entity.
 */
@Schema(description = "Product sold by the Online store")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PrestacionDTO implements Serializable {

    private Long id;

    @NotNull
    @Schema(description = "Codigo: Codigo Base NBU", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer codigo;

    private String codigoInos;

    private BigDecimal valorUb;

    @Schema(description = "Indica que la Practica requiere autorizacion por la Obra Social")
    private Boolean reqAutorizacion;

    @NotNull
    private NomencladorDTO nomenclador;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getCodigoInos() {
        return codigoInos;
    }

    public void setCodigoInos(String codigoInos) {
        this.codigoInos = codigoInos;
    }

    public BigDecimal getValorUb() {
        return valorUb;
    }

    public void setValorUb(BigDecimal valorUb) {
        this.valorUb = valorUb;
    }

    public Boolean getReqAutorizacion() {
        return reqAutorizacion;
    }

    public void setReqAutorizacion(Boolean reqAutorizacion) {
        this.reqAutorizacion = reqAutorizacion;
    }

    public NomencladorDTO getNomenclador() {
        return nomenclador;
    }

    public void setNomenclador(NomencladorDTO nomenclador) {
        this.nomenclador = nomenclador;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrestacionDTO)) {
            return false;
        }

        PrestacionDTO prestacionDTO = (PrestacionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prestacionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrestacionDTO{" +
            "id=" + getId() +
            ", codigo=" + getCodigo() +
            ", codigoInos='" + getCodigoInos() + "'" +
            ", valorUb=" + getValorUb() +
            ", reqAutorizacion='" + getReqAutorizacion() + "'" +
            ", nomenclador=" + getNomenclador() +
            "}";
    }
}
