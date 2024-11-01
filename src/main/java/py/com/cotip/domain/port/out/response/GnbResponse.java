package py.com.cotip.domain.port.out.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GnbResponse {

    // ::: vars

    private String exchangeRate;
    private String currencyCode;
    private Long buyRate;
    private Long sellRate;
}
