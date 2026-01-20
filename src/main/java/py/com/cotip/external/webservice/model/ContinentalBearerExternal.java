package py.com.cotip.external.webservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ContinentalBearerExternal(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("expires_in") Integer expiresIn,
        @JsonProperty("refresh_expires_in") Integer refreshExpiresIn,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("not-before-policy") Integer notBeforePolicy,
        @JsonProperty("scope") String scope
) {}