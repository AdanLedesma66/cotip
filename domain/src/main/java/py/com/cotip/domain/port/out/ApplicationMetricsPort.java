package py.com.cotip.domain.port.out;

import py.com.cotip.domain.model.ExchangeRateBO;
import py.com.cotip.domain.commons.ProviderType;

import java.util.List;

public interface ApplicationMetricsPort {

    void recordScrapeSuccess(ProviderType provider, long startNanos);

    void recordScrapeFailure(ProviderType provider, String errorType, long startNanos);

    void recordIngested(ProviderType provider, int amount);

    void recordFreshness(ProviderType provider, List<ExchangeRateBO> rates);
}
