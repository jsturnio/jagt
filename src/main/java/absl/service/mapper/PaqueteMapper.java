package absl.service.mapper;

import absl.domain.Paquete;
import absl.domain.PlanMutual;
import absl.service.dto.PaqueteDTO;
import absl.service.dto.PlanMutualDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Paquete} and its DTO {@link PaqueteDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaqueteMapper extends EntityMapper<PaqueteDTO, Paquete> {
    @Mapping(target = "plan", source = "plan", qualifiedByName = "planMutualCategoria")
    PaqueteDTO toDto(Paquete s);

    @Named("planMutualCategoria")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "categoria", source = "categoria")
    PlanMutualDTO toDtoPlanMutualCategoria(PlanMutual planMutual);
}
