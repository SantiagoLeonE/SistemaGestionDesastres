package co.edu.uniquindio.models;

/**
 * Modelo para representar equipos de rescate y emergencia
 * Incluye diferentes tipos de equipos especializados y su estado operativo
 */
public class RescueTeam {
    private String id;
    private String name;
    private TeamType type;
    private int members;
    private String assignedLocationId;
    private TeamStatus status;
    private int experienceLevel; // 1-5, donde 5 es máxima experiencia
    private String contactNumber;
    private String lastDeploymentDate;
    private int successfulMissions;

    /**
     * Enum para los tipos de equipos de rescate
     */
    public enum TeamType {
        MEDICAL("Médico"),
        FIREFIGHTERS("Bomberos"),
        POLICE("Policía"),
        SEARCH_AND_RESCUE("Búsqueda y Rescate"),
        PARAMEDICS("Paramédicos"),
        ENGINEERS("Ingenieros"),
        LOGISTICS("Logística"),
        COMMUNICATIONS("Comunicaciones");

        private final String displayName;

        TeamType(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    /**
     * Enum para los estados operativos del equipo
     */
    public enum TeamStatus {
        AVAILABLE("Disponible"),
        DEPLOYED("Desplegado"),
        RETURNING("Regresando"),
        RESTING("Descansando"),
        TRAINING("Entrenamiento"),
        MAINTENANCE("Mantenimiento"),
        EMERGENCY("Emergencia");

        private final String displayName;

        TeamStatus(String displayName) {
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
     * @param id      Identificador único del equipo
     * @param name    Nombre del equipo
     * @param type    Tipo de equipo
     * @param members Número de miembros
     */
    public RescueTeam(String id, String name, TeamType type, int members) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        if (members < 1) {
            throw new IllegalArgumentException("Team must have at least 1 member");
        }

        this.id = id;
        this.name = name;
        this.type = type;
        this.members = members;
        this.assignedLocationId = null;
        this.status = TeamStatus.AVAILABLE;
        this.experienceLevel = 3; // Nivel medio por defecto
        this.contactNumber = "";
        this.lastDeploymentDate = "";
        this.successfulMissions = 0;
    }

    /**
     * Constructor completo con todos los parámetros
     */
    public RescueTeam(String id, String name, TeamType type, int members,
                      int experienceLevel, String contactNumber) {
        this(id, name, type, members);
        this.experienceLevel = Math.max(1, Math.min(5, experienceLevel));
        this.contactNumber = contactNumber != null ? contactNumber : "";
    }

    // ==================== GETTERS ====================

    /**
     * Obtener el ID del equipo
     */
    public String getId() {
        return id;
    }

    /**
     * Obtener el nombre del equipo
     */
    public String getName() {
        return name;
    }

    /**
     * Obtener el tipo de equipo
     */
    public TeamType getType() {
        return type;
    }

    /**
     * Obtener el número de miembros
     */
    public int getMembers() {
        return members;
    }

    /**
     * Obtener el ID de la ubicación asignada
     */
    public String getAssignedLocationId() {
        return assignedLocationId;
    }

    /**
     * Obtener el estado actual del equipo
     */
    public TeamStatus getStatus() {
        return status;
    }

    /**
     * Obtener el nivel de experiencia (1-5)
     */
    public int getExperienceLevel() {
        return experienceLevel;
    }

    /**
     * Obtener el número de contacto
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * Obtener la fecha del último despliegue
     */
    public String getLastDeploymentDate() {
        return lastDeploymentDate;
    }

    /**
     * Obtener el número de misiones exitosas
     */
    public int getSuccessfulMissions() {
        return successfulMissions;
    }

    // ==================== SETTERS ====================

    /**
     * Establecer el nombre del equipo
     */
    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
    }

    /**
     * Establecer el número de miembros
     */
    public void setMembers(int members) {
        if (members > 0) {
            this.members = members;
        }
    }

    /**
     * Establecer el estado del equipo
     */
    public void setStatus(TeamStatus status) {
        if (status != null) {
            this.status = status;
        }
    }

    /**
     * Establecer el nivel de experiencia
     */
    public void setExperienceLevel(int experienceLevel) {
        this.experienceLevel = Math.max(1, Math.min(5, experienceLevel));
    }

    /**
     * Establecer el número de contacto
     */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber != null ? contactNumber : "";
    }

    /**
     * Establecer la fecha del último despliegue
     */
    public void setLastDeploymentDate(String lastDeploymentDate) {
        this.lastDeploymentDate = lastDeploymentDate != null ? lastDeploymentDate : "";
    }

    /**
     * Establecer el tipo de equipo
     */
    public void setType(TeamType type) {
        if (type != null) {
            this.type = type;
        }
    }
}
