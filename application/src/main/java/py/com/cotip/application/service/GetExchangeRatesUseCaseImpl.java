package py.com.cotip.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.model.ExchangeRateBO;
import py.com.cotip.domain.port.in.GetExchangeRatesUseCase;
import py.com.cotip.domain.port.in.request.GetRatesQuery;
import py.com.cotip.domain.port.out.ApplicationMetricsPort;
import py.com.cotip.domain.port.out.ExchangeRateRepositoryPort;
import py.com.cotip.domain.port.out.ProviderRatesSourcePort;
import py.com.cotip.domain.port.out.response.ChacoExchangeResponse;
import py.com.cotip.domain.port.out.response.ContinentalBankResponse;
import py.com.cotip.domain.port.out.response.GnbBankResponse;
import py.com.cotip.domain.port.out.response.MaxiExchangeResponse;

import java.util.List;

public class GetExchangeRatesUseCaseImpl implements GetExchangeRatesUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetExchangeRatesUseCaseImpl.class);

    // ::: inyects

    private final ProviderRatesSourcePort providerRatesSourcePort;
    private final ExchangeRateRepositoryPort exchangeRateRepositoryPort;
    private final ApplicationMetricsPort metricsPort;

    // ::: constructor

    public GetExchangeRatesUseCaseImpl(ProviderRatesSourcePort providerRatesSourcePort,
                                       ExchangeRateRepositoryPort exchangeRateRepositoryPort,
                                       ApplicationMetricsPort metricsPort) {
        this.providerRatesSourcePort = providerRatesSourcePort;
        this.exchangeRateRepositoryPort = exchangeRateRepositoryPort;
        this.metricsPort = metricsPort;
    }

    // ::: impl

    @Override
    public List<ExchangeRateBO> findLatestContinentalBankExchangeRates() {
        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(fromContinental(providerRatesSourcePort.fetchContinentalBankExchangeRates()),
                ProviderType.CONTINENTAL_BANK);

        log.info("Obtenemos la ultima cotizacion guardada");
        List<ExchangeRateBO> result = exchangeRateRepositoryPort.findAllByProviderOrderByUploadDate(
                ProviderType.CONTINENTAL_BANK);
        metricsPort.recordFreshness(ProviderType.CONTINENTAL_BANK, result);
        return result;
    }

    @Override
    public List<ExchangeRateBO> findLatestGnbBankExchangeRates() {
        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(fromGnb(providerRatesSourcePort.fetchGnbBankExchangeRates()),
                ProviderType.GNB_BANK);

        log.info("Obtenemos la ultima cotizacion guardada");
        List<ExchangeRateBO> result = exchangeRateRepositoryPort.findAllByProviderOrderByUploadDate(
                ProviderType.GNB_BANK);
        metricsPort.recordFreshness(ProviderType.GNB_BANK, result);
        return result;
    }

    @Override
    public List<ExchangeRateBO> findLatestMaxiExchangeRates(GetRatesQuery request) {
        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(fromMaxi(providerRatesSourcePort.fetchMaxiCambiosExchangeRates()),
                ProviderType.MAXI_CAMBIOS);

        log.info("Obtenemos la ultima cotizacion guardada");
        List<ExchangeRateBO> result = exchangeRateRepositoryPort.findAllByProviderOrderByUploadDate(
                ProviderType.MAXI_CAMBIOS);
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
        List<ChacoExchangeResponse> chacoRates = providerRatesSourcePort.fetchCambiosChacoExchangeRates(branchOfficeId);
        String resolvedBranchOfficeId = extractBranchOfficeId(chacoRates);

        return saveAndGetChacoRates(chacoRates, resolvedBranchOfficeId != null ? resolvedBranchOfficeId : branchOfficeId);
    }

    private List<ExchangeRateBO> saveAndGetChacoRates(List<ChacoExchangeResponse> chacoRates, String branchOfficeId) {
        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(fromChaco(chacoRates), ProviderType.CAMBIOS_CHACO);

        log.info("Obtenemos la ultima cotizacion guardada");
        List<ExchangeRateBO> result = exchangeRateRepositoryPort
                .findAllByProviderAndBranchOfficeExternalIdOrderByUploadDate(ProviderType.CAMBIOS_CHACO,
                        branchOfficeId);
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
}
