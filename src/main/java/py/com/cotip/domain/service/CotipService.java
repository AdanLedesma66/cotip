package py.com.cotip.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import py.com.cotip.application.rest.model.FamiliarDto;
import py.com.cotip.domain.commons.TipoProveedor;
import py.com.cotip.domain.mapper.ContinentalDomainMapper;
import py.com.cotip.domain.mapper.FamiliarDomainMapper;
import py.com.cotip.domain.port.in.CotipInPort;
import py.com.cotip.domain.port.out.CotipDbOutPort;
import py.com.cotip.domain.port.out.CotipOutPort;
import py.com.cotip.domain.port.out.response.ContinentalResponse;
import py.com.cotip.external.cotipdb.entities.CotipEntity;
import py.com.cotip.external.cotipdb.mapper.CotipDbMapper;
import py.com.cotip.external.webservice.model.ContinentalExternal;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

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
        List<ContinentalExternal> cotizacionExternals = cotipOutPort.findContinentalCotizacion();

        var continentalResponseList = ContinentalDomainMapper.INSTANCE.externalToListResponse(cotizacionExternals);
        log.info("Se ajustan algunos datos de la cotizacion obtenida");
        continentalResponseList.forEach(cotizacion -> {
            switch (cotizacion.getExchangeRate()) {
                case "DOLAR CHQ./TRANSF.":
                    cotizacion.setCurrencyCode("USD");
                    break;
                case "EURO CHQ./TRANSF.":
                    cotizacion.setCurrencyCode("EUR");
                    break;
            }
            cotizacion.setCurrencyCode(cotizacion.getCurrencyCode().trim());
        log.info("Guardamos las cotizaciones");
            saveCotipEntity(CotipDbMapper.INSTANCE.toContinentalResponse(cotizacion), TipoProveedor.BANCO_CONTINENTAL.getDescription());
        });

        return continentalResponseList;
    }

    @Cacheable(value = "familiar", key = "'familiarResponse'")
    @Override
    public List<FamiliarDto> findFamiliarCotizacionResponse() throws Exception {
        var familiarDtoList = FamiliarDomainMapper.INSTANCE.toListFamiliarDto(cotipOutPort.findFamiliarCotizacion());

        log.info("Se ajustan algunos datos de la cotizacion obtenida");
        familiarDtoList.forEach(cotizacion -> {
            switch (cotizacion.getExchangeRate()) {
                case "Dólar Efectivo", "Dólar Cheque / Transferencia":
                    cotizacion.setCurrencyCode("USD");
                    break;
                case "Peso Argentino":
                    cotizacion.setCurrencyCode("ARS");
                    break;
                case "Real Brasileño":
                    cotizacion.setCurrencyCode("BRL");
                    break;
                case "Euro Efectivo", "Euro Transferencia":
                    cotizacion.setCurrencyCode("EUR");
                    break;
            }
            log.info("Guardamos las cotizaciones");
            saveCotipEntity(CotipDbMapper.INSTANCE.toFamiliarDto(cotizacion), TipoProveedor.BANCO_FAMILIAR.getDescription());
        });

        return familiarDtoList;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cacheCotizacionesDiarias() {
        try {
            log.info("Ejecutando carga diaria de cotizaciones");
            getSelf().findCotizacionContinentalResponse();
            getSelf().findFamiliarCotizacionResponse();
        } catch (Exception e) {
            log.error("Error al cargar las cotizaciones diarias: ", e);
        }
    }

    private void saveCotipEntity(CotipEntity cotizacion, String provider) {

        cotizacion.setId(UUID.randomUUID());
        cotizacion.setProvider(provider);
        cotizacion.setUploadDate(OffsetDateTime.now());

        cotipDbOutPort.saveCotipEntity(cotizacion);
    }
}
