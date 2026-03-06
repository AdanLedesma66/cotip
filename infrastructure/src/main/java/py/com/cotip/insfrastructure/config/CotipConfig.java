package py.com.cotip.insfrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import py.com.cotip.domain.port.in.SyncExchangeRatesUseCase;
import py.com.cotip.domain.port.out.ApplicationMetricsPort;
import py.com.cotip.domain.port.out.BranchOfficeQueryPort;
import py.com.cotip.domain.port.out.ExchangeRateRepositoryPort;
import py.com.cotip.domain.port.out.ProviderRatesSourcePort;
import py.com.cotip.application.service.GetExchangeRatesUseCaseImpl;
import py.com.cotip.application.service.SyncExchangeRatesUseCaseImpl;


@Configuration
public class CotipConfig {

    // ::: beans

    @Bean
    public GetExchangeRatesUseCaseImpl cotizacionService(ExchangeRateRepositoryPort exchangeRateRepositoryPort,
                                                         BranchOfficeQueryPort branchOfficeQueryPort,
                                                         ApplicationMetricsPort metricsPort,
                                                         @Value("${cotip.chaco.default-branch-office-id:3}")
                                                         String defaultChacoBranchOfficeId) {
        return new GetExchangeRatesUseCaseImpl(
                exchangeRateRepositoryPort,
                branchOfficeQueryPort,
                metricsPort,
                defaultChacoBranchOfficeId);
    }

    @Bean
    public SyncExchangeRatesUseCase syncExchangeRatesUseCase(ProviderRatesSourcePort providerRatesSourcePort,
                                                              ExchangeRateRepositoryPort exchangeRateRepositoryPort,
                                                              BranchOfficeQueryPort branchOfficeQueryPort,
                                                              ApplicationMetricsPort metricsPort) {
        return new SyncExchangeRatesUseCaseImpl(
                providerRatesSourcePort,
                exchangeRateRepositoryPort,
                branchOfficeQueryPort,
                metricsPort
        );
    }
}
