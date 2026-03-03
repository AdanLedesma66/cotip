package py.com.cotip.application.mapper;

import py.com.cotip.application.dto.ExchangeRateDto;
import py.com.cotip.domain.model.ExchangeRateBO;

import java.util.List;

public final class ExchangeRateDtoMapper {

    private ExchangeRateDtoMapper() {
    }

    public static ExchangeRateDto toDto(ExchangeRateBO source) {
        return new ExchangeRateDto(
                source.getExchangeRate(),
                source.getCurrencyCode(),
                source.getCurrencyName(),
                source.getQuoteModality(),
                source.getBuyRate(),
                source.getSellRate(),
                source.getBuyRateStatus(),
                source.getSellRateStatus(),
                source.isEnabled(),
                source.getProvider(),
                source.getBranchOffice(),
                source.getCity(),
                source.getLastUpdated()
        );
    }

    public static List<ExchangeRateDto> toDtoList(List<ExchangeRateBO> source) {
        return source.stream().map(ExchangeRateDtoMapper::toDto).toList();
    }
}
