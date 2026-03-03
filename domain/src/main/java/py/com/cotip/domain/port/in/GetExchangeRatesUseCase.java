package py.com.cotip.domain.port.in;


import py.com.cotip.domain.model.BranchOfficeBO;
import py.com.cotip.domain.model.ExchangeRateBO;
import py.com.cotip.domain.port.in.request.GetRatesQuery;

import java.util.List;

public interface GetExchangeRatesUseCase {

    List<ExchangeRateBO> findLatestContinentalBankExchangeRates();

    List<ExchangeRateBO> findLatestGnbBankExchangeRates();

    List<ExchangeRateBO> findLatestMaxiExchangeRates(GetRatesQuery request);

    List<ExchangeRateBO> findLatestCambiosChacoExchangeRates();

    List<ExchangeRateBO> findLatestCambiosChacoExchangeRates(String branchOfficeId);

    List<ExchangeRateBO> findLatestCambiosChacoExchangeRatesByBranchName(String branchOfficeName);

    List<BranchOfficeBO> findCambiosChacoBranches();


}
