package py.com.cotip.insfrastructure.config.security;

import org.springframework.stereotype.Service;
import py.com.cotip.insfrastructure.external.cotipdb.model.ApiClientEntity;
import py.com.cotip.insfrastructure.external.cotipdb.repository.ApiClientRepository;

import java.util.Optional;

@Service
public class ApiClientAuthenticationService {

    private final ApiClientRepository apiClientRepository;

    public ApiClientAuthenticationService(ApiClientRepository apiClientRepository) {
        this.apiClientRepository = apiClientRepository;
    }

    public Optional<ApiClientEntity> authenticate(String rawApiKey) {
        String apiKeyHash = ApiKeyHashing.sha256Hex(rawApiKey);
        if (apiKeyHash.isBlank()) {
            return Optional.empty();
        }

        return apiClientRepository.findByApiKeyHashAndEnabledTrue(apiKeyHash);
    }
}
