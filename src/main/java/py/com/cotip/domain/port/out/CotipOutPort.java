package py.com.cotip.domain.port.out;

import py.com.cotip.domain.port.out.response.*;
import py.com.cotip.external.webservice.model.*;

import java.util.List;

public interface CotipOutPort {

    ContinentalBearerExternal getContinentalBearerToken();

    List<ContinentalBankResponse> fetchContinentalBankExchangeRates();

    List<FamiliarBankResponse> fetchFamiliarBankExchangeRates();

    List<GnbBankResponse> fetchGnbBankExchangeRates();

    List<RioBankResponse> fetchRioBankExchangeRates();

    List<SolarBankResponse> fetchSolarBankExchangeRates();

    List<BnfBankResponse> fetchBnfBankExchangeRates();

    List<AtlasBankResponse> fetchAtlasBankExchangeRates();

    List<FicFinancialResponse> fetchFicFinancialExchangeRates() throws Exception;

}
