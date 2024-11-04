package py.com.cotip.external.webservice.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class CotipProperties {

    // ::: vars

    @Value("${cotip.continental.bank.bearer.path}")
    private String continentalBearerTokenPath;

    @Value("${cotip.continental.bank.path}")
    private String continentalPath;

    @Value("${cotip.familiar.bank.path}")
    private String familiarPath;

    @Value("${cotip.gnb.bank.path}")
    private String gnbPath;

    @Value("${cotip.rio.bank.path}")
    private String rioPath;

    @Value("${cotip.solar.bank.path}")
    private String solarBankPath;

    @Value("${cotip.bnf.bank.path}")
    private String bnfBankPath;

    @Value("${cotip.atlas.bank.path}")
    private String atlasBankPath;

}
