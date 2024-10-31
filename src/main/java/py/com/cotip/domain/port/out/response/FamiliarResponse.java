package py.com.cotip.domain.port.out.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class FamiliarResponse {

    private String exchangeRate;
    private String currencyCode;
    private Long buyRate;
    private Long sellRate;

}
