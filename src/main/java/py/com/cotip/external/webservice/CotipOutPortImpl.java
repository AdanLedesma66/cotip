package py.com.cotip.external.webservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import py.com.cotip.domain.port.out.CotipOutPort;
import py.com.cotip.domain.port.out.response.FamiliarResponse;
import py.com.cotip.external.webservice.config.CotipProperties;
import py.com.cotip.external.webservice.model.ContinentalBearerExternal;
import py.com.cotip.external.webservice.model.ContinentalExternal;
import py.com.cotip.external.webservice.util.CurrencyUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

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
                BigDecimal buyRate = CurrencyUtil.convertRate(cotizacion.getBuyRate());
                BigDecimal sellRate = CurrencyUtil.convertRate(cotizacion.getSellRate());

                // Asignar los valores transformados
                cotizacion.setBuyRate(buyRate);
                cotizacion.setSellRate(sellRate);
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

                    FamiliarResponse cotizacion = FamiliarResponse.builder()
                            .exchangeRate(exchangeRate)
                            .buyRate(buyRate)
                            .sellRate(sellRate)
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

}
