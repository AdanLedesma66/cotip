package py.com.cotip.insfrastructure.external.cotipdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.commons.QuoteModality;
import py.com.cotip.domain.commons.RateChange;
import py.com.cotip.insfrastructure.external.cotipdb.config.CotipBaseEntity;
import py.com.cotip.insfrastructure.external.cotipdb.util.ProviderTypeConverter;
import py.com.cotip.insfrastructure.external.cotipdb.util.RateChangeConverter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cotip_entity")
@Entity
public class CotipEntity extends CotipBaseEntity {

    @Convert(converter = ProviderTypeConverter.class)
    @Column(name = "provider")
    private ProviderType provider;

    @Column(name = "city")
    private String city;

    @Column(name = "branch_office")
    private String branchOffice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_office_id")
    private BranchOfficeEntity branchOfficeRef;

    @Column(name = "exchange_rate")
    private String exchangeRate;

    @Column(name = "currency_code", nullable = false)
    private String currencyCode;

    @Column(name = "currency_name", nullable = false)
    private String currencyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "quote_modality", nullable = false)
    private QuoteModality quoteModality;

    @Column(name = "buy_rate")
    private Long buyRate;

    @Column(name = "sell_rate")
    private Long sellRate;

    @Convert(converter = RateChangeConverter.class)
    @Column(name = "buy_rate_status")
    private RateChange buyRateStatus;

    @Convert(converter = RateChangeConverter.class)
    @Column(name = "sell_rate_status")
    private RateChange sellRateStatus;

}
