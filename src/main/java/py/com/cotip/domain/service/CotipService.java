package py.com.cotip.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import py.com.cotip.application.rest.model.BasaDto;
import py.com.cotip.application.rest.model.FamiliarDto;
import py.com.cotip.application.rest.model.GnbDto;
import py.com.cotip.application.rest.model.RioDto;
import py.com.cotip.domain.commons.TipoProveedor;
import py.com.cotip.domain.mapper.BasaDomainMapper;
import py.com.cotip.domain.mapper.ContinentalDomainMapper;
import py.com.cotip.domain.mapper.FamiliarDomainMapper;
import py.com.cotip.domain.mapper.RioDomainMapper;
import py.com.cotip.domain.port.in.CotipInPort;
import py.com.cotip.domain.port.out.CotipDbOutPort;
import py.com.cotip.domain.port.out.CotipOutPort;
import py.com.cotip.domain.port.out.response.ContinentalResponse;
import py.com.cotip.external.cotipdb.entities.CotipEntity;
import py.com.cotip.external.cotipdb.mapper.CotipDbMapper;

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
    public List<ContinentalResponse> findCotizacionContinentalResponse() throws Exception {
        var continentalResponseList = ContinentalDomainMapper.INSTANCE.externalToListResponse(cotipOutPort.findContinentalCotizacion());

        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(CotipDbMapper.INSTANCE.toListContinentalResponse(continentalResponseList), TipoProveedor.BANCO_CONTINENTAL);

        return continentalResponseList;
    }

    @Cacheable(value = "familiar", key = "'familiarResponse'")
    @Override
    public List<FamiliarDto> findFamiliarCotizacionResponse() throws Exception {
        var familiarDtoList = FamiliarDomainMapper.INSTANCE.toListFamiliarDto(cotipOutPort.findFamiliarCotizacion());

        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(CotipDbMapper.INSTANCE.toListFamiliarDto(familiarDtoList), TipoProveedor.BANCO_FAMILIAR);

        return familiarDtoList;
    }

    @Override
    public List<GnbDto> findGnbCotizacionResponse() throws Exception {
        return null;
    }

    @Cacheable(value = "basa", key = "'basaResponse'")
    @Override
    public List<BasaDto> findBasaCotizacionResponse() throws Exception {
        var basaDtoList = BasaDomainMapper.INSTANCE.toListBasaDto(cotipOutPort.findBasaCotizacion());

        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(CotipDbMapper.INSTANCE.toListBasaDto(basaDtoList), TipoProveedor.BANCO_BASA);

        return basaDtoList;
    }

    @Cacheable(value = "rio", key = "'rioResponse'")
    @Override
    public List<RioDto> findRioCotizacionResponse() throws Exception {
        var rioDtoList = RioDomainMapper.INSTANCE.toListRioDto(cotipOutPort.findRioCotizacion());

        log.info("Guardamos las cotizaciones");
        saveAllCotipEntities(CotipDbMapper.INSTANCE.toListRioDto(rioDtoList), TipoProveedor.BANCO_RIO);

        return rioDtoList;
    }

    // ::: externals

    @Scheduled(cron = "0 0 0 * * *")
    public void cacheCotizacionesDiarias() {
        try {
            log.info("Ejecutando carga diaria de cotizaciones");
            getSelf().findCotizacionContinentalResponse();
            getSelf().findFamiliarCotizacionResponse();
            getSelf().findGnbCotizacionResponse();
            getSelf().findBasaCotizacionResponse();
            getSelf().findRioCotizacionResponse();
        } catch (Exception e) {
            log.error("Error al cargar las cotizaciones diarias: ", e);
        }
    }

    private void saveAllCotipEntities(List<CotipEntity> cotipEntities, TipoProveedor tipoProveedor) {
        cotipDbOutPort.saveAllCotipEntity(cotipEntities, tipoProveedor);
    }
}
