package py.com.cotip.domain.port.in;


import py.com.cotip.application.rest.model.BasaDto;
import py.com.cotip.application.rest.model.FamiliarDto;
import py.com.cotip.application.rest.model.GnbDto;
import py.com.cotip.domain.port.out.response.ContinentalResponse;

import java.util.List;

public interface CotipInPort {

    List<ContinentalResponse> findCotizacionContinentalResponse() throws Exception;

    List<FamiliarDto> findFamiliarCotizacionResponse() throws Exception;

    List<GnbDto> findGnbCotizacionResponse() throws Exception;

    List<BasaDto> findBasaCotizacionResponse() throws Exception;

}
