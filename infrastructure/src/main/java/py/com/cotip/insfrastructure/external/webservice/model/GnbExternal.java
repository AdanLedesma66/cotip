package py.com.cotip.insfrastructure.external.webservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GnbExternal {

    private String currencyCode;
    private String electronicSellPrice;
    private String electronicBuyPrice;
    private String currencyDesc;
    private String currencyAbbreviation;
    private String cashBuyPrice;
    private String checkSellPrice;
    private String checkBuyPrice;
    private Integer order;
}
