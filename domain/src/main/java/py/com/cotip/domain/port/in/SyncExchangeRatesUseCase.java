package py.com.cotip.domain.port.in;

public interface SyncExchangeRatesUseCase {

    int syncContinentalBankExchangeRates();

    int syncGnbBankExchangeRates();

    int syncMaxiCambiosExchangeRates();

    int syncCambiosChacoExchangeRatesForActiveBranches();
}
