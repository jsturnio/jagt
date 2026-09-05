package absl.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import absl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PracticaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PracticaDTO.class);
        PracticaDTO practicaDTO1 = new PracticaDTO();
        practicaDTO1.setId(1L);
        PracticaDTO practicaDTO2 = new PracticaDTO();
        assertThat(practicaDTO1).isNotEqualTo(practicaDTO2);
        practicaDTO2.setId(practicaDTO1.getId());
        assertThat(practicaDTO1).isEqualTo(practicaDTO2);
        practicaDTO2.setId(2L);
        assertThat(practicaDTO1).isNotEqualTo(practicaDTO2);
        practicaDTO1.setId(null);
        assertThat(practicaDTO1).isNotEqualTo(practicaDTO2);
    }
}
