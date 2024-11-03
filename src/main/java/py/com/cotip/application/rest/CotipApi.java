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

    @GetMapping("/continental")
    public ResponseEntity continental() throws Exception{
        return ResponseEntity.ok(action.findCotizacionContinentalResponse());
    }

    @GetMapping("/familiar")
    public ResponseEntity familiar() throws Exception { return ResponseEntity.ok(action.findFamiliarCotizacionResponse());}

    @GetMapping("/gnb")
    public ResponseEntity gnb() throws Exception {
        return ResponseEntity.ok(action.findGnbCotizacionResponse());
    }

    @GetMapping("/banco-rio")
    public ResponseEntity bancoRio() throws Exception{
        return ResponseEntity.ok(action.findRioCotizacionResponse());
    }

    @GetMapping("/banco-solar")
    public ResponseEntity bancoSolar() throws Exception{
        return ResponseEntity.ok(action.findSolarBankCotip());
    }



}
