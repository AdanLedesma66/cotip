package py.com.cotip.external.cotipdb;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import py.com.cotip.domain.commons.RateChange;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.port.out.CotipDbOutPort;
import py.com.cotip.external.cotipdb.model.CotipEntity;
import py.com.cotip.external.cotipdb.repository.CotipRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
public class CotipDbOutPortImpl implements CotipDbOutPort {

    private CotipRepository cotipRepository;

    // ::: impls

    @Override
    public List<CotipEntity> saveAllCotipEntity(List<CotipEntity> cotipEntities, ProviderType tipoProveedor) {

        cotipEntities.forEach(cotipEntity -> {

            Optional<CotipEntity> lastCotipEntity = cotipRepository.findTopByExchangeRateAndProviderOrderByUploadDateDesc(
                    cotipEntity.getExchangeRate(), tipoProveedor);

            cotipEntity.setId(UUID.randomUUID());
            cotipEntity.setEnabled(true);
            cotipEntity.setProvider(tipoProveedor);

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

    @Override
    public List<CotipEntity> findAllByProviderOrderByUploadDate(ProviderType tipoProvedor) {
        return cotipRepository.findAllByProviderOrderByUploadDate(tipoProvedor);
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

}
