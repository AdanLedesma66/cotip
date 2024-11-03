package py.com.cotip.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import py.com.cotip.application.rest.model.CotipDto;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.mapper.CotipDomainMapper;
import py.com.cotip.domain.port.in.CotipInPort;
import py.com.cotip.domain.port.out.CotipDbOutPort;
import py.com.cotip.domain.port.out.CotipOutPort;
import py.com.cotip.external.cotipdb.mapper.CotipDbMapper;
import py.com.cotip.external.cotipdb.model.CotipEntity;

import java.util.List;

@Slf4j
public class CotipService implements CotipInPort {

    // ::: inyects

    private final CotipOutPort cotipOutPort;
    private final CotipDbOutPort cotipDbOutPort;
    private final ApplicationContext applicationContext;

    // ::: constructor

    public CotipService(CotipOutPort cotipOutPort, CotipDbOutPort cotipDbOutPort, @Lazy ApplicationContext applicationContext) {
        this.cotipOutPort = cotipOutPort;
        this.cotipDbOutPort = cotipDbOutPort;
        this.applicationContext = applicationContext;
    }

    // ::: service se inyecta asi mismo para obtener las respuestas cacheadas

    private CotipService getSelf() {
        return applicationContext.getBean(CotipService.class);
    }

    // ::: impl

    @Cacheable(value = "continental-bank", key = "'continentalResponse'")
    @Override
    public List<CotipDto> findLatestContinentalBankExchangeRates() throws Exception {
        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(CotipDbMapper.INSTANCE.toListContinentalBankResponse(cotipOutPort.fetchContinentalBankExchangeRates()),
                ProviderType.CONTINENTAL_BANK);

        log.info("Obtenemos la ultima cotizacion guardada");
        return CotipDomainMapper.INSTANCE.toListCotipDto(cotipDbOutPort.findAllByProviderOrderByUploadDate
                (ProviderType.CONTINENTAL_BANK));
    }

    @Cacheable(value = "familiar-bank", key = "'familiarResponse'")
    @Override
    public List<CotipDto> findLatestFamiliarBankExchangeRates() throws Exception {
        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(CotipDbMapper.INSTANCE.toListFamiliarBankResponse(cotipOutPort.fetchFamiliarBankExchangeRates()),
                ProviderType.FAMILIAR_BANK);

        log.info("Obtenemos la ultima cotizacion guardada");
        return CotipDomainMapper.INSTANCE.toListCotipDto(cotipDbOutPort.findAllByProviderOrderByUploadDate
                (ProviderType.FAMILIAR_BANK));
    }

    @Cacheable(value = "gnb-bank", key = "'gnbResponse'")
    @Override
    public List<CotipDto> findLatestGnbBankExchangeRates() throws Exception {
        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(CotipDbMapper.INSTANCE.toListGnbBankResponse(cotipOutPort.fetchGnbBankExchangeRates()),
                ProviderType.GNB_BANK);

        log.info("Obtenemos la ultima cotizacion guardada");
        return CotipDomainMapper.INSTANCE.toListCotipDto(cotipDbOutPort.findAllByProviderOrderByUploadDate
                (ProviderType.GNB_BANK));
    }

    @Cacheable(value = "rio-bank", key = "'rioResponse'")
    @Override
    public List<CotipDto> findLatestRioBankExchangeRates() throws Exception {
        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(CotipDbMapper.INSTANCE.toListRioBankResponse(cotipOutPort.fetchRioBankExchangeRates()),
                ProviderType.RIO_BANK);

        log.info("Obtenemos la ultima cotizacion guardada");
        return CotipDomainMapper.INSTANCE.toListCotipDto(cotipDbOutPort.findAllByProviderOrderByUploadDate
                (ProviderType.RIO_BANK));
    }

    @Cacheable(value = "solar-bank", key = "'solarResponse'")
    @Override
    public List<CotipDto> findLatestSolarBankExchangeRates() throws Exception {
        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(CotipDbMapper.INSTANCE.toListSolarBankResponse(cotipOutPort.fetchSolarBankExchangeRates()),
                ProviderType.SOLAR_BANK);

        log.info("Obtenemos la ultima cotizacion guardada");
        return CotipDomainMapper.INSTANCE.toListCotipDto(cotipDbOutPort.findAllByProviderOrderByUploadDate
                (ProviderType.SOLAR_BANK));
    }

    // ::: externals

    @Scheduled(cron = "0 0 */6 * * *")
    public void cacheCotizacionesDiarias() {
        try {
            log.info("Ejecutando carga de cotizaciones cada 6 horas");
            getSelf().findLatestContinentalBankExchangeRates();
            getSelf().findLatestFamiliarBankExchangeRates();
            getSelf().findLatestGnbBankExchangeRates();
            getSelf().findLatestRioBankExchangeRates();
            getSelf().findLatestSolarBankExchangeRates();
        } catch (Exception e) {
            log.error("Error al cargar las cotizaciones: ", e);
        }
    }


    private void saveAllCotipEntities(List<CotipEntity> cotipEntities, ProviderType tipoProveedor) {
        cotipDbOutPort.saveAllCotipEntity(cotipEntities, tipoProveedor);
    }
}
