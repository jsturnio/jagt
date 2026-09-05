package absl.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Product sold by the Online store
 */
@Entity
@Table(name = "prestacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Prestacion implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Codigo: Codigo Base NBU
     */
    @NotNull
    @Column(name = "codigo", nullable = false, unique = true)
    private Integer codigo;

    @Column(name = "codigo_inos")
    private String codigoInos;

    @Column(name = "valor_ub", precision = 21, scale = 2)
    private BigDecimal valorUb;

    /**
     * Indica que la Practica requiere autorizacion por la Obra Social
     */
    @Column(name = "req_autorizacion")
    private Boolean reqAutorizacion;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "prestacions", "mutual" }, allowSetters = true)
    private Nomenclador nomenclador;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Prestacion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCodigo() {
        return this.codigo;
    }

    public Prestacion codigo(Integer codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getCodigoInos() {
        return this.codigoInos;
    }

    public Prestacion codigoInos(String codigoInos) {
        this.setCodigoInos(codigoInos);
        return this;
    }

    public void setCodigoInos(String codigoInos) {
        this.codigoInos = codigoInos;
    }

    public BigDecimal getValorUb() {
        return this.valorUb;
    }

    public Prestacion valorUb(BigDecimal valorUb) {
        this.setValorUb(valorUb);
        return this;
    }

    public void setValorUb(BigDecimal valorUb) {
        this.valorUb = valorUb;
    }

    public Boolean getReqAutorizacion() {
        return this.reqAutorizacion;
    }

    public Prestacion reqAutorizacion(Boolean reqAutorizacion) {
        this.setReqAutorizacion(reqAutorizacion);
        return this;
    }

    public void setReqAutorizacion(Boolean reqAutorizacion) {
        this.reqAutorizacion = reqAutorizacion;
    }

    public Nomenclador getNomenclador() {
        return this.nomenclador;
    }

    public void setNomenclador(Nomenclador nomenclador) {
        this.nomenclador = nomenclador;
    }

    public Prestacion nomenclador(Nomenclador nomenclador) {
        this.setNomenclador(nomenclador);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prestacion)) {
            return false;
        }
        return getId() != null && getId().equals(((Prestacion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prestacion{" +
            "id=" + getId() +
            ", codigo=" + getCodigo() +
            ", codigoInos='" + getCodigoInos() + "'" +
            ", valorUb=" + getValorUb() +
            ", reqAutorizacion='" + getReqAutorizacion() + "'" +
            "}";
    }
}
