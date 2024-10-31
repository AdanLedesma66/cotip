package py.com.cotip.external.cotipdb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import py.com.cotip.external.cotipdb.config.CotipBaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cotip_entity")
public class CotipEntity extends CotipBaseEntity {

    // ::: vars

    private String exchangeRate;

    private String currencyCode;

    private long buyRate;

    private long sellRate;


}
