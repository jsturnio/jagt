package absl.service.mapper;

import static absl.domain.PlanMutualAsserts.*;
import static absl.domain.PlanMutualTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlanMutualMapperTest {

    private PlanMutualMapper planMutualMapper;

    @BeforeEach
    void setUp() {
        planMutualMapper = new PlanMutualMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPlanMutualSample1();
        var actual = planMutualMapper.toEntity(planMutualMapper.toDto(expected));
        assertPlanMutualAllPropertiesEquals(expected, actual);
    }
}
