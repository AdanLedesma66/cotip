package py.com.cotip.insfrastructure.external.cotipdb;

import lombok.AllArgsConstructor;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.port.out.ScrapeAuditPort;
import py.com.cotip.insfrastructure.external.cotipdb.model.ScrapeAuditEntity;
import py.com.cotip.insfrastructure.external.cotipdb.repository.ScrapeAuditRepository;

@AllArgsConstructor
public class ScrapeAuditDbOutPortImpl implements ScrapeAuditPort {

    private final ScrapeAuditRepository scrapeAuditRepository;

    @Override
    public void saveSuccess(ProviderType providerType, String rawPayload) {
        ScrapeAuditEntity entity = new ScrapeAuditEntity();
        entity.setProvider(providerType);
        entity.setStatus("SUCCESS");
        entity.setRawPayload(rawPayload);
        scrapeAuditRepository.save(entity);
    }

    @Override
    public void saveFailure(ProviderType providerType, String rawPayload, String errorDetail) {
        ScrapeAuditEntity entity = new ScrapeAuditEntity();
        entity.setProvider(providerType);
        entity.setStatus("FAILURE");
        entity.setRawPayload(rawPayload);
        entity.setErrorDetail(errorDetail);
        scrapeAuditRepository.save(entity);
    }
}
