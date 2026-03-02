package py.com.cotip.insfrastructure.external.cotipdb;

import org.springframework.stereotype.Component;
import py.com.cotip.domain.commons.ProviderType;
import py.com.cotip.insfrastructure.external.cotipdb.model.BranchOfficeEntity;
import py.com.cotip.insfrastructure.external.cotipdb.repository.BranchOfficeRepository;
import py.com.cotip.insfrastructure.external.webservice.config.ChacoBranchCatalog;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Component
public class BranchOfficeResolver {

    private final BranchOfficeRepository branchOfficeRepository;

    public BranchOfficeResolver(BranchOfficeRepository branchOfficeRepository) {
        this.branchOfficeRepository = branchOfficeRepository;
    }

    public BranchOfficeEntity resolve(ProviderType providerType,
                                      String branchOfficeName,
                                      String externalBranchId,
                                      String cityHint) {
        String normalizedName = normalize(branchOfficeName);
        String normalizedExternalId = normalize(externalBranchId);

        if (normalizedName == null && normalizedExternalId == null) {
            return null;
        }

        BranchOfficeEntity branchOffice = findExisting(providerType, normalizedName, normalizedExternalId)
                .orElseGet(() -> BranchOfficeEntity.builder()
                        .id(UUID.randomUUID())
                        .provider(providerType)
                        .build());

        branchOffice.setExternalBranchId(normalizedExternalId != null
                ? normalizedExternalId
                : branchOffice.getExternalBranchId());

        if (branchOffice.getName() == null || branchOffice.getName().isBlank()) {
            branchOffice.setName(normalizedName != null ? normalizedName : "Sucursal " + normalizedExternalId);
        }

        enrichBranchMetadata(branchOffice, cityHint);
        return branchOfficeRepository.save(branchOffice);
    }

    private Optional<BranchOfficeEntity> findExisting(ProviderType providerType,
                                                      String branchOfficeName,
                                                      String externalBranchId) {
        if (externalBranchId != null) {
            Optional<BranchOfficeEntity> byExternal = branchOfficeRepository
                    .findByProviderAndExternalBranchId(providerType, externalBranchId);
            if (byExternal.isPresent()) {
                return byExternal;
            }
        }

        if (branchOfficeName != null) {
            return branchOfficeRepository.findByProviderAndName(providerType, branchOfficeName);
        }

        return Optional.empty();
    }

    private void enrichBranchMetadata(BranchOfficeEntity branchOffice, String cityHint) {
        if (branchOffice.getProvider() == ProviderType.CAMBIOS_CHACO) {
            ChacoBranchCatalog.BranchMetadata branchMetadata = ChacoBranchCatalog
                    .findById(normalize(branchOffice.getExternalBranchId()));
            if (branchMetadata != null) {
                branchOffice.setName(branchMetadata.name());
                branchOffice.setDepartment(branchMetadata.department());
                branchOffice.setCity(branchMetadata.city());
                branchOffice.setNeighborhood(branchMetadata.neighborhood());
            } else if (branchOffice.getCity() == null && cityHint != null && !cityHint.isBlank()) {
                branchOffice.setCity(cityHint);
            }
            return;
        }

        if (branchOffice.getCity() == null && cityHint != null && !cityHint.isBlank()) {
            branchOffice.setCity(cityHint);
        }

        if (branchOffice.getDepartment() == null && branchOffice.getCity() != null) {
            String city = branchOffice.getCity().trim().toLowerCase(Locale.ROOT);
            if ("asuncion".equals(city) || "asunción".equals(city)) {
                branchOffice.setDepartment("Capital");
            } else if (city.contains("ciudad del este")) {
                branchOffice.setDepartment("Alto Parana");
            }
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
