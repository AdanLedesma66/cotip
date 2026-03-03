package py.com.cotip.insfrastructure.external.cotipdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import py.com.cotip.insfrastructure.external.cotipdb.model.ApiClientEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApiClientRepository extends JpaRepository<ApiClientEntity, UUID> {

    Optional<ApiClientEntity> findByApiKeyHashAndEnabledTrue(String apiKeyHash);
}
