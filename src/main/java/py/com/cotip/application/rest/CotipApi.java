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

    // :::

    @Autowired
    private CotipInPort action;

    // ::: requests

    @GetMapping("/continental-bank")
    public CotipResponse<List<CotipDto>> continentalBank() {
        return CotipResponse.of(action.findLatestContinentalBankExchangeRates());
    }

    @GetMapping("/familiar-bank")
    public CotipResponse<List<CotipDto>> familiarBank() {
        return CotipResponse.of(action.findLatestFamiliarBankExchangeRates());
    }

    @GetMapping("/gnb-bank")
    public CotipResponse<List<CotipDto>> gnbBank() {
        return CotipResponse.of(action.findLatestGnbBankExchangeRates());
    }

    @GetMapping("/rio-bank")
    public CotipResponse<List<CotipDto>> rioBank() {
        return CotipResponse.of(action.findLatestRioBankExchangeRates());
    }

    @GetMapping("/solar-bank")
    public CotipResponse<List<CotipDto>> solarBank() {
        return CotipResponse.of(action.findLatestSolarBankExchangeRates());
    }

    @GetMapping("/bnf-bank")
    public CotipResponse<List<CotipDto>> nationalDevelopmentBank() {
        return CotipResponse.of(action.findLatestBnfBankExchangeRates());
    }

    @GetMapping("/atlas-bank")
    public CotipResponse<List<CotipDto>> atlasBank() {
        return CotipResponse.of(action.findLatestAtlasBankExchangeRates());
    }

    @GetMapping("/fic-financial")
    public CotipResponse<List<CotipDto>> ficFinancial() throws Exception {
        return CotipResponse.of(action.findLatestFicFinancialExchangeRates());
    }

    @GetMapping("/maxi-exchange")
    public CotipResponse<List<CotipDto>> maxiExchange(
            @RequestParam(required = false) CotipCity city
    ){
        FindMaxiExchangeRateRequest request = FindMaxiExchangeRateRequest.builder()
                .city(city)
                .build();

        return CotipResponse.of(action.findLatestMaxiExchangeRates(request));
    }


}
