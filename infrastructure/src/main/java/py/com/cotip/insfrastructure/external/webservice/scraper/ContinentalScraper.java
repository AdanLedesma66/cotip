package py.com.cotip.insfrastructure.external.webservice.scraper;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import py.com.cotip.domain.commons.CotipError;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.exception.CotipException;
import py.com.cotip.domain.port.out.response.ContinentalBankResponse;
import py.com.cotip.insfrastructure.external.webservice.config.CotipProperties;
import py.com.cotip.insfrastructure.external.webservice.model.ContinentalBearerExternal;
import py.com.cotip.insfrastructure.external.webservice.model.ContinentalExternal;
import py.com.cotip.insfrastructure.external.webservice.util.CurrencyUtils;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Component
public class ContinentalScraper extends AbstractProviderScraper<ContinentalBankResponse, List<ContinentalExternal>> {

    private final CotipProperties cotipProperties;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ContinentalScraper(CotipProperties cotipProperties) {
        this.cotipProperties = cotipProperties;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(cotipProperties.getHttpConnectTimeoutSeconds()))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public ProviderType provider() {
        return ProviderType.CONTINENTAL_BANK;
    }

    public ContinentalBearerExternal getBearerToken() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(cotipProperties.getContinentalBearerTokenPath()))
                    .timeout(Duration.ofSeconds(cotipProperties.getHttpReadTimeoutSeconds()))
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .header("Client-Id", cotipProperties.getContinentalBearerTokenClientId())
                    .header("Client-Secret", cotipProperties.getContinentalBearerTokenClient())
                    .header("Subscription-Key", cotipProperties.getContinentalBearerTokenApiKey())
                    .header("Grant-Type", cotipProperties.getContinentalBearerTokenGrantType())
                    .header("Scope", cotipProperties.getContinentalBearerTokenScope())
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), ContinentalBearerExternal.class);
        } catch (Exception ex) {
            throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    CotipError.CONTINENTAL_BANK_ERROR.getCode(),
                    CotipError.CONTINENTAL_BANK_ERROR.getDescription(),
                    true);
        }
    }

    @Override
    protected String fetchRaw() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(cotipProperties.getContinentalPath()))
                .timeout(Duration.ofSeconds(cotipProperties.getHttpReadTimeoutSeconds()))
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + getBearerToken().getAccessToken())
                .header("Subscription-Key", cotipProperties.getContinentalApiKey())
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    @Override
    protected List<ContinentalExternal> parse(String rawPayload) throws Exception {
        return objectMapper.readValue(rawPayload, new TypeReference<>() {
        });
    }

    @Override
    protected List<ContinentalBankResponse> normalize(List<ContinentalExternal> parsedPayload) {
        return parsedPayload.stream().map(cotizacion -> {
            String standardizedExchangeRate = CurrencyUtils.getStandardizedExchangeRateName(cotizacion.getExchangeRate());
            String standardizedCurrencyCode = CurrencyUtils.getCurrencyCode(Objects.requireNonNull(standardizedExchangeRate));
            return ContinentalBankResponse.builder()
                    .exchangeRate(standardizedExchangeRate)
                    .currencyCode(standardizedCurrencyCode)
                    .buyRate(cotizacion.getBuyRate().longValue())
                    .sellRate(cotizacion.getSellRate().longValue())
                    .build();
        }).toList();
    }

    @Override
    protected void validate(List<ContinentalBankResponse> normalizedRates) {
        if (normalizedRates == null || normalizedRates.isEmpty()) {
            throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    CotipError.CONTINENTAL_BANK_ERROR.getCode(),
                    "Continental scraper returned no rates",
                    true);
        }
    }
}
