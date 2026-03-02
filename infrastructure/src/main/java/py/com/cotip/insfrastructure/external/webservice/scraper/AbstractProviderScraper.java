package py.com.cotip.insfrastructure.external.webservice.scraper;

import java.util.List;

public abstract class AbstractProviderScraper<T, P> implements ProviderScraper<T> {

    @Override
    public ScrapeExecution<T> execute() {
        String rawPayload = null;
        try {
            rawPayload = fetchRaw();
            P parsedPayload = parse(rawPayload);
            List<T> normalizedRates = normalize(parsedPayload);
            validate(normalizedRates);
            return new ScrapeExecution<>(provider(), rawPayload, normalizedRates);
        } catch (Exception ex) {
            throw new ProviderScrapeException(provider(), rawPayload, ex);
        }
    }

    protected abstract String fetchRaw() throws Exception;

    protected abstract P parse(String rawPayload) throws Exception;

    protected abstract List<T> normalize(P parsedPayload);

    protected abstract void validate(List<T> normalizedRates);
}
