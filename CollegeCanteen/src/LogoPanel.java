import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * LogoPanel - a round "LOGO" placeholder.
 * To use your college logo later, replace this with a JLabel holding an ImageIcon.
 */
public class LogoPanel extends JPanel {

    private final int size;

    public LogoPanel(int size) {
        this.size = size;
        setOpaque(false);
        setPreferredSize(new Dimension(size, size));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillOval(1, 1, size - 3, size - 3);
        g2.setColor(Theme.PRIMARY_DARK);
        g2.drawOval(1, 1, size - 3, size - 3);

        g2.setFont(new Font("SansSerif", Font.BOLD, Math.max(10, size / 4)));
        FontMetrics fm = g2.getFontMetrics();
        String text = "LOGO";
        int x = (size - fm.stringWidth(text)) / 2;
        int y = (size - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(text, x, y);
        g2.dispose();
    }
}
