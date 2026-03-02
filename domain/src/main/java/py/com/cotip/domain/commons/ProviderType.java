package py.com.cotip.domain.commons;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProviderType {

    CONTINENTAL_BANK("Continental Bank", "Banco Continental", "genericProviderStrategy"),
    FAMILIAR_BANK("Familiar Bank", "Banco Familiar", "genericProviderStrategy"), // Asegúrate de que este nombre sea correcto
    RIO_BANK("Rio Bank", "Banco Rio", "rioProviderStrategy"),
    GNB_BANK("Gnb Bank", "Banco Gnb", "gnbProviderStrategy"),
    SOLAR_BANK("Solar Bank", "Banco Solar", "solarProviderStrategy"),
    BNF_BANK("National Development Bank", "Banco Nacional de Fomento", "bnfProviderStrategy"),
    ATLAS_BANK("Atlas Bank", "Banco Atlas", "atlasProviderStrategy"),
    FIC_FINANCIAL("Fic Financial", "Financiera Fic", "ficProviderStrategy"),
    MAXI_CAMBIOS("Maxi Exchange", "Maxi Cambios", "maxiProviderStrategy"),
    CAMBIOS_CHACO("Cambios Chaco", "Cambios Chaco", "genericProviderStrategy");

    private final String codeEn;
    private final String description;
    private final String strategyBeanName;


    @JsonValue
    public String getCodeEn() {
        return codeEn;
    }
}
