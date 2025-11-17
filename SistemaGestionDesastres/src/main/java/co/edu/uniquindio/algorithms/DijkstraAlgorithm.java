package co.edu.uniquindio.algorithms;

import co.edu.uniquindio.structures.CustomList;
import co.edu.uniquindio.structures.CustomMap;
import co.edu.uniquindio.structures.Graph;
import co.edu.uniquindio.structures.PriorityQueue;

import java.util.Comparator;

/**
 * Implementación del algoritmo de Dijkstra para encontrar caminos más cortos
 */
public class DijkstraAlgorithm {

    /**
     * Resultado del algoritmo de Dijkstra
     */
    public static class DijkstraResult {
        private CustomMap<String, Double> distances;
        private CustomMap<String, String> previous;

        public DijkstraResult() {
            this.distances = new CustomMap<>();
            this.previous = new CustomMap<>();
        }

        public double getDistance(String vertexId) {
            Double dist = distances.get(vertexId);
            return dist != null ? dist : Double.POSITIVE_INFINITY;
        }

        public void setDistance(String vertexId, double distance) {
            distances.put(vertexId, distance);
        }

        public String getPrevious(String vertexId) {
            return previous.get(vertexId);
        }

        public void setPrevious(String vertexId, String previousId) {
            previous.put(vertexId, previousId);
        }

        /**
         * Reconstruir el camino desde el origen hasta el destino
         */
        public CustomList<String> getPath(String destination) {
            CustomList<String> path = new CustomList<>();
            String current = destination;

            while (current != null) {
                path.add(0, current);
                current = previous.get(current);
            }

            return path.isEmpty() || path.get(0) == null ? new CustomList<>() : path;
        }
    }

    /**
     * Nodo para la cola de prioridad
     */
    private static class Node {
        String id;
        double distance;

        Node(String id, double distance) {
            this.id = id;
            this.distance = distance;
        }
    }

    /**
     * Ejecutar algoritmo de Dijkstra
     */
    public static DijkstraResult findShortestPaths(Graph graph, String sourceId) {
        DijkstraResult result = new DijkstraResult();
        CustomMap<String, Boolean> visited = new CustomMap<>();

        // Inicializar distancias
        CustomList<String> allVertices = getAllVertexIds(graph);

        for (int i = 0; i < allVertices.size(); i++) {
            String vertexId = allVertices.get(i);
            result.setDistance(vertexId, Double.POSITIVE_INFINITY);
            visited.put(vertexId, false);
        }

        result.setDistance(sourceId, 0.0);

        // Cola de prioridad para procesar nodos
        PriorityQueue<Node> pq = new PriorityQueue<>(
                Comparator.comparingDouble(node -> node.distance)
        );
        pq.offer(new Node(sourceId, 0.0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            String currentId = current.id;

            if (visited.get(currentId) != null && visited.get(currentId)) {
                continue;
            }

            visited.put(currentId, true);

            // Procesar vecinos
            CustomList<String> neighbors = graph.getNeighbors(currentId);
            for (int i = 0; i < neighbors.size(); i++) {
                String neighborId = neighbors.get(i);

                if (visited.get(neighborId) != null && visited.get(neighborId)) {
                    continue;
                }

                double edgeWeight = graph.getEdgeWeight(currentId, neighborId);
                double newDistance = result.getDistance(currentId) + edgeWeight;

                if (newDistance < result.getDistance(neighborId)) {
                    result.setDistance(neighborId, newDistance);
                    result.setPrevious(neighborId, currentId);
                    pq.offer(new Node(neighborId, newDistance));
                }
            }
        }

        return result;
    }

    /**
     * Obtener todos los IDs de vértices del grafo
     */
    private static CustomList<String> getAllVertexIds(Graph graph) {
        CustomList<String> ids = new CustomList<>();
        CustomList<co.edu.uniquindio.models.Location> vertices = graph.getAllVertices();

        for (int i = 0; i < vertices.size(); i++) {
            ids.add(vertices.get(i).getId());
        }

        return ids;
    }

    /**
     * Encontrar el camino más corto entre dos ubicaciones
     */
    public static CustomList<String> findShortestPath(Graph graph, String sourceId, String destinationId) {
        DijkstraResult result = findShortestPaths(graph, sourceId);
        return result.getPath(destinationId);
    }
}
