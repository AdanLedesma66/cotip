package py.com.cotip.domain.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoProveedor {

    BANCO_CONTINENTAL("BC", "Banco Continental"),
    BANCO_FAMILIAR("BF", "Banco Familiar");

    private final String code;
    private final String description;

}