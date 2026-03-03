package py.com.cotip.insfrastructure.external.webservice.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import py.com.cotip.domain.commons.CotipError;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.exception.CotipException;
import py.com.cotip.domain.port.out.response.MaxiExchangeResponse;
import py.com.cotip.insfrastructure.external.webservice.config.CotipProperties;
import py.com.cotip.insfrastructure.external.webservice.util.CurrencyUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MaxiCambiosScraper extends AbstractProviderScraper<MaxiExchangeResponse, Document> {

    private final CotipProperties cotipProperties;

    public MaxiCambiosScraper(CotipProperties cotipProperties) {
        this.cotipProperties = cotipProperties;
    }

    @Override
    public ProviderType provider() {
        return ProviderType.MAXI_CAMBIOS;
    }

    @Override
    protected String fetchRaw() throws Exception {
        return Jsoup.connect(cotipProperties.getMaxicambiosPath())
                .timeout(cotipProperties.getHttpReadTimeoutSeconds() * 1000)
                .get()
                .html();
    }

    @Override
    protected Document parse(String rawPayload) {
        return Jsoup.parse(rawPayload);
    }

    @Override
    protected List<MaxiExchangeResponse> normalize(Document document) {
        List<MaxiExchangeResponse> rates = new ArrayList<>();

        Map<String, String> sectionsMap = Map.of(
                "Asunción", "cotizacion-carousel",
                "Ciudad del Este", "cotizacion-cd",
                "Cheque Transferencia", "cotizacion-cheq-tra",
                "Arbitraje", "cotizacion-arbi"
        );

        for (Map.Entry<String, String> entry : sectionsMap.entrySet()) {
            String sectionName = entry.getKey();
            String sectionId = entry.getValue();
            Elements currencyItems = document.select("#" + sectionId + " .cotizDivSmall");

            for (Element item : currencyItems) {
                Element exchangeRateElement = item.select("p[style*=text-overflow], p[style*=font-size]").first();
                if (exchangeRateElement == null) {
                    continue;
                }

                String exchangeRate = exchangeRateElement.text().trim();
                String buyRateText = item.select("p:contains(Compra) + p").text().replace(",", ".").split(" ")[0];
                String sellRateText = item.select("p:contains(Venta) + p").text().replace(",", ".").split(" ")[0];

                BigDecimal buyRate = new BigDecimal(buyRateText).setScale(0, RoundingMode.DOWN);
                BigDecimal sellRate = new BigDecimal(sellRateText).setScale(0, RoundingMode.DOWN);

                exchangeRate = switch (exchangeRate.toLowerCase()) {
                    case "dólar" -> "Dólar Cheque / Transferencia";
                    case "euro" -> "Euro Cheque / Transferencia";
                    default -> exchangeRate;
                };

                CurrencyUtils.StandardizedRate standardizedRate = CurrencyUtils.standardizeExchangeRate(exchangeRate);
                if (standardizedRate == null) {
                    continue;
                }

                rates.add(MaxiExchangeResponse.builder()
                        .exchangeRate(standardizedRate.exchangeRateName())
                        .currencyCode(standardizedRate.currencyCode())
                        .quoteModality(standardizedRate.quoteModality())
                        .buyRate(buyRate.longValue())
                        .sellRate(sellRate.longValue())
                        .city("Cheque Transferencia".equals(sectionName) ? null : sectionName)
                        .build());
            }
        }

        return rates;
    }

    @Override
    protected void validate(List<MaxiExchangeResponse> normalizedRates) {
        if (normalizedRates == null || normalizedRates.isEmpty()) {
            throw new CotipException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    CotipError.MAXI_CAMBIOS_ERROR.getCode(),
                    "MaxiCambios scraper returned no rates",
                    true);
        }
    }
}
