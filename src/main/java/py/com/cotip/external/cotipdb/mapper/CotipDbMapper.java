package py.com.cotip.external.cotipdb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import py.com.cotip.application.rest.model.BasaDto;
import py.com.cotip.application.rest.model.FamiliarDto;
import py.com.cotip.domain.port.out.response.ContinentalResponse;
import py.com.cotip.domain.port.out.response.FamiliarResponse;
import py.com.cotip.external.cotipdb.entities.CotipEntity;

@Mapper
public interface CotipDbMapper {

    CotipDbMapper INSTANCE = Mappers.getMapper(CotipDbMapper.class);
    
    CotipEntity toFamiliarDto(FamiliarDto familiarDto);

    CotipEntity toContinentalResponse(ContinentalResponse continentalResponse);

    CotipEntity toBasaDto(BasaDto basaDto);


}
