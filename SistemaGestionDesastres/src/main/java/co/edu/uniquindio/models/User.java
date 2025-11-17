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
}
