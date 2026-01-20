package py.com.cotip.external.webservice.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class CotipProperties {

    // ::: General bank paths

    @Value("${cotip.continental.bank.bearer.path}")
    private String continentalBearerTokenPath;

    @Value("${cotip.continental.bank.bearer.apikey}")
    private String continentalBearerTokenApiKey;

    @Value("${cotip.continental.bank.bearer.client}")
    private String continentalBearerTokenClient;

    @Value("${cotip.continental.bank.bearer.client-id}")
    private String continentalBearerTokenClientId;

    @Value("${cotip.continental.bank.bearer.grant-Type}")
    private String continentalBearerTokenGrantType;

    @Value("${cotip.continental.bank.bearer.scope}")
    private String continentalBearerTokenScope;

    @Value("${cotip.continental.bank.path}")
    private String continentalPath;

    @Value("${cotip.continental.bank.apikey}")
    private String continentalApiKey;

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

    @Value("${cotip.fic.financial.path}")
    private String ficFinancialPath;

    @Value("${cotip.maxicambios.path}")
    private String maxicambiosPath;

}