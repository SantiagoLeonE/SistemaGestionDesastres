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

    // Método auxiliar para assertions
    private static void assertCondition(boolean condition, String description) {
        if (condition) {
            System.out.println("  ✓ " + description);
        } else {
            throw new AssertionError("Fallo: " + description);
        }
    }
}
