package co.edu.uniquindio.structures;

import co.edu.uniquindio.models.Location;

/**
 * Implementación propia de un Grafo Dirigido con pesos
 * Representa las ubicaciones y rutas del sistema de desastres
 * Utiliza lista de adyacencia para almacenar las conexiones
 */
public class Graph {
    private CustomMap<String, Location> vertices;
    private CustomMap<String, CustomMap<String, Double>> adjacencyList;

    /**
     * Constructor: Inicializa el grafo vacío
     */
    public Graph() {
        this.vertices = new CustomMap<>();
        this.adjacencyList = new CustomMap<>();
    }

    /**
     * Agregar una ubicación (vértice) al grafo
     * Complejidad: O(1) promedio
     *
     * @param location La ubicación a agregar
     */
    public void addVertex(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }

        if (!vertices.containsKey(location.getId())) {
            vertices.put(location.getId(), location);
            adjacencyList.put(location.getId(), new CustomMap<>());
        }
    }

    /**
     * Agregar múltiples ubicaciones al grafo
     * Complejidad: O(n)
     */
    public void addVertices(CustomList<Location> locations) {
        for (int i = 0; i < locations.size(); i++) {
            addVertex(locations.get(i));
        }
    }

    /**
     * Remover una ubicación del grafo
     * También remueve todas las aristas asociadas
     * Complejidad: O(V) donde V es el número de vértices
     */
    public void removeVertex(String vertexId) {
        if (!vertices.containsKey(vertexId)) {
            return;
        }

        // Remover el vértice
        vertices.remove(vertexId);
        adjacencyList.remove(vertexId);

        // Remover todas las aristas que apuntan a este vértice
        CustomList<String> allVertices = vertices.keys();
        for (int i = 0; i < allVertices.size(); i++) {
            String v = allVertices.get(i);
            adjacencyList.get(v).remove(vertexId);
        }
    }

    /**
     * Agregar una arista dirigida entre dos ubicaciones
     * Complejidad: O(1) promedio
     *
     * @param fromId ID de la ubicación origen
     * @param toId ID de la ubicación destino
     * @param weight Peso de la arista (distancia, tiempo, etc.)
     */
    public void addEdge(String fromId, String toId, double weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }

        if (vertices.containsKey(fromId) && vertices.containsKey(toId)) {
            CustomMap<String, Double> edges = adjacencyList.get(fromId);
            edges.put(toId, weight);
        }
    }

    /**
     * Agregar arista bidireccional (dos aristas dirigidas)
     * Complejidad: O(1) promedio
     */
    public void addBidirectionalEdge(String vertex1, String vertex2, double weight) {
        addEdge(vertex1, vertex2, weight);
        addEdge(vertex2, vertex1, weight);
    }

    /**
     * Remover una arista
     * Complejidad: O(1) promedio
     */
    public void removeEdge(String fromId, String toId) {
        if (adjacencyList.containsKey(fromId)) {
            adjacencyList.get(fromId).remove(toId);
        }
    }

    /**
     * Obtener el peso de una arista
     * Complejidad: O(1) promedio
     *
     * @return El peso de la arista, o POSITIVE_INFINITY si no existe
     */
    public double getEdgeWeight(String fromId, String toId) {
        if (adjacencyList.containsKey(fromId)) {
            CustomMap<String, Double> edges = adjacencyList.get(fromId);
            if (edges.containsKey(toId)) {
                return edges.get(toId);
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Actualizar el peso de una arista existente
     * Complejidad: O(1) promedio
     */
    public boolean updateEdgeWeight(String fromId, String toId, double newWeight) {
        if (hasEdge(fromId, toId)) {
            addEdge(fromId, toId, newWeight);
            return true;
        }
        return false;
    }

    /**
     * Obtener una ubicación por ID
     * Complejidad: O(1) promedio
     */
    public Location getVertex(String id) {
        return vertices.get(id);
    }

    /**
     * Obtener todos los vértices del grafo
     * Complejidad: O(V)
     */
    public CustomList<Location> getAllVertices() {
        return vertices.values();
    }

    /**
     * Obtener todos los IDs de vértices
     * Complejidad: O(V)
     */
    public CustomList<String> getVertexIds() {
        return vertices.keys();
    }

    /**
     * Obtener los vecinos (sucesores) de un vértice
     * Complejidad: O(1) promedio para acceder, O(E) para iterar
     */
    public CustomList<String> getNeighbors(String vertexId) {
        if (adjacencyList.containsKey(vertexId)) {
            return adjacencyList.get(vertexId).keys();
        }
        return new CustomList<>();
    }

    /**
     * Obtener los vecinos con sus pesos
     * Complejidad: O(E) donde E es el número de aristas del vértice
     */
    public CustomMap<String, Double> getNeighborsWithWeights(String vertexId) {
        if (adjacencyList.containsKey(vertexId)) {
            return adjacencyList.get(vertexId);
        }
        return new CustomMap<>();
    }

    /**
     * Obtener el grado de salida de un vértice (número de aristas salientes)
     * Complejidad: O(1)
     */
    public int getOutDegree(String vertexId) {
        if (adjacencyList.containsKey(vertexId)) {
            return adjacencyList.get(vertexId).size();
        }
        return 0;
    }

    /**
     * Obtener el grado de entrada de un vértice (número de aristas entrantes)
     * Complejidad: O(V * E)
     */
    public int getInDegree(String vertexId) {
        int count = 0;
        CustomList<String> allVertices = vertices.keys();

        for (int i = 0; i < allVertices.size(); i++) {
            String v = allVertices.get(i);
            if (adjacencyList.get(v).containsKey(vertexId)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Verificar si existe una arista entre dos vértices
     * Complejidad: O(1) promedio
     */
    public boolean hasEdge(String fromId, String toId) {
        if (adjacencyList.containsKey(fromId)) {
            return adjacencyList.get(fromId).containsKey(toId);
        }
        return false;
    }

    /**
     * Verificar si el grafo contiene un vértice
     * Complejidad: O(1) promedio
     */
    public boolean containsVertex(String id) {
        return vertices.containsKey(id);
    }

    /**
     * Obtener el número de vértices
     * Complejidad: O(1)
     */
    public int getVertexCount() {
        return vertices.size();
    }

    /**
     * Obtener el número total de aristas
     * Complejidad: O(V)
     */
    public int getEdgeCount() {
        int count = 0;
        CustomList<String> allVertices = vertices.keys();

        for (int i = 0; i < allVertices.size(); i++) {
            count += adjacencyList.get(allVertices.get(i)).size();
        }

        return count;
    }

    /**
     * Verificar si el grafo está vacío
     * Complejidad: O(1)
     */
    public boolean isEmpty() {
        return vertices.isEmpty();
    }

    /**
     * Limpiar el grafo (remover todos los vértices y aristas)
     * Complejidad: O(1)
     */
    public void clear() {
        vertices.clear();
        adjacencyList.clear();
    }

    /**
     * Obtener una representación en String del grafo
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph with ").append(getVertexCount()).append(" vertices and ")
                .append(getEdgeCount()).append(" edges:\n");

        CustomList<String> vertexIds = vertices.keys();
        for (int i = 0; i < vertexIds.size(); i++) {
            String vId = vertexIds.get(i);
            Location loc = vertices.get(vId);
            sb.append("  ").append(loc.getName()).append(" -> ");

            CustomList<String> neighbors = getNeighbors(vId);
            if (neighbors.isEmpty()) {
                sb.append("[]");
            } else {
                sb.append("[");
                for (int j = 0; j < neighbors.size(); j++) {
                    String nId = neighbors.get(j);
                    Location neighbor = vertices.get(nId);
                    double weight = getEdgeWeight(vId, nId);
                    sb.append(neighbor.getName()).append("(").append(weight).append(")");
                    if (j < neighbors.size() - 1) {
                        sb.append(", ");
                    }
                }
                sb.append("]");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Verificar si existe un camino entre dos vértices (usando BFS)
     * Complejidad: O(V + E)
     */
    public boolean hasPath(String fromId, String toId) {
        if (!containsVertex(fromId) || !containsVertex(toId)) {
            return false;
        }

        if (fromId.equals(toId)) {
            return true;
        }

        CustomMap<String, Boolean> visited = new CustomMap<>();
        CustomList<String> queue = new CustomList<>();

        queue.add(fromId);
        visited.put(fromId, true);

        while (!queue.isEmpty()) {
            String current = queue.remove(0);
            CustomList<String> neighbors = getNeighbors(current);

            for (int i = 0; i < neighbors.size(); i++) {
                String neighbor = neighbors.get(i);

                if (neighbor.equals(toId)) {
                    return true;
                }

                if (visited.get(neighbor) == null) {
                    visited.put(neighbor, true);
                    queue.add(neighbor);
                }
            }
        }

        return false;
    }
}
