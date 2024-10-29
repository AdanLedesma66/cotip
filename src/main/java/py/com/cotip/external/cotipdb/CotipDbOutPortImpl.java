package py.com.cotip.external.cotipdb;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import py.com.cotip.domain.port.out.CotipDbOutPort;
import py.com.cotip.external.cotipdb.entities.CotipEntity;

@Slf4j
@AllArgsConstructor
public class CotipDbOutPortImpl implements CotipDbOutPort {

    @Override
    public CotipEntity saveCotipEntity(CotipEntity cotipEntity) {
        return null;
    }
}
