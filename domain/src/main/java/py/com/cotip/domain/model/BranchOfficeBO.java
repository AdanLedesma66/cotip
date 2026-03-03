package py.com.cotip.domain.model;

public record BranchOfficeBO(
        String externalBranchId,
        String name,
        String department,
        String city,
        String neighborhood
) {
}
