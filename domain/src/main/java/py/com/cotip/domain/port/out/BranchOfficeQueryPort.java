package py.com.cotip.domain.port.out;

import py.com.cotip.domain.model.BranchOfficeBO;

import java.util.List;
import java.util.Optional;

public interface BranchOfficeQueryPort {

    Optional<BranchOfficeBO> findCambiosChacoByExternalBranchId(String externalBranchId);

    Optional<BranchOfficeBO> findCambiosChacoByName(String branchOfficeName);

    List<BranchOfficeBO> findAllCambiosChacoBranches();
}
