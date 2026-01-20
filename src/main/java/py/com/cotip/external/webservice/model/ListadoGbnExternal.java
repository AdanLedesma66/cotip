package py.com.cotip.external.webservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ListadoGbnExternal(
        List<GnbExternal> exchangeRates
) {}
