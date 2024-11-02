package py.com.cotip.external.cotipdb.config;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private boolean enabled;

    private String provider;

    private String location;

    private OffsetDateTime uploadDate;

}
