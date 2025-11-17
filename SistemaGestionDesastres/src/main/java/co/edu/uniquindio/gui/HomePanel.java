package co.edu.uniquindio.gui;

import co.edu.uniquindio.models.Location;
import co.edu.uniquindio.models.RescueTeam;
import co.edu.uniquindio.models.Resource;
import co.edu.uniquindio.services.DisasterManager;
import co.edu.uniquindio.structures.CustomList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Panel de inicio con vista general del sistema
 * Muestra estadísticas principales y zonas urgentes
 */
public class HomePanel extends JPanel {
    private DisasterManager manager;

    // Componentes de estadísticas
    private JLabel locationsCountLabel;
    private JLabel resourcesCountLabel;
    private JLabel teamsCountLabel;
    private JLabel populationLabel;

    // Áreas de texto
    private JTextArea urgentLocationsArea;
    private JTextArea recentActivitiesArea;

    // Paneles de gráficos
    private JPanel urgencyDistributionPanel;

    /**
     * Constructor del panel de inicio
     *
     * @param manager Gestor del sistema
     */
    public HomePanel(DisasterManager manager) {
        this.manager = manager;
        initializeUI();
        refresh();
    }

    private static class StatCard {
        JPanel card;
        JLabel valueLabel;

        StatCard(JPanel card, JLabel valueLabel) {
            this.card = card;
            this.valueLabel = valueLabel;
        }
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

        // Panel central con contenido principal
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        centerPanel.setBackground(new Color(236, 240, 241));

        // Panel izquierdo: Estadísticas y distribución
        JPanel leftPanel = createLeftPanel();
        centerPanel.add(leftPanel);

        // Panel derecho: Zonas urgentes y actividades
        JPanel rightPanel = createRightPanel();
        centerPanel.add(rightPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Panel inferior con botones de acción
        JPanel actionsPanel = createActionsPanel();
        add(actionsPanel, BorderLayout.SOUTH);
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

        JLabel titleLabel = new JLabel("Panel de Control General");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));

        JLabel subtitleLabel = new JLabel("Vista general del estado del sistema");
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
     * Crear panel izquierdo (estadísticas)
     */
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(236, 240, 241));

        // Panel de estadísticas generales
        JPanel statsPanel = createStatsPanel();
        panel.add(statsPanel);

        panel.add(Box.createVerticalStrut(15));

        // Panel de distribución de urgencias
        JPanel distributionPanel = createUrgencyDistributionPanel();
        panel.add(distributionPanel);

        return panel;
    }

    /**
     * Crear panel de estadísticas generales
     */
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                "Estadísticas Generales",
                0, 0,
                new Font("Arial", Font.BOLD, 14)
        ));

        // Tarjeta de ubicaciones
        StatCard locationsCard = createStatCard(
                "Ubicaciones",
                "0",
                new Color(52, 152, 219),
                "🏢"
        );
        locationsCountLabel = locationsCard.valueLabel;
        panel.add(locationsCard.card);


        // Tarjeta de recursos
        StatCard resourcesCard = createStatCard(
                "Recursos",
                "0",
                new Color(46, 204, 113),
                "📦"
        );
        resourcesCountLabel = resourcesCard.valueLabel;
        panel.add(resourcesCard.card);

        // Tarjeta de equipos
        StatCard teamsCard = createStatCard(
                "Equipos de Rescate",
                "0",
                new Color(155, 89, 182),
                "👥"
        );
        teamsCountLabel = teamsCard.valueLabel;
        panel.add(teamsCard.card);

        // Tarjeta de población
        StatCard populationCard = createStatCard(
                "Población Afectada",
                "0",
                new Color(231, 76, 60),
                "👤"
        );
        populationLabel = populationCard.valueLabel;
        panel.add(populationCard.card);

        return panel;
    }

    /**
     * Crear tarjeta de estadística
     */
    private StatCard createStatCard(String title, String value, Color color, String emoji) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Emoji
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
        emojiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Valor
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Título
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(127, 140, 141));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(emojiLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(valueLabel);
        contentPanel.add(Box.createVerticalStrut(3));
        contentPanel.add(titleLabel);

        card.add(contentPanel, BorderLayout.CENTER);

        return new StatCard(card, valueLabel);
    }

    /**
     * Crear panel de distribución de urgencias
     */
    private JPanel createUrgencyDistributionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel("Distribución por Urgencia");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(titleLabel, BorderLayout.NORTH);

        urgencyDistributionPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawUrgencyChart(g);
            }
        };
        urgencyDistributionPanel.setBackground(Color.WHITE);
        urgencyDistributionPanel.setPreferredSize(new Dimension(0, 200));

        panel.add(urgencyDistributionPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Dibujar gráfico de barras de urgencias
     */
    private void drawUrgencyChart(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int width = urgencyDistributionPanel.getWidth();
        int height = urgencyDistributionPanel.getHeight();

        if (width <= 0 || height <= 0) return;

        // Contar ubicaciones por urgencia
        int[] urgencyCounts = new int[6]; // 0-5
        CustomList<Location> locations = manager.getAllLocations();
        int maxCount = 1;

        for (int i = 0; i < locations.size(); i++) {
            int urgency = locations.get(i).getUrgencyLevel();
            urgencyCounts[urgency]++;
            maxCount = Math.max(maxCount, urgencyCounts[urgency]);
        }

        // Dibujar barras
        int barWidth = width / 7;
        int margin = 40;
        int chartHeight = height - margin * 2;

        Color[] colors = {
                new Color(200, 200, 200), // 0 (no usado)
                new Color(46, 204, 113),  // 1 (verde)
                new Color(241, 196, 15),  // 2 (amarillo)
                new Color(230, 126, 34),  // 3 (naranja)
                new Color(231, 76, 60),   // 4 (rojo)
                new Color(192, 57, 43)    // 5 (rojo oscuro)
        };

        for (int i = 1; i <= 5; i++) {
            int x = margin + (i - 1) * barWidth + barWidth / 4;
            int barHeight = (int) ((urgencyCounts[i] / (double) maxCount) * chartHeight);
            int y = height - margin - barHeight;

            // Dibujar barra
            g2d.setColor(colors[i]);
            g2d.fillRect(x, y, barWidth / 2, barHeight);

            // Dibujar borde
            g2d.setColor(colors[i].darker());
            g2d.drawRect(x, y, barWidth / 2, barHeight);

            // Dibujar etiqueta de nivel
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 11));
            String label = "Nivel " + i;
            FontMetrics fm = g2d.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            g2d.drawString(label, x + (barWidth / 2 - labelWidth) / 2,
                    height - margin + 15);

            // Dibujar cantidad
            if (urgencyCounts[i] > 0) {
                String count = String.valueOf(urgencyCounts[i]);
                int countWidth = fm.stringWidth(count);
                g2d.drawString(count, x + (barWidth / 2 - countWidth) / 2, y - 5);
            }
        }
    }

    /**
     * Crear panel derecho (zonas urgentes)
     */
    private JPanel createRightPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(236, 240, 241));

        // Panel de zonas urgentes
        JPanel urgentPanel = createUrgentLocationsPanel();
        panel.add(urgentPanel);

        panel.add(Box.createVerticalStrut(15));

        // Panel de actividades recientes
        JPanel activitiesPanel = createRecentActivitiesPanel();
        panel.add(activitiesPanel);

        return panel;
    }

    /**
     * Crear panel de zonas urgentes
     */
    private JPanel createUrgentLocationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel("🚨 Zonas con Mayor Urgencia");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(192, 57, 43));
        panel.add(titleLabel, BorderLayout.NORTH);

        urgentLocationsArea = new JTextArea();
        urgentLocationsArea.setEditable(false);
        urgentLocationsArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        urgentLocationsArea.setBackground(new Color(253, 237, 236));
        urgentLocationsArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(urgentLocationsArea);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crear panel de actividades recientes
     */
    private JPanel createRecentActivitiesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel("📋 Actividades Recientes");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(titleLabel, BorderLayout.NORTH);

        recentActivitiesArea = new JTextArea();
        recentActivitiesArea.setEditable(false);
        recentActivitiesArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        recentActivitiesArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(recentActivitiesArea);
        scrollPane.setPreferredSize(new Dimension(0, 180));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crear panel de acciones rápidas
     */
    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(236, 240, 241));

        JButton refreshButton = createActionButton("🔄 Actualizar",
                new Color(52, 152, 219));
        refreshButton.addActionListener(e -> refresh());

        JButton prioritizeButton = createActionButton("⚠️ Mostrar Evacuaciones por Prioridad",
                new Color(231, 76, 60));
        prioritizeButton.addActionListener(e -> showPrioritizedEvacuations());

        JButton criticalButton = createActionButton("🚨 Ver Zonas Críticas",
                new Color(192, 57, 43));
        criticalButton.addActionListener(e -> showCriticalLocations());

        panel.add(refreshButton);
        panel.add(prioritizeButton);
        panel.add(criticalButton);

        return panel;
    }

    /**
     * Crear botón de acción
     */
    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 40));
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }

    /**
     * Actualizar todos los datos del panel
     */
    public void refresh() {
        updateStatistics();
        updateUrgentLocations();
        updateRecentActivities();
        urgencyDistributionPanel.repaint();
    }

    /**
     * Actualizar estadísticas generales
     */
    private void updateStatistics() {
        CustomList<Location> locations = manager.getAllLocations();
        CustomList<Resource> resources = manager.getAllResources();
        CustomList<RescueTeam> teams = manager.getAllRescueTeams();

        locationsCountLabel.setText(String.valueOf(locations.size()));
        resourcesCountLabel.setText(String.valueOf(manager.getTotalResourceQuantity()));
        teamsCountLabel.setText(teams.size() + " (" + manager.getDeployedTeamsCount() + " desplegados)");
        populationLabel.setText(String.format("%,d personas", manager.getTotalPopulation()));
    }

    /**
     * Actualizar zonas urgentes
     */
    private void updateUrgentLocations() {
        CustomList<Location> prioritized = manager.prioritizeEvacuations();
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%-3s %-25s %-15s %s\n",
                "#", "Ubicación", "Tipo", "Urgencia"));
        sb.append("─".repeat(60)).append("\n");

        int count = Math.min(10, prioritized.size());
        for (int i = 0; i < count; i++) {
            Location loc = prioritized.get(i);
            String urgencyBar = getUrgencyBar(loc.getUrgencyLevel());
            sb.append(String.format("%-3d %-25s %-15s %s\n",
                    (i + 1),
                    truncate(loc.getName(), 25),
                    loc.getType(),
                    urgencyBar
            ));
        }

        if (prioritized.size() == 0) {
            sb.append("\nNo hay ubicaciones registradas");
        }

        urgentLocationsArea.setText(sb.toString());
    }

    /**
     * Actualizar actividades recientes
     */
    private void updateRecentActivities() {
        CustomList<String> activities = manager.getRecentOperations(15);
        StringBuilder sb = new StringBuilder();

        if (activities.isEmpty()) {
            sb.append("No hay actividades recientes");
        } else {
            for (int i = activities.size() - 1; i >= 0; i--) {
                sb.append(activities.get(i)).append("\n");
            }
        }

        recentActivitiesArea.setText(sb.toString());
        recentActivitiesArea.setCaretPosition(0);
    }

    /**
     * Obtener barra visual de urgencia
     */
    private String getUrgencyBar(int level) {
        String[] bars = {"░░░░░", "█░░░░", "██░░░", "███░░", "████░", "█████"};
        return bars[level] + " " + level + "/5";
    }

    /**
     * Truncar texto
     */
    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    /**
     * Mostrar evacuaciones priorizadas
     */
    private void showPrioritizedEvacuations() {
        CustomList<Location> prioritized = manager.prioritizeEvacuations();

        StringBuilder message = new StringBuilder();
        message.append("EVACUACIONES PRIORIZADAS\n");
        message.append("═".repeat(60)).append("\n\n");

        for (int i = 0; i < prioritized.size(); i++) {
            Location loc = prioritized.get(i);
            message.append(String.format("%d. %s\n", (i + 1), loc.getName()));
            message.append(String.format("   Tipo: %s | Urgencia: %d/5 | Población: %,d\n",
                    loc.getType(), loc.getUrgencyLevel(), loc.getPopulation()));
            message.append("\n");
        }

        JTextArea textArea = new JTextArea(message.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Evacuaciones Priorizadas por Urgencia",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Mostrar zonas críticas
     */
    private void showCriticalLocations() {
        CustomList<Location> critical = manager.getCriticalLocations();

        if (critical.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay zonas en estado crítico en este momento.",
                    "Zonas Críticas",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append("ZONAS EN ESTADO CRÍTICO (Urgencia ≥ 4)\n");
        message.append("═".repeat(60)).append("\n\n");

        for (int i = 0; i < critical.size(); i++) {
            Location loc = critical.get(i);
            message.append(String.format("🚨 %s\n", loc.getName()));
            message.append(String.format("   Urgencia: %d/5 | Población: %,d personas\n",
                    loc.getUrgencyLevel(), loc.getPopulation()));
            message.append(String.format("   Tipo: %s\n", loc.getType()));
            message.append("\n");
        }

        JTextArea textArea = new JTextArea(message.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        textArea.setForeground(new Color(192, 57, 43));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 350));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Zonas Críticas - Atención Inmediata Requerida",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
