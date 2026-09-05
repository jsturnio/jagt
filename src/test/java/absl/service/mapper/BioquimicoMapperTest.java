package absl.service.mapper;

import static absl.domain.BioquimicoAsserts.*;
import static absl.domain.BioquimicoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BioquimicoMapperTest {

    private BioquimicoMapper bioquimicoMapper;

    @BeforeEach
    void setUp() {
        bioquimicoMapper = new BioquimicoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBioquimicoSample1();
        var actual = bioquimicoMapper.toEntity(bioquimicoMapper.toDto(expected));
        assertBioquimicoAllPropertiesEquals(expected, actual);
    }
}
