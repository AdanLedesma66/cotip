package py.com.cotip.domain.port.out.response;

import lombok.*;
import py.com.cotip.domain.commons.QuoteModality;

@Data
@Builder
@AllArgsConstructor
public class ContinentalBankResponse {

    // ::: vars

    private String exchangeRate;
    private String currencyCode;
    private QuoteModality quoteModality;
    private Long buyRate;
    private Long sellRate;

}
