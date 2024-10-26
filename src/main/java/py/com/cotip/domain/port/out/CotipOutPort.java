package py.com.cotip.domain.port.out;

import py.com.cotip.external.model.ContinentalBearerExternal;
import py.com.cotip.external.model.ContinentalExternal;

import java.util.List;

public interface CotipOutPort {

    ContinentalBearerExternal findContinentalBearerToken();

    List<ContinentalExternal> findContinentalCotizacion();

}
