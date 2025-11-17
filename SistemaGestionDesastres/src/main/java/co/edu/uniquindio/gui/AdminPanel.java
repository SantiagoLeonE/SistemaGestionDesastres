package co.edu.uniquindio.gui;

import co.edu.uniquindio.services.*;
import co.edu.uniquindio.models.*;
import co.edu.uniquindio.structures.CustomList;
import javax.swing.*;
import java.awt.*;

/**
 * Panel de administración para gestión de recursos y equipos
 */
public class AdminPanel extends JPanel {
    private DisasterManager manager;
    private AuthenticationService authService;
    private JList<String> resourcesList;
    private JList<String> teamsList;
    private DefaultListModel<String> resourcesModel;
    private DefaultListModel<String> teamsModel;
    private MainFrame mainFrame;

    public AdminPanel(DisasterManager manager, AuthenticationService authService) {
        this.manager = manager;
        this.authService = authService;
        this.mainFrame = null;
        initializeUI();
        refresh();
    }

    /**
     * Establecer referencia al frame principal para actualizar otros paneles
     */
    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior con título y botón de informe
        JPanel topPanel = new JPanel(new BorderLayout());

        // Título
        JLabel titleLabel = new JLabel("Panel de Administración");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.WEST);

        // Botón de generar informe (solo visible para administradores)
        JButton generateReportBtn = createReportButton();
        topPanel.add(generateReportBtn, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Panel central con dos secciones
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Panel de recursos
        JPanel resourcesPanel = createResourcesPanel();
        centerPanel.add(resourcesPanel);

        // Panel de equipos
        JPanel teamsPanel = createTeamsPanel();
        centerPanel.add(teamsPanel);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JButton createReportButton() {
        JButton reportBtn = new JButton("Generar Informe");
        reportBtn.addActionListener(e -> generateReport());
        return reportBtn;
    }

    private void generateReport() {
        // Crear el generador de informes
        ReportGenerator reportGenerator = new ReportGenerator(manager, authService.getCurrentUser());

        // Diálogo para seleccionar ubicación del archivo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Informe del Sistema");

        // Nombre por defecto
        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        fileChooser.setSelectedFile(new java.io.File("Informe_Sistema_" + timestamp + ".txt"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();

            // Generar el informe
            boolean success = reportGenerator.generateCompleteReport(fileToSave.getAbsolutePath());

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Informe generado exitosamente en:\n" + fileToSave.getAbsolutePath(),
                        "Informe Completado",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al generar el informe",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createResourcesPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Gestión de Recursos"));

        // Lista de recursos
        resourcesModel = new DefaultListModel<>();
        resourcesList = new JList<>(resourcesModel);
        JScrollPane scrollPane = new JScrollPane(resourcesList);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botones de recursos
        JPanel buttonsPanel = new JPanel(new GridLayout(4, 1, 5, 5));

        JButton addResourceBtn = new JButton("Agregar Recurso");
        addResourceBtn.addActionListener(e -> addResource());

        JButton distributeBtn = new JButton("Distribuir Recurso");
        distributeBtn.addActionListener(e -> distributeResource());

        JButton viewResourceBtn = new JButton("Ver Detalles");
        viewResourceBtn.addActionListener(e -> viewResourceDetails());

        JButton refreshResourceBtn = new JButton("Actualizar");
        refreshResourceBtn.addActionListener(e -> refresh());

        buttonsPanel.add(addResourceBtn);
        buttonsPanel.add(distributeBtn);
        buttonsPanel.add(viewResourceBtn);
        buttonsPanel.add(refreshResourceBtn);

        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTeamsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Gestión de Equipos de Rescate"));

        // Lista de equipos
        teamsModel = new DefaultListModel<>();
        teamsList = new JList<>(teamsModel);
        JScrollPane scrollPane = new JScrollPane(teamsList);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botones de equipos
        JPanel buttonsPanel = new JPanel(new GridLayout(4, 1, 5, 5));

        JButton addTeamBtn = new JButton("Agregar Equipo");
        addTeamBtn.addActionListener(e -> addTeam());

        JButton assignTeamBtn = new JButton("Asignar a Ubicación");
        assignTeamBtn.addActionListener(e -> assignTeam());

        JButton viewTeamBtn = new JButton("Ver Detalles");
        viewTeamBtn.addActionListener(e -> viewTeamDetails());

        JButton refreshTeamBtn = new JButton("Actualizar");
        refreshTeamBtn.addActionListener(e -> refresh());

        buttonsPanel.add(addTeamBtn);
        buttonsPanel.add(assignTeamBtn);
        buttonsPanel.add(viewTeamBtn);
        buttonsPanel.add(refreshTeamBtn);

        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    public void refresh() {
        // Refrescar recursos
        resourcesModel.clear();
        CustomList<Resource> resources = manager.getAllResources();
        for (int i = 0; i < resources.size(); i++) {
            Resource r = resources.get(i);
            resourcesModel.addElement(r.getId() + " - " + r.toString());
        }

        // Refrescar equipos
        teamsModel.clear();
        CustomList<RescueTeam> teams = manager.getAllRescueTeams();
        for (int i = 0; i < teams.size(); i++) {
            RescueTeam t = teams.get(i);
            teamsModel.addElement(t.getId() + " - " + t.toString());
        }
    }

    private void addResource() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JComboBox<Resource.ResourceType> typeCombo = new JComboBox<>(Resource.ResourceType.values());
        JTextField quantityField = new JTextField();
        JTextField unitField = new JTextField();

        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Nombre:"));
        panel.add(nameField);
        panel.add(new JLabel("Tipo:"));
        panel.add(typeCombo);
        panel.add(new JLabel("Cantidad:"));
        panel.add(quantityField);
        panel.add(new JLabel("Unidad:"));
        panel.add(unitField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Agregar Recurso",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Resource resource = new Resource(
                        idField.getText(),
                        nameField.getText(),
                        (Resource.ResourceType) typeCombo.getSelectedItem(),
                        Integer.parseInt(quantityField.getText()),
                        unitField.getText()
                );
                manager.addResource(resource);
                refresh();
                JOptionPane.showMessageDialog(this, "Recurso agregado exitosamente");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al agregar recurso: " + e.getMessage());
            }
        }
    }

    private void distributeResource() {
        String selected = resourcesList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un recurso");
            return;
        }

        String resourceId = selected.split(" - ")[0];

        // Obtener ubicaciones
        CustomList<Location> locations = manager.getAllLocations();
        if (locations.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay ubicaciones disponibles");
            return;
        }

        String[] locationNames = new String[locations.size()];
        for (int i = 0; i < locations.size(); i++) {
            Location loc = locations.get(i);
            locationNames[i] = loc.getId() + " - " + loc.getName() +
                    " (Urgencia: " + loc.getUrgencyLevel() + "/5)";
        }

        String location = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione la ubicación destino:",
                "Distribuir Recurso",
                JOptionPane.QUESTION_MESSAGE,
                null,
                locationNames,
                locationNames[0]
        );

        if (location != null) {
            String quantity = JOptionPane.showInputDialog(this, "Cantidad a distribuir:");
            if (quantity != null) {
                try {
                    String locationId = location.split(" - ")[0];
                    boolean success = manager.distributeResource(resourceId, locationId, Integer.parseInt(quantity));

                    if (success) {
                        refresh();

                        // ACTUALIZAR TODOS LOS PANELES
                        if (mainFrame != null) {
                            mainFrame.refreshAllPanels();
                        }

                        Location targetLoc = manager.getLocation(locationId);
                        Resource resource = manager.getResource(resourceId);

                        JOptionPane.showMessageDialog(this,
                                "✅ Recurso distribuido exitosamente\n\n" +
                                        "Recurso: " + resource.getName() + "\n" +
                                        "Cantidad: " + quantity + " " + resource.getUnit() + "\n" +
                                        "Destino: " + targetLoc.getName(),
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "❌ No se pudo distribuir el recurso.\n" +
                                        "Verifique que hay suficiente cantidad disponible.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                            "Por favor ingrese una cantidad válida",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "Error al distribuir: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void viewResourceDetails() {
        String selected = resourcesList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un recurso");
            return;
        }

        String resourceId = selected.split(" - ")[0];
        Resource resource = manager.getResource(resourceId);

        if (resource != null) {
            String stockStatus = resource.isLowStock() ? " ⚠️ BAJO" : " ✅ NORMAL";
            String details = String.format(
                    "ID: %s\n" +
                            "Nombre: %s\n" +
                            "Tipo: %s\n" +
                            "Cantidad: %d %s\n" +
                            "Estado: %s",
                    resource.getId(),
                    resource.getName(),
                    resource.getType(),
                    resource.getQuantity(),
                    resource.getUnit(),
                    stockStatus
            );
            JOptionPane.showMessageDialog(this, details, "Detalles del Recurso",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addTeam() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JComboBox<RescueTeam.TeamType> typeCombo = new JComboBox<>(RescueTeam.TeamType.values());
        JTextField membersField = new JTextField();

        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Nombre:"));
        panel.add(nameField);
        panel.add(new JLabel("Tipo:"));
        panel.add(typeCombo);
        panel.add(new JLabel("Miembros:"));
        panel.add(membersField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Agregar Equipo",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                RescueTeam team = new RescueTeam(
                        idField.getText(),
                        nameField.getText(),
                        (RescueTeam.TeamType) typeCombo.getSelectedItem(),
                        Integer.parseInt(membersField.getText())
                );
                manager.addRescueTeam(team);
                refresh();

                if (mainFrame != null) {
                    mainFrame.refreshAllPanels();
                }

                JOptionPane.showMessageDialog(this, "Equipo agregado exitosamente");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al agregar equipo: " + e.getMessage());
            }
        }
    }

    private void assignTeam() {
        String selected = teamsList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un equipo");
            return;
        }

        String teamId = selected.split(" - ")[0];

        // Obtener ubicaciones
        CustomList<Location> locations = manager.getAllLocations();
        if (locations.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay ubicaciones disponibles");
            return;
        }

        String[] locationNames = new String[locations.size()];
        for (int i = 0; i < locations.size(); i++) {
            Location loc = locations.get(i);
            locationNames[i] = loc.getId() + " - " + loc.getName() +
                    " (Urgencia: " + loc.getUrgencyLevel() + "/5)";
        }

        String location = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione la ubicación destino:",
                "Asignar Equipo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                locationNames,
                locationNames[0]
        );

        if (location != null) {
            String locationId = location.split(" - ")[0];
            boolean success = manager.assignTeamToLocation(teamId, locationId);

            if (success) {
                refresh();

                // Actualizar todos los paneles
                if (mainFrame != null) {
                    mainFrame.refreshAllPanels();
                }

                RescueTeam team = manager.getRescueTeam(teamId);
                Location targetLoc = manager.getLocation(locationId);

                JOptionPane.showMessageDialog(this,
                        "✅ Equipo asignado exitosamente\n\n" +
                                "Equipo: " + team.getName() + "\n" +
                                "Destino: " + targetLoc.getName(),
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "❌ No se pudo asignar el equipo",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewTeamDetails() {
        String selected = teamsList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un equipo");
            return;
        }

        String teamId = selected.split(" - ")[0];
        RescueTeam team = manager.getRescueTeam(teamId);

        if (team != null) {
            String assigned = "No asignado";
            if (team.getAssignedLocationId() != null) {
                Location loc = manager.getLocation(team.getAssignedLocationId());
                assigned = loc != null ? loc.getName() : team.getAssignedLocationId();
            }

            String details = String.format(
                    "ID: %s\nNombre: %s\nTipo: %s\nMiembros: %d\nEstado: %s\nUbicación: %s",
                    team.getId(),
                    team.getName(),
                    team.getType(),
                    team.getMembers(),
                    team.getStatus(),
                    assigned
            );
            JOptionPane.showMessageDialog(this, details, "Detalles del Equipo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
