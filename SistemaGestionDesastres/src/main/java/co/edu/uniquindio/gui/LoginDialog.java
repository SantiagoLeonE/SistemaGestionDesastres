package co.edu.uniquindio.gui;

import co.edu.uniquindio.services.AuthenticationService;
import co.edu.uniquindio.models.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Diálogo de inicio de sesión para el sistema
 * Permite autenticación de usuarios con diferentes roles
 */
public class LoginDialog extends JDialog {
    private AuthenticationService authService;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private JCheckBox showPasswordCheckBox;
    private boolean authenticated = false;

    /**
     * Constructor del diálogo de login
     *
     * @param parent Ventana padre
     * @param authService Servicio de autenticación
     */
    public LoginDialog(Frame parent, AuthenticationService authService) {
        super(parent, "Inicio de Sesión - Sistema de Desastres", true);
        this.authService = authService;

        initializeUI();
        setSize(550, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // Cerrar la aplicación si se cierra el diálogo sin autenticar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!authenticated) {
                    System.exit(0);
                }
            }
        });
    }

    /**
     * Inicializar interfaz de usuario
     */
    private void initializeUI() {
        setLayout(new BorderLayout(0, 0));

        // Panel superior con título y logo
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Panel central con formulario
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Crear panel de encabezado
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(41, 128, 185));
        panel.setPreferredSize(new Dimension(450, 100));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título principal
        JLabel titleLabel = new JLabel("Sistema de Gestión");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtítulo
        JLabel subtitleLabel = new JLabel("de Desastres Naturales");
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subtitleLabel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Crear panel de formulario
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        // Etiqueta de bienvenida
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel welcomeLabel = new JLabel("Ingrese sus credenciales");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(welcomeLabel, gbc);

        gbc.gridwidth = 1;

        // Campo de usuario
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 13));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panel.add(usernameField, gbc);

        // Campo de contraseña
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 13));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panel.add(passwordField, gbc);

        // Checkbox para mostrar contraseña
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        showPasswordCheckBox = new JCheckBox("Mostrar contraseña");
        showPasswordCheckBox.setFont(new Font("Arial", Font.PLAIN, 11));
        showPasswordCheckBox.setBackground(Color.WHITE);
        showPasswordCheckBox.addActionListener(e -> togglePasswordVisibility());
        panel.add(showPasswordCheckBox, gbc);

        // Información de usuarios de prueba
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);

        JTextArea infoArea = new JTextArea(
                "Usuarios de prueba:\n\n" +
                        "Administrador:\n" +
                        "  • admin / admin123\n" +
                        "  • carlos / carlos123\n\n" +
                        "Operador de Emergencia:\n" +
                        "  • operator1 / oper123\n\n" +
                        "Coordinador:\n" +
                        "  • coord1 / coord123"
        );
        infoArea.setEditable(false);
        infoArea.setBackground(new Color(236, 240, 241));
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        infoArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        panel.add(infoArea, gbc);

        return panel;
    }

    /**
     * Crear panel de botones
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(189, 195, 199)));

        // Botón de login
        loginButton = new JButton("Iniciar Sesión");
        loginButton.setPreferredSize(new Dimension(140, 35));
        loginButton.setFont(new Font("Arial", Font.BOLD, 13));
        loginButton.setBackground(new Color(39, 174, 96));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> attemptLogin());

        // Efecto hover para botón login
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(46, 204, 113));
            }
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(39, 174, 96));
            }
        });

        // Botón de cancelar
        cancelButton = new JButton("Cancelar");
        cancelButton.setPreferredSize(new Dimension(140, 35));
        cancelButton.setFont(new Font("Arial", Font.BOLD, 13));
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> {
            dispose();
            System.exit(0);
        });

        // Efecto hover para botón cancelar
        cancelButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                cancelButton.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(MouseEvent e) {
                cancelButton.setBackground(new Color(231, 76, 60));
            }
        });

        panel.add(loginButton);
        panel.add(cancelButton);

        return panel;
    }

    /**
     * Alternar visibilidad de la contraseña
     */
    private void togglePasswordVisibility() {
        if (showPasswordCheckBox.isSelected()) {
            passwordField.setEchoChar((char) 0);
        } else {
            passwordField.setEchoChar('•');
        }
    }

    /**
     * Intentar iniciar sesión
     */
    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Validar campos vacíos
        if (username.isEmpty() || password.isEmpty()) {
            showErrorMessage("Error de Validación",
                    "Por favor ingrese usuario y contraseña");
            return;
        }

        // Deshabilitar botón durante autenticación
        loginButton.setEnabled(false);
        loginButton.setText("Autenticando...");

        // Simular pequeño delay para mejor UX
        Timer timer = new Timer(500, e -> performAuthentication(username, password));
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Realizar autenticación
     */
    private void performAuthentication(String username, String password) {
        User user = authService.authenticate(username, password);

        if (user != null) {
            authenticated = true;

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(
                    this,
                    "¡Bienvenido " + user.getFullName() + "!\n" +
                            "Rol: " + user.getRole(),
                    "Autenticación Exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();
        } else {
            // Restaurar botón
            loginButton.setEnabled(true);
            loginButton.setText("Iniciar Sesión");

            // Mostrar mensaje de error
            showErrorMessage("Error de Autenticación",
                    "Usuario o contraseña incorrectos.\n" +
                            "Verifique sus credenciales e intente nuevamente.");

            // Limpiar contraseña
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }

    /**
     * Mostrar mensaje de error
     */
    private void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Verificar si la autenticación fue exitosa
     *
     * @return true si el usuario se autenticó
     */
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * Configurar acción de tecla Enter
     */
    @Override
    public void addNotify() {
        super.addNotify();

        // Enter en cualquier campo ejecuta login
        KeyListener enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    attemptLogin();
                }
            }
        };

        usernameField.addKeyListener(enterListener);
        passwordField.addKeyListener(enterListener);

        // Establecer botón por defecto
        getRootPane().setDefaultButton(loginButton);

        // Focus en campo de usuario
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }
}
