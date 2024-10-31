package py.com.cotip.external.webservice.model;

import lombok.Data;


import java.math.BigDecimal;


@Data
public class GnbExternal {

    private String currencyCode;
    private BigDecimal electronicSellPrice;
    private BigDecimal electronicBuyPrice;

    /*
    *         {
            "currencyCode": "USD",
            "electronicSellPrice": "7960.00",
            "electronicBuyPrice": "7810.00",
            "currencyDesc": "DOLAR AMERICANO",
            "currencyAbbreviation": "DOLAR",
            "cashBuyPrice": "7710.00",
            "checkSellPrice": "",
            "checkBuyPrice": "",
            "cashSellPrice": "7980.00",
            "order": 1
        },*/

}
