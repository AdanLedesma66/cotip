package py.com.cotip.external.cotipdb.model;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;

@Data
public class CotipSchedule {

    // ::: vars

    @Column(name = "horarios")
    private List<CotipDailySchedule> dailySchedules;

}