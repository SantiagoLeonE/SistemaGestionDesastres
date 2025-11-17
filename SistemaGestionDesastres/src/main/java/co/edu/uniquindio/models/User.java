package co.edu.uniquindio.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Modelo para representar usuarios del sistema
 * Incluye autenticación, roles y permisos
 */
public class User {
    private String username;
    private String password;
    private String fullName;
    private UserRole role;
    private String email;
    private String phoneNumber;
    private boolean isActive;
    private String createdDate;
    private String lastLoginDate;
    private int loginAttempts;
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    /**
     * Enum para los roles de usuario en el sistema
     */
    public enum UserRole {
        ADMINISTRATOR("Administrador"),
        EMERGENCY_OPERATOR("Operador de Emergencia"),
        COORDINATOR("Coordinador"),
        VIEWER("Observador");

        private final String displayName;

        UserRole(String displayName) {
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
     * @param username Nombre de usuario único
     * @param password Contraseña
     * @param fullName Nombre completo del usuario
     * @param role Rol del usuario
     */
    public User(String username, String password, String fullName, UserRole role) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (fullName == null || fullName.isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        this.username = username.toLowerCase().trim();
        this.password = password;
        this.fullName = fullName.trim();
        this.role = role;
        this.email = "";
        this.phoneNumber = "";
        this.isActive = true;
        this.createdDate = getCurrentDateTime();
        this.lastLoginDate = "";
        this.loginAttempts = 0;
    }

    /**
     * Constructor completo con todos los parámetros
     */
    public User(String username, String password, String fullName, UserRole role,
                String email, String phoneNumber) {
        this(username, password, fullName, role);
        this.email = email != null ? email : "";
        this.phoneNumber = phoneNumber != null ? phoneNumber : "";
    }

    // ==================== GETTERS ====================

    /**
     * Obtener el nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Obtener la contraseña (en producción debería estar encriptada)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Obtener el nombre completo
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Obtener el rol del usuario
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Obtener el email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Obtener el número de teléfono
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Verificar si el usuario está activo
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Obtener la fecha de creación
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * Obtener la fecha del último login
     */
    public String getLastLoginDate() {
        return lastLoginDate;
    }

    /**
     * Obtener intentos de login fallidos
     */
    public int getLoginAttempts() {
        return loginAttempts;
    }

    // ==================== SETTERS ====================

    /**
     * Establecer la contraseña
     * En producción debería encriptarse
     */
    public void setPassword(String password) {
        if (password != null && !password.isEmpty()) {
            this.password = password;
        }
    }

    /**
     * Establecer el nombre completo
     */
    public void setFullName(String fullName) {
        if (fullName != null && !fullName.isEmpty()) {
            this.fullName = fullName.trim();
        }
    }

    /**
     * Establecer el rol
     */
    public void setRole(UserRole role) {
        if (role != null) {
            this.role = role;
        }
    }

    /**
     * Establecer el email
     */
    public void setEmail(String email) {
        this.email = email != null ? email : "";
    }

    /**
     * Establecer el número de teléfono
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber != null ? phoneNumber : "";
    }

    /**
     * Activar o desactivar el usuario
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    // ==================== MÉTODOS DE AUTENTICACIÓN ====================

    /**
     * Verificar contraseña
     *
     * @param password Contraseña a verificar
     * @return true si la contraseña es correcta
     */
    public boolean checkPassword(String password) {
        if (password == null) {
            return false;
        }
        return this.password.equals(password);
    }

    /**
     * Cambiar contraseña
     *
     * @param oldPassword Contraseña actual
     * @param newPassword Nueva contraseña
     * @return true si se cambió exitosamente
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (!isActive) {
            return false;
        }

        if (checkPassword(oldPassword) && newPassword != null && !newPassword.isEmpty()) {
            this.password = newPassword;
            return true;
        }
        return false;
    }

    /**
     * Registrar intento de login exitoso
     */
    public void recordSuccessfulLogin() {
        this.lastLoginDate = getCurrentDateTime();
        this.loginAttempts = 0;
    }

    /**
     * Registrar intento de login fallido
     *
     * @return true si la cuenta fue bloqueada
     */
    public boolean recordFailedLogin() {
        loginAttempts++;
        if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            this.isActive = false;
            return true; // Cuenta bloqueada
        }
        return false;
    }

    /**
     * Resetear intentos de login
     */
    public void resetLoginAttempts() {
        this.loginAttempts = 0;
    }

    /**
     * Verificar si la cuenta está bloqueada por intentos fallidos
     */
    public boolean isLocked() {
        return loginAttempts >= MAX_LOGIN_ATTEMPTS || !isActive;
    }

    /**
     * Desbloquear cuenta
     */
    public void unlock() {
        this.isActive = true;
        this.loginAttempts = 0;
    }

    // ==================== MÉTODOS DE VERIFICACIÓN DE PERMISOS ====================

    /**
     * Verificar si el usuario es administrador
     */
    public boolean isAdministrator() {
        return role == UserRole.ADMINISTRATOR;
    }

    /**
     * Verificar si el usuario es operador de emergencia
     */
    public boolean isEmergencyOperator() {
        return role == UserRole.EMERGENCY_OPERATOR;
    }

    /**
     * Verificar si el usuario es coordinador
     */
    public boolean isCoordinator() {
        return role == UserRole.COORDINATOR;
    }

    /**
     * Verificar si el usuario solo puede ver (sin editar)
     */
    public boolean isViewer() {
        return role == UserRole.VIEWER;
    }

    /**
     * Verificar si el usuario puede gestionar recursos
     */
    public boolean canManageResources() {
        return role == UserRole.ADMINISTRATOR ||
                role == UserRole.COORDINATOR;
    }

    /**
     * Verificar si el usuario puede asignar equipos
     */
    public boolean canAssignTeams() {
        return role == UserRole.ADMINISTRATOR ||
                role == UserRole.EMERGENCY_OPERATOR ||
                role == UserRole.COORDINATOR;
    }

    /**
     * Verificar si el usuario puede ver estadísticas
     */
    public boolean canViewStatistics() {
        return isActive; // Todos los usuarios activos pueden ver estadísticas
    }

    /**
     * Verificar si el usuario puede agregar ubicaciones
     */
    public boolean canAddLocations() {
        return role == UserRole.ADMINISTRATOR;
    }

    /**
     * Verificar si el usuario puede modificar rutas
     */
    public boolean canModifyRoutes() {
        return role == UserRole.ADMINISTRATOR ||
                role == UserRole.COORDINATOR;
    }

    /**
     * Verificar si el usuario puede gestionar otros usuarios
     */
    public boolean canManageUsers() {
        return role == UserRole.ADMINISTRATOR;
    }

    /**
     * Verificar si el usuario puede generar reportes
     */
    public boolean canGenerateReports() {
        return role != UserRole.VIEWER;
    }

    // ==================== MÉTODOS DE VALIDACIÓN ====================

    /**
     * Validar formato de email
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Validar formato de teléfono (números y guiones)
     */
    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        String phoneRegex = "^[0-9\\-\\+\\(\\)\\s]{7,20}$";
        return phone.matches(phoneRegex);
    }

    /**
     * Validar fortaleza de contraseña
     *
     * @return nivel de fortaleza (0-3)
     * 0: Débil, 1: Media, 2: Fuerte, 3: Muy fuerte
     */
    public static int getPasswordStrength(String password) {
        if (password == null || password.length() < 6) {
            return 0;
        }

        int strength = 0;

        // Longitud adecuada
        if (password.length() >= 8) strength++;

        // Tiene números
        if (password.matches(".*\\d.*")) strength++;

        // Tiene mayúsculas y minúsculas
        if (password.matches(".*[a-z].*") && password.matches(".*[A-Z].*")) strength++;

        // Tiene caracteres especiales
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) strength++;

        return Math.min(strength, 3);
    }

    /**
     * Obtener descripción de la fortaleza de contraseña
     */
    public static String getPasswordStrengthDescription(String password) {
        int strength = getPasswordStrength(password);
        switch (strength) {
            case 0: return "DÉBIL";
            case 1: return "MEDIA";
            case 2: return "FUERTE";
            case 3: return "MUY FUERTE";
            default: return "DESCONOCIDA";
        }
    }

    // ==================== MÉTODOS DE UTILIDAD ====================

    /**
     * Obtener fecha y hora actual formateada
     */
    private String getCurrentDateTime() {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return now.format(formatter);
        } catch (Exception e) {
            // Si falla, retornar formato simple
            return java.time.Instant.now().toString();
        }
    }

    /**
     * Obtener información completa del usuario
     */
    public String getFullInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(fullName).append(" ===\n");
        sb.append("Usuario: ").append(username).append("\n");
        sb.append("Rol: ").append(role).append("\n");
        sb.append("Estado: ").append(isActive ? "ACTIVO" : "INACTIVO").append("\n");

        if (!email.isEmpty()) {
            sb.append("Email: ").append(email).append("\n");
        }

        if (!phoneNumber.isEmpty()) {
            sb.append("Teléfono: ").append(phoneNumber).append("\n");
        }

        sb.append("Fecha de creación: ").append(createdDate).append("\n");

        if (!lastLoginDate.isEmpty()) {
            sb.append("Último acceso: ").append(lastLoginDate).append("\n");
        }

        if (loginAttempts > 0) {
            sb.append("Intentos de login fallidos: ").append(loginAttempts).append("\n");
        }

        return sb.toString();
    }

    /**
     * Obtener lista de permisos del usuario
     */
    public String getPermissionsSummary() {
        StringBuilder sb = new StringBuilder("Permisos de ").append(fullName).append(":\n");
        sb.append("- Ver estadísticas: ").append(canViewStatistics() ? "Sí" : "No").append("\n");
        sb.append("- Gestionar recursos: ").append(canManageResources() ? "Sí" : "No").append("\n");
        sb.append("- Asignar equipos: ").append(canAssignTeams() ? "Sí" : "No").append("\n");
        sb.append("- Agregar ubicaciones: ").append(canAddLocations() ? "Sí" : "No").append("\n");
        sb.append("- Modificar rutas: ").append(canModifyRoutes() ? "Sí" : "No").append("\n");
        sb.append("- Gestionar usuarios: ").append(canManageUsers() ? "Sí" : "No").append("\n");
        sb.append("- Generar reportes: ").append(canGenerateReports() ? "Sí" : "No").append("\n");
        return sb.toString();
    }

    // ==================== MÉTODOS SOBRESCRITOS ====================

    /**
     * Representación en String del usuario
     */
    @Override
    public String toString() {
        return fullName + " (" + role + ")" + (isActive ? "" : " [INACTIVO]");
    }

    /**
     * Comparar usuarios por username
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return username.equals(user.username);
    }

    /**
     * Hash code basado en el username
     */
    @Override
    public int hashCode() {
        return username.hashCode();
    }

    /**
     * Crear una copia del usuario (sin la contraseña)
     */
    public User copyWithoutPassword() {
        User copy = new User(username, "***", fullName, role, email, phoneNumber);
        copy.isActive = this.isActive;
        copy.createdDate = this.createdDate;
        copy.lastLoginDate = this.lastLoginDate;
        copy.loginAttempts = this.loginAttempts;
        return copy;
    }
}
