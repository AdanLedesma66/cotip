package py.com.cotip.application.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RioDto {

    // ::: vars

    private String exchangeRate;
    private String currencyCode;
    private Long buyRate;
    private Long sellRate;
}
