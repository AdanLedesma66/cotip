package py.com.cotip.domain.port.out;

import py.com.cotip.domain.port.out.response.*;
import py.com.cotip.external.webservice.model.*;

import java.util.List;

public interface CotipOutPort {

    ContinentalBearerExternal getContinentalBearerToken();

    List<ContinentalBankResponse> fetchContinentalBankExchangeRates();

    List<GnbBankResponse> fetchGnbBankExchangeRates();

    List<MaxiExchangeResponse> fetchMaxiCambiosExchangeRates();

}
