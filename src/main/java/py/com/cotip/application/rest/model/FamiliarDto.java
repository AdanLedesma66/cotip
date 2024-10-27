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

    private String exchangeRate;
    //todo agregar currency code
    private BigDecimal buyRate;
    private BigDecimal sellRate;
}
