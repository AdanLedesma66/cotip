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

    private String exchangeRate;
    private String currencyCode;
    private long buyRate;
    private long sellRate;

}
