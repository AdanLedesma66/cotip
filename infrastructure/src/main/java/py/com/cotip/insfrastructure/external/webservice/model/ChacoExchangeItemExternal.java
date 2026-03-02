package py.com.cotip.insfrastructure.external.webservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChacoExchangeItemExternal {

    private String isoCode;
    private BigDecimal purchasePrice;
    private BigDecimal salePrice;
    private Integer main;
    private Integer arbitrage;
    private Integer fractionDigits;
    private String updatedAt;
}
