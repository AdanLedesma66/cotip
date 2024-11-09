package py.com.cotip.domain.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DayOfWeekLocalized {
    SUNDAY("domingo"),
    MONDAY("lunes"),
    TUESDAY("martes"),
    WEDNESDAY("miércoles"),
    THURSDAY("jueves"),
    FRIDAY("viernes"),
    SATURDAY("sábado");

    private final String spanish;

    public static DayOfWeekLocalized fromSpanish(String spanish) {
        for (DayOfWeekLocalized day : values()) {
            if (day.getSpanish().equalsIgnoreCase(spanish)) {
                return day;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + spanish);
    }
}