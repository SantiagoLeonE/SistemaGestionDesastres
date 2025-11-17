package co.edu.uniquindio.services;

import co.edu.uniquindio.models.*;
import co.edu.uniquindio.structures.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Generador de informes del sistema en formato TXT
 * Solo disponible para usuarios administradores
 */
public class ReportGenerator {
    private DisasterManager manager;
    private User currentUser;

    public ReportGenerator(DisasterManager manager, User currentUser) {
        this.manager = manager;
        this.currentUser = currentUser;
    }

    /**
     * Generar informe completo del sistema
     * @param filePath Ruta donde guardar el archivo (si es null, usa ruta por defecto)
     * @return true si se generó exitosamente
     */
    public boolean generateCompleteReport(String filePath) {
        if (!currentUser.isAdministrator()) {
            return false; // Solo administradores pueden generar informes
        }

        try {
            // Si no se especifica ruta, usar una por defecto
            if (filePath == null || filePath.isEmpty()) {
                String timestamp = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                filePath = "Informe_Sistema_" + timestamp + ".txt";
            }

            // Crear el archivo
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);

            // Generar el contenido del informe
            writeHeader(bw);
            writeSystemOverview(bw);
            writeLocationDetails(bw);
            writeResourcesDistribution(bw);
            writeFooter(bw);

            bw.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Escribir encabezado del informe
     */
    private void writeHeader(BufferedWriter bw) throws IOException {
        String line = "=".repeat(90);
        bw.write(line + "\n");
        bw.write(centerText("SISTEMA DE GESTIÓN DE DESASTRES NATURALES", 90) + "\n");
        bw.write(centerText("INFORME GENERAL DEL ESTADO DEL SISTEMA", 90) + "\n");
        bw.write(line + "\n\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dateTime = LocalDateTime.now().format(formatter);

        bw.write("Fecha y hora del informe: " + dateTime + "\n");
        bw.write("Generado por: " + currentUser.getFullName() +
                " (" + currentUser.getUsername() + ")\n");
        bw.write("Rol: " + currentUser.getRole() + "\n\n");
        bw.write(line + "\n\n");
    }

    /**
     * Escribir resumen general del sistema
     */
    private void writeSystemOverview(BufferedWriter bw) throws IOException {
        bw.write("╔══════════════════════════════════════════════════════════════════════════════════╗\n");
        bw.write("║                           RESUMEN GENERAL DEL SISTEMA                            ║\n");
        bw.write("╚══════════════════════════════════════════════════════════════════════════════════╝\n\n");

        CustomList<Location> locations = manager.getAllLocations();

        bw.write(String.format("%-50s %,d\n", "Total de ubicaciones registradas:", locations.size()));
        bw.write(String.format("%-50s %,d personas\n", "Población total afectada:",
                manager.getTotalPopulation()));
        bw.write(String.format("%-50s %,d unidades\n", "Recursos disponibles en inventario:",
                manager.getTotalResourceQuantity()));

        // Distribución por urgencia
        bw.write("DISTRIBUCIÓN POR NIVEL DE URGENCIA:\n");
        bw.write("-".repeat(90) + "\n");

        int[] urgencyCount = new int[6];
        int[] populationByUrgency = new int[6];

        for (int i = 0; i < locations.size(); i++) {
            Location loc = locations.get(i);
            urgencyCount[loc.getUrgencyLevel()]++;
            populationByUrgency[loc.getUrgencyLevel()] += loc.getPopulation();
        }

        for (int i = 5; i >= 1; i--) {
            String urgencyName = getUrgencyName(i);
            double percentage = locations.size() > 0 ?
                    (urgencyCount[i] * 100.0 / locations.size()) : 0;

            bw.write(String.format("  Nivel %d (%s):%s %d ubicaciones (%.1f%%) - %,d personas\n",
                    i, urgencyName,
                    " ".repeat(Math.max(1, 15 - urgencyName.length())),
                    urgencyCount[i], percentage, populationByUrgency[i]));
        }

        bw.write("\n");
    }

    /**
     * Escribir detalles de ubicaciones con recursos asignados
     */
    private void writeLocationDetails(BufferedWriter bw) throws IOException {
        bw.write("╔══════════════════════════════════════════════════════════════════════════════════╗\n");
        bw.write("║                     ESTADO DETALLADO DE UBICACIONES                              ║\n");
        bw.write("╚══════════════════════════════════════════════════════════════════════════════════╝\n\n");

        CustomList<Location> locations = manager.prioritizeEvacuations();

        if (locations.isEmpty()) {
            bw.write("No hay ubicaciones registradas en el sistema.\n\n");
            return;
        }

        for (int i = 0; i < locations.size(); i++) {
            Location loc = locations.get(i);

            bw.write("=".repeat(90) + "\n");
            bw.write(String.format("[%d] %s (ID: %s)\n", i + 1, loc.getName(), loc.getId()));
            bw.write("-".repeat(90) + "\n");
            bw.write(String.format("  Tipo de ubicación:        %s\n", loc.getType()));
            bw.write(String.format("  Población afectada:       %,d personas\n", loc.getPopulation()));
            bw.write(String.format("  Nivel de urgencia:        %d/5 (%s)\n",
                    loc.getUrgencyLevel(), getUrgencyName(loc.getUrgencyLevel())));

            // Información de conexiones
            CustomList<String> neighbors = manager.getNeighborLocations(loc.getId());
            bw.write(String.format("  Conexiones directas:      %d ubicaciones\n", neighbors.size()));

            // Recursos asignados
            CustomList<Resource> locResources = manager.getLocationResources(loc.getId());
            bw.write(String.format("\n  RECURSOS ASIGNADOS: %d\n", locResources.size()));

            if (locResources.isEmpty()) {
                bw.write("    ⚠ No hay recursos asignados a esta ubicación\n");
            } else {
                bw.write("  " + "-".repeat(84) + "\n");

                // Agrupar por tipo
                CustomMap<Resource.ResourceType, CustomList<Resource>> byType = new CustomMap<>();
                for (int j = 0; j < locResources.size(); j++) {
                    Resource res = locResources.get(j);
                    CustomList<Resource> typeList = byType.get(res.getType());
                    if (typeList == null) {
                        typeList = new CustomList<>();
                        byType.put(res.getType(), typeList);
                    }
                    typeList.add(res);
                }

                for (Resource.ResourceType type : Resource.ResourceType.values()) {
                    CustomList<Resource> typeResources = byType.get(type);
                    if (typeResources != null && typeResources.size() > 0) {
                        bw.write(String.format("    %s:\n", type));
                        for (int k = 0; k < typeResources.size(); k++) {
                            Resource res = typeResources.get(k);
                            String warning = res.isLowStock() ? " [STOCK BAJO]" : "";
                            bw.write(String.format("      - %-40s %,8d %s%s\n",
                                    res.getName(), res.getQuantity(), res.getUnit(), warning));
                        }
                    }
                }
            }

            bw.write("\n");
        }
    }

    /**
     * Escribir resumen de distribución de recursos
     */
    private void writeResourcesDistribution(BufferedWriter bw) throws IOException {
        bw.write("╔══════════════════════════════════════════════════════════════════════════════════╗\n");
        bw.write("║                        DISTRIBUCIÓN DE RECURSOS                                  ║\n");
        bw.write("╚══════════════════════════════════════════════════════════════════════════════════╝\n\n");

        CustomList<Resource> resources = manager.getAllResources();

        bw.write("INVENTARIO GENERAL:\n");
        bw.write("-".repeat(90) + "\n");
        bw.write(String.format("%-15s %-35s %-15s %15s\n",
                "ID", "Nombre", "Tipo", "Cantidad"));
        bw.write("-".repeat(90) + "\n");

        for (int i = 0; i < resources.size(); i++) {
            Resource res = resources.get(i);
            String warning = res.isLowStock() ? " [!]" : "";
            bw.write(String.format("%-15s %-35s %-15s %,10d %s%s\n",
                    truncate(res.getId(), 15),
                    truncate(res.getName(), 35),
                    res.getType(),
                    res.getQuantity(),
                    res.getUnit(),
                    warning));
        }

        // Alertas de stock bajo
        CustomList<Resource> lowStock = manager.getLowStockResources();
        if (lowStock.size() > 0) {
            bw.write("\n⚠ ALERTAS DE STOCK BAJO:\n");
            bw.write("-".repeat(90) + "\n");
            for (int i = 0; i < lowStock.size(); i++) {
                Resource res = lowStock.get(i);
                bw.write(String.format("  - %s: %,d %s (Requiere reabastecimiento urgente)\n",
                        res.getName(), res.getQuantity(), res.getUnit()));
            }
        }

        bw.write("\n");
    }

    /**
     * Escribir pie del informe
     */
    private void writeFooter(BufferedWriter bw) throws IOException {
        String line = "=".repeat(90);
        bw.write("\n" + line + "\n");
        bw.write(centerText("FIN DEL INFORME", 90) + "\n");
        bw.write(line + "\n");

        bw.write("\nEste informe es confidencial y solo debe ser accedido por personal autorizado.\n");
        bw.write("Sistema de Gestión de Desastres Naturales v1.0\n");
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

    private String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }

    private String truncate(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}
