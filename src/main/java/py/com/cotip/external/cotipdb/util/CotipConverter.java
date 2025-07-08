package py.com.cotip.external.cotipdb.util;

import py.com.cotip.external.cotipdb.config.properties.CotipDetailsProperties;
import py.com.cotip.external.cotipdb.model.CotipDailySchedule;
import py.com.cotip.external.cotipdb.model.CotipLocation;
import py.com.cotip.external.cotipdb.model.CotipSchedule;
import py.com.cotip.external.webservice.config.CotipProperties;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


public class CotipConverter {

    public static CotipSchedule convertToModelSchedule(CotipDetailsProperties.CotipSchedule propertiesSchedule) {
        List<CotipDailySchedule> dailySchedules = propertiesSchedule.getDailySchedules().stream()
                .map(CotipConverter::convertToModelDailySchedule)
                .collect(Collectors.toList());

        CotipSchedule modelSchedule = new CotipSchedule();
        modelSchedule.setDailySchedules(dailySchedules);
        return modelSchedule;
    }

    private static CotipDailySchedule convertToModelDailySchedule(CotipDetailsProperties.CotipDailySchedule propertiesDailySchedule) {
        CotipDailySchedule modelDailySchedule = new CotipDailySchedule();
        modelDailySchedule.setDay(propertiesDailySchedule.getDay());
        modelDailySchedule.setOpeningTime(LocalTime.parse(propertiesDailySchedule.getOpeningTime()));
        modelDailySchedule.setClosingTime(LocalTime.parse(propertiesDailySchedule.getClosingTime()));
        return modelDailySchedule;
    }

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