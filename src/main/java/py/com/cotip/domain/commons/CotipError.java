package py.com.cotip.domain.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CotipError {

    // ::: errors

    CONTINENTAL_BANK_ERROR("CE0001", "Error al obtener las cotizaciones del banco continental", ""),
    FAMILIAR_BANK_ERROR("CE0002", "Error al obtener las cotizaciones de Banco Familiar", ""),
    GNB_BANK_ERROR("CE0003", "Error al obtener las cotizaciones de Banco Gnb", ""),
    RIO_BANK_ERROR("CE0004", "Error al obtener las cotizaciones de Banco Rio", ""),
    SOLAR_BANK_ERROR("CE0005", "Error al obtener las cotizaciones de Banco Solar", ""),
    BNF_BANK_ERROR("CE0005", "Error al obtener las cotizaciones de Banco Nacional de Fomento", ""),
    ATLAS_BANK_ERROR("CE0005", "Error al obtener las cotizaciones de Banco Atlas", ""),
    FIC_FINANCIAL_ERROR("CE0005", "Error al obtener las cotizaciones de Financiera Fic", ""),;

    // ::: vars

    private final String code;
    private final String description;
    private final String userMessage;

}
