package py.com.cotip.domain.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CotipCity {

    // :::

    ASUNCION("Asunción"),
    CIUDAD_DEL_ESTE("Ciudad del Este");

    // ::: vars

    private final String name;

}
