package py.com.cotip.external.cotipdb.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import py.com.cotip.domain.commons.ProviderType;

import java.util.Arrays;

@Converter(autoApply = true)
public class ProviderTypeConverter implements AttributeConverter<ProviderType, String> {

    // ::: converters

    @Override
    public String convertToDatabaseColumn(ProviderType providerType) {
        return providerType != null ? providerType.getDescription() : null;
    }

    @Override
    public ProviderType convertToEntityAttribute(String description) {
        return Arrays.stream(ProviderType.values())
                .filter(provider -> provider.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown provider description: " + description));
    }
}
