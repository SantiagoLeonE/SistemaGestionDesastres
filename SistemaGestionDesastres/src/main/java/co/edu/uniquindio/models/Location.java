package co.edu.uniquindio.models;

/**
 * Modelo para representar una ubicación afectada por el desastre
 * Incluye información sobre tipo, población, urgencia y coordenadas geográficas
 */
public class Location {
    private String id;
    private String name;
    private LocationType type;
    private int population;
    private int urgencyLevel; // 1-5, donde 5 es más urgente
    private double latitude;
    private double longitude;
    private String description;
    private boolean isEvacuated;

    /**
     * Enum para los tipos de ubicaciones en el sistema
     */
    public enum LocationType {
        CITY("Ciudad"),
        SHELTER("Refugio"),
        AID_CENTER("Centro de Ayuda"),
        HOSPITAL("Hospital"),
        AFFECTED_ZONE("Zona Afectada");

        private final String displayName;

        LocationType(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    /**
     * Constructor principal
     *
     * @param id Identificador único de la ubicación
     * @param name Nombre de la ubicación
     * @param type Tipo de ubicación
     * @param population Población en la ubicación
     * @param urgencyLevel Nivel de urgencia (1-5)
     */
    public Location(String id, String name, LocationType type, int population, int urgencyLevel) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }

        this.id = id;
        this.name = name;
        this.type = type;
        this.population = Math.max(0, population);
        this.urgencyLevel = Math.max(1, Math.min(5, urgencyLevel));
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.description = "";
        this.isEvacuated = false;
    }

    /**
     * Constructor con coordenadas geográficas
     */
    public Location(String id, String name, LocationType type, int population,
                    int urgencyLevel, double latitude, double longitude) {
        this(id, name, type, population, urgencyLevel);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Constructor completo con todos los parámetros
     */
    public Location(String id, String name, LocationType type, int population,
                    int urgencyLevel, double latitude, double longitude, String description) {
        this(id, name, type, population, urgencyLevel, latitude, longitude);
        this.description = description != null ? description : "";
    }

    // ==================== GETTERS ====================

    /**
     * Obtener el ID de la ubicación
     */
    public String getId() {
        return id;
    }

    /**
     * Obtener el nombre de la ubicación
     */
    public String getName() {
        return name;
    }

    /**
     * Obtener el tipo de ubicación
     */
    public LocationType getType() {
        return type;
    }

    /**
     * Obtener la población actual
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Obtener el nivel de urgencia (1-5)
     */
    public int getUrgencyLevel() {
        return urgencyLevel;
    }

    /**
     * Obtener la latitud
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Obtener la longitud
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Obtener la descripción
     */
    public String getDescription() {
        return description;
    }

    /**
     * Verificar si la ubicación está evacuada
     */
    public boolean isEvacuated() {
        return isEvacuated;
    }

    // ==================== SETTERS ====================

    /**
     * Establecer el nombre de la ubicación
     */
    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
    }

    /**
     * Establecer la población
     * No permite valores negativos
     */
    public void setPopulation(int population) {
        this.population = Math.max(0, population);
    }

    /**
     * Establecer el nivel de urgencia
     * Asegura que esté en el rango 1-5
     */
    public void setUrgencyLevel(int urgencyLevel) {
        this.urgencyLevel = Math.max(1, Math.min(5, urgencyLevel));
    }

    /**
     * Establecer la latitud
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Establecer la longitud
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Establecer las coordenadas completas
     */
    public void setCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Establecer la descripción
     */
    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    /**
     * Establecer el tipo de ubicación
     */
    public void setType(LocationType type) {
        if (type != null) {
            this.type = type;
        }
    }

    /**
     * Marcar la ubicación como evacuada o no evacuada
     */
    public void setEvacuated(boolean isEvacuated) {
        this.isEvacuated = isEvacuated;
    }

    // ==================== MÉTODOS DE UTILIDAD ====================

    /**
     * Incrementar el nivel de urgencia
     * No excede el máximo de 5
     */
    public void increaseUrgency() {
        if (urgencyLevel < 5) {
            urgencyLevel++;
        }
    }

    /**
     * Decrementar el nivel de urgencia
     * No baja del mínimo de 1
     */
    public void decreaseUrgency() {
        if (urgencyLevel > 1) {
            urgencyLevel--;
        }
    }

    /**
     * Agregar población a la ubicación
     */
    public void addPopulation(int amount) {
        if (amount > 0) {
            this.population += amount;
        }
    }

    /**
     * Reducir población de la ubicación
     */
    public void reducePopulation(int amount) {
        if (amount > 0) {
            this.population = Math.max(0, this.population - amount);
        }
    }

    /**
     * Verificar si la ubicación está en crisis (urgencia >= 4)
     */
    public boolean isInCrisis() {
        return urgencyLevel >= 4;
    }

    /**
     * Verificar si la ubicación está segura (urgencia <= 2)
     */
    public boolean isSafe() {
        return urgencyLevel <= 2;
    }

    /**
     * Obtener descripción del nivel de urgencia
     */
    public String getUrgencyDescription() {
        switch (urgencyLevel) {
            case 5: return "CRÍTICO";
            case 4: return "ALTO";
            case 3: return "MODERADO";
            case 2: return "BAJO";
            case 1: return "MÍNIMO";
            default: return "DESCONOCIDO";
        }
    }

    /**
     * Calcular distancia aproximada a otra ubicación (en km)
     * Usa la fórmula de Haversine
     */
    public double distanceTo(Location other) {
        if (other == null) {
            return Double.POSITIVE_INFINITY;
        }

        final int EARTH_RADIUS = 6371; // Radio de la Tierra en km

        double lat1Rad = Math.toRadians(this.latitude);
        double lat2Rad = Math.toRadians(other.latitude);
        double deltaLat = Math.toRadians(other.latitude - this.latitude);
        double deltaLon = Math.toRadians(other.longitude - this.longitude);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    /**
     * Obtener información completa de la ubicación
     */
    public String getFullInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(name).append(" ===\n");
        sb.append("ID: ").append(id).append("\n");
        sb.append("Tipo: ").append(type).append("\n");
        sb.append("Población: ").append(population).append(" personas\n");
        sb.append("Urgencia: ").append(urgencyLevel).append("/5 (").append(getUrgencyDescription()).append(")\n");
        sb.append("Coordenadas: (").append(latitude).append(", ").append(longitude).append(")\n");
        sb.append("Estado: ").append(isEvacuated ? "EVACUADA" : "ACTIVA").append("\n");
        if (!description.isEmpty()) {
            sb.append("Descripción: ").append(description).append("\n");
        }
        return sb.toString();
    }

    // ==================== MÉTODOS SOBRESCRITOS ====================

    /**
     * Representación en String de la ubicación
     */
    @Override
    public String toString() {
        return name + " (" + type + ") - Urgencia: " + urgencyLevel + "/5" +
                (isEvacuated ? " [EVACUADA]" : "");
    }

    /**
     * Comparar ubicaciones por ID
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Location location = (Location) obj;
        return id.equals(location.id);
    }

    /**
     * Hash code basado en el ID
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Crear una copia de la ubicación
     */
    public Location copy() {
        Location copy = new Location(id, name, type, population, urgencyLevel,
                latitude, longitude, description);
        copy.setEvacuated(isEvacuated);
        return copy;
    }
}