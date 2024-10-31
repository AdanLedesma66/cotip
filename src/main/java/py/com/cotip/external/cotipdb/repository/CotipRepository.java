package py.com.cotip.external.cotipdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import py.com.cotip.external.cotipdb.model.CotipEntity;

import java.util.UUID;

@Repository
public interface CotipRepository extends JpaRepository<CotipEntity, UUID> {
}
