package absl.service.mapper;

import absl.domain.Mutual;
import absl.domain.PlanMutual;
import absl.service.dto.MutualDTO;
import absl.service.dto.PlanMutualDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlanMutual} and its DTO {@link PlanMutualDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlanMutualMapper extends EntityMapper<PlanMutualDTO, PlanMutual> {
    @Mapping(target = "mutual", source = "mutual", qualifiedByName = "mutualNombre")
    PlanMutualDTO toDto(PlanMutual s);

    @Named("mutualNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    MutualDTO toDtoMutualNombre(Mutual mutual);
}
