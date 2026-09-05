package absl.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import absl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BioquimicoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BioquimicoDTO.class);
        BioquimicoDTO bioquimicoDTO1 = new BioquimicoDTO();
        bioquimicoDTO1.setId(1L);
        BioquimicoDTO bioquimicoDTO2 = new BioquimicoDTO();
        assertThat(bioquimicoDTO1).isNotEqualTo(bioquimicoDTO2);
        bioquimicoDTO2.setId(bioquimicoDTO1.getId());
        assertThat(bioquimicoDTO1).isEqualTo(bioquimicoDTO2);
        bioquimicoDTO2.setId(2L);
        assertThat(bioquimicoDTO1).isNotEqualTo(bioquimicoDTO2);
        bioquimicoDTO1.setId(null);
        assertThat(bioquimicoDTO1).isNotEqualTo(bioquimicoDTO2);
    }
}
