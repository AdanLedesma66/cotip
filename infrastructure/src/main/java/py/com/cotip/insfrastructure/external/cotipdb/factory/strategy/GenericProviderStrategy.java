package py.com.cotip.insfrastructure.external.cotipdb.factory.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.commons.QuoteModality;
import py.com.cotip.domain.commons.RateChange;
import py.com.cotip.insfrastructure.external.cotipdb.factory.CotipSaveStrategy;
import py.com.cotip.insfrastructure.external.cotipdb.model.CotipEntity;
import py.com.cotip.insfrastructure.external.cotipdb.repository.CotipRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Component("genericProviderStrategy")
public class GenericProviderStrategy implements CotipSaveStrategy {

    private final CotipRepository cotipRepository;

    @Autowired
    public GenericProviderStrategy(CotipRepository cotipRepository) {
        this.cotipRepository = cotipRepository;
    }

    @Override
    public List<CotipEntity> saveCotipEntities(List<CotipEntity> cotipEntities, ProviderType providerType) {
        cotipEntities.forEach(cotipEntity -> {
            cotipEntity.setId(UUID.randomUUID());
            cotipEntity.setEnabled(true);
            cotipEntity.setProvider(providerType);

            Optional<CotipEntity> lastCotipEntity = cotipRepository
                    .findTopByCurrencyCodeAndQuoteModalityAndProviderAndBranchOfficeOrderByUpdatedAtDesc(
                            cotipEntity.getCurrencyCode(),
                            quoteModality(cotipEntity.getQuoteModality()),
                            providerType,
                            cotipEntity.getBranchOffice());

            if (lastCotipEntity.isPresent()) {
                CotipEntity previousCotip = lastCotipEntity.get();
                cotipEntity.setBuyRateStatus(compareRates(cotipEntity.getBuyRate(), previousCotip.getBuyRate()));
                cotipEntity.setSellRateStatus(compareRates(cotipEntity.getSellRate(), previousCotip.getSellRate()));
            } else {
                cotipEntity.setBuyRateStatus(RateChange.UNCHANGED);
                cotipEntity.setSellRateStatus(RateChange.UNCHANGED);
            }
        });

        return cotipRepository.saveAll(cotipEntities);
    }

    private RateChange compareRates(Long currentRate, Long previousRate) {
        if (currentRate > previousRate) {
            return RateChange.INCREASED;
        } else if (currentRate < previousRate) {
            return RateChange.DECREASED;
        }
        return RateChange.UNCHANGED;
    }

    private QuoteModality quoteModality(QuoteModality quoteModality) {
        return quoteModality == null ? QuoteModality.CASH : quoteModality;
    }
}
