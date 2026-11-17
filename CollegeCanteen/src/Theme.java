import java.awt.Color;
import java.awt.Font;

/**
 * Theme - colours, fonts and small helpers shared by every screen.
 * Change a colour here and the whole application changes.
 */
public class Theme {

    public static final Color PRIMARY      = new Color(0xE8590C);  // orange
    public static final Color PRIMARY_DARK = new Color(0xC2410C);  // dark orange
    public static final Color BACKGROUND   = new Color(0xFFF7ED);  // cream
    public static final Color CARD         = Color.WHITE;
    public static final Color TEXT         = new Color(0x1F2937);
    public static final Color MUTED        = new Color(0x6B7280);
    public static final Color SUCCESS      = new Color(0x15803D);  // green
    public static final Color DANGER       = new Color(0xDC2626);  // red
    public static final Color BORDER       = new Color(0xE5E7EB);

    public static final Font TITLE_FONT   = new Font("SansSerif", Font.BOLD, 22);
    public static final Font HEADING_FONT = new Font("SansSerif", Font.BOLD, 16);
    public static final Font NORMAL_FONT  = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font BUTTON_FONT  = new Font("SansSerif", Font.BOLD, 14);
    public static final Font BILL_FONT    = new Font(Font.MONOSPACED, Font.PLAIN, 14);

    // \u20B9 is the Rupee sign. Using the escape code avoids file-encoding problems.
    public static final String RUPEE = "\u20B9";

    /** Turns 70 into "₹70". */
    public static String money(int amount) {
        return RUPEE + amount;
    }

    /** Each category has its own colour for the food-picture placeholder. */
    public static Color categoryColor(String category) {
        switch (category) {
            case "Breakfast": return new Color(0xF59E0B);
            case "Lunch":     return new Color(0x16A34A);
            case "Snacks":    return new Color(0xEA580C);
            case "Beverages": return new Color(0x0EA5E9);
            default:          return MUTED;
        }
    }
}
