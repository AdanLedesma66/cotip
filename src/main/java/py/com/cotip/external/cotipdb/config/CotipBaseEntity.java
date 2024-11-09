package py.com.cotip.external.cotipdb.config;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.external.cotipdb.model.CotipDetails;
import py.com.cotip.external.cotipdb.util.ProviderTypeConverter;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@MappedSuperclass
@Data
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

    @Column(name = "fecha_carga")
    private OffsetDateTime uploadDate;

    @Column(name = "cotip_details")
    @JdbcTypeCode(SqlTypes.JSON)
    private CotipDetails cotipDetails;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.uploadDate = OffsetDateTime.now();
    }

}
