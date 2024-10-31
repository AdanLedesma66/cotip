package py.com.cotip.external.cotipdb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import py.com.cotip.application.rest.model.BasaDto;
import py.com.cotip.application.rest.model.FamiliarDto;
import py.com.cotip.application.rest.model.RioDto;
import py.com.cotip.domain.port.out.response.ContinentalResponse;
import py.com.cotip.domain.port.out.response.FamiliarResponse;
import py.com.cotip.external.cotipdb.entities.CotipEntity;

import java.util.List;

@Mapper
public interface CotipDbMapper {

    CotipDbMapper INSTANCE = Mappers.getMapper(CotipDbMapper.class);
    
    CotipEntity toFamiliarDto(FamiliarDto familiarDto);

    List<CotipEntity> toListFamiliarDto(List<FamiliarDto> familiarDtoList);

    CotipEntity toContinentalResponse(ContinentalResponse continentalResponse);

    List<CotipEntity> toListContinentalResponse(List<ContinentalResponse> continentalResponse);

    CotipEntity toBasaDto(BasaDto basaDto);

    List<CotipEntity> toListBasaDto(List<BasaDto> basaDto);

    CotipEntity toRioDto(RioDto rioDto);

    List<CotipEntity> toListRioDto(List<RioDto> rioDto);


}
