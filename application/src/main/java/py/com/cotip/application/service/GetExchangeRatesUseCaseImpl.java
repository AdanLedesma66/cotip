package py.com.cotip.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.com.cotip.domain.commons.CotipError;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.commons.QuoteModality;
import py.com.cotip.domain.exception.CotipException;
import py.com.cotip.domain.model.BranchOfficeBO;
import py.com.cotip.domain.model.ExchangeRateBO;
import py.com.cotip.domain.port.in.GetExchangeRatesUseCase;
import py.com.cotip.domain.port.in.request.GetRatesQuery;
import py.com.cotip.domain.port.out.ApplicationMetricsPort;
import py.com.cotip.domain.port.out.BranchOfficeQueryPort;
import py.com.cotip.domain.port.out.ExchangeRateRepositoryPort;
import py.com.cotip.domain.port.out.ProviderRatesSourcePort;
import py.com.cotip.domain.port.out.response.ChacoExchangeResponse;
import py.com.cotip.domain.port.out.response.ContinentalBankResponse;
import py.com.cotip.domain.port.out.response.GnbBankResponse;
import py.com.cotip.domain.port.out.response.MaxiExchangeResponse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class GetExchangeRatesUseCaseImpl implements GetExchangeRatesUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetExchangeRatesUseCaseImpl.class);

    // ::: inyects

    private final ProviderRatesSourcePort providerRatesSourcePort;
    private final ExchangeRateRepositoryPort exchangeRateRepositoryPort;
    private final BranchOfficeQueryPort branchOfficeQueryPort;
    private final ApplicationMetricsPort metricsPort;

    // ::: constructor

    public GetExchangeRatesUseCaseImpl(ProviderRatesSourcePort providerRatesSourcePort,
                                       ExchangeRateRepositoryPort exchangeRateRepositoryPort,
                                       BranchOfficeQueryPort branchOfficeQueryPort,
                                       ApplicationMetricsPort metricsPort) {
        this.providerRatesSourcePort = providerRatesSourcePort;
        this.exchangeRateRepositoryPort = exchangeRateRepositoryPort;
        this.branchOfficeQueryPort = branchOfficeQueryPort;
        this.metricsPort = metricsPort;
    }

    // ::: impl

    @Override
    public List<ExchangeRateBO> findLatestContinentalBankExchangeRates() {
        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(fromContinental(providerRatesSourcePort.fetchContinentalBankExchangeRates()),
                ProviderType.CONTINENTAL_BANK);

        log.info("Obtenemos la ultima cotizacion guardada");
        List<ExchangeRateBO> storedRates = exchangeRateRepositoryPort.findAllByProviderOrderByUpdatedAt(
                ProviderType.CONTINENTAL_BANK);
        List<ExchangeRateBO> result = latestByCurrencyAndModalityAndBranch(storedRates);
        metricsPort.recordFreshness(ProviderType.CONTINENTAL_BANK, result);
        return result;
    }

    @Override
    public List<ExchangeRateBO> findLatestGnbBankExchangeRates() {
        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(fromGnb(providerRatesSourcePort.fetchGnbBankExchangeRates()),
                ProviderType.GNB_BANK);

        log.info("Obtenemos la ultima cotizacion guardada");
        List<ExchangeRateBO> storedRates = exchangeRateRepositoryPort.findAllByProviderOrderByUpdatedAt(
                ProviderType.GNB_BANK);
        List<ExchangeRateBO> result = latestByCurrencyAndModalityAndBranch(storedRates);
        metricsPort.recordFreshness(ProviderType.GNB_BANK, result);
        return result;
    }

    @Override
    public List<ExchangeRateBO> findLatestMaxiExchangeRates(GetRatesQuery request) {
        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(fromMaxi(providerRatesSourcePort.fetchMaxiCambiosExchangeRates()),
                ProviderType.MAXI_CAMBIOS);

        log.info("Obtenemos la ultima cotizacion guardada");
        List<ExchangeRateBO> storedRates = exchangeRateRepositoryPort.findAllByProviderOrderByUpdatedAt(
                ProviderType.MAXI_CAMBIOS);
        List<ExchangeRateBO> result = latestByCurrencyAndModalityAndBranch(storedRates);
        metricsPort.recordFreshness(ProviderType.MAXI_CAMBIOS, result);
        return result;
    }

    @Override
    public List<ExchangeRateBO> findLatestCambiosChacoExchangeRates() {
        List<ChacoExchangeResponse> chacoRates = providerRatesSourcePort.fetchCambiosChacoExchangeRates();
        String branchOfficeId = extractBranchOfficeId(chacoRates);

        return saveAndGetChacoRates(chacoRates, branchOfficeId);
    }

    @Override
    public List<ExchangeRateBO> findLatestCambiosChacoExchangeRates(String branchOfficeId) {
        String resolvedBranchOfficeId = resolveBranchOfficeId(branchOfficeId);
        List<ChacoExchangeResponse> chacoRates = providerRatesSourcePort.fetchCambiosChacoExchangeRates(resolvedBranchOfficeId);
        String extractedBranchOfficeId = extractBranchOfficeId(chacoRates);

        return saveAndGetChacoRates(chacoRates,
                extractedBranchOfficeId != null ? extractedBranchOfficeId : resolvedBranchOfficeId);
    }

    @Override
    public List<ExchangeRateBO> findLatestCambiosChacoExchangeRatesByBranchName(String branchOfficeName) {
        String resolvedBranchOfficeId = resolveBranchOfficeName(branchOfficeName);
        List<ChacoExchangeResponse> chacoRates = providerRatesSourcePort.fetchCambiosChacoExchangeRates(resolvedBranchOfficeId);
        String extractedBranchOfficeId = extractBranchOfficeId(chacoRates);

        return saveAndGetChacoRates(chacoRates,
                extractedBranchOfficeId != null ? extractedBranchOfficeId : resolvedBranchOfficeId);
    }

    @Override
    public List<BranchOfficeBO> findCambiosChacoBranches() {
        return branchOfficeQueryPort.findAllCambiosChacoBranches();
    }

    private List<ExchangeRateBO> saveAndGetChacoRates(List<ChacoExchangeResponse> chacoRates, String branchOfficeId) {
        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(fromChaco(chacoRates), ProviderType.CAMBIOS_CHACO);

        log.info("Obtenemos la ultima cotizacion guardada");
        List<ExchangeRateBO> storedRates = exchangeRateRepositoryPort
                .findAllByProviderAndBranchOfficeExternalIdOrderByUpdatedAt(ProviderType.CAMBIOS_CHACO,
                        branchOfficeId);
        List<ExchangeRateBO> result = latestByCurrencyAndModality(storedRates);
        metricsPort.recordFreshness(ProviderType.CAMBIOS_CHACO, result);
        return result;
    }

    private void saveAllCotipEntities(List<ExchangeRateBO> exchangeRates, ProviderType providerType) {
        exchangeRateRepositoryPort.saveAllExchangeRates(exchangeRates, providerType);
        metricsPort.recordIngested(providerType, exchangeRates.size());
    }

    private List<ExchangeRateBO> fromContinental(List<ContinentalBankResponse> rates) {
        return rates.stream()
                .map(rate -> ExchangeRateBO.builder()
                        .exchangeRate(rate.getExchangeRate())
                        .currencyCode(rate.getCurrencyCode())
                        .currencyName(rate.getCurrencyName())
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
                        .currencyName(rate.getCurrencyName())
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
                        .currencyName(rate.getCurrencyName())
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
                        .currencyName(rate.getCurrencyName())
                        .quoteModality(rate.getQuoteModality())
                        .buyRate(rate.getBuyRate())
                        .sellRate(rate.getSellRate())
                        .branchOffice(rate.getBranchOffice())
                        .branchOfficeExternalId(rate.getBranchOfficeExternalId())
                        .build())
                .toList();
    }

    private String extractBranchOfficeId(List<ChacoExchangeResponse> rates) {
        return rates.stream()
                .map(ChacoExchangeResponse::getBranchOfficeExternalId)
                .filter(branchOfficeId -> branchOfficeId != null && !branchOfficeId.isBlank())
                .findFirst()
                .orElse(null);
    }

    private String resolveBranchOfficeId(String branchOfficeId) {
        String normalizedId = branchOfficeId.trim();
        return branchOfficeQueryPort.findCambiosChacoByExternalBranchId(normalizedId)
                .map(BranchOfficeBO::externalBranchId)
                .orElseThrow(() -> new CotipException(404,
                        CotipError.CAMBIOS_CHACO_BRANCH_INVALID.getCode(),
                        "No existe sucursal con id: " + normalizedId,
                        true));
    }

    private String resolveBranchOfficeName(String branchOfficeName) {
        String normalizedName = branchOfficeName.trim();
        return branchOfficeQueryPort.findCambiosChacoByName(normalizedName)
                .map(BranchOfficeBO::externalBranchId)
                .orElseThrow(() -> new CotipException(404,
                        CotipError.CAMBIOS_CHACO_BRANCH_INVALID.getCode(),
                        "No existe sucursal con nombre: " + normalizedName,
                        true));
    }

    private List<ExchangeRateBO> latestByCurrencyAndModalityAndBranch(List<ExchangeRateBO> rates) {
        return latestByKey(rates,
                rate -> normalize(rate.getCurrencyCode()) + "|"
                        + normalize(modalityValue(rate.getQuoteModality())) + "|"
                        + normalize(rate.getBranchOffice()));
    }

    private List<ExchangeRateBO> latestByCurrencyAndModality(List<ExchangeRateBO> rates) {
        return latestByKey(rates,
                rate -> normalize(rate.getCurrencyCode()) + "|" + normalize(modalityValue(rate.getQuoteModality())));
    }

    private List<ExchangeRateBO> latestByKey(List<ExchangeRateBO> rates, Function<ExchangeRateBO, String> keyBuilder) {
        Map<String, ExchangeRateBO> latestByKey = new LinkedHashMap<>();
        for (ExchangeRateBO rate : rates) {
            String key = keyBuilder.apply(rate);
            latestByKey.putIfAbsent(key, rate);
        }

        return new ArrayList<>(latestByKey.values());
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }

        return value.trim().toLowerCase(Locale.ROOT);
    }

    private String modalityValue(QuoteModality modality) {
        return modality == null ? QuoteModality.CASH.name() : modality.name();
    }
}
