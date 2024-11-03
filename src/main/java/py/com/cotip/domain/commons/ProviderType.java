package py.com.cotip.domain.commons;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProviderType {

    CONTINENTAL_BANK("Continental Bank", "Banco Continental"),
    FAMILIAR_BANK("Familiar Bank", "Banco Familiar"),
    RIO_BANK("Rio Bank", "Banco Rio"),
    GNB_BANK("Gnb Bank", "Banco Gnb"),
    SOLAR_BANK("Solar Bank", "Banco Solar");

    private final String codeEn;
    private final String description;

    @JsonValue
    public String getCodeEn() {
        return codeEn;
    }
}
