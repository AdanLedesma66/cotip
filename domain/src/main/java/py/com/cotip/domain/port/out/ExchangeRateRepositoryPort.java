package py.com.cotip.domain.port.out;

import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.model.ExchangeRateBO;


import java.util.List;

public interface ExchangeRateRepositoryPort {

    List<ExchangeRateBO> saveAllExchangeRates(List<ExchangeRateBO> exchangeRates, ProviderType providerType);

    List<ExchangeRateBO> findAllByProviderOrderByUploadDate(ProviderType providerType);

    List<ExchangeRateBO> findAllByProviderAndBranchOfficeExternalIdOrderByUploadDate(ProviderType providerType,
                                                                                      String branchOfficeExternalId);

}
