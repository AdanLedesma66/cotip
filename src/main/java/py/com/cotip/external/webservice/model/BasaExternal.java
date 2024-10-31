package py.com.cotip.external.webservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasaExternal {

    // ::: vars

    private String exchangeRate;
    private String currencyCode;
    private Long buyRate;
    private Long sellRate;

}
