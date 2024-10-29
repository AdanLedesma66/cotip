package py.com.cotip.external.webservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContinentalExternal {

    // ::: vars

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "divisa")
    private String exchangeRate;

    @JsonProperty(value = "codigo")
    private String currencyCode;

    @JsonProperty(value = "compra")
    private BigDecimal buyRate;

    @JsonProperty(value = "venta")
    private BigDecimal sellRate;

    @JsonProperty(value = "icono")
    private String icono;

}
