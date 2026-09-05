package absl.domain;

import static absl.domain.EmpleadoTestSamples.*;
import static absl.domain.OrdenTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import absl.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmpleadoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Empleado.class);
        Empleado empleado1 = getEmpleadoSample1();
        Empleado empleado2 = new Empleado();
        assertThat(empleado1).isNotEqualTo(empleado2);

        empleado2.setId(empleado1.getId());
        assertThat(empleado1).isEqualTo(empleado2);

        empleado2 = getEmpleadoSample2();
        assertThat(empleado1).isNotEqualTo(empleado2);
    }

    @Test
    void ordenTest() {
        Empleado empleado = getEmpleadoRandomSampleGenerator();
        Orden ordenBack = getOrdenRandomSampleGenerator();

        empleado.addOrden(ordenBack);
        assertThat(empleado.getOrdens()).containsOnly(ordenBack);
        assertThat(ordenBack.getUsuario()).isEqualTo(empleado);

        empleado.removeOrden(ordenBack);
        assertThat(empleado.getOrdens()).doesNotContain(ordenBack);
        assertThat(ordenBack.getUsuario()).isNull();

        empleado.ordens(new HashSet<>(Set.of(ordenBack)));
        assertThat(empleado.getOrdens()).containsOnly(ordenBack);
        assertThat(ordenBack.getUsuario()).isEqualTo(empleado);

        empleado.setOrdens(new HashSet<>());
        assertThat(empleado.getOrdens()).doesNotContain(ordenBack);
        assertThat(ordenBack.getUsuario()).isNull();
    }
}
