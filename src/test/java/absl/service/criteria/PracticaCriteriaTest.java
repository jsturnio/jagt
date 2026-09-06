package absl.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PracticaCriteriaTest {

    @Test
    void newPracticaCriteriaHasAllFiltersNullTest() {
        var practicaCriteria = new PracticaCriteria();
        assertThat(practicaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void practicaCriteriaFluentMethodsCreatesFiltersTest() {
        var practicaCriteria = new PracticaCriteria();

        setAllFilters(practicaCriteria);

        assertThat(practicaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void practicaCriteriaCopyCreatesNullFilterTest() {
        var practicaCriteria = new PracticaCriteria();
        var copy = practicaCriteria.copy();

        assertThat(practicaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(practicaCriteria)
        );
    }

    @Test
    void practicaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var practicaCriteria = new PracticaCriteria();
        setAllFilters(practicaCriteria);

        var copy = practicaCriteria.copy();

        assertThat(practicaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(practicaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var practicaCriteria = new PracticaCriteria();

        assertThat(practicaCriteria).hasToString("PracticaCriteria{}");
    }

    private static void setAllFilters(PracticaCriteria practicaCriteria) {
        practicaCriteria.id();
        practicaCriteria.cantidad();
        practicaCriteria.pvalor();
        practicaCriteria.prestacionId();
        practicaCriteria.ordenId();
        practicaCriteria.distinct();
    }

    private static Condition<PracticaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCantidad()) &&
                condition.apply(criteria.getPvalor()) &&
                condition.apply(criteria.getPrestacionId()) &&
                condition.apply(criteria.getOrdenId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PracticaCriteria> copyFiltersAre(PracticaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCantidad(), copy.getCantidad()) &&
                condition.apply(criteria.getPvalor(), copy.getPvalor()) &&
                condition.apply(criteria.getPrestacionId(), copy.getPrestacionId()) &&
                condition.apply(criteria.getOrdenId(), copy.getOrdenId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
