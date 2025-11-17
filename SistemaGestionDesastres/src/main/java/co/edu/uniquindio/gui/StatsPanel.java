package co.edu.uniquindio.gui;

import co.edu.uniquindio.models.Location;
import co.edu.uniquindio.models.RescueTeam;
import co.edu.uniquindio.models.Resource;
import co.edu.uniquindio.services.DisasterManager;
import co.edu.uniquindio.structures.CustomList;
import co.edu.uniquindio.structures.CustomMap;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de estadísticas y reportes del sistema
 * Muestra información detallada sobre ubicaciones, recursos y equipos
 */
public class StatsPanel extends JPanel {
    private DisasterManager manager;
    private JTextArea statsArea;
    private JButton currentReportButton;

    /**
     * Constructor del panel de estadísticas
     *
     * @param manager Gestor del sistema
     */
    public StatsPanel(DisasterManager manager) {
        this.manager = manager;
        initializeUI();
        refresh();
    }

    /**
     * Inicializar interfaz de usuario
     */
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(236, 240, 241));

        // Panel superior con título
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);

        // Área central de estadísticas
        statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statsArea.setMargin(new Insets(15, 15, 15, 15));
        statsArea.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(statsArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel buttonsPanel = createButtonsPanel();
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    /**
     * Crear panel de título
     */
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleLabel = new JLabel("📊 Estadísticas y Reportes");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));

        JLabel subtitleLabel = new JLabel("Información detallada del sistema");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(127, 140, 141));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        panel.add(textPanel, BorderLayout.WEST);

        return panel;
    }

    /**
     * Crear panel de botones
     */
    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(236, 240, 241));

        JButton generalBtn = createReportButton("📈 Reporte General",
                new Color(52, 152, 219));
        generalBtn.addActionListener(e -> {
            setActiveButton(generalBtn);
            showGeneralReport();
        });

        JButton locationsBtn = createReportButton("📍 Ubicaciones",
                new Color(46, 204, 113));
        locationsBtn.addActionListener(e -> {
            setActiveButton(locationsBtn);
            showLocationsReport();
        });

        JButton resourcesBtn = createReportButton("📦 Recursos",
                new Color(243, 156, 18));
        resourcesBtn.addActionListener(e -> {
            setActiveButton(resourcesBtn);
            showResourcesReport();
        });

        JButton resourcesByLocationBtn = createReportButton("🗺️ Recursos por Zona",
                new Color(230, 126, 34));
        resourcesByLocationBtn.addActionListener(e -> {
            setActiveButton(resourcesByLocationBtn);
            showResourcesByLocationReport();
        });

        JButton teamsBtn = createReportButton("👥 Equipos",
                new Color(155, 89, 182));
        teamsBtn.addActionListener(e -> {
            setActiveButton(teamsBtn);
            showTeamsReport();
        });

        JButton refreshBtn = createReportButton("🔄 Actualizar",
                new Color(149, 165, 166));
        refreshBtn.addActionListener(e -> refresh());

        panel.add(generalBtn);
        panel.add(locationsBtn);
        panel.add(resourcesBtn);
        panel.add(resourcesByLocationBtn);
        panel.add(teamsBtn);
        panel.add(refreshBtn);

        // Establecer el botón general como activo inicialmente
        currentReportButton = generalBtn;
        setActiveButton(generalBtn);

        return panel;
    }

    /**
     * Crear botón de reporte
     */
    private JButton createReportButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(160, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button != currentReportButton) {
                    button.setBackground(color.brighter());
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button != currentReportButton) {
                    button.setBackground(color);
                }
            }
        });

        return button;
    }

    /**
     * Establecer botón activo
     */
    private void setActiveButton(JButton button) {
        if (currentReportButton != null) {
            // Restaurar color original del botón anterior
            Color originalColor = getOriginalColor(currentReportButton.getText());
            currentReportButton.setBackground(originalColor);
        }

        // Resaltar botón actual
        currentReportButton = button;
        Color activeColor = getOriginalColor(button.getText()).darker();
        button.setBackground(activeColor);
    }

    /**
     * Obtener color original de un botón por su texto
     */
    private Color getOriginalColor(String text) {
        if (text.contains("General")) return new Color(52, 152, 219);
        if (text.contains("Ubicaciones")) return new Color(46, 204, 113);
        if (text.contains("Recursos por Zona")) return new Color(230, 126, 34);
        if (text.contains("Recursos")) return new Color(243, 156, 18);
        if (text.contains("Equipos")) return new Color(155, 89, 182);
        return new Color(149, 165, 166);
    }

    /**
     * Actualizar estadísticas
     */
    public void refresh() {
        if (currentReportButton != null) {
            String text = currentReportButton.getText();
            if (text.contains("General")) showGeneralReport();
            else if (text.contains("Ubicaciones")) showLocationsReport();
            else if (text.contains("Recursos por Zona")) showResourcesByLocationReport();
            else if (text.contains("Recursos")) showResourcesReport();
            else if (text.contains("Equipos")) showTeamsReport();
            else showGeneralReport();
        } else {
            showGeneralReport();
        }
    }

    private void showResourcesByLocationReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔═══════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║           REPORTE DE RECURSOS ASIGNADOS POR UBICACIÓN                    ║\n");
        sb.append("╚═══════════════════════════════════════════════════════════════════════════╝\n\n");

        CustomList<Location> locations = manager.getAllLocations();

        if (locations.isEmpty()) {
            sb.append("No hay ubicaciones registradas en el sistema.\n");
            statsArea.setText(sb.toString());
            return;
        }

        // Contador de ubicaciones con recursos
        int locationsWithResources = 0;
        int totalResourcesAssigned = 0;
        int totalUnitsAssigned = 0;

        for (int i = 0; i < locations.size(); i++) {
            Location loc = locations.get(i);
            CustomList<Resource> locResources = manager.getLocationResources(loc.getId());

            if (locResources.size() > 0) {
                locationsWithResources++;
                totalResourcesAssigned += locResources.size();

                // Encabezado de la ubicación
                sb.append("═".repeat(75)).append("\n");
                sb.append(String.format("📍 %s (ID: %s)\n", loc.getName(), loc.getId()));
                sb.append(String.format("   Tipo: %s | Población: %,d | Urgencia: %d/5 %s\n",
                        loc.getType(),
                        loc.getPopulation(),
                        loc.getUrgencyLevel(),
                        getUrgencyEmoji(loc.getUrgencyLevel())
                ));
                sb.append("─".repeat(75)).append("\n");

                // Agrupar recursos por tipo
                CustomMap<Resource.ResourceType, CustomList<Resource>> resourcesByType = new CustomMap<>();

                for (int j = 0; j < locResources.size(); j++) {
                    Resource res = locResources.get(j);
                    CustomList<Resource> typeList = resourcesByType.get(res.getType());
                    if (typeList == null) {
                        typeList = new CustomList<>();
                        resourcesByType.put(res.getType(), typeList);
                    }
                    typeList.add(res);
                }

                // Mostrar recursos agrupados por tipo
                sb.append("   Recursos asignados:\n");

                for (Resource.ResourceType type : Resource.ResourceType.values()) {
                    CustomList<Resource> typeResources = resourcesByType.get(type);
                    if (typeResources != null && typeResources.size() > 0) {
                        sb.append(String.format("   🔹 %s:\n", type));

                        for (int k = 0; k < typeResources.size(); k++) {
                            Resource res = typeResources.get(k);
                            String stockWarning = res.isLowStock() ? " ⚠️ BAJO" : "";
                            sb.append(String.format("      • %-30s: %,d %s%s\n",
                                    res.getName(),
                                    res.getQuantity(),
                                    res.getUnit(),
                                    stockWarning
                            ));
                            totalUnitsAssigned += res.getQuantity();
                        }
                    }
                }

                // Resumen de la ubicación
                int locationTotalQuantity = 0;
                for (int j = 0; j < locResources.size(); j++) {
                    locationTotalQuantity += locResources.get(j).getQuantity();
                }
                sb.append(String.format("   📊 Total: %d tipos de recursos, %,d unidades\n\n",
                        locResources.size(), locationTotalQuantity));
            }
        }

        // Ubicaciones sin recursos
        sb.append("═".repeat(75)).append("\n");
        sb.append("📋 UBICACIONES SIN RECURSOS ASIGNADOS\n");
        sb.append("─".repeat(75)).append("\n");

        int locationsWithoutResources = 0;
        for (int i = 0; i < locations.size(); i++) {
            Location loc = locations.get(i);
            CustomList<Resource> locResources = manager.getLocationResources(loc.getId());

            if (locResources.isEmpty()) {
                locationsWithoutResources++;
                sb.append(String.format("   • %s (Urgencia: %d/5) %s\n",
                        loc.getName(),
                        loc.getUrgencyLevel(),
                        getUrgencyEmoji(loc.getUrgencyLevel())
                ));
            }
        }

        if (locationsWithoutResources == 0) {
            sb.append("   ✅ Todas las ubicaciones tienen recursos asignados\n");
        }

        // Resumen general
        sb.append("\n");
        sb.append("═".repeat(75)).append("\n");
        sb.append("📊 RESUMEN GENERAL\n");
        sb.append("─".repeat(75)).append("\n");
        sb.append(String.format("   Total de ubicaciones: %d\n", locations.size()));
        sb.append(String.format("   Ubicaciones con recursos: %d (%.1f%%)\n",
                locationsWithResources,
                locations.size() > 0 ? (locationsWithResources * 100.0) / locations.size() : 0));
        sb.append(String.format("   Ubicaciones sin recursos: %d (%.1f%%)\n",
                locationsWithoutResources,
                locations.size() > 0 ? (locationsWithoutResources * 100.0) / locations.size() : 0));
        sb.append(String.format("   Total de asignaciones: %d\n", totalResourcesAssigned));
        sb.append(String.format("   Total de unidades distribuidas: %,d\n", totalUnitsAssigned));

        statsArea.setText(sb.toString());
        statsArea.setCaretPosition(0);
    }

    /**
     * Mostrar reporte general
     */
    private void showGeneralReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔═══════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║             REPORTE GENERAL DEL SISTEMA DE DESASTRES                      ║\n");
        sb.append("╚═══════════════════════════════════════════════════════════════════════════╝\n\n");

        // Estadísticas generales
        CustomList<Location> locations = manager.getAllLocations();
        CustomList<Resource> resources = manager.getAllResources();
        CustomList<RescueTeam> teams = manager.getAllRescueTeams();

        sb.append("RESUMEN EJECUTIVO\n");
        sb.append("─".repeat(75)).append("\n");
        sb.append(String.format("%-40s %,d\n", "Total de ubicaciones registradas:", locations.size()));
        sb.append(String.format("%-40s %,d personas\n", "Población total afectada:",
                manager.getTotalPopulation()));
        sb.append(String.format("%-40s %,d unidades\n", "Recursos disponibles:",
                manager.getTotalResourceQuantity()));
        sb.append(String.format("%-40s %d\n", "Equipos de rescate totales:", teams.size()));
        sb.append(String.format("%-40s %d\n", "Equipos desplegados:",
                manager.getDeployedTeamsCount()));
        sb.append(String.format("%-40s %d\n", "Equipos disponibles:",
                teams.size() - manager.getDeployedTeamsCount()));
        sb.append(String.format("%-40s %d\n\n", "Total de rutas:", manager.getTotalRoutes()));

        // Distribución por urgencia
        sb.append("DISTRIBUCIÓN POR NIVEL DE URGENCIA\n");
        sb.append("─".repeat(75)).append("\n");
        int[] urgencyCount = new int[6];
        int totalPopByUrgency[] = new int[6];

        for (int i = 0; i < locations.size(); i++) {
            int urgency = locations.get(i).getUrgencyLevel();
            urgencyCount[urgency]++;
            totalPopByUrgency[urgency] += locations.get(i).getPopulation();
        }

        for (int i = 5; i >= 1; i--) {
            String urgencyName = getUrgencyName(i);
            String bar = getBar(urgencyCount[i], locations.size(), 30);
            sb.append(String.format("Nivel %d (%s):%s%d ubicaciones (%,d personas)\n",
                    i, urgencyName, bar, urgencyCount[i], totalPopByUrgency[i]));
        }
        sb.append("\n");

        // Tipos de ubicaciones
        sb.append("TIPOS DE UBICACIONES\n");
        sb.append("─".repeat(75)).append("\n");
        CustomMap<Location.LocationType, Integer> typeCount = new CustomMap<>();

        for (int i = 0; i < locations.size(); i++) {
            Location.LocationType type = locations.get(i).getType();
            Integer count = typeCount.get(type);
            typeCount.put(type, count == null ? 1 : count + 1);
        }

        for (Location.LocationType type : Location.LocationType.values()) {
            Integer count = typeCount.get(type);
            if (count != null && count > 0) {
                sb.append(String.format("%-30s: %d\n", type, count));
            }
        }
        sb.append("\n");

        // Tipos de recursos
        sb.append("INVENTARIO DE RECURSOS POR TIPO\n");
        sb.append("─".repeat(75)).append("\n");
        CustomMap<Resource.ResourceType, Integer> resourceTypeCount = new CustomMap<>();

        for (int i = 0; i < resources.size(); i++) {
            Resource.ResourceType type = resources.get(i).getType();
            Integer count = resourceTypeCount.get(type);
            resourceTypeCount.put(type, count == null ? 1 : count + 1);
        }

        for (Resource.ResourceType type : Resource.ResourceType.values()) {
            Integer count = resourceTypeCount.get(type);
            if (count != null && count > 0) {
                sb.append(String.format("%-30s: %d recursos\n", type, count));
            }
        }
        sb.append("\n");

        // Tipos de equipos
        sb.append("EQUIPOS DE RESCATE POR ESPECIALIDAD\n");
        sb.append("─".repeat(75)).append("\n");
        CustomMap<RescueTeam.TeamType, Integer> teamTypeCount = new CustomMap<>();

        for (int i = 0; i < teams.size(); i++) {
            RescueTeam.TeamType type = teams.get(i).getType();
            Integer count = teamTypeCount.get(type);
            teamTypeCount.put(type, count == null ? 1 : count + 1);
        }

        for (RescueTeam.TeamType type : RescueTeam.TeamType.values()) {
            Integer count = teamTypeCount.get(type);
            if (count != null && count > 0) {
                sb.append(String.format("%-30s: %d equipos\n", type, count));
            }
        }

        statsArea.setText(sb.toString());
        statsArea.setCaretPosition(0);
    }

    /**
     * Mostrar reporte de ubicaciones
     */
    private void showLocationsReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔═══════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║              REPORTE DETALLADO DE UBICACIONES                             ║\n");
        sb.append("╚═══════════════════════════════════════════════════════════════════════════╝\n\n");

        CustomList<Location> prioritized = manager.prioritizeEvacuations();

        if (prioritized.isEmpty()) {
            sb.append("No hay ubicaciones registradas en el sistema.\n");
            statsArea.setText(sb.toString());
            return;
        }

        sb.append(String.format("%-4s %-25s %-18s %-12s %-10s\n",
                "Pos", "Nombre", "Tipo", "Población", "Urgencia"));
        sb.append("─".repeat(75)).append("\n");

        for (int i = 0; i < prioritized.size(); i++) {
            Location loc = prioritized.get(i);
            sb.append(String.format("%-4d %-25s %-18s %,-12d %-10s\n",
                    i + 1,
                    truncate(loc.getName(), 25),
                    truncate(loc.getType().toString(), 18),
                    loc.getPopulation(),
                    loc.getUrgencyLevel() + "/5 " + getUrgencyEmoji(loc.getUrgencyLevel())
            ));

            // Mostrar recursos asignados
            CustomList<Resource> locResources = manager.getLocationResources(loc.getId());
            if (locResources.size() > 0) {
                sb.append("      Recursos: ");
                int resCount = Math.min(3, locResources.size());
                for (int j = 0; j < resCount; j++) {
                    sb.append(locResources.get(j).getName());
                    if (j < resCount - 1) sb.append(", ");
                }
                if (locResources.size() > 3) {
                    sb.append("... (+" + (locResources.size() - 3) + " más)");
                }
                sb.append("\n");
            }

            // Mostrar equipos asignados
            CustomList<RescueTeam> locTeams = manager.getTeamsAtLocation(loc.getId());
            if (locTeams.size() > 0) {
                sb.append("      Equipos: ");
                for (int j = 0; j < locTeams.size(); j++) {
                    sb.append(locTeams.get(j).getName());
                    if (j < locTeams.size() - 1) sb.append(", ");
                }
                sb.append("\n");
            }

            sb.append("\n");
        }

        statsArea.setText(sb.toString());
        statsArea.setCaretPosition(0);
    }

    /**
     * Mostrar reporte de recursos
     */
    private void showResourcesReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔═══════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║              REPORTE DE RECURSOS DISPONIBLES                              ║\n");
        sb.append("╚═══════════════════════════════════════════════════════════════════════════╝\n\n");

        CustomList<Resource> resources = manager.getAllResources();

        if (resources.isEmpty()) {
            sb.append("No hay recursos registrados en el sistema.\n");
            statsArea.setText(sb.toString());
            return;
        }

        // Agrupar por tipo
        sb.append("RECURSOS POR TIPO\n");
        sb.append("─".repeat(75)).append("\n");

        for (Resource.ResourceType type : Resource.ResourceType.values()) {
            int count = 0;
            int totalQuantity = 0;

            for (int i = 0; i < resources.size(); i++) {
                Resource r = resources.get(i);
                if (r.getType() == type) {
                    count++;
                    totalQuantity += r.getQuantity();
                }
            }

            if (count > 0) {
                sb.append(String.format("%-30s: %d recursos, %,d unidades totales\n",
                        type, count, totalQuantity));
            }
        }

        sb.append("\n\nDETALLE DE RECURSOS\n");
        sb.append("─".repeat(75)).append("\n");
        sb.append(String.format("%-15s %-28s %-15s %-12s\n",
                "ID", "Nombre", "Tipo", "Cantidad"));
        sb.append("─".repeat(75)).append("\n");

        for (int i = 0; i < resources.size(); i++) {
            Resource r = resources.get(i);
            String status = r.isLowStock() ? " ⚠️" : "";
            sb.append(String.format("%-15s %-28s %-15s %,d %s%s\n",
                    truncate(r.getId(), 15),
                    truncate(r.getName(), 28),
                    truncate(r.getType().toString(), 15),
                    r.getQuantity(),
                    r.getUnit(),
                    status
            ));
        }

        // Recursos con stock bajo
        CustomList<Resource> lowStock = manager.getLowStockResources();
        if (lowStock.size() > 0) {
            sb.append("\n⚠️ RECURSOS CON STOCK BAJO\n");
            sb.append("─".repeat(75)).append("\n");
            for (int i = 0; i < lowStock.size(); i++) {
                Resource r = lowStock.get(i);
                sb.append(String.format("• %s: %,d %s (Necesita reabastecimiento)\n",
                        r.getName(), r.getQuantity(), r.getUnit()));
            }
        }

        statsArea.setText(sb.toString());
        statsArea.setCaretPosition(0);
    }

    /**
     * Mostrar reporte de equipos
     */
    private void showTeamsReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔═══════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║              REPORTE DE EQUIPOS DE RESCATE                                ║\n");
        sb.append("╚═══════════════════════════════════════════════════════════════════════════╝\n\n");

        CustomList<RescueTeam> teams = manager.getAllRescueTeams();

        if (teams.isEmpty()) {
            sb.append("No hay equipos registrados en el sistema.\n");
            statsArea.setText(sb.toString());
            return;
        }

        // Estadísticas por tipo
        sb.append("EQUIPOS POR TIPO\n");
        sb.append("─".repeat(75)).append("\n");

        for (RescueTeam.TeamType type : RescueTeam.TeamType.values()) {
            int count = 0;
            int totalMembers = 0;

            for (int i = 0; i < teams.size(); i++) {
                RescueTeam t = teams.get(i);
                if (t.getType() == type) {
                    count++;
                    totalMembers += t.getMembers();
                }
            }

            if (count > 0) {
                sb.append(String.format("%-30s: %d equipos, %d miembros totales\n",
                        type, count, totalMembers));
            }
        }

        sb.append("\n\nDETALLE DE EQUIPOS\n");
        sb.append("─".repeat(75)).append("\n");
        sb.append(String.format("%-15s %-25s %-18s %-12s\n",
                "ID", "Nombre", "Tipo", "Estado"));
        sb.append("─".repeat(75)).append("\n");

        for (int i = 0; i < teams.size(); i++) {
            RescueTeam t = teams.get(i);
            sb.append(String.format("%-15s %-25s %-18s %-12s\n",
                    truncate(t.getId(), 15),
                    truncate(t.getName(), 25),
                    truncate(t.getType().toString(), 18),
                    t.getStatus()
            ));

            if (t.hasAssignment()) {
                Location loc = manager.getLocation(t.getAssignedLocationId());
                if (loc != null) {
                    sb.append(String.format("      Asignado a: %s\n", loc.getName()));
                }
            }
        }

        // Equipos disponibles
        CustomList<RescueTeam> available = manager.getAvailableTeams();
        sb.append(String.format("\n✅ Equipos disponibles: %d de %d\n",
                available.size(), teams.size()));

        statsArea.setText(sb.toString());
        statsArea.setCaretPosition(0);
    }

    // ========== MÉTODOS AUXILIARES ==========

    private String getUrgencyName(int level) {
        switch (level) {
            case 5: return "CRÍTICO";
            case 4: return "ALTO";
            case 3: return "MODERADO";
            case 2: return "BAJO";
            case 1: return "MÍNIMO";
            default: return "DESCONOCIDO";
        }
    }

    private String getUrgencyEmoji(int level) {
        if (level >= 4) return "🔴";
        if (level == 3) return "🟡";
        return "🟢";
    }

    private String getBar(int value, int total, int width) {
        if (total == 0) return " ";
        int filled = (int) ((value / (double) total) * width);
        StringBuilder bar = new StringBuilder(" [");
        for (int i = 0; i < width; i++) {
            bar.append(i < filled ? "█" : "░");
        }
        bar.append("] ");
        return bar.toString();
    }

    private String truncate(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}
