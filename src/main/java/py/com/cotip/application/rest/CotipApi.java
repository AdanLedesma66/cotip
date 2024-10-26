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
    public ResponseEntity cotizaciones(){
        return ResponseEntity.ok(action.findCotizacionContinentalResponse());
    }

}
