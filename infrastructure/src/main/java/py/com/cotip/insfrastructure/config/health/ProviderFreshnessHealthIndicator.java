package py.com.cotip.insfrastructure.config.health;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.insfrastructure.external.cotipdb.model.CotipEntity;
import py.com.cotip.insfrastructure.external.cotipdb.repository.CotipRepository;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("providerFreshness")
public class ProviderFreshnessHealthIndicator implements HealthIndicator {

    private final CotipRepository cotipRepository;
    private final long maxFreshnessMinutes;

    public ProviderFreshnessHealthIndicator(CotipRepository cotipRepository,
                                            @Value("${cotip.health.max-freshness-minutes:480}") long maxFreshnessMinutes) {
        this.cotipRepository = cotipRepository;
        this.maxFreshnessMinutes = maxFreshnessMinutes;
    }

    @Override
    public Health health() {
        Map<String, Object> details = new HashMap<>();
        boolean allHealthy = true;

        for (ProviderType provider : List.of(ProviderType.CONTINENTAL_BANK,
                ProviderType.GNB_BANK,
                ProviderType.MAXI_CAMBIOS,
                ProviderType.CAMBIOS_CHACO)) {
            List<CotipEntity> latestRates = cotipRepository.findLatestCotizacionesByProvider(provider);

            if (latestRates.isEmpty()) {
                allHealthy = false;
                details.put(provider.name(), "NO_DATA");
                continue;
            }

            OffsetDateTime latestUpdate = latestRates.stream()
                    .map(CotipEntity::getUploadDate)
                    .filter(java.util.Objects::nonNull)
                    .max(OffsetDateTime::compareTo)
                    .orElse(null);

            if (latestUpdate == null) {
                allHealthy = false;
                details.put(provider.name(), "NO_TIMESTAMP");
                continue;
            }

            long freshnessMinutes = Math.max(0, Duration.between(latestUpdate, OffsetDateTime.now()).toMinutes());
            details.put(provider.name(), freshnessMinutes + "m");

            if (freshnessMinutes > maxFreshnessMinutes) {
                allHealthy = false;
            }
        }

        if (allHealthy) {
            return Health.up().withDetails(details).build();
        }

        return Health.down().withDetails(details).withDetail("maxFreshnessMinutes", maxFreshnessMinutes).build();
    }
}
