package co.edu.uniquindio.gui;

import co.edu.uniquindio.services.DisasterManager;
import co.edu.uniquindio.models.Location;
import co.edu.uniquindio.structures.CustomList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Panel para visualización y cálculo de rutas
 * Permite calcular rutas más cortas y gestionar conexiones
 */
public class RoutesPanel extends JPanel {
    private DisasterManager manager;
    private JComboBox<String> originCombo;
    private JComboBox<String> destinationCombo;
    private JTextArea routeArea;
    private JLabel distanceLabel;
    private JLabel routeStepsLabel;

    /**
     * Constructor del panel de rutas
     *
     * @param manager Gestor del sistema
     */
    public RoutesPanel(DisasterManager manager) {
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

        // Panel central dividido en selección y resultados
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(236, 240, 241));

        // Panel de selección (arriba)
        JPanel selectionPanel = createSelectionPanel();
        centerPanel.add(selectionPanel, BorderLayout.NORTH);

        // Panel de resultado (centro)
        JPanel resultPanel = createResultPanel();
        centerPanel.add(resultPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
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

        JLabel titleLabel = new JLabel("🗺️ Planificación de Rutas");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));

        JLabel subtitleLabel = new JLabel("Cálculo de rutas óptimas entre ubicaciones");
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
     * Crear panel de selección
     */
    private JPanel createSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Panel de selección de ubicaciones
        JPanel locationsPanel = new JPanel(new GridLayout(2, 2, 15, 10));
        locationsPanel.setBackground(Color.WHITE);

        // Origen
        JLabel originLabel = new JLabel("📍 Ubicación de Origen:");
        originLabel.setFont(new Font("Arial", Font.BOLD, 13));
        locationsPanel.add(originLabel);

        originCombo = new JComboBox<>();
        originCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        originCombo.setPreferredSize(new Dimension(300, 30));
        locationsPanel.add(originCombo);

        // Destino
        JLabel destLabel = new JLabel("🎯 Ubicación de Destino:");
        destLabel.setFont(new Font("Arial", Font.BOLD, 13));
        locationsPanel.add(destLabel);

        destinationCombo = new JComboBox<>();
        destinationCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        destinationCombo.setPreferredSize(new Dimension(300, 30));
        locationsPanel.add(destinationCombo);

        panel.add(locationsPanel);
        panel.add(Box.createVerticalStrut(15));

        // Panel de botones de acción
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.setBackground(Color.WHITE);

        JButton calculateBtn = createStyledButton("🔍 Calcular Ruta Más Corta",
                new Color(52, 152, 219));
        calculateBtn.addActionListener(e -> calculateRoute());
        buttonsPanel.add(calculateBtn);

        JButton addRouteBtn = createStyledButton("➕ Agregar Nueva Ruta",
                new Color(46, 204, 113));
        addRouteBtn.addActionListener(e -> addRoute());
        buttonsPanel.add(addRouteBtn);

        JButton neighborsBtn = createStyledButton("🔗 Ver Rutas Directas",
                new Color(155, 89, 182));
        neighborsBtn.addActionListener(e -> showNeighbors());
        buttonsPanel.add(neighborsBtn);

        panel.add(buttonsPanel);

        return panel;
    }

    /**
     * Crear botón estilizado
     */
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 35));
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
     * Crear panel de resultados
     */
    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Título del resultado
        JLabel titleLabel = new JLabel("📋 Resultado del Cálculo");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Área de texto para la ruta
        routeArea = new JTextArea();
        routeArea.setEditable(false);
        routeArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        routeArea.setText("Seleccione origen y destino, luego presione 'Calcular Ruta Más Corta'");
        routeArea.setMargin(new Insets(10, 10, 10, 10));
        routeArea.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(routeArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con información de distancia
        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        infoPanel.setBackground(Color.WHITE);

        distanceLabel = new JLabel("Distancia Total: -");
        distanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        distanceLabel.setForeground(new Color(52, 152, 219));

        routeStepsLabel = new JLabel("Paradas: -");
        routeStepsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        routeStepsLabel.setForeground(new Color(155, 89, 182));

        infoPanel.add(distanceLabel);
        infoPanel.add(routeStepsLabel);

        panel.add(infoPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Actualizar comboboxes con ubicaciones
     */
    public void refresh() {
        originCombo.removeAllItems();
        destinationCombo.removeAllItems();

        CustomList<Location> locations = manager.getAllLocations();

        if (locations.isEmpty()) {
            originCombo.addItem("No hay ubicaciones disponibles");
            destinationCombo.addItem("No hay ubicaciones disponibles");
            return;
        }

        for (int i = 0; i < locations.size(); i++) {
            Location loc = locations.get(i);
            String item = loc.getId() + " - " + loc.getName() + " (" + loc.getType() + ")";
            originCombo.addItem(item);
            destinationCombo.addItem(item);
        }
    }

    /**
     * Calcular ruta más corta
     */
    private void calculateRoute() {
        if (originCombo.getSelectedItem() == null ||
                destinationCombo.getSelectedItem() == null) {
            showMessage("Por favor seleccione origen y destino", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String originStr = (String) originCombo.getSelectedItem();
        String destStr = (String) destinationCombo.getSelectedItem();

        if (originStr.contains("No hay ubicaciones")) {
            showMessage("No hay ubicaciones disponibles en el sistema", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String originId = originStr.split(" - ")[0];
        String destId = destStr.split(" - ")[0];

        if (originId.equals(destId)) {
            routeArea.setText("⚠️ El origen y destino son iguales");
            distanceLabel.setText("Distancia: 0.0 km");
            routeStepsLabel.setText("Paradas: 1");
            return;
        }

        // Calcular ruta
        CustomList<String> path = manager.findShortestRoute(originId, destId);
        double distance = manager.getRouteDistance(originId, destId);

        if (path.isEmpty() || distance == Double.POSITIVE_INFINITY) {
            StringBuilder sb = new StringBuilder();
            sb.append("❌ NO EXISTE RUTA\n");
            sb.append("═".repeat(70)).append("\n\n");
            sb.append("No se encontró una ruta entre estas ubicaciones.\n");
            sb.append("Posibles causas:\n");
            sb.append("  • Las ubicaciones no están conectadas\n");
            sb.append("  • No existe un camino directo o indirecto\n\n");
            sb.append("Sugerencia: Agregue rutas intermedias o bidireccionales");

            routeArea.setText(sb.toString());
            distanceLabel.setText("Distancia: ∞");
            routeStepsLabel.setText("Paradas: 0");
        } else {
            displayRoute(path, distance);
        }
    }

    /**
     * Mostrar ruta calculada
     */
    private void displayRoute(CustomList<String> path, double distance) {
        StringBuilder sb = new StringBuilder();
        sb.append("✅ RUTA MÁS CORTA ENCONTRADA\n");
        sb.append("═".repeat(70)).append("\n\n");

        Location origin = manager.getLocation(path.get(0));
        Location dest = manager.getLocation(path.get(path.size() - 1));

        sb.append(String.format("Desde: %s (%s)\n", origin.getName(), origin.getType()));
        sb.append(String.format("Hasta: %s (%s)\n\n", dest.getName(), dest.getType()));
        sb.append("─".repeat(70)).append("\n\n");

        double accumulatedDistance = 0;

        for (int i = 0; i < path.size(); i++) {
            Location loc = manager.getLocation(path.get(i));
            if (loc != null) {
                // Icono según la posición
                String icon = i == 0 ? "🏁" : (i == path.size() - 1 ? "🎯" : "📍");

                sb.append(String.format("%s Parada %d: %s\n", icon, i + 1, loc.getName()));
                sb.append(String.format("   Tipo: %s | Urgencia: %d/5\n",
                        loc.getType(), loc.getUrgencyLevel()));

                if (i < path.size() - 1) {
                    double segmentDistance = manager.getGraph()
                            .getEdgeWeight(path.get(i), path.get(i + 1));
                    accumulatedDistance += segmentDistance;

                    sb.append(String.format("   ↓ %.2f km (Acumulado: %.2f km)\n",
                            segmentDistance, accumulatedDistance));
                }
                sb.append("\n");
            }
        }

        sb.append("═".repeat(70)).append("\n");
        sb.append(String.format("📊 Resumen:\n"));
        sb.append(String.format("   • Total de paradas: %d\n", path.size()));
        sb.append(String.format("   • Segmentos de ruta: %d\n", path.size() - 1));
        sb.append(String.format("   • Distancia promedio por segmento: %.2f km\n",
                distance / (path.size() - 1)));

        routeArea.setText(sb.toString());
        routeArea.setCaretPosition(0);

        distanceLabel.setText(String.format("Distancia Total: %.2f km", distance));
        routeStepsLabel.setText(String.format("Paradas: %d", path.size()));
    }

    /**
     * Agregar nueva ruta
     */
    private void addRoute() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<String> fromCombo = new JComboBox<>();
        JComboBox<String> toCombo = new JComboBox<>();
        JTextField distanceField = new JTextField();
        JCheckBox bidirectionalCheck = new JCheckBox("Ruta bidireccional");

        CustomList<Location> locations = manager.getAllLocations();
        for (int i = 0; i < locations.size(); i++) {
            Location loc = locations.get(i);
            String item = loc.getId() + " - " + loc.getName();
            fromCombo.addItem(item);
            toCombo.addItem(item);
        }

        panel.add(new JLabel("Desde:"));
        panel.add(fromCombo);
        panel.add(new JLabel("Hasta:"));
        panel.add(toCombo);
        panel.add(new JLabel("Distancia (km):"));
        panel.add(distanceField);
        panel.add(new JLabel(""));
        panel.add(bidirectionalCheck);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Agregar Nueva Ruta",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                String fromId = ((String) fromCombo.getSelectedItem()).split(" - ")[0];
                String toId = ((String) toCombo.getSelectedItem()).split(" - ")[0];
                double distance = Double.parseDouble(distanceField.getText());

                if (distance <= 0) {
                    showMessage("La distancia debe ser mayor a 0", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (fromId.equals(toId)) {
                    showMessage("El origen y destino no pueden ser iguales", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (bidirectionalCheck.isSelected()) {
                    manager.addBidirectionalRoute(fromId, toId, distance);
                    showMessage("Ruta bidireccional agregada exitosamente", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    manager.addRoute(fromId, toId, distance);
                    showMessage("Ruta agregada exitosamente", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                showMessage("Por favor ingrese una distancia válida", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Mostrar rutas directas desde origen
     */
    private void showNeighbors() {
        if (originCombo.getSelectedItem() == null) {
            showMessage("Por favor seleccione una ubicación de origen", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String originStr = (String) originCombo.getSelectedItem();
        if (originStr.contains("No hay ubicaciones")) {
            return;
        }

        String locationId = originStr.split(" - ")[0];
        Location location = manager.getLocation(locationId);
        CustomList<String> neighbors = manager.getNeighborLocations(locationId);

        if (neighbors.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("📍 UBICACIÓN: ").append(location.getName()).append("\n");
            sb.append("═".repeat(70)).append("\n\n");
            sb.append("⚠️ Esta ubicación no tiene rutas directas salientes.\n\n");
            sb.append("Sugerencia: Agregue rutas desde esta ubicación\n");
            sb.append("hacia otras ubicaciones del sistema.");

            routeArea.setText(sb.toString());
            distanceLabel.setText("Rutas directas: 0");
            routeStepsLabel.setText("Sin conexiones");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("📍 RUTAS DIRECTAS DESDE: ").append(location.getName()).append("\n");
            sb.append("═".repeat(70)).append("\n\n");

            double totalDistance = 0;

            for (int i = 0; i < neighbors.size(); i++) {
                Location neighbor = manager.getLocation(neighbors.get(i));
                double distance = manager.getGraph().getEdgeWeight(locationId, neighbors.get(i));
                totalDistance += distance;

                if (neighbor != null) {
                    sb.append(String.format("%d. %s\n", (i + 1), neighbor.getName()));
                    sb.append(String.format("   Tipo: %s | Distancia: %.2f km\n",
                            neighbor.getType(), distance));
                    sb.append(String.format("   Urgencia: %d/5 | Población: %,d\n",
                            neighbor.getUrgencyLevel(), neighbor.getPopulation()));
                    sb.append("\n");
                }
            }

            sb.append("─".repeat(70)).append("\n");
            sb.append(String.format("Total de conexiones: %d\n", neighbors.size()));
            sb.append(String.format("Distancia promedio: %.2f km",
                    totalDistance / neighbors.size()));

            routeArea.setText(sb.toString());
            routeArea.setCaretPosition(0);
            distanceLabel.setText(String.format("Rutas directas: %d", neighbors.size()));
            routeStepsLabel.setText(String.format("Dist. promedio: %.2f km",
                    totalDistance / neighbors.size()));
        }
    }

    /**
     * Mostrar mensaje
     */
    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}
