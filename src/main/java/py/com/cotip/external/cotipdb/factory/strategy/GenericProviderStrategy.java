package py.com.cotip.external.cotipdb.factory.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.commons.RateChange;
import py.com.cotip.external.cotipdb.config.properties.CotipDetailsProperties;
import py.com.cotip.external.cotipdb.factory.CotipSaveStrategy;
import py.com.cotip.external.cotipdb.model.CotipDetails;
import py.com.cotip.external.cotipdb.model.CotipEntity;
import py.com.cotip.external.cotipdb.model.CotipLocation;
import py.com.cotip.external.cotipdb.repository.CotipRepository;
import py.com.cotip.external.cotipdb.util.CotipConverter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Component("genericProviderStrategy")
public class GenericProviderStrategy implements CotipSaveStrategy {

    private final CotipRepository cotipRepository;
    private final CotipDetailsProperties cotipDetailsProperties;

    @Autowired
    public GenericProviderStrategy(CotipRepository cotipRepository, CotipDetailsProperties cotipDetailsProperties) {
        this.cotipRepository = cotipRepository;
        this.cotipDetailsProperties = cotipDetailsProperties;
    }

    @Override
    public List<CotipEntity> saveCotipEntities(List<CotipEntity> cotipEntities, ProviderType providerType) {
        cotipEntities.forEach(cotipEntity -> {
            CotipDetailsProperties.ProviderDetails providerDetails = cotipDetailsProperties.getProviders().get(providerType.name());

            if (providerDetails == null) {
                throw new IllegalArgumentException("No provider details found for: " + providerType.name());
            }

            CotipDetails cotipDetails = new CotipDetails();
            cotipDetails.setPhoneNumber(providerDetails.getPhoneNumber());
            cotipDetails.setCorreo(providerDetails.getEmail());

            CotipLocation location = CotipConverter.convertToModelLocation(providerDetails.getLocation());

            cotipDetails.setLocation(location);
            cotipDetails.setWebsite(providerDetails.getWebsite());
            cotipDetails.setWhatsappLink(providerDetails.getWhatsappLink());

            cotipEntity.setId(UUID.randomUUID());
            cotipEntity.setEnabled(true);
            cotipEntity.setCotipDetails(cotipDetails);

            Optional<CotipEntity> lastCotipEntity = cotipRepository.findTopByExchangeRateAndProviderOrderByUploadDateDesc(
                    cotipEntity.getExchangeRate(), providerType);

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
}