package py.com.cotip.domain.port.in;


import py.com.cotip.domain.port.out.response.ContinentalResponse;

import java.util.List;

public interface CotipInPort {

    List<ContinentalResponse> findCotizacionContinentalResponse();

}
