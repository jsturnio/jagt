package absl.service.mapper;

import static absl.domain.MutualAsserts.*;
import static absl.domain.MutualTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MutualMapperTest {

    private MutualMapper mutualMapper;

    @BeforeEach
    void setUp() {
        mutualMapper = new MutualMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMutualSample1();
        var actual = mutualMapper.toEntity(mutualMapper.toDto(expected));
        assertMutualAllPropertiesEquals(expected, actual);
    }
}
