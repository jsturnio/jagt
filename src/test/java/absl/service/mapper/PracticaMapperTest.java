package absl.service.mapper;

import static absl.domain.PracticaAsserts.*;
import static absl.domain.PracticaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PracticaMapperTest {

    private PracticaMapper practicaMapper;

    @BeforeEach
    void setUp() {
        practicaMapper = new PracticaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPracticaSample1();
        var actual = practicaMapper.toEntity(practicaMapper.toDto(expected));
        assertPracticaAllPropertiesEquals(expected, actual);
    }
}
