package py.com.cotip.insfrastructure.external.cotipdb.util;

import py.com.cotip.insfrastructure.external.cotipdb.config.properties.CotipDetailsProperties;
import py.com.cotip.insfrastructure.external.cotipdb.model.CotipLocation;


public class CotipConverter {

    public static CotipLocation convertToModelLocation(CotipDetailsProperties.CotipLocation propertiesLocation) {
        if (propertiesLocation == null) {
            throw new IllegalArgumentException("Location properties are missing");
        }

        CotipLocation modelLocation = new CotipLocation();
        modelLocation.setAddress(propertiesLocation.getAddress());
        modelLocation.setLatitude(propertiesLocation.getLatitude());
        modelLocation.setLongitude(propertiesLocation.getLongitude());
        return modelLocation;
    }

}
