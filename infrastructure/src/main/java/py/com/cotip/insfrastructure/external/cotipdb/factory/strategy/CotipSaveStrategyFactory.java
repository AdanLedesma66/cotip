package py.com.cotip.insfrastructure.external.cotipdb.factory.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.insfrastructure.external.cotipdb.factory.CotipSaveStrategy;

import java.util.Map;

@Component
public class CotipSaveStrategyFactory {

    private final Map<String, CotipSaveStrategy> strategyMap;
    private final CotipSaveStrategy defaultStrategy;

    @Autowired
    public CotipSaveStrategyFactory(Map<String, CotipSaveStrategy> strategyMap, CotipSaveStrategy genericProviderStrategy) {
        this.strategyMap = strategyMap;
        this.defaultStrategy = genericProviderStrategy;
    }

    public CotipSaveStrategy getStrategy(ProviderType providerType) {
        return strategyMap.getOrDefault(providerType.getStrategyBeanName(), defaultStrategy);
    }
}
