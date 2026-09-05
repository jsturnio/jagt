package absl.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import absl.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlanMutualDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanMutualDTO.class);
        PlanMutualDTO planMutualDTO1 = new PlanMutualDTO();
        planMutualDTO1.setId(1L);
        PlanMutualDTO planMutualDTO2 = new PlanMutualDTO();
        assertThat(planMutualDTO1).isNotEqualTo(planMutualDTO2);
        planMutualDTO2.setId(planMutualDTO1.getId());
        assertThat(planMutualDTO1).isEqualTo(planMutualDTO2);
        planMutualDTO2.setId(2L);
        assertThat(planMutualDTO1).isNotEqualTo(planMutualDTO2);
        planMutualDTO1.setId(null);
        assertThat(planMutualDTO1).isNotEqualTo(planMutualDTO2);
    }
}
