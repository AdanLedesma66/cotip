package py.com.cotip.external.cotipdb.config;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.com.cotip.domain.commons.ProviderType;
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

    @Column(name = "sucursal")
    private String location;

    @Column(name = "ciudad")
    private String city;

    @Column(name = "fecha_carga")
    private OffsetDateTime uploadDate;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.uploadDate = OffsetDateTime.now();
    }

}
