package py.com.cotip.insfrastructure.external.cotipdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.insfrastructure.external.cotipdb.model.BranchOfficeEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BranchOfficeRepository extends JpaRepository<BranchOfficeEntity, UUID> {

    Optional<BranchOfficeEntity> findByProviderAndExternalBranchId(ProviderType provider, String externalBranchId);

    Optional<BranchOfficeEntity> findByProviderAndName(ProviderType provider, String name);
}
