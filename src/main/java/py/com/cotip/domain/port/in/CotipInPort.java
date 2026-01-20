package py.com.cotip.domain.port.in;


import py.com.cotip.application.rest.model.CotipDto;
import py.com.cotip.domain.port.in.request.FindMaxiExchangeRateRequest;

import java.util.List;

public interface CotipInPort {

    List<CotipDto> findLatestContinentalBankExchangeRates();

    List<CotipDto> findLatestGnbBankExchangeRates();

    List<CotipDto> findLatestMaxiExchangeRates(FindMaxiExchangeRateRequest request);


}
