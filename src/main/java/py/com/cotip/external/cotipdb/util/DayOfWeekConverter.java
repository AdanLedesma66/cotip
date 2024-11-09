package py.com.cotip.external.cotipdb.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import py.com.cotip.domain.commons.DayOfWeekLocalized;

@Converter(autoApply = true)
public class DayOfWeekConverter implements AttributeConverter<DayOfWeekLocalized, String> {

    @Override
    public String convertToDatabaseColumn(DayOfWeekLocalized day) {
        if (day == null) {
            return null;
        }
        return day.getSpanish();
    }

    @Override
    public DayOfWeekLocalized convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return DayOfWeekLocalized.fromSpanish(dbData);
    }
}