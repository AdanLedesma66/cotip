package py.com.cotip.external.webservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import py.com.cotip.domain.port.out.CotipOutPort;
import py.com.cotip.domain.port.out.response.SolarBankResponse;
import py.com.cotip.domain.port.out.response.FamiliarResponse;
import py.com.cotip.external.webservice.config.CotipProperties;
import py.com.cotip.external.webservice.model.*;
import py.com.cotip.external.webservice.util.CurrencyUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class CotipOutPortImpl implements CotipOutPort {

    // ::: path

    private CotipProperties cotipProperties;

    // ::: externals

    @Override
    public ContinentalBearerExternal findContinentalBearerToken() throws Exception {
        log.info("Obteniendo bearer token del banco continental");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(cotipProperties.getContinentalBearerTokenPath()))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Client-Id", "webinstitucional")
                .header("Client-Secret", "006f7995-3d33-4afe-8c56-e3b47377092c")
                .header("Subscription-Key", "3c35bb9e5fa948adb8d64c123d9d1a45")
                .header("Grant-Type", "client_credentials")
                .header("Scope", "profile")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            ContinentalBearerExternal bearerDto = objectMapper.readValue(response.body(), ContinentalBearerExternal.class);
            log.info("La llamada se ejecuto con exito");
            return bearerDto;
        } catch (IOException | InterruptedException e) {
            log.error("Error al obtener el bearer token de Banco Continental", e);
            throw new Exception("Error al obtener las cotizaciones del Banco Continental");
        }
    }

    @Override
    public List<ContinentalExternal> findContinentalCotizacion() throws Exception {
        log.info("Obteniendo cotizacion del banco continental");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(cotipProperties.getContinentalPath()))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + findContinentalBearerToken().getAccessToken())
                .header("Subscription-Key", "3c35bb9e5fa948adb8d64c123d9d1a45")
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            List<ContinentalExternal> cotizacionExternal = objectMapper.readValue(response.body(),
                    new TypeReference<>() {
                    });

            cotizacionExternal.forEach(cotizacion -> {
                String exchangeRate = cotizacion.getExchangeRate();
                String standardizedExchangeRate = CurrencyUtils.getStandardizedExchangeRateName(exchangeRate);
                String standardizedCurrencyCode = CurrencyUtils.getCurrencyCode(Objects.requireNonNull(standardizedExchangeRate));

                cotizacion.setExchangeRate(standardizedExchangeRate);
                cotizacion.setCurrencyCode(standardizedCurrencyCode);
            });

            log.info("La llamada se ejecuto con exito");
            return cotizacionExternal;
        } catch (IOException | InterruptedException e) {
            log.error("Error al obtener las cotizaciones de Banco Continental", e);
            throw new Exception("Error al obtener las cotizaciones del Banco Continental");
        }

    }

    @Override
    public List<FamiliarResponse> findFamiliarCotizacion() throws Exception {
        log.info("Obteniendo cotizacion del banco familiar");
        List<FamiliarResponse> cotizaciones = new ArrayList<>();

        try {
            log.info("Se scrapean del html los datos de la cotizacion");
            Document doc = Jsoup.connect(cotipProperties.getFamiliarPath()).get();

            Element cotizacionesSection = doc.getElementById("cotizaciones");

            if (cotizacionesSection != null) {
                Elements rows = cotizacionesSection.select("table.table tbody tr");

                for (Element row : rows) {
                    String exchangeRate = row.select("td").get(0).text();
                    BigDecimal buyRate = new BigDecimal(row.select("td").get(1).text().replace(",", ""));
                    BigDecimal sellRate = new BigDecimal(row.select("td").get(2).text().replace(",", ""));

                    long buyRateLong = buyRate.multiply(BigDecimal.valueOf(1000)).longValue();
                    long sellRateLong = sellRate.multiply(BigDecimal.valueOf(1000)).longValue();

                    String standardizedExchangeRate = CurrencyUtils.getStandardizedExchangeRateName(exchangeRate);
                    String standardizedCurrencyCode = CurrencyUtils.getCurrencyCode(Objects.requireNonNull(standardizedExchangeRate));

                    FamiliarResponse cotizacion = FamiliarResponse.builder()
                            .exchangeRate(standardizedExchangeRate)
                            .currencyCode(standardizedCurrencyCode)
                            .buyRate(buyRateLong)
                            .sellRate(sellRateLong)
                            .build();
                    cotizaciones.add(cotizacion);
                }
            }
            log.info("La llamada se ejecuto con exito");
        } catch (IOException e) {
            log.error("Error al obtener las cotizaciones de Banco Familiar", e);
            throw new Exception("Error al obtener las cotizaciones de Banco Familiar");
        }

        return cotizaciones;
    }

    @Override
    public List<GnbExternal> findGnbCotizacion() throws Exception {
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

            listadoGbnExternal.getExchangeRates().forEach(cotizacion -> {
                String exchangeRate = cotizacion.getCurrencyDesc();
                String standardizedExchangeRate = CurrencyUtils.getStandardizedExchangeRateName(exchangeRate);
                String standardizedCurrencyCode = CurrencyUtils.getCurrencyCode(Objects.requireNonNull(standardizedExchangeRate));

                cotizacion.setCurrencyDesc(standardizedExchangeRate);
                cotizacion.setCurrencyCode(standardizedCurrencyCode);

            });

            log.info("La llamada se ejecuto con exito");
            return listadoGbnExternal.getExchangeRates();
        } catch (IOException | InterruptedException e) {
            log.error("Error al obtener las cotizaciones de Banco Continental", e);
            throw new Exception("Error al obtener las cotizaciones del Banco Continental");
        }
    }

    @Override
    public List<RioExternal> findRioCotizacion() throws Exception {
        log.info("Obteniendo cotizacion del banco continental");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(cotipProperties.getRioPath()))
                .header("Accept", "application/json")
                .header("x-xsrf-token", "eyJpdiI6IjlFWjJhcGpLRU92REhmUW5BOFNtM0E9PSIsInZhbHVlIjoia09aZkw2bkNRODV4UHEwYjhzcTdHSGV3bVRtUFBKNFZ3U1FySi9WSTlxUGxkRWw1SWs5LytTam9JOXBhUExOWEdiSTRNY0xmTEQ2TnBvNWhOa0VLekZpU3ZEUUlYSXFoMWoxallrRjVuVFp3UXRueVcyb2IzZVlQNWRSaWRpcWkiLCJtYWMiOiIzNWU5MGQxNGI5YTcxODE0ZDNkOWNkNmQ0YThhYWQxN2YyZGQyYzFhZTVmODYyNzU0MGRmNDdkNTk4ZTg2MjMwIiwidGFnIjoiIn0=")
                .header("x-csrf-token", "xcZ8gj8H3yegoQ5It3O6DhJxeeyELZJxVwOV7eaZ")
                .header("x-requested-with", "XMLHttpRequest")
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            List<RioExternal> rioExternalList = objectMapper.readValue(response.body(),
                    new TypeReference<>() {
                    });

            rioExternalList.forEach(cotizacion -> {
                String exchangeRate = cotizacion.getExchangeRate().trim();
                String standardizedExchangeRate = CurrencyUtils.getStandardizedExchangeRateName(exchangeRate);
                String standardizedCurrencyCode = CurrencyUtils.getCurrencyCode(Objects.requireNonNull
                        (standardizedExchangeRate));

                cotizacion.setExchangeRate(standardizedExchangeRate);
                cotizacion.setCurrencyCode(standardizedCurrencyCode);

            });

            log.info("La llamada se ejecuto con exito");
            return rioExternalList;
        } catch (IOException | InterruptedException e) {
            log.error("Error al obtener las cotizaciones de Banco Rio", e);
            throw new Exception("Error al obtener las cotizaciones del Banco Rio");
        }
    }

    @Override
    public List<SolarBankResponse> findSolarBankCotip() throws Exception {
        List<SolarBankResponse> cotizaciones = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(cotipProperties.getSolarBankPath()).get();

            Element scriptElement = doc.selectFirst("script#__NEXT_DATA__");
            if (scriptElement != null) {
                String jsonData = scriptElement.html();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(jsonData);

                JsonNode quotationsNode = rootNode.path("props").path("pageProps").path("quotation");
                if (quotationsNode.isArray()) {
                    for (JsonNode node : quotationsNode) {
                        String name = node.get("name").asText();
                        long buyRate = new BigDecimal(node.get("bid_price").asText()).longValue();
                        long sellRate = new BigDecimal(node.get("ask_price").asText()).longValue();

                        String standardizedExchangeRate = CurrencyUtils.getStandardizedExchangeRateName(name);
                        String standardizedCurrencyCode = CurrencyUtils.getCurrencyCode(Objects.requireNonNull(standardizedExchangeRate));

                        SolarBankResponse cotizacion = SolarBankResponse.builder()
                                .exchangeRate(standardizedExchangeRate)
                                .currencyCode(standardizedCurrencyCode)
                                .buyRate(buyRate)
                                .sellRate(sellRate)
                                .build();

                        cotizaciones.add(cotizacion);
                    }
                }
            }
            log.info("La llamada se ejecuto con exito");
        } catch (IOException e) {
            log.error("Error al obtener las cotizaciones de Banco Solar", e);
            throw new Exception("Error al obtener las cotizaciones del Banco Solar");
        }

        return cotizaciones;
    }


}
