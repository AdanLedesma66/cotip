package py.com.cotip.insfrastructure.external.webservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChacoExchangeExternal {

    private String updateTs;
    private String branchOfficeId;
    private List<ChacoExchangeItemExternal> items;
}
