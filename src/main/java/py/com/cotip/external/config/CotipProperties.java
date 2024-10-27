package py.com.cotip.external.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "cotizacion")
public class CotipProperties {

    // ::: vars

    @Value("${continental.bearer.path}")
    private String continentalBearerTokenPath;

    @Value("${contizacion.continental.path}")
    private String continentalPath;

    @Value("${cotizacion.familiar.path}")
    private String familiarPath;

}
