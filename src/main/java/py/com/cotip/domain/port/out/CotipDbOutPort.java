package py.com.cotip.domain.port.out;

import py.com.cotip.domain.commons.TipoProveedor;
import py.com.cotip.external.cotipdb.model.CotipEntity;

import java.util.List;

public interface CotipDbOutPort {

    List<CotipEntity> saveAllCotipEntity(List<CotipEntity> cotipEntities, TipoProveedor tipoProveedor);

    List<CotipEntity> findAllByProviderOrderByUploadDate(String tipoProvedor);

}
