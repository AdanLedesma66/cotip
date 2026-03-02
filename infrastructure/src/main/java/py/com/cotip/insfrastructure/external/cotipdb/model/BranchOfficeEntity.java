package py.com.cotip.insfrastructure.external.cotipdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.insfrastructure.external.cotipdb.util.ProviderTypeConverter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "branch_office")
public class BranchOfficeEntity {

    @Id
    private UUID id;

    @Convert(converter = ProviderTypeConverter.class)
    @Column(name = "provider", nullable = false)
    private ProviderType provider;

    @Column(name = "external_branch_id")
    private String externalBranchId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "department")
    private String department;

    @Column(name = "city")
    private String city;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}
