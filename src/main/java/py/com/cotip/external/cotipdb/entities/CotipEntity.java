package py.com.cotip.external.cotipdb.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import py.com.cotip.external.cotipdb.config.CotipBaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "cotip_entity")
public class CotipEntity extends CotipBaseEntity {

    // ::: vars

    private String exchangeRate;

    private String currencyCode;

    private BigDecimal buyRate;

    private BigDecimal sellRate;


}
