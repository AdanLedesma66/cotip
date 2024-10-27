package py.com.cotip.domain.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import py.com.cotip.domain.mapper.ContinentalDomainMapper;
import py.com.cotip.domain.port.in.CotipInPort;
import py.com.cotip.domain.port.out.CotipOutPort;
import py.com.cotip.domain.port.out.response.ContinentalResponse;
import py.com.cotip.external.model.ContinentalExternal;

import java.util.List;
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class CotipService implements CotipInPort {

    // ::: inyects

    private CotipOutPort cotipOutPort;

    //todo sacar imagenes
    @Value("${cotizacion.urlImage.dolar}")
    private String dolarImage;

    @Value("${cotizacion.urlImage.euro}")
    private String euroImage;

    @Value("${cotizacion.urlImage.franco-suizo}")
    private String francoSuizoImage;

    @Value("${cotizacion.urlImage.libra-esterlina}")
    private String libraEsterlinaImage;

    @Value("${cotizacion.urlImage.peso-argentino}")
    private String pesoArgentinoImage;

    @Value("${cotizacion.urlImage.reales}")
    private String realesImage;

    @Value("${cotizacion.urlImage.yen-japones}")
    private String yenJaponesImage;

    public CotipService(CotipOutPort cotipOutPort) {
        this.cotipOutPort = cotipOutPort;
    }


    // ::: impl

    @Cacheable(value = "cotizaciones", key = "'cotizacionResponse'") //todo ajustar cache
    public List<ContinentalResponse> findCotizacionContinentalResponse() {
        List<ContinentalExternal> cotizacionExternals = cotipOutPort.findContinentalCotizacion();

        var cotizacionList = ContinentalDomainMapper.INSTANCE.externalToListResponse(cotizacionExternals);

        cotizacionList.forEach(cotizacion -> {
            switch (cotizacion.getExchangeRate()) {
                case "DOLAR CHQ./TRANSF.":
                case "DOLAR EFECTIVO":
                    cotizacion.setUrlImage(dolarImage);
                    break;
                case "EURO CHQ./TRANSF.":
                case "EURO EFECTIVO":
                    cotizacion.setUrlImage(euroImage);
                    break;
                case "FRANCO SUIZO":
                    cotizacion.setUrlImage(francoSuizoImage);
                    break;
                case "LIBRA ESTERLINA":
                    cotizacion.setUrlImage(libraEsterlinaImage);
                    break;
                case "PESO ARGENTINO":
                    cotizacion.setUrlImage(pesoArgentinoImage);
                    break;
                case "REALES":
                    cotizacion.setUrlImage(realesImage);
                    break;
                case "YEN JAPONES":
                    cotizacion.setUrlImage(yenJaponesImage);
                    break;
                default:
                    cotizacion.setUrlImage(null);
                    break;
            }
        });

        return cotizacionList;
    }
}
