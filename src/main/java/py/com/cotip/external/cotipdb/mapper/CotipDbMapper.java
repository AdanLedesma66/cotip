package py.com.cotip.external.cotipdb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import py.com.cotip.application.rest.model.FamiliarDto;
import py.com.cotip.application.rest.model.GnbDto;
import py.com.cotip.application.rest.model.RioDto;
import py.com.cotip.domain.port.out.response.ContinentalResponse;
import py.com.cotip.external.cotipdb.model.CotipEntity;

import java.util.List;

@Mapper
public interface CotipDbMapper {

    CotipDbMapper INSTANCE = Mappers.getMapper(CotipDbMapper.class);
    
    CotipEntity toFamiliarDto(FamiliarDto familiarDto);

    List<CotipEntity> toListFamiliarDto(List<FamiliarDto> familiarDtoList);

    CotipEntity toContinentalResponse(ContinentalResponse continentalResponse);

    List<CotipEntity> toListContinentalResponse(List<ContinentalResponse> continentalResponse);

    CotipEntity toRioDto(RioDto rioDto);

    List<CotipEntity> toListRioDto(List<RioDto> rioDto);

    CotipEntity toGnbDto(GnbDto gnbDtos);

    List<CotipEntity> toListGnbDto(List<GnbDto> gnbDtos);


}
