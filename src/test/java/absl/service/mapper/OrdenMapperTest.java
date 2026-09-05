package absl.service.mapper;

import static absl.domain.OrdenAsserts.*;
import static absl.domain.OrdenTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrdenMapperTest {

    private OrdenMapper ordenMapper;

    @BeforeEach
    void setUp() {
        ordenMapper = new OrdenMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrdenSample1();
        var actual = ordenMapper.toEntity(ordenMapper.toDto(expected));
        assertOrdenAllPropertiesEquals(expected, actual);
    }
}
