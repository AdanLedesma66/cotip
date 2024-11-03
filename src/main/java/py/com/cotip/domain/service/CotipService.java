package py.com.cotip.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import py.com.cotip.application.rest.model.CotipDto;
import py.com.cotip.domain.commons.TipoProveedor;
import py.com.cotip.domain.mapper.*;
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

    @Cacheable(value = "continental", key = "'continentalResponse'")
    @Override
    public List<CotipDto> findCotizacionContinentalResponse() throws Exception {
        var continentalResponseList = ContinentalDomainMapper.INSTANCE.externalToListResponse(cotipOutPort.findContinentalCotizacion());

        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(CotipDbMapper.INSTANCE.toListContinentalResponse(continentalResponseList), TipoProveedor.BANCO_CONTINENTAL);

        log.info("Obtenemos la ultima cotizacion guardada");
        return CotipDomainMapper.INSTANCE.toListCotipDto(
                cotipDbOutPort.findAllByProviderOrderByUploadDate(TipoProveedor.BANCO_CONTINENTAL.getDescription()));
    }

    @Cacheable(value = "familiar", key = "'familiarResponse'")
    @Override
    public List<CotipDto> findFamiliarCotizacionResponse() throws Exception {
        var familiarDtoList = FamiliarDomainMapper.INSTANCE.toListFamiliarDto(cotipOutPort.findFamiliarCotizacion());

        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(CotipDbMapper.INSTANCE.toListFamiliarDto(familiarDtoList), TipoProveedor.BANCO_FAMILIAR);

        log.info("Obtenemos la ultima cotizacion guardada");
        return CotipDomainMapper.INSTANCE.toListCotipDto(
                cotipDbOutPort.findAllByProviderOrderByUploadDate(TipoProveedor.BANCO_FAMILIAR.getDescription()));
    }

    @Cacheable(value = "gnb", key = "'gnbResponse'")
    @Override
    public List<CotipDto> findGnbCotizacionResponse() throws Exception {
        var listGnbDto = GnbDomainMapper.INSTANCE.toListGnbDto(cotipOutPort.findGnbCotizacion());

        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(CotipDbMapper.INSTANCE.toListGnbDto(listGnbDto), TipoProveedor.BANCO_GNB);

        log.info("Obtenemos la ultima cotizacion guardada");
        return CotipDomainMapper.INSTANCE.toListCotipDto(
                cotipDbOutPort.findAllByProviderOrderByUploadDate(TipoProveedor.BANCO_GNB.getDescription()));
    }

    @Cacheable(value = "rio", key = "'rioResponse'")
    @Override
    public List<CotipDto> findRioCotizacionResponse() throws Exception {
        var rioDtoList = RioDomainMapper.INSTANCE.toListRioDto(cotipOutPort.findRioCotizacion());

        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(CotipDbMapper.INSTANCE.toListRioDto(rioDtoList), TipoProveedor.BANCO_RIO);

        log.info("Obtenemos la ultima cotizacion guardada");
        return CotipDomainMapper.INSTANCE.toListCotipDto(
                cotipDbOutPort.findAllByProviderOrderByUploadDate(TipoProveedor.BANCO_RIO.getDescription()));
    }

    @Cacheable(value = "solar", key = "'solarResponse'")
    @Override
    public List<CotipDto> findSolarBankCotip() throws Exception {
        var cotipDtos = SolarBankDomainMapper.INSTANCE.toListDto(cotipOutPort.findSolarBankCotip());

        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(CotipDbMapper.INSTANCE.toListCotipDto(cotipDtos), TipoProveedor.BANCO_SOLAR);

        log.info("Obtenemos la ultima cotizacion guardada");
        return CotipDomainMapper.INSTANCE.toListCotipDto(
                cotipDbOutPort.findAllByProviderOrderByUploadDate(TipoProveedor.BANCO_SOLAR.getDescription()));
    }

    // ::: externals

    @Scheduled(cron = "0 0 */6 * * *")
    public void cacheCotizacionesDiarias() {
        try {
            log.info("Ejecutando carga de cotizaciones cada 6 horas");
            getSelf().findCotizacionContinentalResponse();
            getSelf().findFamiliarCotizacionResponse();
            getSelf().findGnbCotizacionResponse();
            getSelf().findRioCotizacionResponse();
            getSelf().findSolarBankCotip();
        } catch (Exception e) {
            log.error("Error al cargar las cotizaciones: ", e);
        }
    }


    private void saveAllCotipEntities(List<CotipEntity> cotipEntities, TipoProveedor tipoProveedor) {
        cotipDbOutPort.saveAllCotipEntity(cotipEntities, tipoProveedor);
    }
}
