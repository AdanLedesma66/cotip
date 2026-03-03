package py.com.cotip.insfrastructure.external.webservice.util;

import py.com.cotip.domain.commons.QuoteModality;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class CurrencyUtils {

    private static final Map<String, StandardizedRate> RAW_ALIAS_MAP = new HashMap<>();
    private static final Map<String, StandardizedRate> ISO_CODE_MAP = new HashMap<>();
    private static final Map<String, String> STANDARDIZED_NAME_TO_ENGLISH = new HashMap<>();

    static {
        register("USD", "Dólar Americano", "Dólar Americano", "US Dollar", QuoteModality.CASH, true,
                "DÓLAR AMERICANO", "DÓLAR EFECTIVO", "DOLAR EFECTIVO", "DOLAR AMERICANO",
                "DÓLAR AMERICANO EFECTIVO", "DOLAR", "DÓLAR", "DÓLARES");
        register("USD", "Dólar Americano", "Dólar Cheque / Transferencia", "US Dollar Check/Transfer",
                QuoteModality.CHECK_TRANSFER, false,
                "DÓLAR CHEQUE / TRANSFERENCIA", "DÓLAR AMERICANO TRANSF.", "DOLAR CHQ./TRANSF.");
        register("USD", "Dólar Americano", "Dólar x Euro Arbitraje", "Dollar to Euro Arbitrage",
                QuoteModality.ARBITRAGE, false,
                "DÓLAR X EURO");

        register("ARS", "Peso Argentino", "Peso Argentino", "Argentine Peso", QuoteModality.CASH, true,
                "PESO ARGENTINO", "PESO ARGENTINO EFECTIVO", "PESOS ARG.", "PESO ARG", "PESO");
        register("ARS", "Peso Argentino", "Peso x Dólar Arbitraje", "Peso to Dollar Arbitrage",
                QuoteModality.ARBITRAGE, false,
                "PESO X DÓLAR");

        register("BRL", "Real Brasileño", "Real Brasileño", "Brazilian Real", QuoteModality.CASH, true,
                "REAL BRASILEÑO", "REAL EFECTIVO", "REALES", "REAL");
        register("BRL", "Real Brasileño", "Real x Dólar Arbitraje", "Real to Dollar Arbitrage",
                QuoteModality.ARBITRAGE, false,
                "REAL X DÓLAR");

        register("EUR", "Euro", "Euro Efectivo", "Euro Cash", QuoteModality.CASH, true,
                "EURO EFECTIVO", "EUR EFECTIVO", "EURO", "EUROS");
        register("EUR", "Euro", "Euro Cheque / Transferencia", "Euro Check/Transfer",
                QuoteModality.CHECK_TRANSFER, false,
                "EURO TRANSFERENCIA", "EURO TRANSF.", "EURO CHEQUE / TRANSFERENCIA", "EURO CHQ./TRANSF.");

        register("CHF", "Franco Suizo", "Franco Suizo", "Swiss Franc", QuoteModality.CASH, true,
                "FRANCO SUIZO", "FRANCO");
        register("GBP", "Libra Esterlina", "Libra Esterlina", "British Pound", QuoteModality.CASH, true,
                "LIBRA ESTERLINA", "LIBRA");
        register("JPY", "Yen Japonés", "Yen Japonés", "Japanese Yen", QuoteModality.CASH, true,
                "YEN JAPONÉS", "YEN JAPONES", "YEN");
        register("UYU", "Peso Uruguayo", "Peso Uruguayo", "Uruguayan Peso", QuoteModality.CASH, true,
                "PESO URUGUAYO", "PESO URU");
        register("CLP", "Peso Chileno", "Peso Chileno", "Chilean Peso", QuoteModality.CASH, true,
                "PESO CHILENO", "PESO CHI");
        register("ZAR", "Rand Sudafricano", "Rand Sudafricano", "South African Rand", QuoteModality.CASH, true,
                "RAND SUDAFRICANO", "RAND");
        register("CAD", "Dólar Canadiense", "Dólar Canadiense", "Canadian Dollar", QuoteModality.CASH, true,
                "DÓLAR CANADIENSE", "DÓLAR CA..");
        register("AUD", "Dólar Australiano", "Dólar Australiano", "Australian Dollar", QuoteModality.CASH, true,
                "DÓLAR AUSTRALIANO", "DÓLAR AU..");
        register("MXN", "Peso Mexicano", "Peso Mexicano", "Mexican Peso", QuoteModality.CASH, true,
                "PESO MEXICANO", "PESO MEX");
        register("PEN", "Sol Peruano", "Sol Peruano", "Peruvian Sol", QuoteModality.CASH, true,
                "SOL PERUANO", "SOL");
        register("BOB", "Peso Boliviano", "Peso Boliviano", "Bolivian Peso", QuoteModality.CASH, true,
                "PESO BOLIVIANO", "PESO BOL");
        register("COP", "Peso Colombiano", "Peso Colombiano", "Colombian Peso", QuoteModality.CASH, true,
                "PESO COLOMBIANO", "PESO COL");

        register("NOK", "Corona Noruega", "Corona Noruega", "Norwegian Krone", QuoteModality.CASH, true);
        register("DKK", "Corona Danesa", "Corona Danesa", "Danish Krone", QuoteModality.CASH, true);
        register("SEK", "Corona Sueca", "Corona Sueca", "Swedish Krona", QuoteModality.CASH, true);
        register("KWD", "Dinar Kuwaiti", "Dinar Kuwaiti", "Kuwaiti Dinar", QuoteModality.CASH, true);
        register("ILS", "Shekel Israelí", "Shekel Israelí", "Israeli Shekel", QuoteModality.CASH, true);
        register("RUB", "Rublo Ruso", "Rublo Ruso", "Russian Ruble", QuoteModality.CASH, true);
        register("CNY", "Yuan China", "Yuan China", "Chinese Yuan", QuoteModality.CASH, true);
        register("TWD", "Dólar Taiwanés", "Dólar Taiwanés", "Taiwan Dollar", QuoteModality.CASH, true);
        register("SGD", "Dólar Singapur", "Dólar Singapur", "Singapore Dollar", QuoteModality.CASH, true);
    }

    private CurrencyUtils() {
    }

    public static StandardizedRate standardizeExchangeRate(String exchangeRate) {
        if (exchangeRate == null || exchangeRate.isBlank()) {
            return null;
        }

        return RAW_ALIAS_MAP.get(normalizeKey(exchangeRate));
    }

    public static StandardizedRate fromIsoCode(String isoCode) {
        if (isoCode == null || isoCode.isBlank()) {
            return null;
        }

        return ISO_CODE_MAP.get(normalizeKey(isoCode));
    }

    public static StandardizedRate unknownFromIsoCode(String isoCode) {
        if (isoCode == null || isoCode.isBlank()) {
            return null;
        }

        String normalizedCode = isoCode.trim().toUpperCase(Locale.ROOT);
        return new StandardizedRate(
                normalizedCode,
                normalizedCode,
                normalizedCode,
                normalizedCode,
                QuoteModality.CASH
        );
    }

    public static String getCurrencyCode(String exchangeRate) {
        StandardizedRate standardizedRate = standardizeExchangeRate(exchangeRate);
        return standardizedRate != null ? standardizedRate.currencyCode() : null;
    }

    public static String getStandardizedExchangeRateName(String exchangeRate) {
        StandardizedRate standardizedRate = standardizeExchangeRate(exchangeRate);
        return standardizedRate != null ? standardizedRate.exchangeRateName() : null;
    }

    public static String getExchangeRateNameByIsoCode(String isoCode) {
        StandardizedRate standardizedRate = fromIsoCode(isoCode);
        return standardizedRate != null ? standardizedRate.exchangeRateName() : null;
    }

    public static String getEnglishName(String spanishName) {
        if (spanishName == null || spanishName.isBlank()) {
            return "Unknown Currency";
        }

        return STANDARDIZED_NAME_TO_ENGLISH.getOrDefault(normalizeKey(spanishName), "Unknown Currency");
    }

    private static void register(String currencyCode,
                                 String currencyName,
                                 String exchangeRateName,
                                 String englishName,
                                 QuoteModality quoteModality,
                                 boolean defaultForIso,
                                 String... aliases) {
        StandardizedRate standardizedRate = new StandardizedRate(
                currencyCode,
                currencyName,
                exchangeRateName,
                englishName,
                quoteModality
        );

        RAW_ALIAS_MAP.put(normalizeKey(exchangeRateName), standardizedRate);
        STANDARDIZED_NAME_TO_ENGLISH.put(normalizeKey(exchangeRateName), englishName);

        for (String alias : aliases) {
            RAW_ALIAS_MAP.put(normalizeKey(alias), standardizedRate);
        }

        if (defaultForIso) {
            ISO_CODE_MAP.put(normalizeKey(currencyCode), standardizedRate);
            RAW_ALIAS_MAP.put(normalizeKey(currencyCode), standardizedRate);
        }
    }

    private static String normalizeKey(String value) {
        String normalized = Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        return normalized.toUpperCase(Locale.ROOT);
    }

    public record StandardizedRate(
            String currencyCode,
            String currencyName,
            String exchangeRateName,
            String englishName,
            QuoteModality quoteModality
    ) {
    }
}
