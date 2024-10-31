package py.com.cotip.application.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class GnbDto {

    private String exchangeRate;
    private String currencyCode;
    private BigDecimal buyRate;
    private BigDecimal sellRate;

}
