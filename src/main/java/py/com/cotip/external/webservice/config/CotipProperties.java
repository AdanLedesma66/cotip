package py.com.cotip.external.webservice.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class CotipProperties {

    // ::: vars

    @Value("${cotizacion.continental.bearer.path}")
    private String continentalBearerTokenPath;

    @Value("${contizacion.continental.path}")
    private String continentalPath;

    @Value("${cotizacion.familiar.path}")
    private String familiarPath;

    @Value("${cotizacion.gnb.path}")
    private String gnbPath;

}
