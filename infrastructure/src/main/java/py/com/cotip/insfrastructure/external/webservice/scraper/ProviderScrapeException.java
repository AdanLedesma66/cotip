package py.com.cotip.insfrastructure.external.webservice.scraper;

import py.com.cotip.domain.commons.ProviderType;

public class ProviderScrapeException extends RuntimeException {

    private final ProviderType providerType;
    private final String rawPayload;

    public ProviderScrapeException(ProviderType providerType, String rawPayload, Throwable cause) {
        super(cause);
        this.providerType = providerType;
        this.rawPayload = rawPayload;
    }

    public ProviderType getProviderType() {
        return providerType;
    }

    public String getRawPayload() {
        return rawPayload;
    }
}
