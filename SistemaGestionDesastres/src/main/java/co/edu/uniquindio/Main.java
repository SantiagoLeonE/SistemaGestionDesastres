package co.edu.uniquindio;

import co.edu.uniquindio.gui.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Clase principal del Sistema de Gestión de Desastres Naturales
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Configurar Look and Feel del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Iniciar la aplicación en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}