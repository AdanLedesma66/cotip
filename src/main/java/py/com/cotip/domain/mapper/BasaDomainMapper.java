package py.com.cotip.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import py.com.cotip.application.rest.model.BasaDto;
import py.com.cotip.external.webservice.model.BasaExternal;

import java.util.List;

@Mapper
public interface BasaDomainMapper {

    // ::: INSTANCE
    BasaDomainMapper INSTANCE = Mappers.getMapper(BasaDomainMapper.class);

    // ::: mappers

    BasaDto toBasaDto(BasaExternal basaExternal);

    List<BasaDto> toListBasaDto(List<BasaExternal> basaExternals);

}
