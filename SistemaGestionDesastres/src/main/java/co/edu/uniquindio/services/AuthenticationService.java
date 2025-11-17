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
    
}
