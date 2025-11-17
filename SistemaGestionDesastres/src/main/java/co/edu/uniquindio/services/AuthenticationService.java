package co.edu.uniquindio.services;

import co.edu.uniquindio.models.User;
import co.edu.uniquindio.structures.CustomMap;
import co.edu.uniquindio.structures.CustomList;

/**
 * Servicio de autenticación de usuarios
 * Gestiona el registro, login, logout y permisos de usuarios
 */
public class AuthenticationService {
    private CustomMap<String, User> users;
    private User currentUser;
    private CustomMap<String, Integer> loginAttempts;
    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCKOUT_DURATION = 300000; // 5 minutos en ms
    private CustomMap<String, Long> lockoutTimes;

    /**
     * Constructor: Inicializa el servicio y carga usuarios por defecto
     */
    public AuthenticationService() {
        this.users = new CustomMap<>();
        this.currentUser = null;
        this.loginAttempts = new CustomMap<>();
        this.lockoutTimes = new CustomMap<>();
        initializeDefaultUsers();
    }

    /**
     * Inicializar usuarios por defecto del sistema
     */
    private void initializeDefaultUsers() {
        // Administradores
        registerUser(new User(
                "admin",
                "admin123",
                "Administrador Principal",
                User.UserRole.ADMINISTRATOR,
                "admin@disaster.com",
                "+57 300 1234567"
        ));

        registerUser(new User(
                "carlos",
                "carlos123",
                "Carlos Rodríguez",
                User.UserRole.ADMINISTRATOR,
                "carlos.rodriguez@disaster.com",
                "+57 300 2345678"
        ));

        registerUser(new User(
                "maria",
                "maria123",
                "María García",
                User.UserRole.ADMINISTRATOR,
                "maria.garcia@disaster.com",
                "+57 300 3456789"
        ));

        // Operadores de emergencia
        registerUser(new User(
                "operator1",
                "oper123",
                "María González",
                User.UserRole.EMERGENCY_OPERATOR,
                "maria.gonzalez@disaster.com",
                "+57 300 4567890"
        ));

        registerUser(new User(
                "operator2",
                "oper123",
                "Juan Pérez",
                User.UserRole.EMERGENCY_OPERATOR,
                "juan.perez@disaster.com",
                "+57 300 5678901"
        ));

        registerUser(new User(
                "operator3",
                "oper123",
                "Ana Martínez",
                User.UserRole.EMERGENCY_OPERATOR,
                "ana.martinez@disaster.com",
                "+57 300 6789012"
        ));

        // Coordinadores
        registerUser(new User(
                "coord1",
                "coord123",
                "Luis Torres",
                User.UserRole.COORDINATOR,
                "luis.torres@disaster.com",
                "+57 300 7890123"
        ));

        registerUser(new User(
                "coord2",
                "coord123",
                "Carmen Silva",
                User.UserRole.COORDINATOR,
                "carmen.silva@disaster.com",
                "+57 300 8901234"
        ));

        // Observadores
        registerUser(new User(
                "viewer1",
                "view123",
                "Roberto Díaz",
                User.UserRole.VIEWER,
                "roberto.diaz@disaster.com",
                "+57 300 9012345"
        ));
    }

    // ==================== GESTIÓN DE USUARIOS ====================

    /**
     * Registrar un nuevo usuario en el sistema
     *
     * @param user Usuario a registrar
     * @return true si se registró exitosamente, false si ya existe
     */
    public boolean registerUser(User user) {
        if (user == null) {
            return false;
        }

        String username = user.getUsername().toLowerCase().trim();

        if (users.containsKey(username)) {
            return false; // Usuario ya existe
        }

        users.put(username, user);
        loginAttempts.put(username, 0);
        return true;
    }

    /**
     * Eliminar un usuario del sistema
     * No puede eliminar al usuario actual ni al admin principal
     *
     * @param username Nombre de usuario a eliminar
     * @return true si se eliminó exitosamente
     */
    public boolean deleteUser(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }

        username = username.toLowerCase().trim();

        // No permitir eliminar al usuario actual
        if (currentUser != null && currentUser.getUsername().equals(username)) {
            return false;
        }

        // No permitir eliminar al admin principal
        if (username.equals("admin")) {
            return false;
        }

        if (users.containsKey(username)) {
            users.remove(username);
            loginAttempts.remove(username);
            lockoutTimes.remove(username);
            return true;
        }

        return false;
    }

    /**
     * Obtener un usuario por nombre de usuario
     *
     * @param username Nombre de usuario
     * @return Usuario encontrado o null
     */
    public User getUser(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }
        return users.get(username.toLowerCase().trim());
    }

    /**
     * Obtener todos los usuarios registrados
     *
     * @return Lista de usuarios
     */
    public CustomList<User> getAllUsers() {
        return users.values();
    }

    /**
     * Obtener usuarios por rol
     *
     * @param role Rol a filtrar
     * @return Lista de usuarios con ese rol
     */
    public CustomList<User> getUsersByRole(User.UserRole role) {
        CustomList<User> filtered = new CustomList<>();
        CustomList<User> allUsers = users.values();

        for (int i = 0; i < allUsers.size(); i++) {
            User user = allUsers.get(i);
            if (user.getRole() == role) {
                filtered.add(user);
            }
        }

        return filtered;
    }

    /**
     * Actualizar información de un usuario
     *
     * @param username Usuario a actualizar
     * @param updatedUser Información actualizada
     * @return true si se actualizó exitosamente
     */
    public boolean updateUser(String username, User updatedUser) {
        if (username == null || updatedUser == null) {
            return false;
        }

        username = username.toLowerCase().trim();

        if (users.containsKey(username)) {
            users.put(username, updatedUser);
            return true;
        }

        return false;
    }
    
    // ==================== AUTENTICACIÓN ====================

    /**
     * Autenticar usuario con nombre de usuario y contraseña
     *
     * @param username Nombre de usuario
     * @param password Contraseña
     * @return Usuario autenticado o null si falla
     */
    public User authenticate(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        username = username.toLowerCase().trim();

        // Verificar si el usuario está bloqueado temporalmente
        if (isTemporarilyLocked(username)) {
            long remainingTime = getRemainingLockoutTime(username);
            System.out.println("Usuario bloqueado temporalmente. Intente en " +
                    (remainingTime / 1000) + " segundos.");
            return null;
        }

        User user = users.get(username);

        if (user != null) {
            // Verificar si el usuario está activo
            if (!user.isActive()) {
                System.out.println("La cuenta está desactivada. Contacte al administrador.");
                return null;
            }

            // Verificar contraseña
            if (user.checkPassword(password)) {
                // Autenticación exitosa
                currentUser = user;
                user.recordSuccessfulLogin();
                loginAttempts.put(username, 0);
                lockoutTimes.remove(username);
                return user;
            } else {
                // Contraseña incorrecta
                recordFailedAttempt(username);
            }
        } else {
            // Usuario no existe (también contar como intento fallido)
            recordFailedAttempt(username);
        }

        return null;
    }

    /**
     * Registrar un intento de login fallido
     */
    private void recordFailedAttempt(String username) {
        Integer attempts = loginAttempts.get(username);
        attempts = (attempts == null) ? 1 : attempts + 1;
        loginAttempts.put(username, attempts);

        if (attempts >= MAX_ATTEMPTS) {
            // Bloquear temporalmente
            lockoutTimes.put(username, System.currentTimeMillis());
            System.out.println("Demasiados intentos fallidos. Usuario bloqueado temporalmente.");
        } else {
            System.out.println("Credenciales incorrectas. Intentos restantes: " +
                    (MAX_ATTEMPTS - attempts));
        }
    }

    /**
     * Verificar si un usuario está temporalmente bloqueado
     */
    private boolean isTemporarilyLocked(String username) {
        Long lockoutTime = lockoutTimes.get(username);
        if (lockoutTime == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lockoutTime;

        if (elapsedTime >= LOCKOUT_DURATION) {
            // El bloqueo ha expirado
            lockoutTimes.remove(username);
            loginAttempts.put(username, 0);
            return false;
        }

        return true;
    }

    /**
     * Obtener tiempo restante de bloqueo en milisegundos
     */
    private long getRemainingLockoutTime(String username) {
        Long lockoutTime = lockoutTimes.get(username);
        if (lockoutTime == null) {
            return 0;
        }

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lockoutTime;

        return Math.max(0, LOCKOUT_DURATION - elapsedTime);
    }

    /**
     * Cerrar sesión del usuario actual
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Obtener usuario actual autenticado
     *
     * @return Usuario actual o null si no hay sesión activa
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Verificar si hay una sesión activa
     *
     * @return true si hay un usuario autenticado
     */
    public boolean isAuthenticated() {
        return currentUser != null;
    }

    // ==================== GESTIÓN DE CONTRASEÑAS ====================

    /**
     * Cambiar contraseña del usuario actual
     *
     * @param oldPassword Contraseña actual
     * @param newPassword Nueva contraseña
     * @return true si se cambió exitosamente
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (currentUser == null) {
            return false;
        }

        return currentUser.changePassword(oldPassword, newPassword);
    }

    /**
     * Resetear contraseña (solo administradores)
     *
     * @param username Usuario al que resetear la contraseña
     * @param newPassword Nueva contraseña
     * @return true si se reseteó exitosamente
     */
    public boolean resetPassword(String username, String newPassword) {
        if (!isCurrentUserAdmin()) {
            return false;
        }

        User user = users.get(username.toLowerCase().trim());
        if (user != null) {
            user.setPassword(newPassword);
            loginAttempts.put(username, 0);
            lockoutTimes.remove(username);
            return true;
        }

        return false;
    }

    // ==================== VERIFICACIÓN DE PERMISOS ====================

    /**
     * Verificar si el usuario actual es administrador
     */
    public boolean isCurrentUserAdmin() {
        return currentUser != null && currentUser.isAdministrator();
    }

    /**
     * Verificar si el usuario actual es operador de emergencia
     */
    public boolean isCurrentUserOperator() {
        return currentUser != null && currentUser.isEmergencyOperator();
    }

    /**
     * Verificar si el usuario actual es coordinador
     */
    public boolean isCurrentUserCoordinator() {
        return currentUser != null && currentUser.isCoordinator();
    }

    /**
     * Verificar si el usuario actual puede gestionar recursos
     */
    public boolean canCurrentUserManageResources() {
        return currentUser != null && currentUser.canManageResources();
    }

    /**
     * Verificar si el usuario actual puede asignar equipos
     */
    public boolean canCurrentUserAssignTeams() {
        return currentUser != null && currentUser.canAssignTeams();
    }

    /**
     * Verificar si el usuario actual puede modificar rutas
     */
    public boolean canCurrentUserModifyRoutes() {
        return currentUser != null && currentUser.canModifyRoutes();
    }

    /**
     * Verificar si el usuario actual puede gestionar usuarios
     */
    public boolean canCurrentUserManageUsers() {
        return currentUser != null && currentUser.canManageUsers();
    }

}
