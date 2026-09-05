package absl.domain;

import static absl.domain.MutualTestSamples.*;
import static absl.domain.NomencladorTestSamples.*;
import static absl.domain.PrestacionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import absl.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class NomencladorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Nomenclador.class);
        Nomenclador nomenclador1 = getNomencladorSample1();
        Nomenclador nomenclador2 = new Nomenclador();
        assertThat(nomenclador1).isNotEqualTo(nomenclador2);

        nomenclador2.setId(nomenclador1.getId());
        assertThat(nomenclador1).isEqualTo(nomenclador2);

        nomenclador2 = getNomencladorSample2();
        assertThat(nomenclador1).isNotEqualTo(nomenclador2);
    }

    @Test
    void prestacionTest() {
        Nomenclador nomenclador = getNomencladorRandomSampleGenerator();
        Prestacion prestacionBack = getPrestacionRandomSampleGenerator();

        nomenclador.addPrestacion(prestacionBack);
        assertThat(nomenclador.getPrestacions()).containsOnly(prestacionBack);
        assertThat(prestacionBack.getNomenclador()).isEqualTo(nomenclador);

        nomenclador.removePrestacion(prestacionBack);
        assertThat(nomenclador.getPrestacions()).doesNotContain(prestacionBack);
        assertThat(prestacionBack.getNomenclador()).isNull();

        nomenclador.prestacions(new HashSet<>(Set.of(prestacionBack)));
        assertThat(nomenclador.getPrestacions()).containsOnly(prestacionBack);
        assertThat(prestacionBack.getNomenclador()).isEqualTo(nomenclador);

        nomenclador.setPrestacions(new HashSet<>());
        assertThat(nomenclador.getPrestacions()).doesNotContain(prestacionBack);
        assertThat(prestacionBack.getNomenclador()).isNull();
    }

    @Test
    void mutualTest() {
        Nomenclador nomenclador = getNomencladorRandomSampleGenerator();
        Mutual mutualBack = getMutualRandomSampleGenerator();

        nomenclador.setMutual(mutualBack);
        assertThat(nomenclador.getMutual()).isEqualTo(mutualBack);

        nomenclador.mutual(null);
        assertThat(nomenclador.getMutual()).isNull();
    }
}
