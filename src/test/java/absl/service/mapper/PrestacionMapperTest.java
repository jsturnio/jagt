package absl.service.mapper;

import static absl.domain.PrestacionAsserts.*;
import static absl.domain.PrestacionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrestacionMapperTest {

    private PrestacionMapper prestacionMapper;

    @BeforeEach
    void setUp() {
        prestacionMapper = new PrestacionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPrestacionSample1();
        var actual = prestacionMapper.toEntity(prestacionMapper.toDto(expected));
        assertPrestacionAllPropertiesEquals(expected, actual);
    }
}
