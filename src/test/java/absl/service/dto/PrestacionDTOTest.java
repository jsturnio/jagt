package absl.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import absl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrestacionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrestacionDTO.class);
        PrestacionDTO prestacionDTO1 = new PrestacionDTO();
        prestacionDTO1.setId(1L);
        PrestacionDTO prestacionDTO2 = new PrestacionDTO();
        assertThat(prestacionDTO1).isNotEqualTo(prestacionDTO2);
        prestacionDTO2.setId(prestacionDTO1.getId());
        assertThat(prestacionDTO1).isEqualTo(prestacionDTO2);
        prestacionDTO2.setId(2L);
        assertThat(prestacionDTO1).isNotEqualTo(prestacionDTO2);
        prestacionDTO1.setId(null);
        assertThat(prestacionDTO1).isNotEqualTo(prestacionDTO2);
    }
}
