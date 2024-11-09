package py.com.cotip.external.cotipdb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.Data;
import py.com.cotip.external.cotipdb.util.DayOfWeekConverter;

import java.time.LocalTime;

@Data
public class CotipDailySchedule {

    // ::: vars

    @Column(name = "dia")
    @Convert(converter = DayOfWeekConverter.class)
    private String day;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    @Column(name = "apertura")
    private LocalTime openingTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    @Column(name = "cierre")
    private LocalTime closingTime;
}
