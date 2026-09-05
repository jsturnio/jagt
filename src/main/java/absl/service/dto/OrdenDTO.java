package absl.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link absl.domain.Orden} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrdenDTO implements Serializable {

    private Long id;

    private Long afiliado;

    private Long protocolo;

    private LocalDate fechaOrden;

    private LocalDate fechaPrescripcion;

    private BigDecimal totalOrden;

    private BigDecimal sumaUb;

    private Instant fechaCreacion;

    @NotNull
    private PaqueteDTO paquete;

    @NotNull
    private EmpleadoDTO usuario;

    @NotNull
    private BioquimicoDTO bioquimico;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAfiliado() {
        return afiliado;
    }

    public void setAfiliado(Long afiliado) {
        this.afiliado = afiliado;
    }

    public Long getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(Long protocolo) {
        this.protocolo = protocolo;
    }

    public LocalDate getFechaOrden() {
        return fechaOrden;
    }

    public void setFechaOrden(LocalDate fechaOrden) {
        this.fechaOrden = fechaOrden;
    }

    public LocalDate getFechaPrescripcion() {
        return fechaPrescripcion;
    }

    public void setFechaPrescripcion(LocalDate fechaPrescripcion) {
        this.fechaPrescripcion = fechaPrescripcion;
    }

    public BigDecimal getTotalOrden() {
        return totalOrden;
    }

    public void setTotalOrden(BigDecimal totalOrden) {
        this.totalOrden = totalOrden;
    }

    public BigDecimal getSumaUb() {
        return sumaUb;
    }

    public void setSumaUb(BigDecimal sumaUb) {
        this.sumaUb = sumaUb;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public PaqueteDTO getPaquete() {
        return paquete;
    }

    public void setPaquete(PaqueteDTO paquete) {
        this.paquete = paquete;
    }

    public EmpleadoDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(EmpleadoDTO usuario) {
        this.usuario = usuario;
    }

    public BioquimicoDTO getBioquimico() {
        return bioquimico;
    }

    public void setBioquimico(BioquimicoDTO bioquimico) {
        this.bioquimico = bioquimico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrdenDTO)) {
            return false;
        }

        OrdenDTO ordenDTO = (OrdenDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ordenDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrdenDTO{" +
            "id=" + getId() +
            ", afiliado=" + getAfiliado() +
            ", protocolo=" + getProtocolo() +
            ", fechaOrden='" + getFechaOrden() + "'" +
            ", fechaPrescripcion='" + getFechaPrescripcion() + "'" +
            ", totalOrden=" + getTotalOrden() +
            ", sumaUb=" + getSumaUb() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", paquete=" + getPaquete() +
            ", usuario=" + getUsuario() +
            ", bioquimico=" + getBioquimico() +
            "}";
    }
}
