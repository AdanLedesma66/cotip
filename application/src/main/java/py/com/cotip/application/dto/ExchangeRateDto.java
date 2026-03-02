package py.com.cotip.application.dto;

import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.commons.RateChange;

import java.time.OffsetDateTime;

public record ExchangeRateDto(
        String exchangeRate,
        String currencyCode,
        Long buyRate,
        Long sellRate,
        RateChange buyRateStatus,
        RateChange sellRateStatus,
        boolean enabled,
        ProviderType provider,
        String branchOffice,
        String city,
        OffsetDateTime lastUpdated
) {
}
