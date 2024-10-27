package py.com.cotip.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import py.com.cotip.domain.port.out.response.ContinentalResponse;
import py.com.cotip.external.model.ContinentalExternal;

import java.util.List;

@Mapper
public interface ContinentalDomainMapper {

    // ::: instance

    ContinentalDomainMapper INSTANCE = Mappers.getMapper(ContinentalDomainMapper.class);

    // ::: mappers

    ContinentalResponse externalToResponse(ContinentalExternal cotizacionExternal);

    List<ContinentalResponse> externalToListResponse(List<ContinentalExternal> cotizacionExternals);


}
