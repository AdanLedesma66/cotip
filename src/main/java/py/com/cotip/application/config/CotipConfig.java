package py.com.cotip.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import py.com.cotip.domain.port.in.CotipInPort;
import py.com.cotip.domain.port.out.CotipOutPort;
import py.com.cotip.domain.service.CotipService;


@Configuration
public class CotipConfig {

    // ::: beans

    @Bean
    public CotipInPort cotizacionService(CotipOutPort cotizacionOutPort){
        return new CotipService(cotizacionOutPort);
    }

}
