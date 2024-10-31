package py.com.cotip.application.rest.model;

import lombok.Data;

@Data
public class BasaDto {

    // ::: vars

    private String exchangeRate;
    private String currencyCode;
    private Long buyRate;
    private Long sellRate;
}
