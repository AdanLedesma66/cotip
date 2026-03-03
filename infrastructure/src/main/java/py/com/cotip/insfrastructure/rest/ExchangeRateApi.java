package py.com.cotip.insfrastructure.rest;

import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import py.com.cotip.application.dto.BranchOfficeDto;
import py.com.cotip.application.dto.ExchangeRateDto;
import py.com.cotip.application.mapper.ExchangeRateDtoMapper;
import py.com.cotip.insfrastructure.config.model.CotipResponse;
import py.com.cotip.domain.commons.CotipCity;
import py.com.cotip.domain.port.in.GetExchangeRatesUseCase;
import py.com.cotip.domain.port.in.request.GetRatesQuery;

import java.util.List;

import static py.com.cotip.insfrastructure.config.CotipConstants.VERSION_V1;

@RestController
@Validated
@RequestMapping("${cotip.api.base-path:/cotip}")
public class ExchangeRateApi {

    // ::: API

    @Autowired
    private GetExchangeRatesUseCase action;

    // ::: REQUEST

    @GetMapping(path = "/banco-continental", version = VERSION_V1)
    public CotipResponse<List<ExchangeRateDto>> continentalBank() {
        return CotipResponse.of(ExchangeRateDtoMapper.toDtoList(action.findLatestContinentalBankExchangeRates()));
    }

    @GetMapping(path = "/banco-gnb", version = VERSION_V1)
    public CotipResponse<List<ExchangeRateDto>> gnbBank() {
        return CotipResponse.of(ExchangeRateDtoMapper.toDtoList(action.findLatestGnbBankExchangeRates()));
    }

    @GetMapping(path = "/maxi-cambios", version = VERSION_V1)
    public CotipResponse<List<ExchangeRateDto>> maxiExchange(
            @RequestParam(required = false) CotipCity city
    ){
        GetRatesQuery request = GetRatesQuery.builder()
                .city(city)
                .build();

        return CotipResponse.of(ExchangeRateDtoMapper.toDtoList(action.findLatestMaxiExchangeRates(request)));
    }

    @GetMapping(path = "/cambios-chaco", version = VERSION_V1)
    public CotipResponse<List<ExchangeRateDto>> cambiosChaco() {
        return CotipResponse.of(ExchangeRateDtoMapper.toDtoList(action.findLatestCambiosChacoExchangeRates()));
    }

    @GetMapping(path = "/version", version = VERSION_V1)
    public CotipResponse<String> apiVersion() {
        return CotipResponse.of(VERSION_V1);
    }

    @GetMapping(path = "/cambios-chaco/sucursal/{branchOfficeId}", version = VERSION_V1)
    public CotipResponse<List<ExchangeRateDto>> cambiosChacoByBranch(
            @PathVariable
            @NotBlank(message = "Debe enviar id de sucursal") String branchOfficeId
    ) {
        return CotipResponse.of(ExchangeRateDtoMapper.toDtoList(
                action.findLatestCambiosChacoExchangeRates(branchOfficeId)));
    }

    @GetMapping(path = "/cambios-chaco/sucursal", version = VERSION_V1, params = {"id", "!nombre"})
    public CotipResponse<List<ExchangeRateDto>> cambiosChacoByBranchLookupById(
            @RequestParam("id")
            @NotBlank(message = "Debe enviar id de sucursal") String branchOfficeId
    ) {
        return CotipResponse.of(ExchangeRateDtoMapper.toDtoList(
                action.findLatestCambiosChacoExchangeRates(branchOfficeId)));
    }

    @GetMapping(path = "/cambios-chaco/sucursal", version = VERSION_V1, params = {"nombre", "!id"})
    public CotipResponse<List<ExchangeRateDto>> cambiosChacoByBranchLookupByName(
            @RequestParam("nombre")
            @NotBlank(message = "Debe enviar nombre de sucursal") String branchOfficeName
    ) {
        return CotipResponse.of(ExchangeRateDtoMapper.toDtoList(
                action.findLatestCambiosChacoExchangeRatesByBranchName(branchOfficeName)));
    }

    @GetMapping(path = "/cambios-chaco/sucursales", version = VERSION_V1)
    public CotipResponse<List<BranchOfficeDto>> cambiosChacoBranches() {
        List<BranchOfficeDto> branches = action.findCambiosChacoBranches().stream()
                .map(branch -> new BranchOfficeDto(
                        branch.externalBranchId(),
                        branch.name(),
                        branch.department(),
                        branch.city(),
                        branch.neighborhood()))
                .toList();

        return CotipResponse.of(branches);
    }

}
