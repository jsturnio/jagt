package absl.service.mapper;

import absl.domain.Nomenclador;
import absl.domain.Prestacion;
import absl.service.dto.NomencladorDTO;
import absl.service.dto.PrestacionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Prestacion} and its DTO {@link PrestacionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PrestacionMapper extends EntityMapper<PrestacionDTO, Prestacion> {
    @Mapping(target = "nomenclador", source = "nomenclador", qualifiedByName = "nomencladorNombre")
    PrestacionDTO toDto(Prestacion s);

    @Named("nomencladorNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    NomencladorDTO toDtoNomencladorNombre(Nomenclador nomenclador);
}
