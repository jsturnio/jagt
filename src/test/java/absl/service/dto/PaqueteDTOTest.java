package absl.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import absl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaqueteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaqueteDTO.class);
        PaqueteDTO paqueteDTO1 = new PaqueteDTO();
        paqueteDTO1.setId(1L);
        PaqueteDTO paqueteDTO2 = new PaqueteDTO();
        assertThat(paqueteDTO1).isNotEqualTo(paqueteDTO2);
        paqueteDTO2.setId(paqueteDTO1.getId());
        assertThat(paqueteDTO1).isEqualTo(paqueteDTO2);
        paqueteDTO2.setId(2L);
        assertThat(paqueteDTO1).isNotEqualTo(paqueteDTO2);
        paqueteDTO1.setId(null);
        assertThat(paqueteDTO1).isNotEqualTo(paqueteDTO2);
    }
}
