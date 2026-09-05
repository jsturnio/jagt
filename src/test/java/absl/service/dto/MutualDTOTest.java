package absl.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import absl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MutualDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MutualDTO.class);
        MutualDTO mutualDTO1 = new MutualDTO();
        mutualDTO1.setId(1L);
        MutualDTO mutualDTO2 = new MutualDTO();
        assertThat(mutualDTO1).isNotEqualTo(mutualDTO2);
        mutualDTO2.setId(mutualDTO1.getId());
        assertThat(mutualDTO1).isEqualTo(mutualDTO2);
        mutualDTO2.setId(2L);
        assertThat(mutualDTO1).isNotEqualTo(mutualDTO2);
        mutualDTO1.setId(null);
        assertThat(mutualDTO1).isNotEqualTo(mutualDTO2);
    }
}
