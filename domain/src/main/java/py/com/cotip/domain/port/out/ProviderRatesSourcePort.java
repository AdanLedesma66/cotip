package py.com.cotip.domain.port.out;

import py.com.cotip.domain.port.out.response.*;

import java.util.List;

public interface ProviderRatesSourcePort {

    List<ContinentalBankResponse> fetchContinentalBankExchangeRates();

    List<GnbBankResponse> fetchGnbBankExchangeRates();

    List<MaxiExchangeResponse> fetchMaxiCambiosExchangeRates();

    List<ChacoExchangeResponse> fetchCambiosChacoExchangeRates();

    List<ChacoExchangeResponse> fetchCambiosChacoExchangeRates(String branchOfficeId);

}
