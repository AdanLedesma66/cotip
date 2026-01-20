package py.com.cotip.external.cotipdb.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import py.com.cotip.domain.port.out.response.*;
import py.com.cotip.external.cotipdb.model.CotipEntity;

import java.util.List;

@Mapper
public interface CotipDbMapper {

    CotipDbMapper INSTANCE = Mappers.getMapper(CotipDbMapper.class);
    
    CotipEntity toFamiliarBankResponse(FamiliarBankResponse familiarBankResponse);

    List<CotipEntity> toListFamiliarBankResponse(List<FamiliarBankResponse> familiarBankResponseList);

    CotipEntity toContinentalBankResponse(ContinentalBankResponse continentalResponse);

    List<CotipEntity> toListContinentalBankResponse(List<ContinentalBankResponse> continentalResponse);

    CotipEntity toGnbBankResponse(GnbBankResponse gnbBankResponse);

    List<CotipEntity> toListGnbBankResponse(List<GnbBankResponse> gnbBankResponseList);

    CotipEntity toMaxiCambiosResponse(MaxiExchangeResponse maxiCambioResponse);

    List<CotipEntity> toListMaxiCambiosResponse(List<MaxiExchangeResponse> maxiCambioResponseList);


}
