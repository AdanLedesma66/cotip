package py.com.cotip.domain.port.in;


import py.com.cotip.application.rest.model.CotipDto;

import java.util.List;

public interface CotipInPort {

    List<CotipDto> findLatestContinentalBankExchangeRates() throws Exception;

    List<CotipDto> findLatestFamiliarBankExchangeRates() throws Exception;

    List<CotipDto> findLatestGnbBankExchangeRates() throws Exception;

    List<CotipDto> findLatestRioBankExchangeRates() throws Exception;

    List<CotipDto> findLatestSolarBankExchangeRates() throws Exception;

    List<CotipDto> findLatestBnfBankExchangeRates() throws Exception;

}
