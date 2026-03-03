package py.com.cotip.insfrastructure.external.cotipdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.insfrastructure.external.cotipdb.config.CotipBaseEntity;
import py.com.cotip.insfrastructure.external.cotipdb.util.ProviderTypeConverter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "scrape_audit")
public class ScrapeAuditEntity extends CotipBaseEntity {

    @Convert(converter = ProviderTypeConverter.class)
    @Column(name = "provider", nullable = false)
    private ProviderType provider;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "error_detail", length = 500)
    private String errorDetail;

    @Column(name = "raw_payload", columnDefinition = "TEXT")
    private String rawPayload;
}
