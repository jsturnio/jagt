package absl.domain;

import static absl.domain.OrdenTestSamples.*;
import static absl.domain.PracticaTestSamples.*;
import static absl.domain.PrestacionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import absl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PracticaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Practica.class);
        Practica practica1 = getPracticaSample1();
        Practica practica2 = new Practica();
        assertThat(practica1).isNotEqualTo(practica2);

        practica2.setId(practica1.getId());
        assertThat(practica1).isEqualTo(practica2);

        practica2 = getPracticaSample2();
        assertThat(practica1).isNotEqualTo(practica2);
    }

    @Test
    void prestacionTest() {
        Practica practica = getPracticaRandomSampleGenerator();
        Prestacion prestacionBack = getPrestacionRandomSampleGenerator();

        practica.setPrestacion(prestacionBack);
        assertThat(practica.getPrestacion()).isEqualTo(prestacionBack);

        practica.prestacion(null);
        assertThat(practica.getPrestacion()).isNull();
    }

    @Test
    void ordenTest() {
        Practica practica = getPracticaRandomSampleGenerator();
        Orden ordenBack = getOrdenRandomSampleGenerator();

        practica.setOrden(ordenBack);
        assertThat(practica.getOrden()).isEqualTo(ordenBack);

        practica.orden(null);
        assertThat(practica.getOrden()).isNull();
    }
}
