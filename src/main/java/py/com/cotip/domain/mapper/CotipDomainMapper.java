package py.com.cotip.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import py.com.cotip.application.rest.model.CotipDto;
import py.com.cotip.external.cotipdb.model.CotipEntity;
import py.com.cotip.external.webservice.util.CurrencyUtils;

import java.util.List;

@Mapper
public interface CotipDomainMapper {

    // ::: INSTANCE

    CotipDomainMapper INSTANCE = Mappers.getMapper(CotipDomainMapper.class);

    // ::: mappers

    @Mapping(source = "uploadDate", target = "lastUpdated")
    @Mapping(target = "exchangeRate", qualifiedByName = "mapExchangeRate")
    CotipDto toCotipDto(CotipEntity cotipEntity);

    List<CotipDto> toListCotipDto(List<CotipEntity> cotipEntities);

    @Named("mapExchangeRate")
    default String mapExchangeRate(String exchangeRate) {
        return CurrencyUtils.getEnglishName(exchangeRate);
    }
}
