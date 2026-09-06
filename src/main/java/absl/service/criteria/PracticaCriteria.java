package absl.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link absl.domain.Practica} entity. This class is used
 * in {@link absl.web.rest.PracticaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /practicas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PracticaCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter cantidad;

    private BigDecimalFilter pvalor;

    private LongFilter prestacionId;

    private LongFilter ordenId;

    private Boolean distinct;

    public PracticaCriteria() {}

    public PracticaCriteria(PracticaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.cantidad = other.optionalCantidad().map(IntegerFilter::copy).orElse(null);
        this.pvalor = other.optionalPvalor().map(BigDecimalFilter::copy).orElse(null);
        this.prestacionId = other.optionalPrestacionId().map(LongFilter::copy).orElse(null);
        this.ordenId = other.optionalOrdenId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PracticaCriteria copy() {
        return new PracticaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getCantidad() {
        return cantidad;
    }

    public Optional<IntegerFilter> optionalCantidad() {
        return Optional.ofNullable(cantidad);
    }

    public IntegerFilter cantidad() {
        if (cantidad == null) {
            setCantidad(new IntegerFilter());
        }
        return cantidad;
    }

    public void setCantidad(IntegerFilter cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimalFilter getPvalor() {
        return pvalor;
    }

    public Optional<BigDecimalFilter> optionalPvalor() {
        return Optional.ofNullable(pvalor);
    }

    public BigDecimalFilter pvalor() {
        if (pvalor == null) {
            setPvalor(new BigDecimalFilter());
        }
        return pvalor;
    }

    public void setPvalor(BigDecimalFilter pvalor) {
        this.pvalor = pvalor;
    }

    public LongFilter getPrestacionId() {
        return prestacionId;
    }

    public Optional<LongFilter> optionalPrestacionId() {
        return Optional.ofNullable(prestacionId);
    }

    public LongFilter prestacionId() {
        if (prestacionId == null) {
            setPrestacionId(new LongFilter());
        }
        return prestacionId;
    }

    public void setPrestacionId(LongFilter prestacionId) {
        this.prestacionId = prestacionId;
    }

    public LongFilter getOrdenId() {
        return ordenId;
    }

    public Optional<LongFilter> optionalOrdenId() {
        return Optional.ofNullable(ordenId);
    }

    public LongFilter ordenId() {
        if (ordenId == null) {
            setOrdenId(new LongFilter());
        }
        return ordenId;
    }

    public void setOrdenId(LongFilter ordenId) {
        this.ordenId = ordenId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PracticaCriteria that = (PracticaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cantidad, that.cantidad) &&
            Objects.equals(pvalor, that.pvalor) &&
            Objects.equals(prestacionId, that.prestacionId) &&
            Objects.equals(ordenId, that.ordenId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cantidad, pvalor, prestacionId, ordenId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PracticaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCantidad().map(f -> "cantidad=" + f + ", ").orElse("") +
            optionalPvalor().map(f -> "pvalor=" + f + ", ").orElse("") +
            optionalPrestacionId().map(f -> "prestacionId=" + f + ", ").orElse("") +
            optionalOrdenId().map(f -> "ordenId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
