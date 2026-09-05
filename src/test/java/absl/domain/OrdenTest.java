package absl.domain;

import static absl.domain.BioquimicoTestSamples.*;
import static absl.domain.EmpleadoTestSamples.*;
import static absl.domain.OrdenTestSamples.*;
import static absl.domain.PaqueteTestSamples.*;
import static absl.domain.PracticaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import absl.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrdenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Orden.class);
        Orden orden1 = getOrdenSample1();
        Orden orden2 = new Orden();
        assertThat(orden1).isNotEqualTo(orden2);

        orden2.setId(orden1.getId());
        assertThat(orden1).isEqualTo(orden2);

        orden2 = getOrdenSample2();
        assertThat(orden1).isNotEqualTo(orden2);
    }

    @Test
    void practicasTest() {
        Orden orden = getOrdenRandomSampleGenerator();
        Practica practicaBack = getPracticaRandomSampleGenerator();

        orden.addPracticas(practicaBack);
        assertThat(orden.getPracticases()).containsOnly(practicaBack);
        assertThat(practicaBack.getOrden()).isEqualTo(orden);

        orden.removePracticas(practicaBack);
        assertThat(orden.getPracticases()).doesNotContain(practicaBack);
        assertThat(practicaBack.getOrden()).isNull();

        orden.practicases(new HashSet<>(Set.of(practicaBack)));
        assertThat(orden.getPracticases()).containsOnly(practicaBack);
        assertThat(practicaBack.getOrden()).isEqualTo(orden);

        orden.setPracticases(new HashSet<>());
        assertThat(orden.getPracticases()).doesNotContain(practicaBack);
        assertThat(practicaBack.getOrden()).isNull();
    }

    @Test
    void paqueteTest() {
        Orden orden = getOrdenRandomSampleGenerator();
        Paquete paqueteBack = getPaqueteRandomSampleGenerator();

        orden.setPaquete(paqueteBack);
        assertThat(orden.getPaquete()).isEqualTo(paqueteBack);

        orden.paquete(null);
        assertThat(orden.getPaquete()).isNull();
    }

    @Test
    void usuarioTest() {
        Orden orden = getOrdenRandomSampleGenerator();
        Empleado empleadoBack = getEmpleadoRandomSampleGenerator();

        orden.setUsuario(empleadoBack);
        assertThat(orden.getUsuario()).isEqualTo(empleadoBack);

        orden.usuario(null);
        assertThat(orden.getUsuario()).isNull();
    }

    @Test
    void bioquimicoTest() {
        Orden orden = getOrdenRandomSampleGenerator();
        Bioquimico bioquimicoBack = getBioquimicoRandomSampleGenerator();

        orden.setBioquimico(bioquimicoBack);
        assertThat(orden.getBioquimico()).isEqualTo(bioquimicoBack);

        orden.bioquimico(null);
        assertThat(orden.getBioquimico()).isNull();
    }
}
