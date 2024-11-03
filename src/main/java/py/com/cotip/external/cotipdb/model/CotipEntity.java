package py.com.cotip.external.cotipdb.model;

import jakarta.persistence.*;
import lombok.*;
import py.com.cotip.domain.commons.RateChange;
import py.com.cotip.external.cotipdb.config.CotipBaseEntity;
import py.com.cotip.external.cotipdb.util.RateChangeConverter;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cotip_entity")
public class CotipEntity extends CotipBaseEntity {

    // ::: vars

    @Column(name = "tipo_cambio")
    private String exchangeRate;

    @Column(name = "codigo_moneda")
    private String currencyCode;

    @Column(name = "tasa_compra")
    private Long buyRate;

    @Column(name = "tasa_venta")
    private Long sellRate;

    @Convert(converter = RateChangeConverter.class)
    @Column(name = "estado_compra")
    private RateChange buyRateStatus;

    @Convert(converter = RateChangeConverter.class)
    @Column(name = "estado_venta")
    private RateChange sellRateStatus;


}
