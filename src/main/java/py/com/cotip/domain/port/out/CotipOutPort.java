package py.com.cotip.domain.port.out;

import py.com.cotip.domain.port.out.response.FamiliarResponse;
import py.com.cotip.external.webservice.model.BasaExternal;
import py.com.cotip.external.webservice.model.ContinentalBearerExternal;
import py.com.cotip.external.webservice.model.ContinentalExternal;
import py.com.cotip.external.webservice.model.GnbExternal;

import java.util.List;

public interface CotipOutPort {

    ContinentalBearerExternal findContinentalBearerToken() throws Exception;

    List<ContinentalExternal> findContinentalCotizacion() throws Exception;

    List<FamiliarResponse> findFamiliarCotizacion() throws Exception;

    List<GnbExternal> findGnbCotizacion() throws Exception;

    List<BasaExternal> findBasaCotizacion() throws Exception;

}
