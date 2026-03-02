package py.com.cotip.insfrastructure.external.cotipdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.insfrastructure.external.cotipdb.util.ProviderTypeConverter;

import jakarta.persistence.Convert;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "scrape_audit")
public class ScrapeAuditEntity {

    @Id
    private UUID id;

    @Convert(converter = ProviderTypeConverter.class)
    @Column(name = "provider", nullable = false)
    private ProviderType provider;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "error_detail", length = 500)
    private String errorDetail;

    @Column(name = "raw_payload", columnDefinition = "TEXT")
    private String rawPayload;

    @Column(name = "executed_at", nullable = false)
    private OffsetDateTime executedAt;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        executedAt = OffsetDateTime.now();
    }
}
