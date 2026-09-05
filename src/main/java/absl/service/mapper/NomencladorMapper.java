package absl.service.mapper;

import absl.domain.Mutual;
import absl.domain.Nomenclador;
import absl.service.dto.MutualDTO;
import absl.service.dto.NomencladorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Nomenclador} and its DTO {@link NomencladorDTO}.
 */
@Mapper(componentModel = "spring")
public interface NomencladorMapper extends EntityMapper<NomencladorDTO, Nomenclador> {
    @Mapping(target = "mutual", source = "mutual", qualifiedByName = "mutualNombre")
    NomencladorDTO toDto(Nomenclador s);

    @Named("mutualNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    MutualDTO toDtoMutualNombre(Mutual mutual);
}
