package absl.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import absl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NomencladorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NomencladorDTO.class);
        NomencladorDTO nomencladorDTO1 = new NomencladorDTO();
        nomencladorDTO1.setId(1L);
        NomencladorDTO nomencladorDTO2 = new NomencladorDTO();
        assertThat(nomencladorDTO1).isNotEqualTo(nomencladorDTO2);
        nomencladorDTO2.setId(nomencladorDTO1.getId());
        assertThat(nomencladorDTO1).isEqualTo(nomencladorDTO2);
        nomencladorDTO2.setId(2L);
        assertThat(nomencladorDTO1).isNotEqualTo(nomencladorDTO2);
        nomencladorDTO1.setId(null);
        assertThat(nomencladorDTO1).isNotEqualTo(nomencladorDTO2);
    }
}
