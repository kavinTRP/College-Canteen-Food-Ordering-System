import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * RoundButton - a JButton with rounded corners, a flat colour and a hover effect.
 * Plain Swing buttons look old-fashioned, so we draw our own background.
 */
public class RoundButton extends JButton {

    private Color baseColor;
    private boolean hovering = false;

    public RoundButton(String text, Color color) {
        super(text);
        this.baseColor = color;
        setForeground(Color.WHITE);
        setFont(Theme.BUTTON_FONT);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);   // we paint the background ourselves
        setOpaque(false);
        setMargin(new Insets(6, 16, 6, 16));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hovering = true;  repaint(); }
            @Override public void mouseExited(MouseEvent e)  { hovering = false; repaint(); }
        });
    }

    /** Lets a screen change the colour later (used for the selected category). */
    public void setBaseColor(Color color) {
        this.baseColor = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color fill;
        if (!isEnabled())      fill = new Color(0xE5E7EB);
        else if (hovering)     fill = baseColor.darker();
        else                   fill = baseColor;
        g2.setColor(fill);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
        g2.dispose();
        super.paintComponent(g);   // draws the text on top
    }
}
