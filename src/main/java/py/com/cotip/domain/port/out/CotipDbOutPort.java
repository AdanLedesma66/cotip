package py.com.cotip.domain.port.out;

import py.com.cotip.domain.commons.TipoProveedor;
import py.com.cotip.external.cotipdb.entities.CotipEntity;

import java.util.List;

public interface CotipDbOutPort {

    CotipEntity saveCotipEntity(CotipEntity cotipEntity);

    List<CotipEntity> saveAllCotipEntity(List<CotipEntity> cotipEntities, TipoProveedor tipoProveedor);
}
