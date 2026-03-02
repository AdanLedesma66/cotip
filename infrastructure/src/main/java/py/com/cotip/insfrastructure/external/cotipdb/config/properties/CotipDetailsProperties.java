package py.com.cotip.insfrastructure.external.cotipdb.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "cotip.providers")
@Data
public class CotipDetailsProperties {

    private Map<String, ProviderDetails> providers;

    @Data
    public static class ProviderDetails {
        private String phoneNumber;
        private String email;
        private CotipLocation location;
        private CotipSchedule schedule;
        private String website;
        private String whatsappLink;
    }

    @Data
    public static class CotipLocation {
        private String address;
        private Double latitude;
        private Double longitude;
    }

    @Data
    public static class CotipSchedule {
        private List<CotipDailySchedule> dailySchedules;
    }

    @Data
    public static class CotipDailySchedule {
        private String day;
        private String openingTime;
        private String closingTime;
    }
}
