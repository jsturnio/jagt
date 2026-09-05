package absl.service.mapper;

import absl.domain.Bioquimico;
import absl.domain.Empleado;
import absl.domain.Orden;
import absl.domain.Paquete;
import absl.service.dto.BioquimicoDTO;
import absl.service.dto.EmpleadoDTO;
import absl.service.dto.OrdenDTO;
import absl.service.dto.PaqueteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Orden} and its DTO {@link OrdenDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrdenMapper extends EntityMapper<OrdenDTO, Orden> {
    @Mapping(target = "paquete", source = "paquete", qualifiedByName = "paquetePaqDescrip")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "empleadoId")
    @Mapping(target = "bioquimico", source = "bioquimico", qualifiedByName = "bioquimicoNombreCompleto")
    OrdenDTO toDto(Orden s);

    @Named("paquetePaqDescrip")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "paqDescrip", source = "paqDescrip")
    PaqueteDTO toDtoPaquetePaqDescrip(Paquete paquete);

    @Named("empleadoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmpleadoDTO toDtoEmpleadoId(Empleado empleado);

    @Named("bioquimicoNombreCompleto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombreCompleto", source = "nombreCompleto")
    BioquimicoDTO toDtoBioquimicoNombreCompleto(Bioquimico bioquimico);
}
