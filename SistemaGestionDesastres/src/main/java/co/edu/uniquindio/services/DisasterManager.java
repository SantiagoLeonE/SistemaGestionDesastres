package co.edu.uniquindio.services;

import co.edu.uniquindio.models.*;
import co.edu.uniquindio.structures.*;
import co.edu.uniquindio.algorithms.DijkstraAlgorithm;
import java.util.Comparator;

/**
 * Gestor principal del sistema de gestión de desastres
 * Coordina todas las operaciones del sistema: ubicaciones, recursos, equipos y rutas
 */
public class DisasterManager {
    private Graph locationGraph;
    private CustomMap<String, Resource> resources;
    private CustomMap<String, RescueTeam> rescueTeams;
    private DistributionTree distributionTree;
    private User currentUser;
    private CustomList<String> operationLog;

    /**
     * Constructor: Inicializa todas las estructuras del sistema
     */
    public DisasterManager() {
        this.locationGraph = new Graph();
        this.resources = new CustomMap<>();
        this.rescueTeams = new CustomMap<>();
        this.distributionTree = null;
        this.currentUser = null;
        this.operationLog = new CustomList<>();
        logOperation("Sistema de Gestión de Desastres iniciado");
    }

    // ==================== GESTIÓN DE UBICACIONES ====================

    /**
     * Agregar una ubicación al sistema
     *
     * @param location Ubicación a agregar
     * @return true si se agregó exitosamente
     */
    public boolean addLocation(Location location) {
        if (location == null) {
            return false;
        }

        try {
            locationGraph.addVertex(location);

            // Inicializar árbol de distribución con la primera ubicación
            if (distributionTree == null) {
                distributionTree = new DistributionTree(location.getId());
            } else {
                // Agregar al árbol de distribución (como hijo de la raíz por defecto)
                distributionTree.addChild(distributionTree.getRootLocationId(), location.getId());
            }

            logOperation("Ubicación agregada: " + location.getName());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Remover una ubicación del sistema
     *
     * @param locationId ID de la ubicación a remover
     * @return true si se removió exitosamente
     */
    public boolean removeLocation(String locationId) {
        if (locationId == null || !locationGraph.containsVertex(locationId)) {
            return false;
        }

        Location location = locationGraph.getVertex(locationId);
        locationGraph.removeVertex(locationId);

        if (distributionTree != null) {
            distributionTree.removeLocation(locationId);
        }

        logOperation("Ubicación removida: " + location.getName());
        return true;
    }

    /**
     * Actualizar información de una ubicación
     *
     * @param locationId ID de la ubicación
     * @param updatedLocation Información actualizada
     * @return true si se actualizó exitosamente
     */
    public boolean updateLocation(String locationId, Location updatedLocation) {
        if (locationId == null || updatedLocation == null) {
            return false;
        }

        if (locationGraph.containsVertex(locationId)) {
            removeLocation(locationId);
            addLocation(updatedLocation);
            logOperation("Ubicación actualizada: " + updatedLocation.getName());
            return true;
        }

        return false;
    }

    /**
     * Obtener una ubicación por ID
     *
     * @param id ID de la ubicación
     * @return Ubicación encontrada o null
     */
    public Location getLocation(String id) {
        return locationGraph.getVertex(id);
    }

    /**
     * Obtener todas las ubicaciones del sistema
     *
     * @return Lista de ubicaciones
     */
    public CustomList<Location> getAllLocations() {
        return locationGraph.getAllVertices();
    }

    /**
     * Obtener ubicaciones por tipo
     *
     * @param type Tipo de ubicación a filtrar
     * @return Lista de ubicaciones del tipo especificado
     */
    public CustomList<Location> getLocationsByType(Location.LocationType type) {
        CustomList<Location> filtered = new CustomList<>();
        CustomList<Location> all = getAllLocations();

        for (int i = 0; i < all.size(); i++) {
            Location loc = all.get(i);
            if (loc.getType() == type) {
                filtered.add(loc);
            }
        }

        return filtered;
    }

    /**
     * Obtener ubicaciones por nivel de urgencia
     *
     * @param urgencyLevel Nivel de urgencia (1-5)
     * @return Lista de ubicaciones con ese nivel de urgencia
     */
    public CustomList<Location> getLocationsByUrgency(int urgencyLevel) {
        CustomList<Location> filtered = new CustomList<>();
        CustomList<Location> all = getAllLocations();

        for (int i = 0; i < all.size(); i++) {
            Location loc = all.get(i);
            if (loc.getUrgencyLevel() == urgencyLevel) {
                filtered.add(loc);
            }
        }

        return filtered;
    }

    /**
     * Obtener ubicaciones en crisis (urgencia >= 4)
     */
    public CustomList<Location> getCriticalLocations() {
        CustomList<Location> critical = new CustomList<>();
        CustomList<Location> all = getAllLocations();

        for (int i = 0; i < all.size(); i++) {
            Location loc = all.get(i);
            if (loc.isInCrisis()) {
                critical.add(loc);
            }
        }

        return critical;
    }

    // ==================== GESTIÓN DE RUTAS ====================

    /**
     * Agregar una ruta entre dos ubicaciones
     *
     * @param fromId ID de ubicación origen
     * @param toId ID de ubicación destino
     * @param distance Distancia en kilómetros
     * @return true si se agregó exitosamente
     */
    public boolean addRoute(String fromId, String toId, double distance) {
        if (fromId == null || toId == null || distance < 0) {
            return false;
        }

        if (locationGraph.containsVertex(fromId) && locationGraph.containsVertex(toId)) {
            locationGraph.addEdge(fromId, toId, distance);

            Location from = locationGraph.getVertex(fromId);
            Location to = locationGraph.getVertex(toId);
            logOperation("Ruta agregada: " + from.getName() + " -> " + to.getName() +
                    " (" + distance + " km)");
            return true;
        }

        return false;
    }

    /**
     * Agregar ruta bidireccional
     *
     * @param location1Id ID de primera ubicación
     * @param location2Id ID de segunda ubicación
     * @param distance Distancia en kilómetros
     * @return true si se agregó exitosamente
     */
    public boolean addBidirectionalRoute(String location1Id, String location2Id, double distance) {
        boolean success1 = addRoute(location1Id, location2Id, distance);
        boolean success2 = addRoute(location2Id, location1Id, distance);
        return success1 && success2;
    }

    /**
     * Remover una ruta
     *
     * @param fromId ID de ubicación origen
     * @param toId ID de ubicación destino
     * @return true si se removió exitosamente
     */
    public boolean removeRoute(String fromId, String toId) {
        if (locationGraph.hasEdge(fromId, toId)) {
            locationGraph.removeEdge(fromId, toId);
            logOperation("Ruta removida: " + fromId + " -> " + toId);
            return true;
        }
        return false;
    }

    /**
     * Obtener ubicaciones vecinas (conectadas directamente)
     *
     * @param locationId ID de la ubicación
     * @return Lista de IDs de ubicaciones vecinas
     */
    public CustomList<String> getNeighborLocations(String locationId) {
        return locationGraph.getNeighbors(locationId);
    }

    /**
     * Verificar si existe una ruta entre dos ubicaciones
     */
    public boolean hasRoute(String fromId, String toId) {
        return locationGraph.hasEdge(fromId, toId);
    }

    /**
     * Obtener distancia de una ruta específica
     */
    public double getRouteDistance(String fromId, String toId) {
        DijkstraAlgorithm.DijkstraResult result =
                DijkstraAlgorithm.findShortestPaths(locationGraph, fromId);
        return result.getDistance(toId);
    }

    // ==================== ALGORITMOS DE RUTAS ====================

    /**
     * Encontrar la ruta más corta entre dos ubicaciones
     * Usa el algoritmo de Dijkstra
     *
     * @param fromId Ubicación origen
     * @param toId Ubicación destino
     * @return Lista de IDs de ubicaciones en la ruta más corta
     */
    public CustomList<String> findShortestRoute(String fromId, String toId) {
        return DijkstraAlgorithm.findShortestPath(locationGraph, fromId, toId);
    }

    /**
     * Calcular todas las rutas más cortas desde una ubicación
     *
     * @param fromId Ubicación origen
     * @return Resultado con todas las distancias y caminos
     */
    public DijkstraAlgorithm.DijkstraResult calculateAllRoutes(String fromId) {
        return DijkstraAlgorithm.findShortestPaths(locationGraph, fromId);
    }

    /**
     * Encontrar la ubicación más cercana de un tipo específico
     *
     * @param fromId Ubicación de origen
     * @param type Tipo de ubicación buscada
     * @return ID de la ubicación más cercana o null
     */
    public String findNearestLocationOfType(String fromId, Location.LocationType type) {
        CustomList<Location> locations = getLocationsByType(type);

        if (locations.isEmpty()) {
            return null;
        }

        DijkstraAlgorithm.DijkstraResult routes = calculateAllRoutes(fromId);

        String nearest = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (int i = 0; i < locations.size(); i++) {
            String locId = locations.get(i).getId();
            double distance = routes.getDistance(locId);

            if (distance < minDistance) {
                minDistance = distance;
                nearest = locId;
            }
        }

        return nearest;
    }

    // ==================== GESTIÓN DE RECURSOS ====================

    /**
     * Agregar un recurso al inventario
     *
     * @param resource Recurso a agregar
     * @return true si se agregó exitosamente
     */
    public boolean addResource(Resource resource) {
        if (resource == null) {
            return false;
        }

        resources.put(resource.getId(), resource);
        logOperation("Recurso agregado: " + resource.getName() + " (" +
                resource.getQuantity() + " " + resource.getUnit() + ")");
        return true;
    }

    /**
     * Remover un recurso del inventario
     *
     * @param resourceId ID del recurso
     * @return true si se removió exitosamente
     */
    public boolean removeResource(String resourceId) {
        if (resourceId == null) {
            return false;
        }

        Resource resource = resources.remove(resourceId);
        if (resource != null) {
            logOperation("Recurso removido: " + resource.getName());
            return true;
        }
        return false;
    }

    /**
     * Obtener un recurso por ID
     *
     * @param id ID del recurso
     * @return Recurso encontrado o null
     */
    public Resource getResource(String id) {
        return resources.get(id);
    }

    /**
     * Obtener todos los recursos del sistema
     *
     * @return Lista de recursos
     */
    public CustomList<Resource> getAllResources() {
        return resources.values();
    }

    /**
     * Obtener recursos por tipo
     *
     * @param type Tipo de recurso
     * @return Lista de recursos del tipo especificado
     */
    public CustomList<Resource> getResourcesByType(Resource.ResourceType type) {
        CustomList<Resource> filtered = new CustomList<>();
        CustomList<Resource> all = getAllResources();

        for (int i = 0; i < all.size(); i++) {
            Resource res = all.get(i);
            if (res.getType() == type) {
                filtered.add(res);
            }
        }

        return filtered;
    }

    /**
     * Obtener recursos con stock bajo
     */
    public CustomList<Resource> getLowStockResources() {
        CustomList<Resource> lowStock = new CustomList<>();
        CustomList<Resource> all = getAllResources();

        for (int i = 0; i < all.size(); i++) {
            Resource res = all.get(i);
            if (res.isLowStock()) {
                lowStock.add(res);
            }
        }

        return lowStock;
    }

    /**
     * Distribuir un recurso a una ubicación
     *
     * @param resourceId ID del recurso
     * @param locationId ID de la ubicación
     * @param quantity Cantidad a distribuir
     * @return true si se distribuyó exitosamente
     */
    public boolean distributeResource(String resourceId, String locationId, int quantity) {
        Resource resource = resources.get(resourceId);

        if (resource == null || !locationGraph.containsVertex(locationId)) {
            return false;
        }

        if (resource.reduceQuantity(quantity)) {
            // Crear copia del recurso para la ubicación
            Resource distributed = new Resource(
                    resourceId + "_" + locationId,
                    resource.getName(),
                    resource.getType(),
                    quantity,
                    resource.getUnit()
            );

            if (distributionTree != null) {
                distributionTree.assignResources(locationId, distributed);
            }

            Location location = locationGraph.getVertex(locationId);
            logOperation("Recurso distribuido: " + resource.getName() + " (" + quantity + " " +
                    resource.getUnit() + ") a " + location.getName());
            return true;
        }

        return false;
    }

    /**
     * Obtener recursos asignados a una ubicación
     *
     * @param locationId ID de la ubicación
     * @return Lista de recursos asignados
     */
    public CustomList<Resource> getLocationResources(String locationId) {
        if (distributionTree == null) {
            return new CustomList<>();
        }
        return distributionTree.getResources(locationId);
    }

    // ==================== GESTIÓN DE EQUIPOS DE RESCATE ====================

    /**
     * Agregar un equipo de rescate
     *
     * @param team Equipo a agregar
     * @return true si se agregó exitosamente
     */
    public boolean addRescueTeam(RescueTeam team) {
        if (team == null) {
            return false;
        }

        rescueTeams.put(team.getId(), team);
        logOperation("Equipo agregado: " + team.getName() + " (" + team.getType() + ")");
        return true;
    }

    /**
     * Remover un equipo de rescate
     *
     * @param teamId ID del equipo
     * @return true si se removió exitosamente
     */
    public boolean removeRescueTeam(String teamId) {
        if (teamId == null) {
            return false;
        }

        RescueTeam team = rescueTeams.remove(teamId);
        if (team != null) {
            logOperation("Equipo removido: " + team.getName());
            return true;
        }
        return false;
    }

    /**
     * Obtener un equipo por ID
     *
     * @param id ID del equipo
     * @return Equipo encontrado o null
     */
    public RescueTeam getRescueTeam(String id) {
        return rescueTeams.get(id);
    }

    /**
     * Obtener todos los equipos de rescate
     *
     * @return Lista de equipos
     */
    public CustomList<RescueTeam> getAllRescueTeams() {
        return rescueTeams.values();
    }

    /**
     * Obtener equipos por tipo
     *
     * @param type Tipo de equipo
     * @return Lista de equipos del tipo especificado
     */
    public CustomList<RescueTeam> getTeamsByType(RescueTeam.TeamType type) {
        CustomList<RescueTeam> filtered = new CustomList<>();
        CustomList<RescueTeam> all = getAllRescueTeams();

        for (int i = 0; i < all.size(); i++) {
            RescueTeam team = all.get(i);
            if (team.getType() == type) {
                filtered.add(team);
            }
        }

        return filtered;
    }

    /**
     * Obtener equipos disponibles para asignación
     *
     * @return Lista de equipos disponibles
     */
    public CustomList<RescueTeam> getAvailableTeams() {
        CustomList<RescueTeam> available = new CustomList<>();
        CustomList<RescueTeam> all = getAllRescueTeams();

        for (int i = 0; i < all.size(); i++) {
            RescueTeam team = all.get(i);
            if (team.isAvailable()) {
                available.add(team);
            }
        }

        return available;
    }

    /**
     * Asignar un equipo a una ubicación
     *
     * @param teamId ID del equipo
     * @param locationId ID de la ubicación
     * @return true si se asignó exitosamente
     */
    public boolean assignTeamToLocation(String teamId, String locationId) {
        RescueTeam team = rescueTeams.get(teamId);

        if (team == null || !locationGraph.containsVertex(locationId)) {
            return false;
        }

        if (team.assignToLocation(locationId)) {
            Location location = locationGraph.getVertex(locationId);
            logOperation("Equipo asignado: " + team.getName() + " a " + location.getName());
            return true;
        }

        return false;
    }

    /**
     * Desasignar un equipo de su ubicación actual
     *
     * @param teamId ID del equipo
     * @return true si se desasignó exitosamente
     */
    public boolean unassignTeam(String teamId) {
        RescueTeam team = rescueTeams.get(teamId);

        if (team != null && team.hasAssignment()) {
            String locationId = team.getAssignedLocationId();
            team.unassign();
            logOperation("Equipo desasignado: " + team.getName());
            return true;
        }

        return false;
    }

    /**
     * Obtener equipos asignados a una ubicación
     *
     * @param locationId ID de la ubicación
     * @return Lista de equipos asignados
     */
    public CustomList<RescueTeam> getTeamsAtLocation(String locationId) {
        CustomList<RescueTeam> teamsAtLocation = new CustomList<>();
        CustomList<RescueTeam> all = getAllRescueTeams();

        for (int i = 0; i < all.size(); i++) {
            RescueTeam team = all.get(i);
            if (team.hasAssignment() && team.getAssignedLocationId().equals(locationId)) {
                teamsAtLocation.add(team);
            }
        }

        return teamsAtLocation;
    }
}
