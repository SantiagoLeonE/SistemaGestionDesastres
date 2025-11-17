package co.edu.uniquindio.gui;

import co.edu.uniquindio.services.*;
import co.edu.uniquindio.models.User;
import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la aplicación
 */
public class MainFrame extends JFrame {
    private AuthenticationService authService;
    private DisasterManager disasterManager;
    private JTabbedPane tabbedPane;
    private JLabel statusLabel;

    // Paneles
    private HomePanel homePanel;
    private AdminPanel adminPanel;
    private RoutesPanel routesPanel;
    private StatsPanel statsPanel;
    private MapPanel mapPanel;

    public MainFrame() {
        initializeServices();
        initializeFrame();
        showLoginDialog();
    }

    private void initializeServices() {
        authService = new AuthenticationService();
        disasterManager = new DisasterManager();
        loadTestData();
    }

    private void initializeFrame() {
        setTitle("Sistema de Gestión de Desastres Naturales");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal
        setLayout(new BorderLayout());

        // Barra de menú
        createMenuBar();

        // Panel de pestañas
        tabbedPane = new JTabbedPane();

        homePanel = new HomePanel(disasterManager);
        adminPanel = new AdminPanel(disasterManager, authService);
        routesPanel = new RoutesPanel(disasterManager);
        statsPanel = new StatsPanel(disasterManager);
        mapPanel = new MapPanel(disasterManager);

        adminPanel.setMainFrame(this);

        tabbedPane.addTab("Inicio", homePanel);
        tabbedPane.addTab("Administración", adminPanel);
        tabbedPane.addTab("Rutas", routesPanel);
        tabbedPane.addTab("Estadísticas", statsPanel);
        tabbedPane.addTab("Mapa Interactivo", mapPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Barra de estado
        statusLabel = new JLabel("Sistema iniciado");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menú Archivo
        JMenu fileMenu = new JMenu("Archivo");
        JMenuItem exitItem = new JMenuItem("Salir");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // Menú Usuario
        JMenu userMenu = new JMenu("Usuario");
        JMenuItem logoutItem = new JMenuItem("Cerrar Sesión");
        logoutItem.addActionListener(e -> logout());
        userMenu.add(logoutItem);

        // Menú Ayuda
        JMenu helpMenu = new JMenu("Ayuda");
        JMenuItem aboutItem = new JMenuItem("Acerca de");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(userMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void showLoginDialog() {
        LoginDialog loginDialog = new LoginDialog(this, authService);
        loginDialog.setVisible(true);

        if (!authService.isAuthenticated()) {
            System.exit(0);
        }

        User currentUser = authService.getCurrentUser();
        statusLabel.setText("Usuario: " + currentUser.getFullName() + " (" + currentUser.getRole() + ")");

        // Deshabilitar panel de administración si no es administrador
        if (!currentUser.isAdministrator()) {
            tabbedPane.setEnabledAt(1, false);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea cerrar sesión?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            authService.logout();
            dispose();
            new MainFrame().setVisible(true);
        }
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(
                this,
                "Sistema de Gestión de Desastres Naturales\n" +
                        "Versión 1.0\n" +
                        "Proyecto de Estructuras de Datos\n" +
                        "2025",
                "Acerca de",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void loadTestData() {
        DataLoader.loadTestData(disasterManager);
    }

    public void updateStatus(String message) {
        statusLabel.setText(message);
    }

    public void refreshAllPanels() {
        homePanel.refresh();
        adminPanel.refresh();
        routesPanel.refresh();
        statsPanel.refresh();
        mapPanel.refresh();

        updateStatus("✅ Datos actualizados - " + java.time.LocalTime.now().toString().substring(0, 8));
    }
}
