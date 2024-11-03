package py.com.cotip.domain.port.out;

import py.com.cotip.domain.port.out.response.CotipOutResponse;
import py.com.cotip.domain.port.out.response.FamiliarResponse;
import py.com.cotip.domain.port.out.response.GnbResponse;
import py.com.cotip.external.webservice.model.*;

import java.util.List;

public interface CotipOutPort {

    ContinentalBearerExternal findContinentalBearerToken() throws Exception;

    List<ContinentalExternal> findContinentalCotizacion() throws Exception;

    List<FamiliarResponse> findFamiliarCotizacion() throws Exception;

    List<GnbExternal> findGnbCotizacion() throws Exception;

    List<RioExternal> findRioCotizacion() throws Exception;

    List<CotipOutResponse> findSolarBankCotip() throws Exception;

}
