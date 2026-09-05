package absl.service.mapper;

import absl.domain.Bioquimico;
import absl.domain.User;
import absl.service.dto.BioquimicoDTO;
import absl.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bioquimico} and its DTO {@link BioquimicoDTO}.
 */
@Mapper(componentModel = "spring")
public interface BioquimicoMapper extends EntityMapper<BioquimicoDTO, Bioquimico> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    BioquimicoDTO toDto(Bioquimico s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
