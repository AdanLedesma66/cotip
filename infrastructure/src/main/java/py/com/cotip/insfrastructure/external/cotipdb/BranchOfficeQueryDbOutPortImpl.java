package py.com.cotip.insfrastructure.external.cotipdb;

import lombok.AllArgsConstructor;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.domain.model.BranchOfficeBO;
import py.com.cotip.domain.port.out.BranchOfficeQueryPort;
import py.com.cotip.insfrastructure.external.cotipdb.model.BranchOfficeEntity;
import py.com.cotip.insfrastructure.external.cotipdb.repository.BranchOfficeRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class BranchOfficeQueryDbOutPortImpl implements BranchOfficeQueryPort {

    private final BranchOfficeRepository branchOfficeRepository;

    @Override
    public Optional<BranchOfficeBO> findCambiosChacoByExternalBranchId(String externalBranchId) {
        return branchOfficeRepository.findByProviderAndExternalBranchIdAndEnabledTrue(
                        ProviderType.CAMBIOS_CHACO,
                        externalBranchId
                )
                .map(this::toBo);
    }

    @Override
    public Optional<BranchOfficeBO> findCambiosChacoByName(String branchOfficeName) {
        return branchOfficeRepository.findByProviderAndNameIgnoreCaseAndEnabledTrue(
                        ProviderType.CAMBIOS_CHACO,
                        branchOfficeName
                )
                .map(this::toBo);
    }

    @Override
    public List<BranchOfficeBO> findAllCambiosChacoBranches() {
        return branchOfficeRepository.findByProviderAndEnabledTrueOrderByNameAsc(ProviderType.CAMBIOS_CHACO)
                .stream()
                .map(this::toBo)
                .toList();
    }

    private BranchOfficeBO toBo(BranchOfficeEntity entity) {
        return new BranchOfficeBO(
                entity.getExternalBranchId(),
                entity.getName(),
                entity.getDepartment(),
                entity.getCity(),
                entity.getNeighborhood()
        );
    }
}
