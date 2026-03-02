package py.com.cotip.insfrastructure.external.cotipdb.config;

import jakarta.persistence.*;
import lombok.*;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.insfrastructure.external.cotipdb.model.BranchOfficeEntity;
import py.com.cotip.insfrastructure.external.cotipdb.util.ProviderTypeConverter;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CotipBaseEntity implements Serializable {

    // ::: vars

    @Id
    private UUID id;

    @Column(name = "habilitado")
    private boolean enabled;

    @Convert(converter = ProviderTypeConverter.class)
    @Column(name = "proveedor")
    private ProviderType provider;

    @Column(name = "ciudad")
    private String city;

    @Column(name = "sucursal")
    private String branchOffice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_office_id")
    private BranchOfficeEntity branchOfficeRef;

    @Column(name = "fecha_carga")
    private OffsetDateTime uploadDate;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.uploadDate = OffsetDateTime.now();
    }

}
