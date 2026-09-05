package absl.domain;

import static absl.domain.MutualTestSamples.*;
import static absl.domain.NomencladorTestSamples.*;
import static absl.domain.PlanMutualTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import absl.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MutualTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mutual.class);
        Mutual mutual1 = getMutualSample1();
        Mutual mutual2 = new Mutual();
        assertThat(mutual1).isNotEqualTo(mutual2);

        mutual2.setId(mutual1.getId());
        assertThat(mutual1).isEqualTo(mutual2);

        mutual2 = getMutualSample2();
        assertThat(mutual1).isNotEqualTo(mutual2);
    }

    @Test
    void nomencladorTest() {
        Mutual mutual = getMutualRandomSampleGenerator();
        Nomenclador nomencladorBack = getNomencladorRandomSampleGenerator();

        mutual.addNomenclador(nomencladorBack);
        assertThat(mutual.getNomencladors()).containsOnly(nomencladorBack);
        assertThat(nomencladorBack.getMutual()).isEqualTo(mutual);

        mutual.removeNomenclador(nomencladorBack);
        assertThat(mutual.getNomencladors()).doesNotContain(nomencladorBack);
        assertThat(nomencladorBack.getMutual()).isNull();

        mutual.nomencladors(new HashSet<>(Set.of(nomencladorBack)));
        assertThat(mutual.getNomencladors()).containsOnly(nomencladorBack);
        assertThat(nomencladorBack.getMutual()).isEqualTo(mutual);

        mutual.setNomencladors(new HashSet<>());
        assertThat(mutual.getNomencladors()).doesNotContain(nomencladorBack);
        assertThat(nomencladorBack.getMutual()).isNull();
    }

    @Test
    void planTest() {
        Mutual mutual = getMutualRandomSampleGenerator();
        PlanMutual planMutualBack = getPlanMutualRandomSampleGenerator();

        mutual.addPlan(planMutualBack);
        assertThat(mutual.getPlans()).containsOnly(planMutualBack);
        assertThat(planMutualBack.getMutual()).isEqualTo(mutual);

        mutual.removePlan(planMutualBack);
        assertThat(mutual.getPlans()).doesNotContain(planMutualBack);
        assertThat(planMutualBack.getMutual()).isNull();

        mutual.plans(new HashSet<>(Set.of(planMutualBack)));
        assertThat(mutual.getPlans()).containsOnly(planMutualBack);
        assertThat(planMutualBack.getMutual()).isEqualTo(mutual);

        mutual.setPlans(new HashSet<>());
        assertThat(mutual.getPlans()).doesNotContain(planMutualBack);
        assertThat(planMutualBack.getMutual()).isNull();
    }
}
