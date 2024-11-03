package py.com.cotip.domain.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoProveedor {

    BANCO_CONTINENTAL("BC", "Banco Continental"),
    BANCO_FAMILIAR("BF", "Banco Familiar"),
    BANCO_BASA("BB", "Banco Basa"),
    BANCO_RIO("BR", "Banco Rio"),
    BANCO_GNB("BG", "Banco Gnb"),
    BANCO_SOLAR("BS", "Banco Solar");

    private final String code;
    private final String description;

}
