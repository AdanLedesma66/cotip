package py.com.cotip.external.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ContinentalBearerExternal {

    // ::: vars

    @JsonProperty(value = "access_token")
    public String accessToken;

    @JsonProperty(value = "expires_in")
    private Integer expiresIn;

    @JsonProperty(value = "refresh_expires_in")
    private Integer refreshExpiresIn;

    @JsonProperty(value = "token_type")
    private String tokenType;
    
    @JsonProperty(value = "not-before-policy")
    private Integer notBeforePolicy;

    @JsonProperty(value = "scope")
    private String scope;

}
