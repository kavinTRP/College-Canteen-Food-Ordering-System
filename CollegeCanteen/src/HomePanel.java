import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * HomePanel - the welcome page with the project name and an "Order Now" button.
 */
public class HomePanel extends JPanel {

    public HomePanel(MainFrame frame) {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(6, 0, 6, 0);

        gbc.gridy = 0;
        add(new LogoPanel(120), gbc);

        gbc.gridy = 1;
        add(makeLabel("College Canteen", 46, Font.BOLD, Color.WHITE), gbc);

        gbc.gridy = 2;
        add(makeLabel("Food Ordering System", 28, Font.PLAIN, Color.WHITE), gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(14, 0, 24, 0);
        String categories = String.join("   \u2022   ", MenuData.CATEGORIES);
        add(makeLabel(categories, 18, Font.PLAIN, new Color(0xFFEDD5)), gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(6, 0, 6, 0);
        RoundButton orderButton = new RoundButton("Order Now", Color.WHITE);
        orderButton.setForeground(Theme.PRIMARY_DARK);
        orderButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        orderButton.setMargin(new Insets(12, 40, 12, 40));
        orderButton.addActionListener(e -> frame.showMenu());
        add(orderButton, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(30, 0, 0, 0);
        add(makeLabel("Pick your food  \u2022  Add to cart  \u2022  Get your bill",
                14, Font.PLAIN, new Color(0xFFEDD5)), gbc);
    }

    private JLabel makeLabel(String text, int size, int style, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", style, size));
        label.setForeground(color);
        return label;
    }

    /** Paints the orange gradient background. */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(new GradientPaint(0, 0, Theme.PRIMARY_DARK,
                                      0, getHeight(), new Color(0xF59E0B)));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}
