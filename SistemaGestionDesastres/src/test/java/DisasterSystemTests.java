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
 * Pruebas unitarias para el sistema de gestión de desastres
 */
public class DisasterSystemTests {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║    PRUEBAS UNITARIAS - SISTEMA DE GESTIÓN DE DESASTRES        ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");

        // Ejecutar todas las pruebas
        testCustomList();
        testCustomMap();
        testPriorityQueue();
        testGraph();
        testDijkstraAlgorithm();
        testDisasterManager();
        testAuthenticationService();

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

    // ========== PRUEBA 3: PriorityQueue ==========
    private static void testPriorityQueue() {
        System.out.println("Prueba 3: PriorityQueue");
        System.out.println("-".repeat(65));

        try {
            PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> a - b);

            // Agregar elementos
            pq.offer(5);
            pq.offer(2);
            pq.offer(8);
            pq.offer(1);
            pq.offer(3);

            assertCondition(pq.size() == 5, "Tamaño después de agregar 5 elementos");

            // Verificar orden (min-heap)
            assertCondition(pq.peek() == 1, "Peek devuelve el mínimo");
            assertCondition(pq.poll() == 1, "Poll devuelve 1");
            assertCondition(pq.poll() == 2, "Poll devuelve 2");
            assertCondition(pq.poll() == 3, "Poll devuelve 3");
            assertCondition(pq.size() == 2, "Tamaño después de 3 polls");

            System.out.println("✓ Prueba PriorityQueue PASADA\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Prueba PriorityQueue FALLIDA: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // ========== PRUEBA 4: Graph ==========
    private static void testGraph() {
        System.out.println("Prueba 4: Graph");
        System.out.println("-".repeat(65));

        try {
            Graph graph = new Graph();

            // Agregar vértices
            Location loc1 = new Location("L1", "Loc1", Location.LocationType.CITY, 1000, 3);
            Location loc2 = new Location("L2", "Loc2", Location.LocationType.SHELTER, 500, 2);
            Location loc3 = new Location("L3", "Loc3", Location.LocationType.HOSPITAL, 200, 4);

            graph.addVertex(loc1);
            graph.addVertex(loc2);
            graph.addVertex(loc3);

            assertCondition(graph.getVertexCount() == 3, "Número de vértices");

            // Agregar aristas
            graph.addEdge("L1", "L2", 5.0);
            graph.addEdge("L2", "L3", 3.0);
            graph.addEdge("L1", "L3", 10.0);

            assertCondition(graph.hasEdge("L1", "L2"), "Existe arista L1->L2");
            assertCondition(graph.getEdgeWeight("L1", "L2") == 5.0, "Peso de arista L1->L2");

            // Verificar vecinos
            CustomList<String> neighbors = graph.getNeighbors("L1");
            assertCondition(neighbors.size() == 2, "L1 tiene 2 vecinos");

            System.out.println("✓ Prueba Graph PASADA\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Prueba Graph FALLIDA: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // ========== PRUEBA 5: Algoritmo de Dijkstra ==========
    private static void testDijkstraAlgorithm() {
        System.out.println("Prueba 5: Algoritmo de Dijkstra");
        System.out.println("-".repeat(65));

        try {
            Graph graph = new Graph();

            // Crear grafo de prueba
            graph.addVertex(new Location("A", "A", Location.LocationType.CITY, 100, 1));
            graph.addVertex(new Location("B", "B", Location.LocationType.CITY, 100, 1));
            graph.addVertex(new Location("C", "C", Location.LocationType.CITY, 100, 1));
            graph.addVertex(new Location("D", "D", Location.LocationType.CITY, 100, 1));

            graph.addEdge("A", "B", 4.0);
            graph.addEdge("A", "C", 2.0);
            graph.addEdge("B", "D", 5.0);
            graph.addEdge("C", "B", 1.0);
            graph.addEdge("C", "D", 8.0);

            // Calcular caminos más cortos desde A
            DijkstraAlgorithm.DijkstraResult result =
                    DijkstraAlgorithm.findShortestPaths(graph, "A");

            assertCondition(result.getDistance("A") == 0.0, "Distancia a A es 0");
            assertCondition(result.getDistance("B") == 3.0, "Distancia a B es 3");
            assertCondition(result.getDistance("C") == 2.0, "Distancia a C es 2");
            assertCondition(result.getDistance("D") == 8.0, "Distancia a D es 8");

            // Verificar camino
            CustomList<String> path = result.getPath("D");
            assertCondition(path.size() == 4, "Camino A->D tiene 4 nodos");
            assertCondition(path.get(0).equals("A"), "Camino inicia en A");
            assertCondition(path.get(path.size() - 1).equals("D"), "Camino termina en D");

            System.out.println("✓ Prueba Dijkstra PASADA\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Prueba Dijkstra FALLIDA: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // ========== PRUEBA 6: DisasterManager ==========
    private static void testDisasterManager() {
        System.out.println("Prueba 6: DisasterManager");
        System.out.println("-".repeat(65));

        try {
            DisasterManager manager = new DisasterManager();

            // Agregar ubicaciones
            Location loc1 = new Location("L1", "Ciudad", Location.LocationType.CITY, 5000, 5);
            Location loc2 = new Location("L2", "Refugio", Location.LocationType.SHELTER, 0, 2);
            manager.addLocation(loc1);
            manager.addLocation(loc2);

            assertCondition(manager.getAllLocations().size() == 2,
                    "Número de ubicaciones agregadas");

            // Agregar recursos
            Resource res = new Resource("R1", "Agua", Resource.ResourceType.WATER, 1000, "L");
            manager.addResource(res);

            assertCondition(manager.getAllResources().size() == 1, "Recurso agregado");

            // Agregar equipo
            RescueTeam team = new RescueTeam("T1", "Médicos", RescueTeam.TeamType.MEDICAL, 5);
            manager.addRescueTeam(team);

            assertCondition(manager.getAllRescueTeams().size() == 1, "Equipo agregado");

            // Asignar equipo
            manager.assignTeamToLocation("T1", "L1");
            RescueTeam retrieved = manager.getRescueTeam("T1");
            assertCondition(retrieved.getAssignedLocationId().equals("L1"),
                    "Equipo asignado correctamente");

            // Priorizar evacuaciones
            CustomList<Location> prioritized = manager.prioritizeEvacuations();
            assertCondition(prioritized.get(0).getUrgencyLevel() >=
                            prioritized.get(1).getUrgencyLevel(),
                    "Evacuaciones priorizadas correctamente");

            System.out.println("✓ Prueba DisasterManager PASADA\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Prueba DisasterManager FALLIDA: " + e.getMessage() + "\n");
            testsFailed++;
        }
    }

    // ========== PRUEBA 7: AuthenticationService ==========
    private static void testAuthenticationService() {
        System.out.println("Prueba 7: AuthenticationService");
        System.out.println("-".repeat(65));

        try {
            AuthenticationService auth = new AuthenticationService();

            // Autenticación exitosa
            User user = auth.authenticate("admin", "admin123");
            assertCondition(user != null, "Autenticación exitosa con credenciales válidas");
            assertCondition(user.isAdministrator(), "Usuario es administrador");

            // Autenticación fallida
            User failed = auth.authenticate("admin", "wrongpassword");
            assertCondition(failed == null, "Autenticación falla con password incorrecto");

            // Verificar usuario actual
            assertCondition(auth.isAuthenticated(), "Sesión activa");
            assertCondition(auth.getCurrentUser().getUsername().equals("admin"),
                    "Usuario actual correcto");

            // Cerrar sesión
            auth.logout();
            assertCondition(!auth.isAuthenticated(), "Sesión cerrada");
            assertCondition(auth.getCurrentUser() == null, "No hay usuario actual");

            System.out.println("✓ Prueba AuthenticationService PASADA\n");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("✗ Prueba AuthenticationService FALLIDA: " + e.getMessage() + "\n");
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
