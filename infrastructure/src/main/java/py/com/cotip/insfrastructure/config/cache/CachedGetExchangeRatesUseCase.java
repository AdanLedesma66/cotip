package py.com.cotip.insfrastructure.config.cache;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import py.com.cotip.application.service.GetExchangeRatesUseCaseImpl;
import py.com.cotip.domain.model.BranchOfficeBO;
import py.com.cotip.domain.model.ExchangeRateBO;
import py.com.cotip.domain.port.in.GetExchangeRatesUseCase;
import py.com.cotip.domain.port.in.request.GetRatesQuery;

import java.util.List;

@Component
@Primary
public class CachedGetExchangeRatesUseCase implements GetExchangeRatesUseCase {

    private final GetExchangeRatesUseCaseImpl delegate;

    public CachedGetExchangeRatesUseCase(GetExchangeRatesUseCaseImpl delegate) {
        this.delegate = delegate;
    }

    @Override
    @Cacheable(value = "continental-bank", key = "'continentalResponse'")
    public List<ExchangeRateBO> findLatestContinentalBankExchangeRates() {
        return delegate.findLatestContinentalBankExchangeRates();
    }

    @Override
    @Cacheable(value = "gnb-bank", key = "'gnbResponse'")
    public List<ExchangeRateBO> findLatestGnbBankExchangeRates() {
        return delegate.findLatestGnbBankExchangeRates();
    }

    @Override
    @Cacheable(value = "maxi-exchange", key = "'maxiResponse'")
    public List<ExchangeRateBO> findLatestMaxiExchangeRates(GetRatesQuery request) {
        return delegate.findLatestMaxiExchangeRates(request);
    }

    @Override
    @Cacheable(value = "chaco-exchange", key = "'chacoResponse'")
    public List<ExchangeRateBO> findLatestCambiosChacoExchangeRates() {
        return delegate.findLatestCambiosChacoExchangeRates();
    }

    @Override
    @Cacheable(value = "chaco-exchange", key = "'chacoResponse:' + #branchOfficeId")
    public List<ExchangeRateBO> findLatestCambiosChacoExchangeRates(String branchOfficeId) {
        return delegate.findLatestCambiosChacoExchangeRates(branchOfficeId);
    }

    @Override
    @Cacheable(value = "chaco-exchange", key = "'chacoResponse:name:' + #branchOfficeName")
    public List<ExchangeRateBO> findLatestCambiosChacoExchangeRatesByBranchName(String branchOfficeName) {
        return delegate.findLatestCambiosChacoExchangeRatesByBranchName(branchOfficeName);
    }

    @Override
    @Cacheable(value = "chaco-branches", key = "'chacoBranches'" )
    public List<BranchOfficeBO> findCambiosChacoBranches() {
        return delegate.findCambiosChacoBranches();
    }
}
