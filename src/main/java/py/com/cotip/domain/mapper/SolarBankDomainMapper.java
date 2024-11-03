package py.com.cotip.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import py.com.cotip.application.rest.model.CotipDto;
import py.com.cotip.domain.port.out.response.CotipOutResponse;

import java.util.List;

@Mapper
public interface SolarBankDomainMapper {

    // ::: INSTANCE

    SolarBankDomainMapper INSTANCE = Mappers.getMapper(SolarBankDomainMapper.class);

    // ::: mappers

    CotipDto toDto(CotipOutResponse solarBankExternal);
    List<CotipDto> toListDto(List<CotipOutResponse> solarBankExternal);

}
