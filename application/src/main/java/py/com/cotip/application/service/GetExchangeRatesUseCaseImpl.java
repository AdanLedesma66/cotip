package py.com.cotip.application.service;

import py.com.cotip.domain.commons.CotipError;
import py.com.cotip.domain.commons.CotipCity;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class GetExchangeRatesUseCaseImpl implements GetExchangeRatesUseCase {

    private final ExchangeRateRepositoryPort exchangeRateRepositoryPort;
    private final BranchOfficeQueryPort branchOfficeQueryPort;
    private final ApplicationMetricsPort metricsPort;
    private final String defaultChacoBranchOfficeId;

    public GetExchangeRatesUseCaseImpl(ExchangeRateRepositoryPort exchangeRateRepositoryPort,
                                       BranchOfficeQueryPort branchOfficeQueryPort,
                                       ApplicationMetricsPort metricsPort,
                                       String defaultChacoBranchOfficeId) {
        this.exchangeRateRepositoryPort = exchangeRateRepositoryPort;
        this.branchOfficeQueryPort = branchOfficeQueryPort;
        this.metricsPort = metricsPort;
        this.defaultChacoBranchOfficeId = defaultChacoBranchOfficeId;
    }

    @Override
    public List<ExchangeRateBO> findLatestContinentalBankExchangeRates() {
        List<ExchangeRateBO> storedRates = exchangeRateRepositoryPort.findAllByProviderOrderByUpdatedAt(
                ProviderType.CONTINENTAL_BANK);
        List<ExchangeRateBO> result = latestByCurrencyAndModalityAndBranch(storedRates);
        metricsPort.recordFreshness(ProviderType.CONTINENTAL_BANK, result);
        return result;
    }

    @Override
    public List<ExchangeRateBO> findLatestGnbBankExchangeRates() {
        List<ExchangeRateBO> storedRates = exchangeRateRepositoryPort.findAllByProviderOrderByUpdatedAt(
                ProviderType.GNB_BANK);
        List<ExchangeRateBO> result = latestByCurrencyAndModalityAndBranch(storedRates);
        metricsPort.recordFreshness(ProviderType.GNB_BANK, result);
        return result;
    }

    @Override
    public List<ExchangeRateBO> findLatestMaxiExchangeRates(GetRatesQuery request) {
        List<ExchangeRateBO> storedRates = exchangeRateRepositoryPort.findAllByProviderOrderByUpdatedAt(
                ProviderType.MAXI_CAMBIOS);

        List<ExchangeRateBO> filteredRates = filterByCity(storedRates, request != null ? request.getCity() : null);
        List<ExchangeRateBO> result = latestByCurrencyAndModalityAndBranch(filteredRates);
        metricsPort.recordFreshness(ProviderType.MAXI_CAMBIOS, result);
        return result;
    }

    @Override
    public List<ExchangeRateBO> findLatestCambiosChacoExchangeRates() {
        return findLatestCambiosChacoExchangeRates(defaultChacoBranchOfficeId);
    }

    @Override
    public List<ExchangeRateBO> findLatestCambiosChacoExchangeRates(String branchOfficeId) {
        String resolvedBranchOfficeId = resolveBranchOfficeId(branchOfficeId);
        List<ExchangeRateBO> storedRates = exchangeRateRepositoryPort
                .findAllByProviderAndBranchOfficeExternalIdOrderByUpdatedAt(ProviderType.CAMBIOS_CHACO,
                        resolvedBranchOfficeId);
        List<ExchangeRateBO> result = latestByCurrencyAndModality(storedRates);
        metricsPort.recordFreshness(ProviderType.CAMBIOS_CHACO, result);
        return result;
    }

    @Override
    public List<ExchangeRateBO> findLatestCambiosChacoExchangeRatesByBranchName(String branchOfficeName) {
        String resolvedBranchOfficeId = resolveBranchOfficeName(branchOfficeName);
        return findLatestCambiosChacoExchangeRates(resolvedBranchOfficeId);
    }

    @Override
    public List<BranchOfficeBO> findCambiosChacoBranches() {
        return branchOfficeQueryPort.findAllCambiosChacoBranches();
    }

    private List<ExchangeRateBO> filterByCity(List<ExchangeRateBO> rates, CotipCity city) {
        if (city == null) {
            return rates;
        }

        String expectedCity = normalize(city.getName());
        return rates.stream()
                .filter(rate -> normalize(rate.getCity()).equals(expectedCity))
                .toList();
    }

    private String resolveBranchOfficeId(String branchOfficeId) {
        if (branchOfficeId == null || branchOfficeId.isBlank()) {
            throw new CotipException(400,
                    CotipError.CAMBIOS_CHACO_BRANCH_INVALID.getCode(),
                    "Debe enviar id de sucursal",
                    true);
        }

        String normalizedId = branchOfficeId.trim();
        return branchOfficeQueryPort.findCambiosChacoByExternalBranchId(normalizedId)
                .map(BranchOfficeBO::externalBranchId)
                .orElseThrow(() -> new CotipException(404,
                        CotipError.CAMBIOS_CHACO_BRANCH_INVALID.getCode(),
                        "No existe sucursal con id: " + normalizedId,
                        true));
    }

    private String resolveBranchOfficeName(String branchOfficeName) {
        if (branchOfficeName == null || branchOfficeName.isBlank()) {
            throw new CotipException(400,
                    CotipError.CAMBIOS_CHACO_BRANCH_INVALID.getCode(),
                    "Debe enviar nombre de sucursal",
                    true);
        }

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
