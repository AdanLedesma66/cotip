package py.com.cotip.domain.port.out.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class ContinentalBankResponse {

    // ::: vars

    private String exchangeRate;
    private String currencyCode;
    private Long buyRate;
    private Long sellRate;

}
