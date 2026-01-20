package py.com.cotip.external.cotipdb.util;

import py.com.cotip.external.cotipdb.config.properties.CotipDetailsProperties;
import py.com.cotip.external.cotipdb.model.CotipLocation;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


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