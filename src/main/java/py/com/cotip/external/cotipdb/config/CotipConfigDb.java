package py.com.cotip.external.cotipdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import py.com.cotip.domain.port.out.CotipDbOutPort;
import py.com.cotip.external.cotipdb.CotipDbOutPortImpl;
import py.com.cotip.external.cotipdb.config.properties.CotipDetailsProperties;
import py.com.cotip.external.cotipdb.factory.strategy.CotipSaveStrategyFactory;
import py.com.cotip.external.cotipdb.repository.CotipRepository;
import py.com.cotip.external.webservice.config.CotipProperties;

@Configuration
public class CotipConfigDb {

    @Bean
    public CotipDbOutPort cotipDbOutPort(CotipRepository cotipRepository){
        return new CotipDbOutPortImpl(cotipRepository);
    }


}
