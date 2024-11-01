package py.com.cotip.external.webservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GnbExternal {

    // ::: vars

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
