package py.com.cotip.insfrastructure.external.cotipdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import py.com.cotip.insfrastructure.external.cotipdb.config.CotipBaseEntity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "api_client")
public class ApiClientEntity extends CotipBaseEntity {

    @Column(name = "client_name", nullable = false, length = 120, unique = true)
    private String clientName;

    @Column(name = "api_key_hash", nullable = false, length = 128, unique = true)
    private String apiKeyHash;

    @Column(name = "requests_per_minute", nullable = false)
    private Integer requestsPerMinute;

    @PrePersist
    public void applyClientDefaults() {
        if (this.requestsPerMinute == null || this.requestsPerMinute <= 0) {
            this.requestsPerMinute = 120;
        }
    }
}
