package py.com.cotip.insfrastructure.external.webservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import py.com.cotip.domain.commons.CotipError;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.exception.CotipException;
import py.com.cotip.domain.port.out.ApplicationMetricsPort;
import py.com.cotip.domain.port.out.ProviderRatesSourcePort;
import py.com.cotip.domain.port.out.ScrapeAuditPort;
import py.com.cotip.domain.port.out.response.ChacoExchangeResponse;
import py.com.cotip.domain.port.out.response.ContinentalBankResponse;
import py.com.cotip.domain.port.out.response.GnbBankResponse;
import py.com.cotip.domain.port.out.response.MaxiExchangeResponse;
import py.com.cotip.insfrastructure.external.webservice.scraper.CambiosChacoScraper;
import py.com.cotip.insfrastructure.external.webservice.scraper.ContinentalScraper;
import py.com.cotip.insfrastructure.external.webservice.scraper.GnbScraper;
import py.com.cotip.insfrastructure.external.webservice.scraper.MaxiCambiosScraper;
import py.com.cotip.insfrastructure.external.webservice.scraper.ProviderScrapeException;
import py.com.cotip.insfrastructure.external.webservice.scraper.ScrapeExecution;

import java.io.IOException;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class ProviderRatesSourceAdapter implements ProviderRatesSourcePort {

    private static final int MAX_AUDIT_PAYLOAD_LENGTH = 15_000;

    private final ContinentalScraper continentalScraper;
    private final GnbScraper gnbScraper;
    private final MaxiCambiosScraper maxiCambiosScraper;
    private final CambiosChacoScraper cambiosChacoScraper;
    private final ApplicationMetricsPort metricsPort;
    private final ScrapeAuditPort scrapeAuditPort;

    @Override
    @Retryable(
            value = { CotipException.class, IOException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public List<ContinentalBankResponse> fetchContinentalBankExchangeRates() {
        return executeScrape(ProviderType.CONTINENTAL_BANK, continentalScraper::execute, CotipError.CONTINENTAL_BANK_ERROR);
    }

    @Override
    @Retryable(
            value = { CotipException.class, IOException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public List<GnbBankResponse> fetchGnbBankExchangeRates() {
        return executeScrape(ProviderType.GNB_BANK, gnbScraper::execute, CotipError.GNB_BANK_ERROR);
    }

    @Override
    @Retryable(
            value = { CotipException.class, IOException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public List<MaxiExchangeResponse> fetchMaxiCambiosExchangeRates() {
        return executeScrape(ProviderType.MAXI_CAMBIOS, maxiCambiosScraper::execute, CotipError.MAXI_CAMBIOS_ERROR);
    }

    @Override
    @Retryable(
            value = { CotipException.class, IOException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public List<ChacoExchangeResponse> fetchCambiosChacoExchangeRates() {
        return executeScrape(ProviderType.CAMBIOS_CHACO, cambiosChacoScraper::execute, CotipError.CAMBIOS_CHACO_ERROR);
    }

    @Override
    @Retryable(
            value = { CotipException.class, IOException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public List<ChacoExchangeResponse> fetchCambiosChacoExchangeRates(String branchOfficeId) {
        return executeScrape(ProviderType.CAMBIOS_CHACO,
                () -> cambiosChacoScraper.executeForBranch(branchOfficeId),
                CotipError.CAMBIOS_CHACO_ERROR);
    }

    private <T> List<T> executeScrape(ProviderType providerType,
                                      ScrapeSupplier<T> supplier,
                                      CotipError cotipError) {
        long startNanos = System.nanoTime();
        try {
            ScrapeExecution<T> execution = supplier.get();
            scrapeAuditPort.saveSuccess(providerType, sanitizePayload(execution.rawPayload()));
            metricsPort.recordScrapeSuccess(providerType, startNanos);
            return execution.rates();
        } catch (ProviderScrapeException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }

            scrapeAuditPort.saveFailure(providerType, sanitizePayload(ex.getRawPayload()),
                    cause != null ? cause.getMessage() : ex.getMessage());
            metricsPort.recordScrapeFailure(providerType, "pipeline_failure", startNanos);
            log.error("Error scraping {}", providerType, ex);

            if (cause instanceof CotipException cotipException) {
                throw cotipException;
            }

            throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    cotipError.getCode(),
                    cotipError.getDescription(),
                    true);
        }
    }

    private String sanitizePayload(String payload) {
        if (payload == null || payload.isBlank()) {
            return null;
        }

        if (payload.length() <= MAX_AUDIT_PAYLOAD_LENGTH) {
            return payload;
        }

        return payload.substring(0, MAX_AUDIT_PAYLOAD_LENGTH) + "...";
    }

    @FunctionalInterface
    private interface ScrapeSupplier<T> {
        ScrapeExecution<T> get();
    }
}
