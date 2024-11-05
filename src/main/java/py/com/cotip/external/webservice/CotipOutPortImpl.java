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
import org.springframework.http.HttpStatus;
import py.com.cotip.application.config.exception.CotipException;
import py.com.cotip.domain.commons.CotipError;
import py.com.cotip.domain.port.out.CotipOutPort;
import py.com.cotip.domain.port.out.response.*;
import py.com.cotip.external.webservice.config.CotipProperties;
import py.com.cotip.external.webservice.model.ContinentalBearerExternal;
import py.com.cotip.external.webservice.model.ContinentalExternal;
import py.com.cotip.external.webservice.model.ListadoGbnExternal;
import py.com.cotip.external.webservice.model.RioExternal;
import py.com.cotip.external.webservice.util.CurrencyUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
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
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + "getContinentalBearerToken().getAccessToken()")
                    .header("Subscription-Key", "3c35bb9e5fa948adb8d64c123d9d1a45")
                    .GET()
                    .build();

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

                ContinentalBankResponse bankResponse = ContinentalBankResponse.builder()
                        .exchangeRate(cotizacion.getExchangeRate())
                        .currencyCode(cotizacion.getCurrencyCode())
                        .buyRate(cotizacion.getBuyRate().longValue())
                        .sellRate(cotizacion.getSellRate().longValue())
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
    public List<FamiliarBankResponse> fetchFamiliarBankExchangeRates() {
        log.info("Obteniendo cotizacion del banco familiar");
        List<FamiliarBankResponse> exchangeRates = new ArrayList<>();

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

                    FamiliarBankResponse cotizacion = FamiliarBankResponse.builder()
                            .exchangeRate(standardizedExchangeRate)
                            .currencyCode(standardizedCurrencyCode)
                            .buyRate(buyRateLong)
                            .sellRate(sellRateLong)
                            .build();
                    exchangeRates.add(cotizacion);
                }
            }
            log.info("La llamada se ejecuto con exito");
        } catch (IOException e) {
            log.error("Error al obtener las cotizaciones de Banco Familiar", e);
            throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(), CotipError.FAMILIAR_BANK_ERROR.getCode(),
                    CotipError.FAMILIAR_BANK_ERROR.getDescription());
        }

        return exchangeRates;
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

            listadoGbnExternal.getExchangeRates().forEach(cotizacion -> {
                String exchangeRate = cotizacion.getCurrencyDesc();
                String standardizedExchangeRate = CurrencyUtils.getStandardizedExchangeRateName(exchangeRate);
                String standardizedCurrencyCode = CurrencyUtils.getCurrencyCode(Objects.requireNonNull(standardizedExchangeRate));

                cotizacion.setCurrencyDesc(standardizedExchangeRate);
                cotizacion.setCurrencyCode(standardizedCurrencyCode);

                GnbBankResponse bankResponse = GnbBankResponse.builder()
                        .exchangeRate(cotizacion.getCurrencyDesc())
                        .currencyCode(cotizacion.getCurrencyCode())
                        .buyRate(stringToLong(cotizacion.getElectronicBuyPrice()))
                        .sellRate(stringToLong(cotizacion.getElectronicSellPrice()))
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
    public List<RioBankResponse> fetchRioBankExchangeRates() {
        List<RioBankResponse> exchangeRates = new ArrayList<>();

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

                RioBankResponse bankResponse = RioBankResponse.builder()
                        .exchangeRate(cotizacion.getExchangeRate())
                        .currencyCode(cotizacion.getCurrencyCode())
                        .buyRate(stringToLong(cotizacion.getBuyRate()))
                        .sellRate(stringToLong(cotizacion.getSellRate()))
                        .build();

                exchangeRates.add(bankResponse);

            });

            log.info("La llamada se ejecuto con exito");
            return exchangeRates;
        } catch (IOException | InterruptedException e) {
            log.error("Error al obtener las cotizaciones de Banco Rio", e);
            throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(), CotipError.RIO_BANK_ERROR.getCode(),
                    CotipError.RIO_BANK_ERROR.getDescription());
        }
    }

    @Override
    public List<SolarBankResponse> fetchSolarBankExchangeRates() {
        List<SolarBankResponse> exchangeRates = new ArrayList<>();

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

                        exchangeRates.add(cotizacion);
                    }
                }
            }
            log.info("La llamada se ejecuto con exito");
        } catch (IOException e) {
            log.error("Error al obtener las cotizaciones de Banco Solar", e);
            throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(), CotipError.SOLAR_BANK_ERROR.getCode(),
                    CotipError.SOLAR_BANK_ERROR.getDescription());
        }

        return exchangeRates;
    }

    @Override
    public List<BnfBankResponse> fetchBnfBankExchangeRates() {
        List<BnfBankResponse> exchangeRates = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(cotipProperties.getBnfBankPath()).get();

            Element cotizacionesTable = doc.selectFirst("table.table-condensed.cotiz-tabla tbody");

            if (cotizacionesTable != null) {
                Elements rows = cotizacionesTable.select("tr");

                for (Element row : rows) {
                    Elements cells = row.select("td");

                    if (cells.size() >= 4) {
                        String exchangeRate = cells.get(1).text().trim();

                        BigDecimal buyRate = new BigDecimal(cells.get(2).text().trim());
                        BigDecimal sellRate = new BigDecimal(cells.get(3).text().trim());

                        String standardizedExchangeRate = CurrencyUtils.getStandardizedExchangeRateName(exchangeRate);
                        String currencyCode = CurrencyUtils.getCurrencyCode(standardizedExchangeRate);

                        if (standardizedExchangeRate != null && currencyCode != null) {
                            BnfBankResponse bankResponse = BnfBankResponse.builder()
                                    .exchangeRate(standardizedExchangeRate)
                                    .currencyCode(currencyCode)
                                    .buyRate(buyRate.longValue())
                                    .sellRate(sellRate.longValue())
                                    .build();
                            exchangeRates.add(bankResponse);
                        }
                    }
                }
                log.info("La llamada se ejecutó con éxito");
            }
        } catch (IOException e) {
            log.error("Error al obtener las cotizaciones de Banco Nacional de Fomento");
            throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(), CotipError.BNF_BANK_ERROR.getCode(),
                    CotipError.BNF_BANK_ERROR.getDescription());
        }

        return exchangeRates;
    }

    @Override
    public List<AtlasBankResponse> fetchAtlasBankExchangeRates() {
        List<AtlasBankResponse> exchangeRates = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(cotipProperties.getAtlasBankPath()).get();
            Element carousel = doc.selectFirst("#desktop-currency-carousel .carousel-inner");

            if (carousel != null) {
                Elements items = carousel.select(".carousel-item");

                for (Element item : items) {
                    String exchangeRate = item.select(".currency-nombre").text().trim();
                    BigDecimal buyRate = new BigDecimal(item.select(".currency-compra").text().trim());
                    BigDecimal sellRate = new BigDecimal(item.select(".currency-venta").text().trim());

                    String standardizedExchangeRate = CurrencyUtils.getStandardizedExchangeRateName(exchangeRate);
                    String standardizedCurrencyCode = CurrencyUtils.getCurrencyCode(standardizedExchangeRate);

                    if (standardizedExchangeRate != null && standardizedCurrencyCode != null) {
                        AtlasBankResponse bankResponse = AtlasBankResponse.builder()
                                .exchangeRate(standardizedExchangeRate)
                                .currencyCode(standardizedCurrencyCode)
                                .buyRate(buyRate.longValue())
                                .sellRate(sellRate.longValue())
                                .build();
                        exchangeRates.add(bankResponse);
                    }
                }
            }
            log.info("La llamada se ejecutó con éxito");
        } catch (IOException e) {
            log.error("Error al obtener las cotizaciones de Banco Atlas");
            throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(), CotipError.ATLAS_BANK_ERROR.getCode(),
                    CotipError.ATLAS_BANK_ERROR.getDescription());
        }

        return exchangeRates;
    }

    @Override
    public List<FicFinancialResponse> fetchFicFinancialExchangeRates() throws Exception {
        List<FicFinancialResponse> exchangeRates = new ArrayList<>();

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            Document doc = Jsoup.connect(cotipProperties.getFicFinancialPath())
                    .sslSocketFactory(sslContext.getSocketFactory())
                    .get();

            Elements rows = doc.select("div.cotz_fila");

            for (Element row : rows) {
                String exchangeRate = row.select("div.cotz_moneda").text().trim();

                String buyRateText = row.select("div.cotz_item").get(1).text().trim().replace(",", ".");
                String sellRateText = row.select("div.cotz_item").get(2).text().trim().replace(",", ".");

                BigDecimal buyRate = new BigDecimal(buyRateText).setScale(0, RoundingMode.DOWN);
                BigDecimal sellRate = new BigDecimal(sellRateText).setScale(0, RoundingMode.DOWN);

                String standardizedExchangeRate = CurrencyUtils.getStandardizedExchangeRateName(exchangeRate);
                String standardizedCurrencyCode = CurrencyUtils.getCurrencyCode(standardizedExchangeRate);

                FicFinancialResponse response = FicFinancialResponse.builder()
                        .exchangeRate(standardizedExchangeRate)
                        .currencyCode(standardizedCurrencyCode)
                        .buyRate(buyRate.longValue())
                        .sellRate(sellRate.longValue())
                        .build();

                exchangeRates.add(response);
            }

            log.info("La llamada se ejecutó con éxito");
        } catch (IOException e) {
            log.error("Error al obtener las cotizaciones de Financiera Fic", e);
            throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(), CotipError.FIC_FINANCIAL_ERROR.getCode(),
                    CotipError.FIC_FINANCIAL_ERROR.getDescription());        }

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
