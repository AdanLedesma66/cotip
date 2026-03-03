package py.com.cotip.insfrastructure.external.cotipdb;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.commons.QuoteModality;
import py.com.cotip.domain.commons.RateChange;
import py.com.cotip.domain.model.ExchangeRateBO;
import py.com.cotip.domain.port.out.ExchangeRateRepositoryPort;
import py.com.cotip.insfrastructure.external.cotipdb.model.BranchOfficeEntity;
import py.com.cotip.insfrastructure.external.cotipdb.model.CotipEntity;
import py.com.cotip.insfrastructure.external.cotipdb.repository.CotipRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class ExchangeRateRepositoryAdapter implements ExchangeRateRepositoryPort {

    private CotipRepository cotipRepository;
    private BranchOfficeResolver branchOfficeResolver;

    // ::: IMPLS

    @Override
    public List<ExchangeRateBO> saveAllExchangeRates(List<ExchangeRateBO> exchangeRates, ProviderType providerType) {

        List<CotipEntity> cotipEntities = exchangeRates.stream()
                .map(exchangeRate -> toEntity(exchangeRate, providerType))
                .toList();

        cotipEntities.forEach(cotipEntity -> {

            Optional<CotipEntity> lastCotipEntity = cotipRepository
                    .findTopByCurrencyCodeAndQuoteModalityAndProviderAndBranchOfficeOrderByUpdatedAtDesc(
                    cotipEntity.getCurrencyCode(),
                    quoteModality(cotipEntity.getQuoteModality()),
                    providerType,
                    cotipEntity.getBranchOffice());

            cotipEntity.setId(UUID.randomUUID());
            cotipEntity.setEnabled(true);
            cotipEntity.setProvider(providerType);

            if (lastCotipEntity.isPresent()) {
                CotipEntity previousCotip = lastCotipEntity.get();
                cotipEntity.setBuyRateStatus(compareRates(cotipEntity.getBuyRate(), previousCotip.getBuyRate()));
                cotipEntity.setSellRateStatus(compareRates(cotipEntity.getSellRate(), previousCotip.getSellRate()));
            } else {
                cotipEntity.setBuyRateStatus(RateChange.UNCHANGED);
                cotipEntity.setSellRateStatus(RateChange.UNCHANGED);
            }

        });

        return cotipRepository.saveAll(cotipEntities).stream().map(this::toDomain).toList();
    }

    @Override
    public List<ExchangeRateBO> findAllByProviderOrderByUpdatedAt(ProviderType providerType) {
        return cotipRepository.findAllByProviderOrderByUpdatedAtDesc(providerType).stream().map(this::toDomain).toList();
    }

    @Override
    public List<ExchangeRateBO> findAllByProviderAndBranchOfficeExternalIdOrderByUpdatedAt(ProviderType providerType,
                                                                                             String branchOfficeExternalId) {
        return cotipRepository.findAllByProviderAndBranchOfficeRef_ExternalBranchIdOrderByUpdatedAtDesc(
                        providerType,
                        branchOfficeExternalId
                )
                .stream()
                .map(this::toDomain)
                .toList();
    }

    // ::: externals

    private RateChange compareRates(Long currentRate, Long previousRate) {
        if (currentRate.compareTo(previousRate) > 0) {
            return RateChange.INCREASED;
        } else if (currentRate.compareTo(previousRate) < 0) {
            return RateChange.DECREASED;
        } else {
            return RateChange.UNCHANGED;
        }
    }

    private CotipEntity toEntity(ExchangeRateBO source, ProviderType providerType) {
        CotipEntity entity = new CotipEntity();
        entity.setExchangeRate(source.getExchangeRate());
        entity.setCurrencyCode(source.getCurrencyCode());
        entity.setQuoteModality(quoteModality(source.getQuoteModality()));
        entity.setBuyRate(source.getBuyRate());
        entity.setSellRate(source.getSellRate());
        entity.setCity(source.getCity());
        entity.setBranchOffice(source.getBranchOffice());
        BranchOfficeEntity resolvedBranchOffice = branchOfficeResolver.resolve(providerType,
                source.getBranchOffice(),
                source.getBranchOfficeExternalId(),
                source.getCity());
        entity.setBranchOfficeRef(resolvedBranchOffice);
        if ((entity.getBranchOffice() == null || entity.getBranchOffice().isBlank()) && resolvedBranchOffice != null) {
            entity.setBranchOffice(resolvedBranchOffice.getName());
        }
        if ((entity.getCity() == null || entity.getCity().isBlank()) && resolvedBranchOffice != null) {
            entity.setCity(resolvedBranchOffice.getCity());
        }
        return entity;
    }

    private ExchangeRateBO toDomain(CotipEntity source) {
        BranchOfficeEntity branchOffice = source.getBranchOfficeRef();
        String branchOfficeName = branchOffice != null ? branchOffice.getName() : source.getBranchOffice();
        String branchOfficeExternalId = branchOffice != null ? branchOffice.getExternalBranchId() : null;
        String city = source.getCity();
        if ((city == null || city.isBlank()) && branchOffice != null) {
            city = branchOffice.getCity();
        }

        return ExchangeRateBO.builder()
                .exchangeRate(source.getExchangeRate())
                .currencyCode(source.getCurrencyCode())
                .quoteModality(quoteModality(source.getQuoteModality()))
                .buyRate(source.getBuyRate())
                .sellRate(source.getSellRate())
                .buyRateStatus(source.getBuyRateStatus())
                .sellRateStatus(source.getSellRateStatus())
                .enabled(source.isEnabled())
                .provider(source.getProvider())
                .branchOffice(branchOfficeName)
                .branchOfficeExternalId(branchOfficeExternalId)
                .city(city)
                .lastUpdated(source.getUpdatedAt())
                .build();
    }

    private QuoteModality quoteModality(QuoteModality quoteModality) {
        return quoteModality == null ? QuoteModality.CASH : quoteModality;
    }

}
