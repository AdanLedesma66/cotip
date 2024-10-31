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
                return "USD";
            case "PESO ARGENTINO":
                return "ARS";
            case "REAL BRASILEÑO":
                return "BRL";
            case "EURO EFECTIVO":
            case "EURO TRANSFERENCIA":
            case "EURO":
                return "EUR";
            case "FRANCO SUIZO":
                return "CHF";
            case "LIBRA ESTERLINA":
                return "GBP";
            case "YEN JAPONÉS":
                return "JPY";
            default:
                return null;
        }
    }

    public static String getStandardizedExchangeRateName(String exchangeRate) {
        switch (exchangeRate.toUpperCase()) {
            case "DÓLAR EFECTIVO":
            case "DOLAR EFECTIVO":
            case "DOLAR AMERICANO":
            case "USD":
                return "Dólar Americano";
            case "DÓLAR CHEQUE / TRANSFERENCIA":
            case "DOLAR CHQ./TRANSF.":
                return "Dólar Cheque / Transferencia";
            case "PESO ARGENTINO":
            case "ARS":
                return "Peso Argentino";
            case "REAL BRASILEÑO":
            case "REALES":
            case "BRL":
                return "Real Brasileño";
            case "EURO EFECTIVO":
            case "EUR EFECTIVO":
                return "Euro Efectivo";
            case "EURO TRANSFERENCIA":
            case "EURO CHQ./TRANSF.":
                return "Euro Transferencia";
            case "FRANCO SUIZO":
            case "CHF":
                return "Franco Suizo";
            case "LIBRA ESTERLINA":
            case "GBP":
                return "Libra Esterlina";
            case "YEN JAPONES":
            case "JPY":
                return "Yen Japonés";
            default:
                return null;
        }
    }

    public static String getStandardizedExchangeRateNameFromImageSrc(String srcText) {
        if (srcText.contains("estados-unidos-de-america")) {
            return "Dólar Americano";
        } else if (srcText.contains("reales")) {
            return "Real Brasileño";
        } else if (srcText.contains("pesos%20argentinos")) {
            return "Peso Argentino";
        } else if (srcText.contains("euros")) {
            return "Euro Efectivo";
        }
        return null;
    }

}
