package absl.domain;

import static absl.domain.NomencladorTestSamples.*;
import static absl.domain.PrestacionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import absl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrestacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prestacion.class);
        Prestacion prestacion1 = getPrestacionSample1();
        Prestacion prestacion2 = new Prestacion();
        assertThat(prestacion1).isNotEqualTo(prestacion2);

        prestacion2.setId(prestacion1.getId());
        assertThat(prestacion1).isEqualTo(prestacion2);

        prestacion2 = getPrestacionSample2();
        assertThat(prestacion1).isNotEqualTo(prestacion2);
    }

    @Test
    void nomencladorTest() {
        Prestacion prestacion = getPrestacionRandomSampleGenerator();
        Nomenclador nomencladorBack = getNomencladorRandomSampleGenerator();

        prestacion.setNomenclador(nomencladorBack);
        assertThat(prestacion.getNomenclador()).isEqualTo(nomencladorBack);

        prestacion.nomenclador(null);
        assertThat(prestacion.getNomenclador()).isNull();
    }
}
