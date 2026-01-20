package py.com.cotip.application.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import py.com.cotip.application.config.model.CotipResponse;
import py.com.cotip.application.rest.model.CotipDto;
import py.com.cotip.domain.commons.CotipCity;
import py.com.cotip.domain.port.in.CotipInPort;
import py.com.cotip.domain.port.in.request.FindMaxiExchangeRateRequest;

import java.util.List;

@RestController
@RequestMapping("/cotip/v1")
public class CotipApi {

    // ::: API

    @Autowired
    private CotipInPort action;

    // ::: REQUEST

    @GetMapping("/banco-continental")
    public CotipResponse<List<CotipDto>> continentalBank() {
        return CotipResponse.of(action.findLatestContinentalBankExchangeRates());
    }

    @GetMapping("/banco-gnb")
    public CotipResponse<List<CotipDto>> gnbBank() {
        return CotipResponse.of(action.findLatestGnbBankExchangeRates());
    }

    @GetMapping("/maxi-cambios")
    public CotipResponse<List<CotipDto>> maxiExchange(
            @RequestParam(required = false) CotipCity city
    ){
        FindMaxiExchangeRateRequest request = FindMaxiExchangeRateRequest.builder()
                .city(city)
                .build();

        return CotipResponse.of(action.findLatestMaxiExchangeRates(request));
    }


}
