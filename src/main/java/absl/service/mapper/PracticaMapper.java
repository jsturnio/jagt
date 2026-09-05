package absl.service.mapper;

import absl.domain.Orden;
import absl.domain.Practica;
import absl.domain.Prestacion;
import absl.service.dto.OrdenDTO;
import absl.service.dto.PracticaDTO;
import absl.service.dto.PrestacionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Practica} and its DTO {@link PracticaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PracticaMapper extends EntityMapper<PracticaDTO, Practica> {
    @Mapping(target = "prestacion", source = "prestacion", qualifiedByName = "prestacionCodigo")
    @Mapping(target = "orden", source = "orden", qualifiedByName = "ordenId")
    PracticaDTO toDto(Practica s);

    @Named("prestacionCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    PrestacionDTO toDtoPrestacionCodigo(Prestacion prestacion);

    @Named("ordenId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrdenDTO toDtoOrdenId(Orden orden);
}
