package py.com.cotip.insfrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import py.com.cotip.domain.port.out.ApplicationMetricsPort;
import py.com.cotip.domain.port.out.BranchOfficeQueryPort;
import py.com.cotip.domain.port.out.ExchangeRateRepositoryPort;
import py.com.cotip.domain.port.out.ProviderRatesSourcePort;
import py.com.cotip.application.service.GetExchangeRatesUseCaseImpl;


@Configuration
public class CotipConfig {

    // ::: beans

    @Bean
    public GetExchangeRatesUseCaseImpl cotizacionService(ProviderRatesSourcePort providerRatesSourcePort,
                                                         ExchangeRateRepositoryPort exchangeRateRepositoryPort,
                                                         BranchOfficeQueryPort branchOfficeQueryPort,
                                                         ApplicationMetricsPort metricsPort) {
        return new GetExchangeRatesUseCaseImpl(providerRatesSourcePort,
                exchangeRateRepositoryPort,
                branchOfficeQueryPort,
                metricsPort);
    }
}
