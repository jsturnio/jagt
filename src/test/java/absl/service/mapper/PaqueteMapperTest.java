package absl.service.mapper;

import static absl.domain.PaqueteAsserts.*;
import static absl.domain.PaqueteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaqueteMapperTest {

    private PaqueteMapper paqueteMapper;

    @BeforeEach
    void setUp() {
        paqueteMapper = new PaqueteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaqueteSample1();
        var actual = paqueteMapper.toEntity(paqueteMapper.toDto(expected));
        assertPaqueteAllPropertiesEquals(expected, actual);
    }
}
