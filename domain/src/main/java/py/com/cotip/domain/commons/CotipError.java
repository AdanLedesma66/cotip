package py.com.cotip.domain.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CotipError {

    // ::: errors

    CONTINENTAL_BANK_ERROR("CE0001", "Error retrieving exchange rates from Continental Bank"),
    FAMILIAR_BANK_ERROR("CE0002", "Error retrieving exchange rates from Familiar Bank"),
    GNB_BANK_ERROR("CE0003", "Error retrieving exchange rates from Gnb Bank"),
    RIO_BANK_ERROR("CE0004", "Error retrieving exchange rates from Rio Bank"),
    SOLAR_BANK_ERROR("CE0005", "Error retrieving exchange rates from Solar Bank"),
    BNF_BANK_ERROR("CE0006", "Error retrieving exchange rates from National Development Bank"),
    ATLAS_BANK_ERROR("CE0007", "Error retrieving exchange rates from Atlas Bank"),
    FIC_FINANCIAL_ERROR("CE0008", "Error retrieving exchange rates from Fic Financial"),
    MAXI_CAMBIOS_ERROR("CE0009", "Error retrieving exchange rates from Maxicambios"),
    CAMBIOS_CHACO_ERROR("CE0010", "Error retrieving exchange rates from Cambios Chaco"),
    CAMBIOS_CHACO_BRANCH_INVALID("CE0011", "Invalid Cambios Chaco branch office identifier"),
    API_KEY_REQUIRED("CE0012", "Missing API key header"),
    API_KEY_INVALID("CE0013", "Invalid API key"),
    API_KEY_RATE_LIMIT_EXCEEDED("CE0014", "API key rate limit exceeded"),
    REQUEST_VALIDATION_ERROR("CE0015", "Request validation error");

    // ::: vars

    private final String code;
    private final String description;

}
