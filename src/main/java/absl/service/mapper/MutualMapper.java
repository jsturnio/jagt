package absl.service.mapper;

import absl.domain.Mutual;
import absl.service.dto.MutualDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Mutual} and its DTO {@link MutualDTO}.
 */
@Mapper(componentModel = "spring")
public interface MutualMapper extends EntityMapper<MutualDTO, Mutual> {}
