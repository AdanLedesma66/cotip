package py.com.cotip.external.cotipdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import py.com.cotip.domain.port.out.CotipDbOutPort;
import py.com.cotip.external.cotipdb.CotipDbOutPortImpl;

@Configuration
public class CotipConfigDb {

    @Bean
    public CotipDbOutPort cotipDbOutPort(){
        return new CotipDbOutPortImpl();
    }


}
