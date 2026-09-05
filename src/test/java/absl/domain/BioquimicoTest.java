package absl.domain;

import static absl.domain.BioquimicoTestSamples.*;
import static absl.domain.OrdenTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import absl.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BioquimicoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bioquimico.class);
        Bioquimico bioquimico1 = getBioquimicoSample1();
        Bioquimico bioquimico2 = new Bioquimico();
        assertThat(bioquimico1).isNotEqualTo(bioquimico2);

        bioquimico2.setId(bioquimico1.getId());
        assertThat(bioquimico1).isEqualTo(bioquimico2);

        bioquimico2 = getBioquimicoSample2();
        assertThat(bioquimico1).isNotEqualTo(bioquimico2);
    }

    @Test
    void ordenesTest() {
        Bioquimico bioquimico = getBioquimicoRandomSampleGenerator();
        Orden ordenBack = getOrdenRandomSampleGenerator();

        bioquimico.addOrdenes(ordenBack);
        assertThat(bioquimico.getOrdeneses()).containsOnly(ordenBack);
        assertThat(ordenBack.getBioquimico()).isEqualTo(bioquimico);

        bioquimico.removeOrdenes(ordenBack);
        assertThat(bioquimico.getOrdeneses()).doesNotContain(ordenBack);
        assertThat(ordenBack.getBioquimico()).isNull();

        bioquimico.ordeneses(new HashSet<>(Set.of(ordenBack)));
        assertThat(bioquimico.getOrdeneses()).containsOnly(ordenBack);
        assertThat(ordenBack.getBioquimico()).isEqualTo(bioquimico);

        bioquimico.setOrdeneses(new HashSet<>());
        assertThat(bioquimico.getOrdeneses()).doesNotContain(ordenBack);
        assertThat(ordenBack.getBioquimico()).isNull();
    }
}
