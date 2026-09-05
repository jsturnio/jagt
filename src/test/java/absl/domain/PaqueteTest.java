package absl.domain;

import static absl.domain.OrdenTestSamples.*;
import static absl.domain.PaqueteTestSamples.*;
import static absl.domain.PlanMutualTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import absl.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PaqueteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paquete.class);
        Paquete paquete1 = getPaqueteSample1();
        Paquete paquete2 = new Paquete();
        assertThat(paquete1).isNotEqualTo(paquete2);

        paquete2.setId(paquete1.getId());
        assertThat(paquete1).isEqualTo(paquete2);

        paquete2 = getPaqueteSample2();
        assertThat(paquete1).isNotEqualTo(paquete2);
    }

    @Test
    void ordenesTest() {
        Paquete paquete = getPaqueteRandomSampleGenerator();
        Orden ordenBack = getOrdenRandomSampleGenerator();

        paquete.addOrdenes(ordenBack);
        assertThat(paquete.getOrdeneses()).containsOnly(ordenBack);
        assertThat(ordenBack.getPaquete()).isEqualTo(paquete);

        paquete.removeOrdenes(ordenBack);
        assertThat(paquete.getOrdeneses()).doesNotContain(ordenBack);
        assertThat(ordenBack.getPaquete()).isNull();

        paquete.ordeneses(new HashSet<>(Set.of(ordenBack)));
        assertThat(paquete.getOrdeneses()).containsOnly(ordenBack);
        assertThat(ordenBack.getPaquete()).isEqualTo(paquete);

        paquete.setOrdeneses(new HashSet<>());
        assertThat(paquete.getOrdeneses()).doesNotContain(ordenBack);
        assertThat(ordenBack.getPaquete()).isNull();
    }

    @Test
    void planTest() {
        Paquete paquete = getPaqueteRandomSampleGenerator();
        PlanMutual planMutualBack = getPlanMutualRandomSampleGenerator();

        paquete.setPlan(planMutualBack);
        assertThat(paquete.getPlan()).isEqualTo(planMutualBack);

        paquete.plan(null);
        assertThat(paquete.getPlan()).isNull();
    }
}
