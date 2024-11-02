package py.com.cotip.domain.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import py.com.cotip.application.rest.model.FamiliarDto;
import py.com.cotip.domain.port.out.response.FamiliarResponse;
import py.com.cotip.external.cotipdb.config.CotipBaseEntity;
import py.com.cotip.external.cotipdb.model.CotipEntity;

import java.util.List;

@Mapper
public interface FamiliarDomainMapper {

    FamiliarDomainMapper INSTANCE = Mappers.getMapper(FamiliarDomainMapper.class);

    FamiliarDto toFamiliarDto(FamiliarResponse familiarResponse);

    List<FamiliarDto> toListFamiliarDto(List<FamiliarResponse> familiarResponses);

    List<CotipEntity> toListCotipEntity(List<FamiliarDto> dtos);

    FamiliarDto toFamiliarEntity(CotipEntity cotipEntity);

    List<FamiliarDto> toListFamiliarEntities(List<CotipEntity> cotipEntities);

}
