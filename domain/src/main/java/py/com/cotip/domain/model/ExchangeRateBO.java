package py.com.cotip.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.commons.QuoteModality;
import py.com.cotip.domain.commons.RateChange;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateBO {

    // ::: vars

    private String exchangeRate;
    private String currencyCode;
    private String currencyName;
    private QuoteModality quoteModality;
    private Long buyRate;
    private Long sellRate;
    private RateChange buyRateStatus;
    private RateChange sellRateStatus;
    private boolean enabled;
    private ProviderType provider;
    private String branchOffice;
    private String branchOfficeExternalId;
    private String city;
    private OffsetDateTime lastUpdated;

}
