package py.com.cotip.external.cotipdb.config;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CotipBaseEntity {

    // ::: vars

    @Id
    private UUID id;

    private String provider;

    private OffsetDateTime uploadDate;

}
