package py.com.cotip.insfrastructure.external.cotipdb.model;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CotipDetails {

    // ::: vars

    @Column(name = "numero_celular")
    private String phoneNumber;

    @Column(name = "correo")
    private String correo;

    @Column(name = "ubicacion")
    private CotipLocation location;

    @Column(name = "sitio_web")
    private String website;

    @Column(name = "link_whatsapp")
    private String whatsappLink;

}
