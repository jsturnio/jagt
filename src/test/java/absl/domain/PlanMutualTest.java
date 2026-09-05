package absl.domain;

import static absl.domain.MutualTestSamples.*;
import static absl.domain.PaqueteTestSamples.*;
import static absl.domain.PlanMutualTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import absl.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PlanMutualTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanMutual.class);
        PlanMutual planMutual1 = getPlanMutualSample1();
        PlanMutual planMutual2 = new PlanMutual();
        assertThat(planMutual1).isNotEqualTo(planMutual2);

        planMutual2.setId(planMutual1.getId());
        assertThat(planMutual1).isEqualTo(planMutual2);

        planMutual2 = getPlanMutualSample2();
        assertThat(planMutual1).isNotEqualTo(planMutual2);
    }

    @Test
    void paqueteTest() {
        PlanMutual planMutual = getPlanMutualRandomSampleGenerator();
        Paquete paqueteBack = getPaqueteRandomSampleGenerator();

        planMutual.addPaquete(paqueteBack);
        assertThat(planMutual.getPaquetes()).containsOnly(paqueteBack);
        assertThat(paqueteBack.getPlan()).isEqualTo(planMutual);

        planMutual.removePaquete(paqueteBack);
        assertThat(planMutual.getPaquetes()).doesNotContain(paqueteBack);
        assertThat(paqueteBack.getPlan()).isNull();

        planMutual.paquetes(new HashSet<>(Set.of(paqueteBack)));
        assertThat(planMutual.getPaquetes()).containsOnly(paqueteBack);
        assertThat(paqueteBack.getPlan()).isEqualTo(planMutual);

        planMutual.setPaquetes(new HashSet<>());
        assertThat(planMutual.getPaquetes()).doesNotContain(paqueteBack);
        assertThat(paqueteBack.getPlan()).isNull();
    }

    @Test
    void mutualTest() {
        PlanMutual planMutual = getPlanMutualRandomSampleGenerator();
        Mutual mutualBack = getMutualRandomSampleGenerator();

        planMutual.setMutual(mutualBack);
        assertThat(planMutual.getMutual()).isEqualTo(mutualBack);

        planMutual.mutual(null);
        assertThat(planMutual.getMutual()).isNull();
    }
}
