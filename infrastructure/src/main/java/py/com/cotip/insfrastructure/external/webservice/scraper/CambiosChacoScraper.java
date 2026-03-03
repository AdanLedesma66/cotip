package py.com.cotip.insfrastructure.external.webservice.scraper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import py.com.cotip.domain.commons.CotipError;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.exception.CotipException;
import py.com.cotip.domain.port.out.response.ChacoExchangeResponse;
import py.com.cotip.insfrastructure.external.webservice.config.ChacoBranchCatalog;
import py.com.cotip.insfrastructure.external.webservice.config.CotipProperties;
import py.com.cotip.insfrastructure.external.webservice.model.ChacoExchangeExternal;
import py.com.cotip.insfrastructure.external.webservice.model.ChacoExchangeItemExternal;
import py.com.cotip.insfrastructure.external.webservice.util.CurrencyUtils;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.exc.MismatchedInputException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Component
public class CambiosChacoScraper extends AbstractProviderScraper<ChacoExchangeResponse, ChacoExchangeExternal> {

    private final CotipProperties cotipProperties;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public CambiosChacoScraper(CotipProperties cotipProperties) {
        this.cotipProperties = cotipProperties;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(cotipProperties.getHttpConnectTimeoutSeconds()))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public ProviderType provider() {
        return ProviderType.CAMBIOS_CHACO;
    }

    @Override
    protected String fetchRaw() throws Exception {
        return fetchRaw(resolveDefaultBranchOfficeId());
    }

    public ScrapeExecution<ChacoExchangeResponse> executeForBranch(String branchOfficeId) {
        String normalizedBranchOfficeId = normalizeBranchOfficeId(branchOfficeId);
        String rawPayload = null;

        try {
            rawPayload = fetchRaw(normalizedBranchOfficeId);
            ChacoExchangeExternal parsedPayload = parse(rawPayload);
            List<ChacoExchangeResponse> normalizedRates = normalizeBranchPayload(parsedPayload,
                    resolvePayloadBranchOfficeId(parsedPayload, normalizedBranchOfficeId));
            validate(normalizedRates);

            return new ScrapeExecution<>(provider(), rawPayload, normalizedRates);
        } catch (Exception ex) {
            throw new ProviderScrapeException(provider(), rawPayload, ex);
        }
    }

    @Override
    protected ChacoExchangeExternal parse(String rawPayload) throws Exception {
        try {
            return objectMapper.readValue(rawPayload, ChacoExchangeExternal.class);
        } catch (MismatchedInputException ex) {
            if (rawPayload != null && rawPayload.trim().startsWith("[")) {
                throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        CotipError.CAMBIOS_CHACO_ERROR.getCode(),
                        "Cambios Chaco devolvio un listado de sucursales en vez de cotizaciones. " +
                                "Verifica 'cotip.chaco.base-url' y el branchOfficeId solicitado",
                        true,
                        ex);
            }
            throw ex;
        }
    }

    @Override
    protected List<ChacoExchangeResponse> normalize(ChacoExchangeExternal parsedPayload) {
        return normalizeBranchPayload(parsedPayload,
                resolvePayloadBranchOfficeId(parsedPayload, resolveDefaultBranchOfficeId()));
    }

    @Override
    protected void validate(List<ChacoExchangeResponse> normalizedRates) {
        if (normalizedRates == null || normalizedRates.isEmpty()) {
            throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    CotipError.CAMBIOS_CHACO_ERROR.getCode(),
                    "Cambios Chaco scraper returned no rates",
                    true);
        }
    }

    private String fetchRaw(String branchOfficeId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(buildExchangeUrl(branchOfficeId)))
                .timeout(Duration.ofSeconds(cotipProperties.getHttpReadTimeoutSeconds()))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) {
            throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    CotipError.CAMBIOS_CHACO_ERROR.getCode(),
                    "Cambios Chaco response status: " + response.statusCode(),
                    true);
        }

        return response.body();
    }

    private List<ChacoExchangeResponse> normalizeBranchPayload(ChacoExchangeExternal branchPayload,
                                                               String branchOfficeId) {
        ChacoBranchCatalog.BranchMetadata branchMetadata = ChacoBranchCatalog.findById(branchOfficeId);
        String branchOfficeName = branchMetadata != null
                ? branchMetadata.name()
                : resolveFallbackBranchName(branchOfficeId);

        List<ChacoExchangeItemExternal> items = branchPayload != null && branchPayload.getItems() != null
                ? branchPayload.getItems()
                : Collections.emptyList();

        return items.stream()
                .filter(item -> item.getIsoCode() != null && item.getPurchasePrice() != null && item.getSalePrice() != null)
                .map(item -> {
                    String currencyCode = item.getIsoCode().trim().toUpperCase(Locale.ROOT);
                    CurrencyUtils.StandardizedRate standardizedRate = CurrencyUtils.fromIsoCode(currencyCode);
                    if (standardizedRate == null) {
                        standardizedRate = CurrencyUtils.unknownFromIsoCode(currencyCode);
                    }

                    return ChacoExchangeResponse.builder()
                            .exchangeRate(standardizedRate.exchangeRateName())
                            .currencyCode(standardizedRate.currencyCode())
                            .currencyName(standardizedRate.currencyName())
                            .quoteModality(standardizedRate.quoteModality())
                            .buyRate(toLong(item.getPurchasePrice()))
                            .sellRate(toLong(item.getSalePrice()))
                            .branchOffice(branchOfficeName)
                            .branchOfficeExternalId(branchOfficeId)
                            .build();
                })
                .toList();
    }

    private String buildExchangeUrl(String branchOfficeId) {
        String baseUrl = cotipProperties.getChacoBaseUrl();
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "https://www.cambioschaco.com.py/api";
        }

        baseUrl = baseUrl.trim();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        if (baseUrl.contains("{branchOfficeId}")) {
            return baseUrl.replace("{branchOfficeId}", branchOfficeId);
        }

        if (baseUrl.endsWith("/branch_office")) {
            return baseUrl + "/" + branchOfficeId + "/exchange";
        }

        if (baseUrl.matches(".*/branch_office/[^/]+/exchange$")) {
            return baseUrl.replaceFirst("/branch_office/[^/]+/exchange$",
                    "/branch_office/" + branchOfficeId + "/exchange");
        }

        return baseUrl + "/branch_office/" + branchOfficeId + "/exchange";
    }

    private String resolveDefaultBranchOfficeId() {
        return normalizeBranchOfficeId(cotipProperties.getChacoDefaultBranchOfficeId());
    }

    private String resolvePayloadBranchOfficeId(ChacoExchangeExternal payload, String fallbackBranchOfficeId) {
        if (payload != null && payload.getBranchOfficeId() != null && !payload.getBranchOfficeId().isBlank()) {
            return payload.getBranchOfficeId().trim();
        }

        return fallbackBranchOfficeId;
    }

    private String normalizeBranchOfficeId(String branchOfficeId) {
        String normalizedBranchOfficeId = branchOfficeId;
        if (normalizedBranchOfficeId == null || normalizedBranchOfficeId.isBlank()) {
            normalizedBranchOfficeId = "3";
        } else {
            normalizedBranchOfficeId = normalizedBranchOfficeId.trim();
        }

        if (!ChacoBranchCatalog.existsId(normalizedBranchOfficeId)) {
            throw new CotipException(HttpStatus.BAD_REQUEST.value(),
                    CotipError.CAMBIOS_CHACO_BRANCH_INVALID.getCode(),
                    "Sucursal de Cambios Chaco invalida: " + normalizedBranchOfficeId,
                    true);
        }

        return normalizedBranchOfficeId;
    }

    private String resolveFallbackBranchName(String branchOfficeId) {
        if (branchOfficeId == null || branchOfficeId.isBlank()) {
            return null;
        }

        return "Sucursal " + branchOfficeId;
    }

    private Long toLong(BigDecimal value) {
        return value.setScale(0, RoundingMode.DOWN).longValue();
    }
}
