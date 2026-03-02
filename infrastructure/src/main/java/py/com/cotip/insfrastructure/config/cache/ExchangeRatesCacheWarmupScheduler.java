package py.com.cotip.insfrastructure.config.cache;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import py.com.cotip.domain.port.out.ProviderRatesSourcePort;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public class ExchangeRatesCacheWarmupScheduler {

    private final ProviderRatesSourcePort providerRatesSourcePort;
    private final ExecutorService virtualThreadExecutor;

    public ExchangeRatesCacheWarmupScheduler(ProviderRatesSourcePort providerRatesSourcePort,
                                             ExecutorService virtualThreadExecutor) {
        this.providerRatesSourcePort = providerRatesSourcePort;
        this.virtualThreadExecutor = virtualThreadExecutor;
    }

    @Scheduled(cron = "0 0 */6 * * *")
    @SchedulerLock(name = "exchangeRatesCacheWarmupScheduler.warmupProviders", lockAtMostFor = "PT10M", lockAtLeastFor = "PT15S")
    public void warmupProviders() {
        log.info("Executing exchange rates warmup every 6 hours");

        List<Runnable> tasks = List.of(
                providerRatesSourcePort::fetchContinentalBankExchangeRates,
                providerRatesSourcePort::fetchGnbBankExchangeRates,
                providerRatesSourcePort::fetchMaxiCambiosExchangeRates,
                providerRatesSourcePort::fetchCambiosChacoExchangeRates
        );

        tasks.forEach(virtualThreadExecutor::submit);
    }
}
