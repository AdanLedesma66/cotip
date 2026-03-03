package py.com.cotip.domain.port.out.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import py.com.cotip.domain.commons.QuoteModality;

@Data
@Builder
@AllArgsConstructor
public class GnbBankResponse {

    // ::: vars

    private String exchangeRate;
    private String currencyCode;
    private String currencyName;
    private QuoteModality quoteModality;
    private Long buyRate;
    private Long sellRate;
}
