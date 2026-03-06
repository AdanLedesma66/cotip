package py.com.cotip.insfrastructure.config.cache;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import py.com.cotip.domain.port.in.SyncExchangeRatesUseCase;

import java.util.function.IntSupplier;

@Slf4j
@Component
@ConditionalOnProperty(value = "cotip.ingestion.scheduler.enabled", havingValue = "true", matchIfMissing = true)
public class ExchangeRatesIngestionScheduler {

    private final SyncExchangeRatesUseCase syncExchangeRatesUseCase;
    private final CacheManager cacheManager;

    public ExchangeRatesIngestionScheduler(SyncExchangeRatesUseCase syncExchangeRatesUseCase,
                                           CacheManager cacheManager) {
        this.syncExchangeRatesUseCase = syncExchangeRatesUseCase;
        this.cacheManager = cacheManager;
    }

    @Scheduled(cron = "${cotip.ingestion.scheduler.continental-cron:0 */30 * * * *}")
    @SchedulerLock(name = "exchangeRatesIngestionScheduler.syncContinental",
            lockAtMostFor = "PT10M",
            lockAtLeastFor = "PT5S")
    public void syncContinentalBankRates() {
        runIngestion("CONTINENTAL_BANK",
                syncExchangeRatesUseCase::syncContinentalBankExchangeRates,
                "continental-bank");
    }

    @Scheduled(cron = "${cotip.ingestion.scheduler.gnb-cron:0 */30 * * * *}")
    @SchedulerLock(name = "exchangeRatesIngestionScheduler.syncGnb",
            lockAtMostFor = "PT10M",
            lockAtLeastFor = "PT5S")
    public void syncGnbBankRates() {
        runIngestion("GNB_BANK",
                syncExchangeRatesUseCase::syncGnbBankExchangeRates,
                "gnb-bank");
    }

    @Scheduled(cron = "${cotip.ingestion.scheduler.maxi-cron:0 */10 * * * *}")
    @SchedulerLock(name = "exchangeRatesIngestionScheduler.syncMaxi",
            lockAtMostFor = "PT10M",
            lockAtLeastFor = "PT5S")
    public void syncMaxiCambiosRates() {
        runIngestion("MAXI_CAMBIOS",
                syncExchangeRatesUseCase::syncMaxiCambiosExchangeRates,
                "maxi-exchange");
    }

    @Scheduled(cron = "${cotip.ingestion.scheduler.chaco-cron:0 */5 * * * *}")
    @SchedulerLock(name = "exchangeRatesIngestionScheduler.syncChaco",
            lockAtMostFor = "PT25M",
            lockAtLeastFor = "PT10S")
    public void syncCambiosChacoRates() {
        runIngestion("CAMBIOS_CHACO",
                syncExchangeRatesUseCase::syncCambiosChacoExchangeRatesForActiveBranches,
                "chaco-exchange");
    }

    private void runIngestion(String providerName, IntSupplier ingestion, String... cacheNames) {
        long startMillis = System.currentTimeMillis();
        try {
            int ingestedRates = ingestion.getAsInt();
            evictCaches(cacheNames);

            long elapsedMillis = System.currentTimeMillis() - startMillis;
            log.info("Ingestion finished for provider {}. rates={} elapsedMs={}",
                    providerName,
                    ingestedRates,
                    elapsedMillis);
        }
        catch (RuntimeException ex) {
            long elapsedMillis = System.currentTimeMillis() - startMillis;
            log.error("Ingestion failed for provider {} after {}ms", providerName, elapsedMillis, ex);
        }
    }

    private void evictCaches(String... cacheNames) {
        for (String cacheName : cacheNames) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
            }
        }
    }
}
