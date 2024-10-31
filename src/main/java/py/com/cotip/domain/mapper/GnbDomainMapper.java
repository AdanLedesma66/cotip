package py.com.cotip.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import py.com.cotip.application.rest.model.GnbDto;
import py.com.cotip.external.webservice.model.GnbExternal;

import java.util.List;

@Mapper
public interface GnbDomainMapper {

    GnbDomainMapper INSTANCE = Mappers.getMapper(GnbDomainMapper.class);

    GnbDto toGnbDto(GnbExternal gnbResponse);

    List<GnbDto> toListGnbDto(List<GnbExternal> gnbResponses);
}