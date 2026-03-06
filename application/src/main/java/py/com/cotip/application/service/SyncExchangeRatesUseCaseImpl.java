package py.com.cotip.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.model.BranchOfficeBO;
import py.com.cotip.domain.model.ExchangeRateBO;
import py.com.cotip.domain.port.in.SyncExchangeRatesUseCase;
import py.com.cotip.domain.port.out.ApplicationMetricsPort;
import py.com.cotip.domain.port.out.BranchOfficeQueryPort;
import py.com.cotip.domain.port.out.ExchangeRateRepositoryPort;
import py.com.cotip.domain.port.out.ProviderRatesSourcePort;
import py.com.cotip.domain.port.out.response.ChacoExchangeResponse;
import py.com.cotip.domain.port.out.response.ContinentalBankResponse;
import py.com.cotip.domain.port.out.response.GnbBankResponse;
import py.com.cotip.domain.port.out.response.MaxiExchangeResponse;

import java.util.ArrayList;
import java.util.List;

public class SyncExchangeRatesUseCaseImpl implements SyncExchangeRatesUseCase {

    private static final Logger log = LoggerFactory.getLogger(SyncExchangeRatesUseCaseImpl.class);

    private final ProviderRatesSourcePort providerRatesSourcePort;
    private final ExchangeRateRepositoryPort exchangeRateRepositoryPort;
    private final BranchOfficeQueryPort branchOfficeQueryPort;
    private final ApplicationMetricsPort metricsPort;

    public SyncExchangeRatesUseCaseImpl(ProviderRatesSourcePort providerRatesSourcePort,
                                        ExchangeRateRepositoryPort exchangeRateRepositoryPort,
                                        BranchOfficeQueryPort branchOfficeQueryPort,
                                        ApplicationMetricsPort metricsPort) {
        this.providerRatesSourcePort = providerRatesSourcePort;
        this.exchangeRateRepositoryPort = exchangeRateRepositoryPort;
        this.branchOfficeQueryPort = branchOfficeQueryPort;
        this.metricsPort = metricsPort;
    }

    @Override
    public int syncContinentalBankExchangeRates() {
        return saveAllCotipEntities(
                fromContinental(providerRatesSourcePort.fetchContinentalBankExchangeRates()),
                ProviderType.CONTINENTAL_BANK
        );
    }

    @Override
    public int syncGnbBankExchangeRates() {
        return saveAllCotipEntities(
                fromGnb(providerRatesSourcePort.fetchGnbBankExchangeRates()),
                ProviderType.GNB_BANK
        );
    }

    @Override
    public int syncMaxiCambiosExchangeRates() {
        return saveAllCotipEntities(
                fromMaxi(providerRatesSourcePort.fetchMaxiCambiosExchangeRates()),
                ProviderType.MAXI_CAMBIOS
        );
    }

    @Override
    public int syncCambiosChacoExchangeRatesForActiveBranches() {
        List<BranchOfficeBO> activeBranches = branchOfficeQueryPort.findAllCambiosChacoBranches();

        if (activeBranches == null || activeBranches.isEmpty()) {
            log.warn("No active Cambios Chaco branches found in DB. Falling back to default branch ingestion");
            return syncCambiosChacoDefaultBranch();
        }

        List<ExchangeRateBO> allRates = new ArrayList<>();
        int successfulBranches = 0;

        for (BranchOfficeBO branch : activeBranches) {
            String branchOfficeId = branch.externalBranchId();
            if (branchOfficeId == null || branchOfficeId.isBlank()) {
                continue;
            }

            try {
                List<ChacoExchangeResponse> rates = providerRatesSourcePort.fetchCambiosChacoExchangeRates(branchOfficeId);
                allRates.addAll(fromChaco(rates));
                successfulBranches++;
            }
            catch (RuntimeException ex) {
                log.warn("Failed to scrape Cambios Chaco branch {}", branchOfficeId, ex);
            }
        }

        if (allRates.isEmpty()) {
            log.warn("No Cambios Chaco branch ingestion succeeded. Falling back to default branch");
            return syncCambiosChacoDefaultBranch();
        }

        int ingested = saveAllCotipEntities(allRates, ProviderType.CAMBIOS_CHACO);
        log.info("Cambios Chaco ingestion completed. Successful branches: {}, rates ingested: {}",
                successfulBranches,
                ingested);
        return ingested;
    }

    private int syncCambiosChacoDefaultBranch() {
        return saveAllCotipEntities(
                fromChaco(providerRatesSourcePort.fetchCambiosChacoExchangeRates()),
                ProviderType.CAMBIOS_CHACO
        );
    }

    private int saveAllCotipEntities(List<ExchangeRateBO> exchangeRates, ProviderType providerType) {
        if (exchangeRates == null || exchangeRates.isEmpty()) {
            log.warn("No exchange rates to ingest for provider {}", providerType);
            return 0;
        }

        exchangeRateRepositoryPort.saveAllExchangeRates(exchangeRates, providerType);
        metricsPort.recordIngested(providerType, exchangeRates.size());
        return exchangeRates.size();
    }

    private List<ExchangeRateBO> fromContinental(List<ContinentalBankResponse> rates) {
        return rates.stream()
                .map(rate -> ExchangeRateBO.builder()
                        .exchangeRate(rate.getExchangeRate())
                        .currencyCode(rate.getCurrencyCode())
                        .quoteModality(rate.getQuoteModality())
                        .buyRate(rate.getBuyRate())
                        .sellRate(rate.getSellRate())
                        .build())
                .toList();
    }

    private List<ExchangeRateBO> fromGnb(List<GnbBankResponse> rates) {
        return rates.stream()
                .map(rate -> ExchangeRateBO.builder()
                        .exchangeRate(rate.getExchangeRate())
                        .currencyCode(rate.getCurrencyCode())
                        .quoteModality(rate.getQuoteModality())
                        .buyRate(rate.getBuyRate())
                        .sellRate(rate.getSellRate())
                        .build())
                .toList();
    }

    private List<ExchangeRateBO> fromMaxi(List<MaxiExchangeResponse> rates) {
        return rates.stream()
                .map(rate -> ExchangeRateBO.builder()
                        .exchangeRate(rate.getExchangeRate())
                        .currencyCode(rate.getCurrencyCode())
                        .quoteModality(rate.getQuoteModality())
                        .buyRate(rate.getBuyRate())
                        .sellRate(rate.getSellRate())
                        .branchOffice(rate.getCity())
                        .city(rate.getCity())
                        .build())
                .toList();
    }

    private List<ExchangeRateBO> fromChaco(List<ChacoExchangeResponse> rates) {
        return rates.stream()
                .map(rate -> ExchangeRateBO.builder()
                        .exchangeRate(rate.getExchangeRate())
                        .currencyCode(rate.getCurrencyCode())
                        .quoteModality(rate.getQuoteModality())
                        .buyRate(rate.getBuyRate())
                        .sellRate(rate.getSellRate())
                        .branchOffice(rate.getBranchOffice())
                        .branchOfficeExternalId(rate.getBranchOfficeExternalId())
                        .build())
                .toList();
    }
}
