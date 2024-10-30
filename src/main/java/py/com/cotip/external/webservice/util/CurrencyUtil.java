package py.com.cotip.external.webservice.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyUtil {

    // ::: utils

    public static BigDecimal convertRate(BigDecimal rate) {
        return rate.divide(new BigDecimal("1000"), 3, RoundingMode.HALF_UP);
    }

}
