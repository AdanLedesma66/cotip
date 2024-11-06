package py.com.cotip.external.webservice.util;

import lombok.Data;

@Data
public class CurrencyUtils {

    // ::: utils

    private CurrencyUtils() {
    }
    public static String getCurrencyCode(String exchangeRate) {
        switch (exchangeRate.toUpperCase()) {
            case "DÓLAR AMERICANO":
            case "DÓLAR CHEQUE / TRANSFERENCIA":
            case "DÓLAR X EURO":
                return "USD";
            case "PESO ARGENTINO":
            case "PESO X DÓLAR":
                return "ARS";
            case "REAL BRASILEÑO":
            case "REAL X DÓLAR":
                return "BRL";
            case "EURO EFECTIVO":
            case "EURO TRANSFERENCIA":
            case "EURO":
            case "EURO CHEQUE / TRANSFERENCIA":
                return "EUR";
            case "FRANCO SUIZO":
                return "CHF";
            case "LIBRA ESTERLINA":
                return "GBP";
            case "YEN JAPONÉS":
                return "JPY";
            case "PESO URUGUAYO":
                return "UYU";
            case "PESO CHILENO":
                return "CLP";
            case "RAND SUDAFRICANO":
                return "ZAR";
            case "DÓLAR CANADIENSE":
                return "CAD";
            case "DÓLAR AUSTRALIANO":
                return "AUD";
            case "PESO MEXICANO":
                return "MXN";
            case "SOL PERUANO":
                return "PEN";
            case "PESO BOLIVIANO":
                return "BOB";
            case "PESO COLOMBIANO":
                return "COP";
            default:
                return null;
        }
    }



    public static String getStandardizedExchangeRateName(String exchangeRate) {
        switch (exchangeRate.toUpperCase()) {
            case "DÓLAR EFECTIVO":
            case "DOLAR EFECTIVO":
            case "DOLAR AMERICANO":
            case "DÓLAR AMERICANO EFECTIVO":
            case "DOLAR":
            case "DÓLAR":
            case "DÓLARES":
            case "USD":
                return "Dólar Americano";
            case "DÓLAR CHEQUE / TRANSFERENCIA":
            case "DÓLAR AMERICANO TRANSF.":
            case "DOLAR CHQ./TRANSF.":
                return "Dólar Cheque / Transferencia";
            case "PESO ARGENTINO":
            case "PESO ARGENTINO EFECTIVO":
            case "PESOS ARG.":
            case "PESO ARG":
            case "PESO":
            case "ARS":
                return "Peso Argentino";
            case "REAL BRASILEÑO":
            case "REAL EFECTIVO":
            case "REALES":
            case "REAL":
            case "BRL":
                return "Real Brasileño";
            case "EURO EFECTIVO":
            case "EUR EFECTIVO":
            case "EURO":
            case "EUROS":
            case "EUR":
                return "Euro Efectivo";
            case "EURO TRANSFERENCIA":
            case "EURO TRANSF.":
            case "EURO CHEQUE / TRANSFERENCIA":
            case "EURO CHQ./TRANSF.":
                return "Euro Cheque / Transferencia";
            case "DÓLAR X EURO":
                return "Dólar x Euro Arbitraje";
            case "PESO X DÓLAR":
                return "Peso x Dólar Arbitraje";
            case "REAL X DÓLAR":
                return "Real x Dólar Arbitraje";
            case "FRANCO SUIZO":
            case "FRANCO":
            case "CHF":
                return "Franco Suizo";
            case "LIBRA ESTERLINA":
            case "LIBRA":
            case "GBP":
                return "Libra Esterlina";
            case "YEN JAPONES":
            case "YEN":
            case "JPY":
                return "Yen Japonés";
            case "PESO URU":
                return "Peso Uruguayo";
            case "PESO CHI":
                return "Peso Chileno";
            case "RAND":
                return "Rand Sudafricano";
            case "DÓLAR CA..":
                return "Dólar Canadiense";
            case "DÓLAR AU..":
                return "Dólar Australiano";
            case "PESO MEX":
                return "Peso Mexicano";
            case "SOL":
                return "Sol Peruano";
            case "PESO BOL":
                return "Peso Boliviano";
            case "PESO COL":
                return "Peso Colombiano";
            default:
                return null;
        }
    }

    public static String getEnglishName(String spanishName) {
        switch (spanishName) {
            case "Dólar Americano":
                return "US Dollar";
            case "Dólar Cheque / Transferencia":
                return "US Dollar Check/Transfer";
            case "Peso Argentino":
                return "Argentine Peso";
            case "Real Brasileño":
                return "Brazilian Real";
            case "Euro Efectivo":
                return "Euro Cash";
            case "Euro Cheque / Transferencia":
                return "Euro Cheque / Transfer";
            case "Dólar x Euro Arbitraje":
                return "Dollar to Euro Arbitrage";
            case "Peso x Dólar Arbitraje":
                return "Peso to Dollar Arbitrage";
            case "Real x Dólar Arbitraje":
                return "Real to Dollar Arbitrage";
            case "Franco Suizo":
                return "Swiss Franc";
            case "Libra Esterlina":
                return "British Pound";
            case "Yen Japonés":
                return "Japanese Yen";
            case "Peso Uruguayo":
                return "Uruguayan Peso";
            case "Peso Chileno":
                return "Chilean Peso";
            case "Rand Sudafricano":
                return "South African Rand";
            case "Dólar Canadiense":
                return "Canadian Dollar";
            case "Dólar Australiano":
                return "Australian Dollar";
            case "Peso Mexicano":
                return "Mexican Peso";
            case "Sol Peruano":
                return "Peruvian Sol";
            case "Peso Boliviano":
                return "Bolivian Peso";
            case "Peso Colombiano":
                return "Colombian Peso";
            default:
                return "Unknown Currency";
        }
    }

}
