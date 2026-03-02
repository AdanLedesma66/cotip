package py.com.cotip.insfrastructure.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@RestController
@RequestMapping("${cotip.api.base-path:/cotip/v1}")
public class ExchangeRateApi {

    // ::: API

    @Autowired
    private GetExchangeRatesUseCase action;

    @Value("${cotip.api.version:v1}")
    private String apiVersion;

    // ::: REQUEST

    @GetMapping("/banco-continental")
    public CotipResponse<List<ExchangeRateDto>> continentalBank() {
        return CotipResponse.of(ExchangeRateDtoMapper.toDtoList(action.findLatestContinentalBankExchangeRates()));
    }

    @GetMapping("/banco-gnb")
    public CotipResponse<List<ExchangeRateDto>> gnbBank() {
        return CotipResponse.of(ExchangeRateDtoMapper.toDtoList(action.findLatestGnbBankExchangeRates()));
    }

    @GetMapping("/maxi-cambios")
    public CotipResponse<List<ExchangeRateDto>> maxiExchange(
            @RequestParam(required = false) CotipCity city
    ){
        GetRatesQuery request = GetRatesQuery.builder()
                .city(city)
                .build();

        return CotipResponse.of(ExchangeRateDtoMapper.toDtoList(action.findLatestMaxiExchangeRates(request)));
    }

    @GetMapping("/cambios-chaco")
    public CotipResponse<List<ExchangeRateDto>> cambiosChaco() {
        return CotipResponse.of(ExchangeRateDtoMapper.toDtoList(action.findLatestCambiosChacoExchangeRates()));
    }

    @GetMapping("/version")
    public CotipResponse<String> apiVersion() {
        return CotipResponse.of(apiVersion);
    }

    @GetMapping("/cambios-chaco/sucursal/{branchOfficeId}")
    public CotipResponse<List<ExchangeRateDto>> cambiosChacoByBranch(@PathVariable String branchOfficeId) {
        return CotipResponse.of(ExchangeRateDtoMapper.toDtoList(
                action.findLatestCambiosChacoExchangeRates(branchOfficeId)));
    }

    @GetMapping("/cambios-chaco/sucursal")
    public CotipResponse<List<ExchangeRateDto>> cambiosChacoByBranchLookup(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String nombre
    ) {
        String branchOfficeId = resolveBranchOfficeId(id, nombre);
        return CotipResponse.of(ExchangeRateDtoMapper.toDtoList(
                action.findLatestCambiosChacoExchangeRates(branchOfficeId)));
    }

    @GetMapping("/cambios-chaco/sucursales")
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
