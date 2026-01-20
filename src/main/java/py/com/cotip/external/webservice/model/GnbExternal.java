package py.com.cotip.external.webservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GnbExternal(
        String currencyCode,
        String electronicSellPrice,
        String electronicBuyPrice,
        String currencyDesc,
        String currencyAbbreviation,
        String cashBuyPrice,
        String checkSellPrice,
        String checkBuyPrice,
        Integer order
) { }
