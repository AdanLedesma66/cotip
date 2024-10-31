package py.com.cotip.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import py.com.cotip.application.rest.model.RioDto;
import py.com.cotip.external.webservice.model.RioExternal;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface RioDomainMapper {

    // ::: INSTANCE

    RioDomainMapper INSTANCE = Mappers.getMapper(RioDomainMapper.class);

    // ::: mappers

    @Mappings({
            @Mapping(source = "buyRate", target = "buyRate", qualifiedByName = "stringToLong"),
            @Mapping(source = "sellRate", target = "sellRate", qualifiedByName = "stringToLong")
    })
    RioDto toRioDto(RioExternal rioExternal);

    List<RioDto> toListRioDto(List<RioExternal> rioExternalList);

    // ::: converters

    @Named("stringToLong")
    default Long stringToLong(String rate) {
        BigDecimal rateDecimal = new BigDecimal(rate.replace(",", "").trim());
        return rateDecimal.longValue();
    }

}
