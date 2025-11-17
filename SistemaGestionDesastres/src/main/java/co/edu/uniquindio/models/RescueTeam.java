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

    // ==================== MÉTODOS DE GESTIÓN DE ASIGNACIÓN ====================

    /**
     * Asignar equipo a una ubicación
     *
     * @param locationId ID de la ubicación
     * @return true si se asignó exitosamente
     */
    public boolean assignToLocation(String locationId) {
        if (locationId == null || locationId.isEmpty()) {
            return false;
        }

        if (status == TeamStatus.AVAILABLE || status == TeamStatus.RESTING) {
            this.assignedLocationId = locationId;
            this.status = TeamStatus.DEPLOYED;
            return true;
        }

        return false;
    }

    /**
     * Desasignar equipo de su ubicación actual
     */
    public void unassign() {
        this.assignedLocationId = null;
        this.status = TeamStatus.AVAILABLE;
    }

    /**
     * Reasignar equipo a una nueva ubicación
     *
     * @param newLocationId Nueva ubicación
     * @return true si se reasignó exitosamente
     */
    public boolean reassignToLocation(String newLocationId) {
        if (status == TeamStatus.DEPLOYED) {
            unassign();
            return assignToLocation(newLocationId);
        }
        return false;
    }

    /**
     * Marcar equipo como en ruta de regreso
     */
    public void startReturning() {
        if (status == TeamStatus.DEPLOYED) {
            this.status = TeamStatus.RETURNING;
        }
    }

    /**
     * Completar misión y registrar éxito
     */
    public void completeMission(boolean successful) {
        if (successful) {
            successfulMissions++;
            // Incrementar experiencia cada 5 misiones exitosas
            if (successfulMissions % 5 == 0 && experienceLevel < 5) {
                experienceLevel++;
            }
        }

        unassign();
        this.status = TeamStatus.RESTING;
    }

    // ==================== MÉTODOS DE VERIFICACIÓN DE ESTADO ====================

    /**
     * Verificar si el equipo está disponible para asignación
     */
    public boolean isAvailable() {
        return status == TeamStatus.AVAILABLE;
    }

    /**
     * Verificar si el equipo está desplegado
     */
    public boolean isDeployed() {
        return status == TeamStatus.DEPLOYED;
    }

    /**
     * Verificar si el equipo está operativo
     */
    public boolean isOperational() {
        return status != TeamStatus.MAINTENANCE && members > 0;
    }

    /**
     * Verificar si el equipo puede ser desplegado
     */
    public boolean canDeploy() {
        return (status == TeamStatus.AVAILABLE || status == TeamStatus.RESTING)
                && isOperational();
    }

    /**
     * Verificar si el equipo está asignado a una ubicación
     */
    public boolean hasAssignment() {
        return assignedLocationId != null;
    }

    /**
     * Verificar si el equipo es experimentado (nivel >= 4)
     */
    public boolean isExperienced() {
        return experienceLevel >= 4;
    }

    /**
     * Verificar si el equipo es novato (nivel <= 2)
     */
    public boolean isNovice() {
        return experienceLevel <= 2;
    }

    // ==================== MÉTODOS DE GESTIÓN DE EQUIPO ====================

    /**
     * Agregar miembros al equipo
     */
    public void addMembers(int count) {
        if (count > 0) {
            this.members += count;
        }
    }

    /**
     * Remover miembros del equipo
     */
    public boolean removeMembers(int count) {
        if (count > 0 && count < members) {
            this.members -= count;
            return true;
        }
        return false;
    }

    /**
     * Poner equipo en mantenimiento
     */
    public void startMaintenance() {
        if (!isDeployed()) {
            this.status = TeamStatus.MAINTENANCE;
        }
    }

    /**
     * Finalizar mantenimiento
     */
    public void endMaintenance() {
        if (status == TeamStatus.MAINTENANCE) {
            this.status = TeamStatus.AVAILABLE;
        }
    }

    /**
     * Poner equipo en entrenamiento
     */
    public void startTraining() {
        if (!isDeployed()) {
            this.status = TeamStatus.TRAINING;
        }
    }

    /**
     * Finalizar entrenamiento (puede aumentar experiencia)
     */
    public void endTraining() {
        if (status == TeamStatus.TRAINING) {
            // 50% de probabilidad de incrementar experiencia
            if (Math.random() < 0.5 && experienceLevel < 5) {
                experienceLevel++;
            }
            this.status = TeamStatus.AVAILABLE;
        }
    }

    /**
     * Activar modo emergencia
     */
    public void activateEmergencyMode() {
        this.status = TeamStatus.EMERGENCY;
    }

    /**
     * Desactivar modo emergencia
     */
    public void deactivateEmergencyMode() {
        if (status == TeamStatus.EMERGENCY) {
            this.status = hasAssignment() ? TeamStatus.DEPLOYED : TeamStatus.AVAILABLE;
        }
    }

    // ==================== MÉTODOS DE CAPACIDAD Y EFICIENCIA ====================

    /**
     * Calcular la capacidad operativa del equipo
     * Basado en número de miembros y experiencia
     *
     * @return Valor entre 0 y 100
     */
    public int getOperationalCapacity() {
        if (!isOperational()) {
            return 0;
        }

        // Factor de experiencia (20-60%)
        int experienceFactor = experienceLevel * 12;

        // Factor de personal (40%)
        // Asumiendo 10 miembros como ideal
        int personnelFactor = Math.min(40, (members * 4));

        return experienceFactor + personnelFactor;
    }

    /**
     * Obtener descripción del nivel de experiencia
     */
    public String getExperienceDescription() {
        switch (experienceLevel) {
            case 5: return "EXPERTO";
            case 4: return "AVANZADO";
            case 3: return "INTERMEDIO";
            case 2: return "BÁSICO";
            case 1: return "NOVATO";
            default: return "DESCONOCIDO";
        }
    }

    /**
     * Calcular tasa de éxito aproximada
     */
    public double getSuccessRate() {
        int totalMissions = successfulMissions +
                (experienceLevel * 2); // Estimación de misiones fallidas

        if (totalMissions == 0) {
            return 0.0;
        }

        return (successfulMissions * 100.0) / totalMissions;
    }

    // ==================== MÉTODOS DE INFORMACIÓN ====================

    /**
     * Obtener información completa del equipo
     */
    public String getFullInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(name).append(" ===\n");
        sb.append("ID: ").append(id).append("\n");
        sb.append("Tipo: ").append(type).append("\n");
        sb.append("Miembros: ").append(members).append("\n");
        sb.append("Estado: ").append(status).append("\n");
        sb.append("Experiencia: ").append(experienceLevel).append("/5 (")
                .append(getExperienceDescription()).append(")\n");
        sb.append("Capacidad operativa: ").append(getOperationalCapacity()).append("%\n");
        sb.append("Misiones exitosas: ").append(successfulMissions).append("\n");
        sb.append("Tasa de éxito: ").append(String.format("%.1f", getSuccessRate())).append("%\n");

        if (hasAssignment()) {
            sb.append("Asignado a: ").append(assignedLocationId).append("\n");
        }

        if (!contactNumber.isEmpty()) {
            sb.append("Contacto: ").append(contactNumber).append("\n");
        }

        if (!lastDeploymentDate.isEmpty()) {
            sb.append("Último despliegue: ").append(lastDeploymentDate).append("\n");
        }

        return sb.toString();
    }

    // ==================== MÉTODOS SOBRESCRITOS ====================

    /**
     * Representación en String del equipo
     */
    @Override
    public String toString() {
        return name + " (" + type + ") - " + members + " miembros - " + status +
                (hasAssignment() ? " [Asignado]" : "");
    }

    /**
     * Comparar equipos por ID
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RescueTeam team = (RescueTeam) obj;
        return id.equals(team.id);
    }

    /**
     * Hash code basado en el ID
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Crear una copia del equipo
     */
    public RescueTeam copy() {
        RescueTeam copy = new RescueTeam(id, name, type, members,
                experienceLevel, contactNumber);
        copy.setStatus(status);
        copy.assignedLocationId = assignedLocationId;
        copy.lastDeploymentDate = lastDeploymentDate;
        copy.successfulMissions = successfulMissions;
        return copy;
    }
}
