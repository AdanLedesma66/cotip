package py.com.cotip.insfrastructure.external.cotipdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.insfrastructure.external.cotipdb.model.CotipEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CotipRepository extends JpaRepository<CotipEntity, UUID> {

    Optional<CotipEntity> findTopByExchangeRateAndProviderAndBranchOfficeOrderByUpdatedAtDesc(String exchangeRate,
                                                                                                ProviderType tipoProveedor,
                                                                                                String branchOffice);

    Optional<CotipEntity> findTopByProviderOrderByUpdatedAtDesc(ProviderType providerType);

    List<CotipEntity> findAllByProviderOrderByUpdatedAtDesc(ProviderType providerType);

    List<CotipEntity> findAllByProviderAndBranchOfficeRef_ExternalBranchIdOrderByUpdatedAtDesc(ProviderType providerType,
                                                                                                 String branchOfficeExternalId);

}
