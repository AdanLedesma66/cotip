package py.com.cotip.external.cotipdb.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import py.com.cotip.domain.commons.RateChange;

@Converter(autoApply = true)
public class RateChangeConverter implements AttributeConverter<RateChange, String> {

    @Override
    public String convertToDatabaseColumn(RateChange rateChange) {
        if (rateChange == null) {
            return null;
        }
        switch (rateChange) {
            case INCREASED:
                return "INCREMENTADO";
            case DECREASED:
                return "DISMINUIDO";
            case UNCHANGED:
                return "SIN CAMBIOS";
            default:
                throw new IllegalArgumentException("Unexpected value: " + rateChange);
        }
    }

    @Override
    public RateChange convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        switch (dbData.toUpperCase()) {
            case "INCREMENTADO":
                return RateChange.INCREASED;
            case "DISMINUIDO":
                return RateChange.DECREASED;
            case "SIN CAMBIOS":
                return RateChange.UNCHANGED;
            default:
                throw new IllegalArgumentException("Unexpected value: " + dbData);
        }
    }
}