package co.edu.uniquindio.gui;

import co.edu.uniquindio.services.DisasterManager;
import co.edu.uniquindio.models.Location;
import co.edu.uniquindio.structures.CustomList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * Panel con visualización gráfica del grafo de ubicaciones
 * Muestra un mapa interactivo con nodos y aristas
 */
public class MapPanel extends JPanel {
    private DisasterManager manager;
    private MapCanvas canvas;
    private JLabel infoLabel;
    private JCheckBox showLabelsCheck;
    private JCheckBox showWeightsCheck;

    /**
     * Constructor del panel de mapa
     *
     * @param manager Gestor del sistema
     */
    public MapPanel(DisasterManager manager) {
        this.manager = manager;
        initializeUI();
    }

    /**
     * Inicializar interfaz de usuario
     */
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(236, 240, 241));

        // Panel superior con título y controles
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Canvas del mapa (centro)
        canvas = new MapCanvas();
        add(canvas, BorderLayout.CENTER);

        // Panel inferior con información
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Crear panel superior
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Título
        JLabel titleLabel = new JLabel("🗺️ Mapa Interactivo de Ubicaciones");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(52, 73, 94));
        panel.add(titleLabel, BorderLayout.WEST);

        // Controles
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlsPanel.setBackground(Color.WHITE);

        showLabelsCheck = new JCheckBox("Mostrar etiquetas", true);
        showLabelsCheck.addActionListener(e -> canvas.repaint());

        showWeightsCheck = new JCheckBox("Mostrar distancias", true);
        showWeightsCheck.addActionListener(e -> canvas.repaint());

        JButton refreshBtn = new JButton("🔄 Actualizar");
        refreshBtn.addActionListener(e -> refresh());

        controlsPanel.add(showLabelsCheck);
        controlsPanel.add(showWeightsCheck);
        controlsPanel.add(refreshBtn);

        panel.add(controlsPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Crear panel inferior
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        infoLabel = new JLabel("Haga clic en una ubicación para ver detalles");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(infoLabel, BorderLayout.WEST);

        // Leyenda
        JPanel legendPanel = createLegendPanel();
        panel.add(legendPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Crear panel de leyenda
     */
    private JPanel createLegendPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panel.setBackground(Color.WHITE);

        panel.add(createLegendItem("🔴 Nivel 5", new Color(220, 20, 20)));
        panel.add(createLegendItem("🟠 Nivel 4", new Color(255, 100, 100)));
        panel.add(createLegendItem("🟡 Nivel 3", new Color(255, 165, 0)));
        panel.add(createLegendItem("🟢 Nivel 2", new Color(255, 215, 0)));
        panel.add(createLegendItem("🔵 Nivel 1", new Color(100, 200, 100)));

        return panel;
    }

    /**
     * Crear item de leyenda
     */
    private JPanel createLegendItem(String text, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(label);

        return panel;
    }

    /**
     * Actualizar el mapa
     */
    public void refresh() {
        canvas.repaint();
    }

    /**
     * Canvas personalizado para dibujar el grafo
     */
    private class MapCanvas extends JPanel {
        private Location selectedLocation;
        private CustomList<Point2D> nodePositions;
        private Point dragStart;
        private Point offset;
        private double scale = 1.0;

        public MapCanvas() {
            setBackground(new Color(250, 250, 250));
            setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
            offset = new Point(0, 0);

            // Mouse listeners
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleClick(e.getPoint());
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    dragStart = e.getPoint();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    handleHover(e.getPoint());
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (dragStart != null) {
                        int dx = e.getX() - dragStart.x;
                        int dy = e.getY() - dragStart.y;
                        offset.translate(dx, dy);
                        dragStart = e.getPoint();
                        repaint();
                    }
                }
            });

            // Mouse wheel para zoom
            addMouseWheelListener(e -> {
                double delta = e.getPreciseWheelRotation();
                if (delta < 0) {
                    scale = Math.min(2.0, scale * 1.1);
                } else {
                    scale = Math.max(0.5, scale / 1.1);
                }
                repaint();
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            CustomList<Location> locations = manager.getAllLocations();
            if (locations.isEmpty()) {
                drawEmptyMessage(g2d);
                return;
            }

            // Aplicar transformaciones
            g2d.translate(offset.x, offset.y);
            g2d.scale(scale, scale);

            // Calcular posiciones
            calculateNodePositions(locations);

            // Dibujar aristas primero
            drawEdges(g2d, locations);

            // Dibujar nodos encima
            drawNodes(g2d, locations);
        }

        /**
         * Dibujar mensaje cuando no hay ubicaciones
         */
        private void drawEmptyMessage(Graphics2D g2d) {
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.setColor(new Color(127, 140, 141));
            String message = "No hay ubicaciones para mostrar";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(message)) / 2;
            int y = getHeight() / 2;
            g2d.drawString(message, x, y);

            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            String hint = "Agregue ubicaciones desde el panel de Administración";
            x = (getWidth() - g2d.getFontMetrics().stringWidth(hint)) / 2;
            g2d.drawString(hint, x, y + 25);
        }

        /**
         * Calcular posiciones de los nodos
         */
        private void calculateNodePositions(CustomList<Location> locations) {
            if (nodePositions != null && nodePositions.size() == locations.size()) {
                return; // Ya calculado
            }

            nodePositions = new CustomList<>();
            int width = getWidth() - 100;
            int height = getHeight() - 100;
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

            int n = locations.size();

            if (n == 1) {
                nodePositions.add(new Point2D.Double(centerX, centerY));
                return;
            }

            double angleStep = 2 * Math.PI / n;
            double radius = Math.min(width, height) / 3.0;

            // Distribuir nodos en círculo
            for (int i = 0; i < n; i++) {
                double angle = i * angleStep - Math.PI / 2; // Empezar desde arriba
                double x = centerX + radius * Math.cos(angle);
                double y = centerY + radius * Math.sin(angle);
                nodePositions.add(new Point2D.Double(x, y));
            }
        }

        /**
         * Dibujar aristas
         */
        private void drawEdges(Graphics2D g2d, CustomList<Location> locations) {
            g2d.setStroke(new BasicStroke(2));

            for (int i = 0; i < locations.size(); i++) {
                Location from = locations.get(i);
                CustomList<String> neighbors = manager.getNeighborLocations(from.getId());
                Point2D fromPos = nodePositions.get(i);

                for (int j = 0; j < neighbors.size(); j++) {
                    String neighborId = neighbors.get(j);
                    int neighborIndex = findLocationIndex(locations, neighborId);

                    if (neighborIndex >= 0) {
                        Point2D toPos = nodePositions.get(neighborIndex);

                        // Color de arista
                        g2d.setColor(new Color(149, 165, 166, 150));

                        // Dibujar línea
                        g2d.drawLine((int) fromPos.getX(), (int) fromPos.getY(),
                                (int) toPos.getX(), (int) toPos.getY());

                        // Dibujar flecha
                        drawArrow(g2d, fromPos, toPos);

                        // Dibujar peso de la arista
                        if (showWeightsCheck.isSelected()) {
                            double weight = manager.getGraph()
                                    .getEdgeWeight(from.getId(), neighborId);
                            int midX = (int) ((fromPos.getX() + toPos.getX()) / 2);
                            int midY = (int) ((fromPos.getY() + toPos.getY()) / 2);

                            g2d.setColor(Color.WHITE);
                            g2d.fillRect(midX - 20, midY - 8, 40, 16);

                            g2d.setColor(new Color(52, 73, 94));
                            g2d.setFont(new Font("Arial", Font.BOLD, 10));
                            String weightStr = String.format("%.1f km", weight);
                            FontMetrics fm = g2d.getFontMetrics();
                            int strWidth = fm.stringWidth(weightStr);
                            g2d.drawString(weightStr, midX - strWidth / 2, midY + 4);
                        }
                    }
                }
            }
        }

        /**
         * Dibujar flecha direccional
         */
        private void drawArrow(Graphics2D g2d, Point2D from, Point2D to) {
            double dx = to.getX() - from.getX();
            double dy = to.getY() - from.getY();
            double angle = Math.atan2(dy, dx);

            int arrowSize = 10;
            double x = to.getX() - 35 * Math.cos(angle);
            double y = to.getY() - 35 * Math.sin(angle);

            int x1 = (int) (x - arrowSize * Math.cos(angle - Math.PI / 6));
            int y1 = (int) (y - arrowSize * Math.sin(angle - Math.PI / 6));
            int x2 = (int) (x - arrowSize * Math.cos(angle + Math.PI / 6));
            int y2 = (int) (y - arrowSize * Math.sin(angle + Math.PI / 6));

            g2d.drawLine((int) x, (int) y, x1, y1);
            g2d.drawLine((int) x, (int) y, x2, y2);
        }

        /**
         * Dibujar nodos
         */
        private void drawNodes(Graphics2D g2d, CustomList<Location> locations) {
            for (int i = 0; i < locations.size(); i++) {
                Location loc = locations.get(i);
                Point2D pos = nodePositions.get(i);

                // Color según urgencia
                Color nodeColor = getUrgencyColor(loc.getUrgencyLevel());

                // Tamaño según población (20-50px)
                int baseSize = 25;
                int sizeBonus = Math.min(25, loc.getPopulation() / 200);
                int nodeSize = baseSize + sizeBonus;

                int x = (int) pos.getX() - nodeSize / 2;
                int y = (int) pos.getY() - nodeSize / 2;

                // Sombra
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillOval(x + 2, y + 2, nodeSize, nodeSize);

                // Resaltar si está seleccionado
                if (loc == selectedLocation) {
                    g2d.setColor(new Color(241, 196, 15));
                    g2d.setStroke(new BasicStroke(4));
                    g2d.drawOval(x - 5, y - 5, nodeSize + 10, nodeSize + 10);
                }

                // Nodo
                g2d.setColor(nodeColor);
                g2d.fillOval(x, y, nodeSize, nodeSize);

                // Borde
                g2d.setColor(nodeColor.darker());
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(x, y, nodeSize, nodeSize);

                // Icono según tipo
                String icon = getLocationIcon(loc.getType());
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Dialog", Font.PLAIN, 16));
                FontMetrics fm = g2d.getFontMetrics();
                int iconWidth = fm.stringWidth(icon);
                g2d.drawString(icon,
                        (int) pos.getX() - iconWidth / 2,
                        (int) pos.getY() + 6);

                // Etiqueta con nombre
                if (showLabelsCheck.isSelected()) {
                    g2d.setFont(new Font("Arial", Font.BOLD, 11));
                    g2d.setColor(new Color(52, 73, 94));
                    String name = loc.getName();
                    if (name.length() > 20) {
                        name = name.substring(0, 17) + "...";
                    }
                    fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(name);

                    // Fondo para el texto
                    g2d.setColor(new Color(255, 255, 255, 200));
                    g2d.fillRoundRect((int) pos.getX() - textWidth / 2 - 5,
                            (int) pos.getY() + nodeSize / 2 + 5,
                            textWidth + 10, 18, 5, 5);

                    g2d.setColor(new Color(52, 73, 94));
                    g2d.drawString(name,
                            (int) pos.getX() - textWidth / 2,
                            (int) pos.getY() + nodeSize / 2 + 18);
                }
            }
        }

        /**
         * Obtener color según urgencia
         */
        private Color getUrgencyColor(int urgency) {
            switch (urgency) {
                case 5: return new Color(220, 20, 20);
                case 4: return new Color(255, 100, 100);
                case 3: return new Color(255, 165, 0);
                case 2: return new Color(255, 215, 0);
                case 1: return new Color(100, 200, 100);
                default: return Color.GRAY;
            }
        }

        /**
         * Obtener icono según tipo
         */
        private String getLocationIcon(Location.LocationType type) {
            switch (type) {
                case CITY: return "🏢";
                case SHELTER: return "🏠";
                case AID_CENTER: return "📦";
                case HOSPITAL: return "🏥";
                case AFFECTED_ZONE: return "⚠️";
                default: return "📍";
            }
        }

        /**
         * Encontrar índice de ubicación
         */
        private int findLocationIndex(CustomList<Location> locations, String id) {
            for (int i = 0; i < locations.size(); i++) {
                if (locations.get(i).getId().equals(id)) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * Manejar clic en el canvas
         */
        private void handleClick(Point point) {
            CustomList<Location> locations = manager.getAllLocations();

            // Ajustar punto por transformaciones
            Point2D adjustedPoint = new Point2D.Double(
                    (point.x - offset.x) / scale,
                    (point.y - offset.y) / scale
            );

            for (int i = 0; i < locations.size(); i++) {
                Location loc = locations.get(i);
                Point2D pos = nodePositions.get(i);

                double distance = adjustedPoint.distance(pos);
                if (distance < 30) {
                    selectedLocation = loc;
                    showLocationDetails(loc);
                    repaint();
                    return;
                }
            }

            selectedLocation = null;
            infoLabel.setText("Haga clic en una ubicación para ver detalles");
            repaint();
        }

        /**
         * Manejar hover
         */
        private void handleHover(Point point) {
            CustomList<Location> locations = manager.getAllLocations();

            Point2D adjustedPoint = new Point2D.Double(
                    (point.x - offset.x) / scale,
                    (point.y - offset.y) / scale
            );

            for (int i = 0; i < locations.size(); i++) {
                Point2D pos = nodePositions.get(i);
                double distance = adjustedPoint.distance(pos);
                if (distance < 30) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    return;
                }
            }

            setCursor(Cursor.getDefaultCursor());
        }

        /**
         * Mostrar detalles de ubicación
         */
        private void showLocationDetails(Location loc) {
            String info = String.format(
                    "%s | Tipo: %s | Población: %,d | Urgencia: %d/5",
                    loc.getName(), loc.getType(),
                    loc.getPopulation(), loc.getUrgencyLevel()
            );
            infoLabel.setText(info);
        }
    }
}
