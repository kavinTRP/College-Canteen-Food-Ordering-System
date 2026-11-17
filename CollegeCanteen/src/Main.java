import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main - the starting point of the program. Run THIS file.
 */
public class Main {

    public static void main(String[] args) {
        // Swing programs must create their windows on the "Event Dispatch Thread".
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                // If this fails, Java simply uses its default look. Nothing to do.
            }
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
