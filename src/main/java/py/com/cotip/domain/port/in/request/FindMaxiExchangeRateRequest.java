package py.com.cotip.domain.port.in.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import py.com.cotip.domain.commons.CotipCity;

@Data
@Builder
@AllArgsConstructor
public class FindMaxiExchangeRateRequest {

    // ::: vars

    private CotipCity city;
}
