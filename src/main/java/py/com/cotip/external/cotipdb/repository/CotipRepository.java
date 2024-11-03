package py.com.cotip.external.cotipdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.external.cotipdb.model.CotipEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CotipRepository extends JpaRepository<CotipEntity, UUID> {

    Optional<CotipEntity> findTopByExchangeRateAndProviderOrderByUploadDateDesc(String currencyCode, ProviderType tipoProveedor);
    List<CotipEntity> findAllByProviderOrderByUploadDate(ProviderType tipoProveedor);

}
