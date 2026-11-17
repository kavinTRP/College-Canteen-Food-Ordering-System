import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

/**
 * FoodImagePanel - shows the picture of a food item.
 *
 * It looks for a file in the "images" folder named like the food:
 *     Idli -> images/idli.png        Veg Biryani -> images/veg_biryani.png
 * (.png, .jpg and .jpeg are all accepted).
 * If no picture is found, a coloured placeholder with the first letter is drawn,
 * so the app works even with an empty images folder.
 */
public class FoodImagePanel extends JPanel {

    private final String foodName;
    private final Color color;
    private Image image;   // stays null when no picture file exists

    public FoodImagePanel(FoodItem item, int width, int height) {
        this.foodName = item.getName();
        this.color = Theme.categoryColor(item.getCategory());
        setOpaque(false);
        setPreferredSize(new Dimension(width, height));
        loadImage();
    }

    private void loadImage() {
        String baseName = "images/" + foodName.toLowerCase().replace(" ", "_");
        for (String extension : new String[]{".png", ".jpg", ".jpeg"}) {
            File file = new File(baseName + extension);
            if (file.exists()) {
                image = new ImageIcon(file.getPath()).getImage();
                return;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        int w = getWidth();
        int h = getHeight();
        g2.setClip(new RoundRectangle2D.Float(0, 0, w, h, 16, 16));

        if (image != null) {
            g2.drawImage(image, 0, 0, w, h, this);
        } else {
            g2.setColor(color);
            g2.fillRect(0, 0, w, h);
            g2.setColor(new Color(255, 255, 255, 70));
            g2.fillOval(w / 2 - h / 2 - 10, -h / 3, h, h);   // soft decoration

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, h / 2));
            FontMetrics fm = g2.getFontMetrics();
            String letter = foodName.substring(0, 1).toUpperCase();
            int x = (w - fm.stringWidth(letter)) / 2;
            int y = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(letter, x, y);
        }
        g2.dispose();
    }
}
