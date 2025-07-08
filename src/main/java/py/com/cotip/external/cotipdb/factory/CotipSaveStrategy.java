package py.com.cotip.external.cotipdb.factory;

import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.external.cotipdb.config.properties.CotipDetailsProperties;
import py.com.cotip.external.cotipdb.model.CotipEntity;
import py.com.cotip.external.webservice.config.CotipProperties;

import java.util.List;

public interface CotipSaveStrategy {
    List<CotipEntity> saveCotipEntities(List<CotipEntity> cotipEntities, ProviderType providerType);
}
