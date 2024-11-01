package py.com.cotip.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import py.com.cotip.application.rest.model.GnbDto;
import py.com.cotip.external.webservice.model.GnbExternal;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface GnbDomainMapper {

    // ::: INSTANCE

    GnbDomainMapper INSTANCE = Mappers.getMapper(GnbDomainMapper.class);

    // ::: mappers

    @Mapping(source = "currencyCode", target = "currencyCode")
    @Mapping(source = "electronicSellPrice", target = "sellRate", qualifiedByName = "stringToLong")
    @Mapping(source = "electronicBuyPrice", target = "buyRate", qualifiedByName = "stringToLong")
    @Mapping(source = "currencyDesc", target = "exchangeRate")
    GnbDto toGnbDto(GnbExternal gnbExternal);

    List<GnbDto> toListGnbDto(List<GnbExternal> gnbExternals);

    // ::: externals
    @Named("stringToLong")
    static Long stringToLong(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        BigDecimal decimalValue = new BigDecimal(value.replace(",", "").trim());
        return decimalValue.longValue();
    }

}
