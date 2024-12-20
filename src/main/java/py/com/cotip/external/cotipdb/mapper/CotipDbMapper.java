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

    CotipEntity toRioBankResponse(RioBankResponse rioBankResponse);

    List<CotipEntity> toListRioBankResponse(List<RioBankResponse> rioBankResponseList);

    CotipEntity toGnbBankResponse(GnbBankResponse gnbBankResponse);

    List<CotipEntity> toListGnbBankResponse(List<GnbBankResponse> gnbBankResponseList);

    CotipEntity toSolarBankResponse(SolarBankResponse solarBankResponse);

    List<CotipEntity> toListSolarBankResponse(List<SolarBankResponse> solarBankResponseList);

    CotipEntity toBnfBankResponse(BnfBankResponse bnfBankResponse);

    List<CotipEntity> toListBnfBankResponse(List<BnfBankResponse> bankResponseList);

    CotipEntity toAtlasBankResponse(AtlasBankResponse atlasBankResponse);

    List<CotipEntity> toListAtlasBankResponse(List<AtlasBankResponse> atlasBankResponseList);

    CotipEntity toFicFinancialResponse(FicFinancialResponse ficFinancialResponse);

    List<CotipEntity> toListFicFinancialResponse(List<FicFinancialResponse> ficFinancialResponseList);

    CotipEntity toMaxiCambiosResponse(MaxiExchangeResponse maxiCambioResponse);

    List<CotipEntity> toListMaxiCambiosResponse(List<MaxiExchangeResponse> maxiCambioResponseList);


}
