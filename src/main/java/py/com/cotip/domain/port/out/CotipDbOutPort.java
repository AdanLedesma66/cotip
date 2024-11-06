package py.com.cotip.domain.port.out;

import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.external.cotipdb.model.CotipEntity;

import java.util.List;

public interface CotipDbOutPort {

    List<CotipEntity> saveAllCotipEntity(List<CotipEntity> cotipEntities, ProviderType tipoProveedor);

    List<CotipEntity> findAllByProviderOrderByUploadDate(ProviderType tipoProvedor);

    List<CotipEntity> findAllByProviderAndCityOrderByUploadDate(ProviderType tipoProvedor, String city);

}
