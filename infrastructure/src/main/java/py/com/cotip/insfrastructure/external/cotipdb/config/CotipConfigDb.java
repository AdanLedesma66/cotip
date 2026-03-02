package py.com.cotip.insfrastructure.external.cotipdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import py.com.cotip.domain.port.out.ExchangeRateRepositoryPort;
import py.com.cotip.domain.port.out.ScrapeAuditPort;
import py.com.cotip.insfrastructure.external.cotipdb.BranchOfficeResolver;
import py.com.cotip.insfrastructure.external.cotipdb.ExchangeRateRepositoryAdapter;
import py.com.cotip.insfrastructure.external.cotipdb.ScrapeAuditDbOutPortImpl;
import py.com.cotip.insfrastructure.external.cotipdb.repository.CotipRepository;
import py.com.cotip.insfrastructure.external.cotipdb.repository.ScrapeAuditRepository;

@Configuration
public class CotipConfigDb {

    @Bean
    public ExchangeRateRepositoryPort cotipDbOutPort(CotipRepository cotipRepository,
                                                     BranchOfficeResolver branchOfficeResolver){
        return new ExchangeRateRepositoryAdapter(cotipRepository, branchOfficeResolver);
    }

    @Bean
    public ScrapeAuditPort scrapeAuditPort(ScrapeAuditRepository scrapeAuditRepository) {
        return new ScrapeAuditDbOutPortImpl(scrapeAuditRepository);
    }


}
