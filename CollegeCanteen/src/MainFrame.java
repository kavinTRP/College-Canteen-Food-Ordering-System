import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * MainFrame - the main window. It holds the navigation bar (top) and
 * four screens that are switched with a CardLayout: Home, Menu, Cart, Bill.
 */
public class MainFrame extends JFrame {

    private static final String HOME = "HOME";
    private static final String MENU = "MENU";
    private static final String CART = "CART";
    private static final String BILL = "BILL";

    private final Cart cart = new Cart();               // ONE cart shared by all screens
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel content = new JPanel(cardLayout);
    private final RoundButton cartButton = new RoundButton("Cart (0)", Theme.SUCCESS);
    private final BillPanel billPanel;

    public MainFrame() {
        setTitle("College Canteen Food Ordering System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1150, 740);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);   // centre on screen
        setLayout(new BorderLayout());

        billPanel = new BillPanel(this);

        content.add(new HomePanel(this), HOME);
        content.add(new MenuPanel(this, cart), MENU);
        content.add(new CartPanel(this, cart), CART);
        content.add(billPanel, BILL);

        add(createNavBar(), BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);

        cart.addChangeListener(this::updateCartButton);
        showHome();
    }

    private JPanel createNavBar() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(Theme.PRIMARY);
        nav.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        // Left side: logo + name
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        left.add(new LogoPanel(44));
        JLabel title = new JLabel("College Canteen");
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Color.WHITE);
        left.add(title);

        // Right side: navigation buttons
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        RoundButton homeButton = new RoundButton("Home", Theme.PRIMARY_DARK);
        homeButton.addActionListener(e -> showHome());
        RoundButton menuButton = new RoundButton("Menu", Theme.PRIMARY_DARK);
        menuButton.addActionListener(e -> showMenu());
        cartButton.addActionListener(e -> showCart());

        right.add(homeButton);
        right.add(menuButton);
        right.add(cartButton);

        nav.add(left, BorderLayout.WEST);
        nav.add(right, BorderLayout.EAST);
        return nav;
    }

    private void updateCartButton() {
        cartButton.setText("Cart (" + cart.getTotalQuantity() + ")");
    }

    // ---- Methods the screens call to move between pages ----
    public void showHome() { cardLayout.show(content, HOME); }
    public void showMenu() { cardLayout.show(content, MENU); }
    public void showCart() { cardLayout.show(content, CART); }

    public void showBill(Order order) {
        billPanel.showOrder(order);
        cardLayout.show(content, BILL);
    }
}
