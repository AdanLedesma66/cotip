package py.com.cotip.external.webservice;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import py.com.cotip.application.config.exception.CotipException;
import py.com.cotip.domain.commons.CotipError;
import py.com.cotip.domain.port.out.CotipOutPort;
import py.com.cotip.domain.port.out.response.ContinentalBankResponse;
import py.com.cotip.domain.port.out.response.FamiliarBankResponse;
import py.com.cotip.domain.port.out.response.GnbBankResponse;
import py.com.cotip.domain.port.out.response.MaxiExchangeResponse;
import py.com.cotip.external.webservice.config.CotipProperties;
import py.com.cotip.external.webservice.model.ContinentalBearerExternal;
import py.com.cotip.external.webservice.model.ContinentalExternal;
import py.com.cotip.external.webservice.model.ListadoGbnExternal;
import py.com.cotip.external.webservice.util.CurrencyUtils;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class CotipOutPortImpl implements CotipOutPort {

    // ::: path

    private CotipProperties cotipProperties;

    // ::: impls

    @Override
    public ContinentalBearerExternal getContinentalBearerToken() {
        log.info("Obteniendo bearer token del banco continental");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(cotipProperties.getContinentalBearerTokenPath()))
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .header("Client-Id", cotipProperties.getContinentalBearerTokenClientId())
                .header("Client-Secret", cotipProperties.getContinentalBearerTokenClient())
                .header("Subscription-Key", cotipProperties.getContinentalBearerTokenApiKey())
                .header("Grant-Type", cotipProperties.getContinentalBearerTokenGrantType())
                .header("Scope", cotipProperties.getContinentalBearerTokenScope())
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            ContinentalBearerExternal bearerDto = objectMapper.readValue(response.body(), ContinentalBearerExternal.class);
            log.info("La llamada se ejecuto con exito");
            return bearerDto;
        } catch (Exception e) {
            log.error("Error al obtener el bearer token de Banco Continental", e);
            throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(), CotipError.CONTINENTAL_BANK_ERROR.getCode(),
                    CotipError.CONTINENTAL_BANK_ERROR.getDescription());
        }
    }

    @Override
    public List<ContinentalBankResponse> fetchContinentalBankExchangeRates() {
        List<ContinentalBankResponse> exchangeRates = new ArrayList<>();
        try {

            log.info("Obteniendo cotizacion del banco continental");
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(cotipProperties.getContinentalPath()))
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Bearer " + getContinentalBearerToken().accessToken())
                    .header("Subscription-Key", cotipProperties.getContinentalApiKey())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            List<ContinentalExternal> cotizacionExternal = objectMapper.readValue(response.body(),
                    new TypeReference<>() {
                    });

            cotizacionExternal.forEach(cotizacion -> {

                String exchangeRate = cotizacion.exchangeRate();
                String standardizedExchangeRate = CurrencyUtils.getStandardizedExchangeRateName(exchangeRate);
                String standardizedCurrencyCode = CurrencyUtils.getCurrencyCode(Objects.requireNonNull(standardizedExchangeRate));

                ContinentalBankResponse bankResponse = ContinentalBankResponse.builder()
                        .exchangeRate(standardizedExchangeRate)
                        .currencyCode(standardizedCurrencyCode)
                        .buyRate(cotizacion.buyRate().longValue())
                        .sellRate(cotizacion.sellRate().longValue())
                        .build();
                exchangeRates.add(bankResponse);

            });

            log.info("La llamada se ejecuto con exito");
            return exchangeRates;
        } catch (IOException | InterruptedException e) {
            log.error("Error al obtener las cotizaciones de Banco Continental", e);
            throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(), CotipError.CONTINENTAL_BANK_ERROR.getCode(),
                    CotipError.CONTINENTAL_BANK_ERROR.getDescription());
        }

    }

    @Override
    public List<GnbBankResponse> fetchGnbBankExchangeRates() {
        List<GnbBankResponse> exchangeRates = new ArrayList<>();

        log.info("Obteniendo cotizacion del banco gnb");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(cotipProperties.getGnbPath()))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            ListadoGbnExternal listadoGbnExternal = objectMapper.readValue(response.body(),
                    new TypeReference<>() {
                    });

            listadoGbnExternal.exchangeRates().forEach(cotizacion -> {
                String exchangeRate = cotizacion.currencyDesc();
                String standardizedExchangeRate = CurrencyUtils.getStandardizedExchangeRateName(exchangeRate);
                String standardizedCurrencyCode = CurrencyUtils.getCurrencyCode(Objects.requireNonNull(standardizedExchangeRate));

                GnbBankResponse bankResponse = GnbBankResponse.builder()
                        .exchangeRate(standardizedExchangeRate)
                        .currencyCode(standardizedCurrencyCode)
                        .buyRate(stringToLong(cotizacion.electronicBuyPrice()))
                        .sellRate(stringToLong(cotizacion.electronicSellPrice()))
                        .build();

                exchangeRates.add(bankResponse);
            });

            log.info("La llamada se ejecuto con exito");
            return exchangeRates;
        } catch (IOException | InterruptedException e) {
            log.error("Error al obtener las cotizaciones de Banco Gnb", e);
            throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(), CotipError.GNB_BANK_ERROR.getCode(),
                    CotipError.GNB_BANK_ERROR.getDescription());
        }
    }

    @Override
    public List<MaxiExchangeResponse> fetchMaxiCambiosExchangeRates() {
        List<MaxiExchangeResponse> exchangeRates = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(cotipProperties.getMaxicambiosPath()).get();

            Map<String, String> sectionsMap = Map.of(
                    "Asunción", "cotizacion-carousel",
                    "Ciudad del Este", "cotizacion-cd",
                    "Cheque Transferencia", "cotizacion-cheq-tra",
                    "Arbitraje", "cotizacion-arbi"
            );

            for (Map.Entry<String, String> entry : sectionsMap.entrySet()) {
                String sectionName = entry.getKey();
                String sectionId = entry.getValue();

                Elements currencyItems = doc.select("#" + sectionId + " .cotizDivSmall");

                for (Element item : currencyItems) {
                    String exchangeRate = item.select("p[style*=text-overflow], p[style*=font-size]").first().text().trim();

                    String buyRateText = item.select("p:contains(Compra) + p").text().replace(",", ".").split(" ")[0];
                    String sellRateText = item.select("p:contains(Venta) + p").text().replace(",", ".").split(" ")[0];

                    BigDecimal buyRate = new BigDecimal(buyRateText).setScale(0, RoundingMode.DOWN);
                    BigDecimal sellRate = new BigDecimal(sellRateText).setScale(0, RoundingMode.DOWN);

                    if ("Cheque Transferencia".equals(sectionName)) {
                        if (exchangeRate.equalsIgnoreCase("Dólar")) {
                            exchangeRate = "Dólar Cheque / Transferencia";
                        } else if (exchangeRate.equalsIgnoreCase("Euro")) {
                            exchangeRate = "Euro Cheque / Transferencia";
                        }
                    }

                    String standardizedExchangeRate = CurrencyUtils.getStandardizedExchangeRateName(exchangeRate);
                    String standardizedCurrencyCode = CurrencyUtils.getCurrencyCode(standardizedExchangeRate);

                    if (standardizedExchangeRate != null && standardizedCurrencyCode != null) {
                        MaxiExchangeResponse response = MaxiExchangeResponse.builder()
                                .exchangeRate(standardizedExchangeRate)
                                .currencyCode(standardizedCurrencyCode)
                                .buyRate(buyRate.longValue())
                                .sellRate(sellRate.longValue())
                                .city("Cheque Transferencia".equals(sectionName) ? null : sectionName)
                                .build();
                        exchangeRates.add(response);
                    }
                }
            }

            log.info("La llamada se ejecutó con éxito");
        } catch (IOException e) {
            log.error("Error al obtener las cotizaciones de MaxiCambios", e);
            throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(), CotipError.MAXI_CAMBIOS_ERROR.getCode(),
                    CotipError.MAXI_CAMBIOS_ERROR.getDescription());
        }

        return exchangeRates;
    }

    // ::: externals

    static Long stringToLong(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        BigDecimal decimalValue = new BigDecimal(value.replace(",", "").trim());
        return decimalValue.longValue();
    }


}
