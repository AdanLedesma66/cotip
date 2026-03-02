package py.com.cotip.domain.port.out;

import py.com.cotip.domain.commons.ProviderType;

public interface ScrapeAuditPort {

    void saveSuccess(ProviderType providerType, String rawPayload);

    void saveFailure(ProviderType providerType, String rawPayload, String errorDetail);
}
