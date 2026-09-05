package absl.service.mapper;

import static absl.domain.NomencladorAsserts.*;
import static absl.domain.NomencladorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NomencladorMapperTest {

    private NomencladorMapper nomencladorMapper;

    @BeforeEach
    void setUp() {
        nomencladorMapper = new NomencladorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNomencladorSample1();
        var actual = nomencladorMapper.toEntity(nomencladorMapper.toDto(expected));
        assertNomencladorAllPropertiesEquals(expected, actual);
    }
}
