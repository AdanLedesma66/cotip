package py.com.cotip.application.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FamiliarDto {

    // ::: vars

    private String exchangeRate;
    private String currencyCode;
    private BigDecimal buyRate;
    private BigDecimal sellRate;

}
