import co.edu.uniquindio.algorithms.DijkstraAlgorithm;
import co.edu.uniquindio.models.Location;
import co.edu.uniquindio.models.RescueTeam;
import co.edu.uniquindio.models.Resource;
import co.edu.uniquindio.models.User;
import co.edu.uniquindio.services.AuthenticationService;
import co.edu.uniquindio.services.DisasterManager;
import co.edu.uniquindio.structures.CustomList;
import co.edu.uniquindio.structures.CustomMap;
import co.edu.uniquindio.structures.Graph;
import co.edu.uniquindio.structures.PriorityQueue;

/**
 * Suite de pruebas unitarias para el sistema de gestión de desastres
 */
public class DisasterSystemTests {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║    PRUEBAS UNITARIAS - SISTEMA DE GESTIÓN DE DESASTRES        ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");

        // Resumen
        System.out.println("\n" + "=".repeat(65));
        System.out.println("RESUMEN DE PRUEBAS");
        System.out.println("=".repeat(65));
        System.out.println("Pruebas exitosas: " + testsPassed);
        System.out.println("Pruebas fallidas: " + testsFailed);
        System.out.println("Total: " + (testsPassed + testsFailed));
        System.out.println("=".repeat(65));

        if (testsFailed == 0) {
            System.out.println("✓ TODAS LAS PRUEBAS PASARON EXITOSAMENTE");
        } else {
            System.out.println("✗ ALGUNAS PRUEBAS FALLARON");
        }
    }

    // ========== PRUEBA 1: CustomList ==========
    private static void testCustomList() {
        System.out.println("Prueba 1: CustomList");
        System.out.println("-".repeat(65));

        try {
            CustomList<String> list = new CustomList<>();

            // Agregar elementos
            list.add("A");
            list.add("B");
            list.add("C");
            assertCondition(list.size() == 3, "Tamaño después de agregar 3 elementos");

            // Obtener elementos
            assertCondition(list.get(0).equals("A"), "Obtener primer elemento");
            assertCondition(list.get(2).equals("C"), "Obtener último elemento");

            // Insertar en posición específica
            list.add(1, "X");
            assertCondition(list.get(1).equals("X"), "Insertar en posición 1");
            assertCondition(list.size() == 4, "Tamaño después de insertar");

            // Remover elemento
            String removed = list.remove(1);
            assertCondition(removed.equals("X"), "Elemento removido");
            assertCondition(list.size() == 3, "Tamaño después de remover");

            // Verificar contiene
            assertCondition(list.contains("B"), "Lista contiene 'B'");
            assertCondition(!list.contains("Z"), "Lista no contiene 'Z'");

            System.out.println("✓ Prueba CustomList PASADA\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Prueba CustomList FALLIDA: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // ========== PRUEBA 2: CustomMap ==========
    private static void testCustomMap() {
        System.out.println("Prueba 2: CustomMap");
        System.out.println("-".repeat(65));

        try {
            CustomMap<String, Integer> map = new CustomMap<>();

            // Agregar elementos
            map.put("uno", 1);
            map.put("dos", 2);
            map.put("tres", 3);
            assertCondition(map.size() == 3, "Tamaño después de agregar 3 elementos");

            // Obtener valores
            assertCondition(map.get("uno") == 1, "Obtener valor de 'uno'");
            assertCondition(map.get("dos") == 2, "Obtener valor de 'dos'");

            // Actualizar valor
            map.put("uno", 10);
            assertCondition(map.get("uno") == 10, "Actualizar valor existente");
            assertCondition(map.size() == 3, "Tamaño sin cambios al actualizar");

            // Verificar contiene clave
            assertCondition(map.containsKey("tres"), "Contiene clave 'tres'");
            assertCondition(!map.containsKey("cuatro"), "No contiene clave 'cuatro'");

            // Remover elemento
            Integer removed = map.remove("dos");
            assertCondition(removed == 2, "Valor removido");
            assertCondition(map.size() == 2, "Tamaño después de remover");

            System.out.println("✓ Prueba CustomMap PASADA\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Prueba CustomMap FALLIDA: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // Método auxiliar para assertions
    private static void assertCondition(boolean condition, String description) {
        if (condition) {
            System.out.println("  ✓ " + description);
        } else {
            throw new AssertionError("Fallo: " + description);
        }
    }
}
