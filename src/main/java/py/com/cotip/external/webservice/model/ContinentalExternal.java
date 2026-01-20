package py.com.cotip.external.webservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ContinentalExternal(
        @JsonProperty(value = "id") String id,
        @JsonProperty(value = "divisa") String exchangeRate,
        @JsonProperty(value = "codigo") String currencyCode,
        @JsonProperty(value = "compra") BigDecimal buyRate,
        @JsonProperty(value = "venta") BigDecimal sellRate,
        @JsonProperty(value = "icono") String icono
) {}
