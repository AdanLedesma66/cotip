package py.com.cotip.insfrastructure.external.webservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContinentalExternal {

    @JsonProperty("id")
    private String id;

    @JsonProperty("divisa")
    private String exchangeRate;

    @JsonProperty("codigo")
    private String currencyCode;

    @JsonProperty("compra")
    private BigDecimal buyRate;

    @JsonProperty("venta")
    private BigDecimal sellRate;

    @JsonProperty("icono")
    private String icono;
}
