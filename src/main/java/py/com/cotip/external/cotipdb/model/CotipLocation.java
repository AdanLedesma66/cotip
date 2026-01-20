package py.com.cotip.external.cotipdb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cotip_entity")
public class CotipLocation {

    // ::: vars

    @Column(name = "direccion")
    private String address;

    @Column(name = "latitud")
    private Double latitude;

    @Column(name = "longitud")
    private Double longitude;

}
