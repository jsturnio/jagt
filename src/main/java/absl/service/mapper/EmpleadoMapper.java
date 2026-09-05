package absl.service.mapper;

import absl.domain.Empleado;
import absl.domain.User;
import absl.service.dto.EmpleadoDTO;
import absl.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Empleado} and its DTO {@link EmpleadoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmpleadoMapper extends EntityMapper<EmpleadoDTO, Empleado> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    EmpleadoDTO toDto(Empleado s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
