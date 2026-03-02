package py.com.cotip.insfrastructure.external.webservice.scraper;

import py.com.cotip.domain.commons.ProviderType;

import java.util.List;

public record ScrapeExecution<T>(ProviderType providerType, String rawPayload, List<T> rates) {
}
