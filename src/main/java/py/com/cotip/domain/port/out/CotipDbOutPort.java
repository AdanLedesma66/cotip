package py.com.cotip.domain.port.out;

import py.com.cotip.external.cotipdb.entities.CotipEntity;

public interface CotipDbOutPort {

    CotipEntity saveCotipEntity(CotipEntity cotipEntity);
}
