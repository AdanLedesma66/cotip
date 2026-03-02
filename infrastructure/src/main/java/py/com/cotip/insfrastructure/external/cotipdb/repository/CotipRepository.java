package py.com.cotip.insfrastructure.external.cotipdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.insfrastructure.external.cotipdb.model.CotipEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CotipRepository extends JpaRepository<CotipEntity, UUID> {

    Optional<CotipEntity> findTopByExchangeRateAndProviderAndBranchOfficeOrderByUploadDateDesc(String exchangeRate,
                                                                                                 ProviderType tipoProveedor,
                                                                                                 String branchOffice);

    @Query("SELECT c FROM CotipEntity c WHERE c.provider = :tipoProveedor " +
            "AND c.uploadDate = (SELECT MAX(c2.uploadDate) FROM CotipEntity c2 " +
            "WHERE c2.provider = :tipoProveedor " +
            "AND c2.exchangeRate = c.exchangeRate " +
            "AND COALESCE(c2.branchOffice, '') = COALESCE(c.branchOffice, ''))")
    List<CotipEntity> findLatestCotizacionesByProvider(@Param("tipoProveedor") ProviderType tipoProveedor);

    @Query("SELECT c FROM CotipEntity c WHERE c.provider = :tipoProveedor AND c.city = :city AND c.uploadDate = (SELECT MAX(c2.uploadDate) FROM CotipEntity c2 WHERE c2.provider = :tipoProveedor AND c2.exchangeRate = c.exchangeRate AND c2.city = :city)")
    List<CotipEntity> findLatestCotizacionesByProviderAndCity(@Param("tipoProveedor") ProviderType tipoProveedor, @Param("city") String city);

    @Query("SELECT c FROM CotipEntity c JOIN c.branchOfficeRef b WHERE c.provider = :tipoProveedor " +
            "AND b.externalBranchId = :branchOfficeExternalId " +
            "AND c.uploadDate = (SELECT MAX(c2.uploadDate) FROM CotipEntity c2 JOIN c2.branchOfficeRef b2 " +
            "WHERE c2.provider = :tipoProveedor " +
            "AND c2.exchangeRate = c.exchangeRate " +
            "AND b2.externalBranchId = :branchOfficeExternalId)")
    List<CotipEntity> findLatestCotizacionesByProviderAndBranchOfficeExternalId(
            @Param("tipoProveedor") ProviderType tipoProveedor,
            @Param("branchOfficeExternalId") String branchOfficeExternalId
    );

}
