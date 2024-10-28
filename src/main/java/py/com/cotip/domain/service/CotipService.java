package py.com.cotip.domain.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import py.com.cotip.application.rest.model.FamiliarDto;
import py.com.cotip.domain.mapper.ContinentalDomainMapper;
import py.com.cotip.domain.mapper.FamiliarDomainMapper;
import py.com.cotip.domain.port.in.CotipInPort;
import py.com.cotip.domain.port.out.CotipOutPort;
import py.com.cotip.domain.port.out.response.ContinentalResponse;
import py.com.cotip.external.model.ContinentalExternal;

import java.util.List;
@Slf4j
@AllArgsConstructor
public class CotipService implements CotipInPort {

    // ::: inyects

    private CotipOutPort cotipOutPort;

    // ::: impl

    @Cacheable(value = "continental", key = "'continentalResponse'")
    @Override
    public List<ContinentalResponse> findCotizacionContinentalResponse() {
        List<ContinentalExternal> cotizacionExternals = cotipOutPort.findContinentalCotizacion();

        return ContinentalDomainMapper.INSTANCE.externalToListResponse(cotizacionExternals);
    }

    @Cacheable(value = "familiar", key = "'familiarResponse'")
    @Override
    public List<FamiliarDto> findFamiliarCotizacionResponse() throws Exception {
        var familiarDtoList = FamiliarDomainMapper.INSTANCE.toListFamiliarDto(cotipOutPort.findFamiliarCotizacion());
        return familiarDtoList;
    }
}
