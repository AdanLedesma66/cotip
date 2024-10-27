package py.com.cotip.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import py.com.cotip.application.rest.model.FamiliarDto;
import py.com.cotip.domain.port.out.response.FamiliarResponse;

import java.util.List;

@Mapper
public interface FamiliarDomainMapper {

    FamiliarDomainMapper INSTANCE = Mappers.getMapper(FamiliarDomainMapper.class);

    FamiliarDto toFamiliarDto(FamiliarResponse familiarResponse);

    List<FamiliarDto> toListFamiliarDto(List<FamiliarResponse> familiarResponses);
}
