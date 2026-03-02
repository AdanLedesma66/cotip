package py.com.cotip.insfrastructure.external.cotipdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import py.com.cotip.insfrastructure.external.cotipdb.model.ScrapeAuditEntity;

import java.util.UUID;

@Repository
public interface ScrapeAuditRepository extends JpaRepository<ScrapeAuditEntity, UUID> {
}
