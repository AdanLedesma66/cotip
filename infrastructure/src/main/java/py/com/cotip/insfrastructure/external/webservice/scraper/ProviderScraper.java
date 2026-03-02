package py.com.cotip.insfrastructure.external.webservice.scraper;

import py.com.cotip.domain.commons.ProviderType;

public interface ProviderScraper<T> {

    ProviderType provider();

    ScrapeExecution<T> execute();
}
