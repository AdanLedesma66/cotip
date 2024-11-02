package py.com.cotip.domain.port.in;


import py.com.cotip.application.rest.model.CotipDto;

import java.util.List;

public interface CotipInPort {

    List<CotipDto> findCotizacionContinentalResponse() throws Exception;

    List<CotipDto> findFamiliarCotizacionResponse() throws Exception;

    List<CotipDto> findGnbCotizacionResponse() throws Exception;

    List<CotipDto> findRioCotizacionResponse() throws Exception;

}
