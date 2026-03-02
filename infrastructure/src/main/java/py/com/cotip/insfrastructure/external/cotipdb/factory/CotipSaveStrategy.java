package py.com.cotip.insfrastructure.external.cotipdb.factory;

import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.insfrastructure.external.cotipdb.model.CotipEntity;

import java.util.List;

public interface CotipSaveStrategy {
    List<CotipEntity> saveCotipEntities(List<CotipEntity> cotipEntities, ProviderType providerType);
}
