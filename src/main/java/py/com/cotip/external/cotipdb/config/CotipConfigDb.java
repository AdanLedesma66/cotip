package py.com.cotip.external.cotipdb.config;

import org.springframework.context.annotation.Configuration;
import py.com.cotip.domain.port.out.CotipDbOutPort;
import py.com.cotip.external.cotipdb.CotipDbOutPortImpl;

@Configuration
public class CotipConfigDb {

    public CotipDbOutPort cotipConfigDb(){
        return new CotipDbOutPortImpl();
    }


}
