package py.com.cotip.external.cotipdb.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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

    @Column(name = "horarios")
    private CotipSchedule schedule;

    @Column(name = "sitio_web")
    private String website;

    @Column(name = "link_whatsapp")
    private String whatsappLink;

}
