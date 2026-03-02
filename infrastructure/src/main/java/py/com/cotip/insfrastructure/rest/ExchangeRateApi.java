package py.com.cotip.insfrastructure.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import py.com.cotip.application.dto.BranchOfficeDto;
import py.com.cotip.application.dto.ExchangeRateDto;
import py.com.cotip.application.mapper.ExchangeRateDtoMapper;
import py.com.cotip.insfrastructure.config.model.CotipResponse;
import py.com.cotip.insfrastructure.external.webservice.config.ChacoBranchCatalog;
import py.com.cotip.domain.commons.CotipCity;
import py.com.cotip.domain.commons.CotipError;
import py.com.cotip.domain.exception.CotipException;
import py.com.cotip.domain.port.in.GetExchangeRatesUseCase;
import py.com.cotip.domain.port.in.request.GetRatesQuery;

import java.util.List;

import static py.com.cotip.insfrastructure.config.CotipConstants.VERSION_V1;

@RestController
@RequestMapping("/cotip")
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
    public CotipResponse<List<ExchangeRateDto>> cambiosChacoByBranch(@PathVariable String branchOfficeId) {
        return CotipResponse.of(ExchangeRateDtoMapper.toDtoList(
                action.findLatestCambiosChacoExchangeRates(branchOfficeId)));
    }

    @GetMapping(path = "/cambios-chaco/sucursal", version = VERSION_V1)
    public CotipResponse<List<ExchangeRateDto>> cambiosChacoByBranchLookup(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String nombre
    ) {
        String branchOfficeId = resolveBranchOfficeId(id, nombre);
        return CotipResponse.of(ExchangeRateDtoMapper.toDtoList(
                action.findLatestCambiosChacoExchangeRates(branchOfficeId)));
    }

    @GetMapping(path = "/cambios-chaco/sucursales", version = VERSION_V1)
    public CotipResponse<List<BranchOfficeDto>> cambiosChacoBranches() {
        List<BranchOfficeDto> branches = ChacoBranchCatalog.knownBranches().stream()
                .map(branch -> new BranchOfficeDto(
                        branch.id(),
                        branch.name(),
                        branch.department(),
                        branch.city(),
                        branch.neighborhood()))
                .toList();

        return CotipResponse.of(branches);
    }

    private String resolveBranchOfficeId(String branchOfficeId, String branchOfficeName) {
        boolean hasId = branchOfficeId != null && !branchOfficeId.isBlank();
        boolean hasName = branchOfficeName != null && !branchOfficeName.isBlank();

        if (!hasId && !hasName) {
            throw new CotipException(400,
                    CotipError.CAMBIOS_CHACO_BRANCH_INVALID.getCode(),
                    "Debe enviar id o nombre de sucursal",
                    true);
        }

        if (hasId) {
            String normalizedId = branchOfficeId.trim();
            if (!ChacoBranchCatalog.existsId(normalizedId)) {
                throw new CotipException(400,
                        CotipError.CAMBIOS_CHACO_BRANCH_INVALID.getCode(),
                        "Sucursal de Cambios Chaco invalida: " + normalizedId,
                        true);
            }
            return normalizedId;
        }

        ChacoBranchCatalog.BranchMetadata branch = ChacoBranchCatalog.findByName(branchOfficeName);
        if (branch == null) {
            throw new CotipException(404,
                    CotipError.CAMBIOS_CHACO_BRANCH_INVALID.getCode(),
                    "No existe sucursal con nombre: " + branchOfficeName,
                    true);
        }

        return branch.id();
    }


}
