package py.com.cotip.external.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import py.com.cotip.domain.port.out.CotipOutPort;
import py.com.cotip.external.CotipOutPortImpl;

@Configuration
public class CotipExternalConfig {

    // ::: beans

    @Bean
    public CotipOutPort cotizacionOutPort(){
        return new CotipOutPortImpl();
    }
}
