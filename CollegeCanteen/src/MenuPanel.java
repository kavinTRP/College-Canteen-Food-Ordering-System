import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MenuPanel - shows the food cards of one category at a time,
 * with a mini cart on the right side.
 */
public class MenuPanel extends JPanel {

    private final MainFrame frame;
    private final Cart cart;

    private final JPanel cardsPanel = new JPanel(new GridLayout(0, 3, 15, 15));
    private final JPanel miniCartList = new JPanel();
    private final JLabel miniTotalLabel = new JLabel();
    private final JLabel statusLabel = new JLabel(" ");
    private final Map<String, RoundButton> categoryButtons = new LinkedHashMap<>();
    private String selectedCategory = MenuData.CATEGORIES[0];

    public MenuPanel(MainFrame frame, Cart cart) {
        this.frame = frame;
        this.cart = cart;

        setLayout(new BorderLayout(15, 0));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        add(createMenuSide(), BorderLayout.CENTER);
        add(createMiniCart(), BorderLayout.EAST);

        cart.addChangeListener(this::refreshMiniCart);   // auto-refresh when cart changes
        showCategory(selectedCategory);
        refreshMiniCart();
    }

    // ------------------------------------------------------------------
    //  Left side: category buttons + food cards
    // ------------------------------------------------------------------
    private JPanel createMenuSide() {
        JPanel side = new JPanel(new BorderLayout(0, 10));
        side.setOpaque(false);

        // Category buttons + status message
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JPanel categoryBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        categoryBar.setOpaque(false);
        for (String category : MenuData.CATEGORIES) {
            RoundButton button = new RoundButton(category, Theme.MUTED);
            button.addActionListener(e -> showCategory(category));
            categoryButtons.put(category, button);
            categoryBar.add(button);
        }
        top.add(categoryBar, BorderLayout.NORTH);

        statusLabel.setFont(Theme.NORMAL_FONT);
        statusLabel.setForeground(Theme.SUCCESS);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 12, 0, 0));
        top.add(statusLabel, BorderLayout.SOUTH);
        side.add(top, BorderLayout.NORTH);

        // Cards (inside a scroll pane, pinned to the top)
        cardsPanel.setOpaque(false);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(cardsPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        side.add(scroll, BorderLayout.CENTER);
        return side;
    }

    /** Fills the card area with the items of the chosen category. */
    private void showCategory(String category) {
        selectedCategory = category;

        for (Map.Entry<String, RoundButton> entry : categoryButtons.entrySet()) {
            boolean selected = entry.getKey().equals(category);
            entry.getValue().setBaseColor(selected ? Theme.PRIMARY : Theme.MUTED);
        }

        cardsPanel.removeAll();
        for (FoodItem item : MenuData.getItemsByCategory(category)) {
            cardsPanel.add(createFoodCard(item));
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
        statusLabel.setText(" ");
    }

    /** One food card: picture, name, price, quantity box and Add to Cart button. */
    private JPanel createFoodCard(FoodItem item) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Theme.CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        FoodImagePanel picture = new FoodImagePanel(item, 190, 110);
        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(Theme.HEADING_FONT);
        nameLabel.setForeground(Theme.TEXT);

        JLabel priceLabel = new JLabel(Theme.money(item.getPrice()));
        priceLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 18));
        priceLabel.setForeground(Theme.PRIMARY);

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, Cart.MAX_QTY, 1));
        quantitySpinner.setPreferredSize(new Dimension(70, 28));
        JPanel quantityRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        quantityRow.setOpaque(false);
        JLabel qtyText = new JLabel("Qty:");
        qtyText.setFont(Theme.NORMAL_FONT);
        quantityRow.add(qtyText);
        quantityRow.add(quantitySpinner);

        RoundButton addButton = new RoundButton("Add to Cart", Theme.PRIMARY);
        addButton.addActionListener(e -> addToCart(item, quantitySpinner));

        // Centre everything inside the card
        for (JComponent c : new JComponent[]{picture, nameLabel, priceLabel, quantityRow, addButton}) {
            c.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        }
        card.add(picture);
        card.add(javax.swing.Box.createVerticalStrut(8));
        card.add(nameLabel);
        card.add(javax.swing.Box.createVerticalStrut(2));
        card.add(priceLabel);
        card.add(javax.swing.Box.createVerticalStrut(8));
        card.add(quantityRow);
        card.add(javax.swing.Box.createVerticalStrut(8));
        card.add(addButton);
        return card;
    }

    /** Reads the quantity box (with validation) and adds the item to the cart. */
    private void addToCart(FoodItem item, JSpinner spinner) {
        int quantity;
        try {
            spinner.commitEdit();   // makes Java read what the student typed
            quantity = (Integer) spinner.getValue();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a whole number between 1 and " + Cart.MAX_QTY + ".",
                    "Invalid Quantity", JOptionPane.WARNING_MESSAGE);
            spinner.setValue(1);
            return;
        }

        try {
            cart.addItem(item, quantity);
            spinner.setValue(1);
            statusLabel.setText("Added " + quantity + " x " + item.getName() + " to your cart.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Cannot Add Item", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ------------------------------------------------------------------
    //  Right side: mini cart
    // ------------------------------------------------------------------
    private JPanel createMiniCart() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Theme.CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        panel.setPreferredSize(new Dimension(290, 0));

        JLabel title = new JLabel("Your Cart");
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.TEXT);
        panel.add(title, BorderLayout.NORTH);

        miniCartList.setLayout(new BoxLayout(miniCartList, BoxLayout.Y_AXIS));
        miniCartList.setOpaque(false);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(miniCartList, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        bottom.setOpaque(false);
        miniTotalLabel.setFont(Theme.HEADING_FONT);
        miniTotalLabel.setForeground(Theme.TEXT);
        bottom.add(miniTotalLabel, BorderLayout.NORTH);

        RoundButton checkoutButton = new RoundButton("View Cart & Checkout", Theme.SUCCESS);
        checkoutButton.addActionListener(e -> frame.showCart());
        bottom.add(checkoutButton, BorderLayout.SOUTH);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshMiniCart() {
        miniCartList.removeAll();
        if (cart.isEmpty()) {
            JLabel empty = new JLabel("No items yet. Add some food!");
            empty.setFont(Theme.NORMAL_FONT);
            empty.setForeground(Theme.MUTED);
            miniCartList.add(empty);
        } else {
            for (CartItem ci : cart.getItems()) {
                JPanel row = new JPanel(new BorderLayout());
                row.setOpaque(false);
                row.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
                JLabel left = new JLabel(ci.getFood().getName() + "  \u00D7 " + ci.getQuantity());
                left.setFont(Theme.NORMAL_FONT);
                JLabel right = new JLabel(Theme.money(ci.getSubtotal()));
                right.setFont(Theme.NORMAL_FONT);
                row.add(left, BorderLayout.WEST);
                row.add(right, BorderLayout.EAST);
                miniCartList.add(row);
            }
        }
        miniTotalLabel.setText("Total: " + Theme.money(cart.getTotal()));
        miniCartList.revalidate();
        miniCartList.repaint();
    }
}
