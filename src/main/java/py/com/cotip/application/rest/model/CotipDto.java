package py.com.cotip.application.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.commons.RateChange;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CotipDto {

    // ::: vars

    private String exchangeRate;
    private String currencyCode;
    private Long buyRate;
    private Long sellRate;
    private RateChange buyRateStatus;
    private RateChange sellRateStatus;
    private boolean enabled;
    private ProviderType provider;
    private String location;
    private String city;
    private OffsetDateTime lastUpdated;

}
