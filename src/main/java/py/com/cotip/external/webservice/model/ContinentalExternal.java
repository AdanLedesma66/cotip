package py.com.cotip.external.webservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
