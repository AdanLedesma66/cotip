package py.com.cotip.insfrastructure.external.webservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import py.com.cotip.domain.port.out.ApplicationMetricsPort;
import py.com.cotip.domain.port.out.ProviderRatesSourcePort;
import py.com.cotip.domain.port.out.ScrapeAuditPort;
import py.com.cotip.insfrastructure.external.webservice.ProviderRatesSourceAdapter;
import py.com.cotip.insfrastructure.external.webservice.scraper.CambiosChacoScraper;
import py.com.cotip.insfrastructure.external.webservice.scraper.ContinentalScraper;
import py.com.cotip.insfrastructure.external.webservice.scraper.GnbScraper;
import py.com.cotip.insfrastructure.external.webservice.scraper.MaxiCambiosScraper;

@Configuration
public class CotipExternalConfig {

    // ::: beans

    @Bean
    public ProviderRatesSourcePort cotizacionOutPort(ContinentalScraper continentalScraper,
                                                      GnbScraper gnbScraper,
                                                      MaxiCambiosScraper maxiCambiosScraper,
                                                      CambiosChacoScraper cambiosChacoScraper,
                                                      ApplicationMetricsPort metricsPort,
                                                      ScrapeAuditPort scrapeAuditPort) {
        return new ProviderRatesSourceAdapter(continentalScraper, gnbScraper, maxiCambiosScraper,
                cambiosChacoScraper,
                metricsPort, scrapeAuditPort);
    }
}
