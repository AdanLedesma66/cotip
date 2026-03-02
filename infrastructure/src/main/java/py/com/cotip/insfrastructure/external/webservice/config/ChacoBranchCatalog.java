package py.com.cotip.insfrastructure.external.webservice.config;

import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ChacoBranchCatalog {

    private static final List<BranchMetadata> KNOWN_BRANCHES = List.of(
            new BranchMetadata("1", "Casa Central - Asuncion", "Capital", "Asuncion", null),
            new BranchMetadata("2", "Shopping Villa Morra - Asuncion", "Capital", "Asuncion", "Villa Morra"),
            new BranchMetadata("3", "Shopping Multiplaza - Asuncion", "Capital", "Asuncion", null),
            new BranchMetadata("30", "Palma", "Capital", "Asuncion", "Palma"),
            new BranchMetadata("6", "Agencia San Lorenzo", "Central", "San Lorenzo", null),
            new BranchMetadata("7", "Aeropuerto Intl Silvio Pettirossi", "Central", "Luque", null),
            new BranchMetadata("14", "Ag. Plaza Madero", "Central", "Luque", "Plaza Madero"),
            new BranchMetadata("8", "Paseo Lambare", "Central", "Lambare", null),
            new BranchMetadata("9", "Ciudad del Este - Sucursal Adrian Jara", "Alto Parana", "Ciudad del Este", "Adrian Jara"),
            new BranchMetadata("11", "Ciudad del Este - Ita Ybate", "Alto Parana", "Ciudad del Este", "Ita Ybate"),
            new BranchMetadata("13", "Ciudad del Este - Noblesse - KM 3,5", "Alto Parana", "Ciudad del Este", "KM 3,5"),
            new BranchMetadata("32", "Ciudad del Este - Km7", "Alto Parana", "Ciudad del Este", "Km7"),
            new BranchMetadata("40", "Ciudad del Este - Curupayty", "Alto Parana", "Ciudad del Este", "Curupayty"),
            new BranchMetadata("16", "Hernandarias - Super Carretera", "Alto Parana", "Hernandarias", null),
            new BranchMetadata("22", "Itaipu", "Alto Parana", "Hernandarias", "Itaipu"),
            new BranchMetadata("23", "Sucursal Super 6 - ENC", "Itapua", "Encarnacion", null),
            new BranchMetadata("24", "ENC. Mcal. Estigarribia - Zona Alta", "Itapua", "Encarnacion", "Zona Alta"),
            new BranchMetadata("27", "Sucursal Santa Rita", "Alto Parana", "Santa Rita", null),
            new BranchMetadata("28", "Sucursal Pedro Juan Caballero", "Amambay", "Pedro Juan Caballero", null),
            new BranchMetadata("29", "Agencia Pedro Juan Caballero", "Amambay", "Pedro Juan Caballero", null),
            new BranchMetadata("41", "Minga Guazu - Abasto Este", "Alto Parana", "Minga Guazu", "Abasto Este")
    );

    private static final Map<String, BranchMetadata> BRANCHES_BY_ID = KNOWN_BRANCHES.stream()
            .collect(Collectors.toUnmodifiableMap(BranchMetadata::id, branch -> branch));

    private static final Map<String, BranchMetadata> BRANCHES_BY_NORMALIZED_NAME = KNOWN_BRANCHES.stream()
            .collect(Collectors.toUnmodifiableMap(
                    branch -> normalizeName(branch.name()),
                    branch -> branch,
                    (left, right) -> left
            ));

    private ChacoBranchCatalog() {
    }

    public static List<String> knownBranchIds() {
        return KNOWN_BRANCHES.stream()
                .map(BranchMetadata::id)
                .toList();
    }

    public static List<BranchMetadata> knownBranches() {
        return KNOWN_BRANCHES;
    }

    public static BranchMetadata findById(String branchId) {
        if (branchId == null) {
            return null;
        }
        return BRANCHES_BY_ID.get(branchId.trim());
    }

    public static BranchMetadata findByName(String branchName) {
        if (branchName == null) {
            return null;
        }

        return BRANCHES_BY_NORMALIZED_NAME.get(normalizeName(branchName));
    }

    public static boolean existsId(String branchId) {
        return findById(branchId) != null;
    }

    private static String normalizeName(String value) {
        String normalized = value == null ? "" : value.trim().toLowerCase();
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        return normalized.replaceAll("\\s+", " ");
    }

    public record BranchMetadata(String id, String name, String department, String city, String neighborhood) {
    }
}
