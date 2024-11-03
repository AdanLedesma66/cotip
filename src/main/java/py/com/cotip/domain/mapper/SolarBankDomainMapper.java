package py.com.cotip.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import py.com.cotip.application.rest.model.CotipDto;
import py.com.cotip.domain.port.out.response.SolarBankResponse;

import java.util.List;

@Mapper
public interface SolarBankDomainMapper {

    // ::: INSTANCE

    SolarBankDomainMapper INSTANCE = Mappers.getMapper(SolarBankDomainMapper.class);

    // ::: mappers

    CotipDto toDto(SolarBankResponse solarBankExternal);
    List<CotipDto> toListDto(List<SolarBankResponse> solarBankExternal);

}
