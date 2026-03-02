package py.com.cotip.application.dto;

public record BranchOfficeDto(
        String id,
        String name,
        String department,
        String city,
        String neighborhood
) {
}
