package py.com.cotip.insfrastructure.external.webservice.scraper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import py.com.cotip.domain.commons.CotipError;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.exception.CotipException;
import py.com.cotip.domain.port.out.response.GnbBankResponse;
import py.com.cotip.insfrastructure.external.webservice.config.CotipProperties;
import py.com.cotip.insfrastructure.external.webservice.model.ListadoGbnExternal;
import py.com.cotip.insfrastructure.external.webservice.util.CurrencyUtils;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Component
public class GnbScraper extends AbstractProviderScraper<GnbBankResponse, ListadoGbnExternal> {

    private final CotipProperties cotipProperties;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GnbScraper(CotipProperties cotipProperties) {
        this.cotipProperties = cotipProperties;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(cotipProperties.getHttpConnectTimeoutSeconds()))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public ProviderType provider() {
        return ProviderType.GNB_BANK;
    }

    @Override
    protected String fetchRaw() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(cotipProperties.getGnbPath()))
                .timeout(Duration.ofSeconds(cotipProperties.getHttpReadTimeoutSeconds()))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) {
            throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    CotipError.GNB_BANK_ERROR.getCode(),
                    "GNB response status: " + response.statusCode(),
                    true);
        }

        return response.body();
    }

    @Override
    protected ListadoGbnExternal parse(String rawPayload) throws Exception {
        return objectMapper.readValue(rawPayload, ListadoGbnExternal.class);
    }

    @Override
    protected List<GnbBankResponse> normalize(ListadoGbnExternal parsedPayload) {
        return parsedPayload.getExchangeRates().stream()
                .map(cotizacion -> {
                    CurrencyUtils.StandardizedRate standardizedRate = CurrencyUtils
                            .standardizeExchangeRate(cotizacion.getCurrencyDesc());
                    if (standardizedRate == null) {
                        return null;
                    }

                    return GnbBankResponse.builder()
                            .exchangeRate(standardizedRate.exchangeRateName())
                            .currencyCode(standardizedRate.currencyCode())
                            .quoteModality(standardizedRate.quoteModality())
                            .buyRate(stringToLong(cotizacion.getElectronicBuyPrice()))
                            .sellRate(stringToLong(cotizacion.getElectronicSellPrice()))
                            .build();
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    protected void validate(List<GnbBankResponse> normalizedRates) {
        if (normalizedRates == null || normalizedRates.isEmpty()) {
            throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    CotipError.GNB_BANK_ERROR.getCode(),
                    "GNB scraper returned no rates",
                    true);
        }
    }

    private static Long stringToLong(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof String stringValue) {
            BigDecimal decimalValue = new BigDecimal(stringValue.replace(",", "").trim());
            return decimalValue.longValue();
        }
        if (value instanceof BigDecimal decimalValue) {
            return decimalValue.longValue();
        }
        if (value instanceof Number numberValue) {
            return numberValue.longValue();
        }

        throw new IllegalArgumentException("Unsupported type: " + value.getClass());
    }
}
