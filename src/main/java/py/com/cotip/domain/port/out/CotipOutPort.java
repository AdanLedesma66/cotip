package py.com.cotip.domain.port.out;

import py.com.cotip.domain.port.out.response.*;
import py.com.cotip.external.webservice.model.*;

import java.util.List;

public interface CotipOutPort {

    ContinentalBearerExternal getContinentalBearerToken() throws Exception;

    List<ContinentalBankResponse> fetchContinentalBankExchangeRates() throws Exception;

    List<FamiliarBankResponse> fetchFamiliarBankExchangeRates() throws Exception;

    List<GnbBankResponse> fetchGnbBankExchangeRates() throws Exception;

    List<RioBankResponse> fetchRioBankExchangeRates() throws Exception;

    List<SolarBankResponse> fetchSolarBankExchangeRates() throws Exception;

    List<BnfBankResponse> fetchBnfBankExchangeRates() throws Exception;

}
