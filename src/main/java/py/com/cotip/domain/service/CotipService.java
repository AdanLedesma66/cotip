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
import py.com.cotip.domain.port.in.request.FindMaxiExchangeRateRequest;
import py.com.cotip.domain.port.out.CotipDbOutPort;
import py.com.cotip.domain.port.out.CotipOutPort;
import py.com.cotip.external.cotipdb.mapper.CotipDbMapper;
import py.com.cotip.external.cotipdb.model.CotipEntity;
import py.com.cotip.external.webservice.util.CurrencyUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

@Slf4j
public class CotipService implements CotipInPort {

    // ::: inyects

    private final CotipOutPort cotipOutPort;
    private final CotipDbOutPort cotipDbOutPort;
    private final ApplicationContext applicationContext;
    private final ExecutorService virtualThreadExecutor;

    // ::: constructor

    public CotipService(CotipOutPort cotipOutPort, CotipDbOutPort cotipDbOutPort, ApplicationContext applicationContext, ExecutorService virtualThreadExecutor) {
        this.cotipOutPort = cotipOutPort;
        this.cotipDbOutPort = cotipDbOutPort;
        this.applicationContext = applicationContext;
        this.virtualThreadExecutor = virtualThreadExecutor;
    }


    // ::: service se inyecta asi mismo para obtener las respuestas cacheadas

    private CotipService getSelf() {
        return applicationContext.getBean(CotipService.class);
    }

    // ::: impl

    @Cacheable(value = "continental-bank", key = "'continentalResponse'")
    @Override
    public List<CotipDto> findLatestContinentalBankExchangeRates() {
        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(CotipDbMapper.INSTANCE.toListContinentalBankResponse(cotipOutPort.fetchContinentalBankExchangeRates()),
                ProviderType.CONTINENTAL_BANK);

        log.info("Obtenemos la ultima cotizacion guardada");
        return CotipDomainMapper.INSTANCE.toListCotipDto(cotipDbOutPort.findAllByProviderOrderByUploadDate
                (ProviderType.CONTINENTAL_BANK));
    }

    @Cacheable(value = "gnb-bank", key = "'gnbResponse'")
    @Override
    public List<CotipDto> findLatestGnbBankExchangeRates() {
        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(CotipDbMapper.INSTANCE.toListGnbBankResponse(cotipOutPort.fetchGnbBankExchangeRates()),
                ProviderType.GNB_BANK);

        log.info("Obtenemos la ultima cotizacion guardada");
        return CotipDomainMapper.INSTANCE.toListCotipDto(cotipDbOutPort.findAllByProviderOrderByUploadDate
                (ProviderType.GNB_BANK));
    }

    @Cacheable(value = "maxi-exchange", key = "'maxiResponse'")
    @Override
    public List<CotipDto> findLatestMaxiExchangeRates(FindMaxiExchangeRateRequest request) {
        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(CotipDbMapper.INSTANCE.toListMaxiCambiosResponse(cotipOutPort.fetchMaxiCambiosExchangeRates()),
                ProviderType.MAXI_CAMBIOS);

        log.info("Obtenemos la ultima cotizacion guardada");
        return CotipDomainMapper.INSTANCE.toListCotipDto(cotipDbOutPort.findAllByProviderOrderByUploadDate
                (ProviderType.MAXI_CAMBIOS));
    }

    // ::: EXTERNALS

    @Scheduled(cron = "0 0 */6 * * *")
    public void cacheCotizacionesDiarias() {
        log.info("Ejecutando carga de cotizaciones cada 6 horas");

        List<Runnable> tasks = List.of(
                cotipOutPort::fetchContinentalBankExchangeRates,
                cotipOutPort::fetchGnbBankExchangeRates,
                cotipOutPort::fetchMaxiCambiosExchangeRates
        );

        tasks.forEach(virtualThreadExecutor::submit);
    }


    private void saveAllCotipEntities(List<CotipEntity> cotipEntities, ProviderType tipoProveedor) {
        cotipDbOutPort.saveAllCotipEntity(cotipEntities, tipoProveedor);
    }
}
