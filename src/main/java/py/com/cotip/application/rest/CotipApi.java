package py.com.cotip.application.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import py.com.cotip.domain.port.in.CotipInPort;

@RestController
@RequestMapping("/cotip/v1")
public class CotipApi {

    // :::

    @Autowired
    private CotipInPort action;

    // ::: requests

    @GetMapping("/continental-bank")
    public ResponseEntity continentalBank() throws Exception{
        return ResponseEntity.ok(action.findLatestContinentalBankExchangeRates());
    }

    @GetMapping("/familiar-bank")
    public ResponseEntity familiarBank() throws Exception { return ResponseEntity.ok(action.findLatestFamiliarBankExchangeRates());}

    @GetMapping("/gnb-bank")
    public ResponseEntity gnbBank() throws Exception {
        return ResponseEntity.ok(action.findLatestGnbBankExchangeRates());
    }

    @GetMapping("/rio-bank")
    public ResponseEntity rioBank() throws Exception{
        return ResponseEntity.ok(action.findLatestRioBankExchangeRates());
    }

    @GetMapping("/solar-bank")
    public ResponseEntity solarBank() throws Exception{
        return ResponseEntity.ok(action.findLatestSolarBankExchangeRates());
    }

    @GetMapping("/bnf-bank")
    public ResponseEntity nationalDevelopmentBank() throws Exception{
        return ResponseEntity.ok(action.findLatestBnfBankExchangeRates());
    }

    @GetMapping("/atlas-bank")
    public ResponseEntity atlasBank() throws Exception{
        return ResponseEntity.ok(action.findLatestAtlasBankExchangeRates());
    }



}
