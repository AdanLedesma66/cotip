package py.com.cotip.domain.port.out.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class ContinentalResponse {

    // ::: vars

    @JsonProperty(value = "divisa")
    private String exchangeRate;

    @JsonProperty(value = "codigo")
    private String currencyCode;

    @JsonProperty(value = "compra")
    private BigDecimal buyRate;

    @JsonProperty(value = "venta")
    private BigDecimal sellRate;

    @JsonProperty(value = "urlImagen")
    private String urlImage;
}
