package py.com.cotip.external.webservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import py.com.cotip.domain.port.out.CotipOutPort;
import py.com.cotip.external.webservice.CotipOutPortImpl;

@Configuration
public class CotipExternalConfig {

    // ::: beans

    @Bean
    public CotipOutPort cotizacionOutPort(CotipProperties cotipProperties){
        return new CotipOutPortImpl(cotipProperties);
    }
}
