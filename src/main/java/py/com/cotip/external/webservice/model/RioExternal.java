package py.com.cotip.external.webservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RioExternal {

    // ::: vars

    @JsonProperty(value = "MONEDA")
    private String exchangeRate;

    @JsonProperty(value = "CODIGO")
    private String currencyCode;

    @JsonProperty(value = "COMPRA")
    private String buyRate;

    @JsonProperty(value = "VENTA")
    private String sellRate;

}
