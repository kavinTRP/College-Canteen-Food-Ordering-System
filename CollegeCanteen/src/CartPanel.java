import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * CartPanel - the full cart page. Students can change quantities, remove items,
 * enter their details, cancel the order, or place it (after seeing a summary).
 */
public class CartPanel extends JPanel {

    private final MainFrame frame;
    private final Cart cart;

    private final JPanel rowsPanel = new JPanel();
    private final JLabel totalLabel = new JLabel();
    private final JTextField nameField = new JTextField(16);
    private final JTextField rollField = new JTextField(12);

    public CartPanel(MainFrame frame, Cart cart) {
        this.frame = frame;
        this.cart = cart;

        setLayout(new BorderLayout(0, 10));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel title = new JLabel("Your Cart");
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.TEXT);
        add(title, BorderLayout.NORTH);

        // Cart rows (scrollable)
        rowsPanel.setLayout(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));
        rowsPanel.setOpaque(false);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(rowsPanel, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        add(createBottomPanel(), BorderLayout.SOUTH);

        cart.addChangeListener(this::refresh);   // auto-refresh when cart changes
        refresh();
    }

    // ------------------------------------------------------------------
    //  Bottom area: student details, total and buttons
    // ------------------------------------------------------------------
    private JPanel createBottomPanel() {
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBackground(Theme.CARD);
        bottom.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)));

        // Student details
        JPanel details = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        details.setOpaque(false);
        details.add(makeLabel("Student Name:"));
        details.add(nameField);
        details.add(makeLabel("Roll No:"));
        details.add(rollField);

        // Total
        totalLabel.setFont(Theme.TITLE_FONT);
        totalLabel.setForeground(Theme.PRIMARY);
        JPanel totalRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        totalRow.setOpaque(false);
        totalRow.add(totalLabel);

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        buttons.setOpaque(false);
        RoundButton backButton = new RoundButton("Back to Menu", Theme.MUTED);
        backButton.addActionListener(e -> frame.showMenu());
        RoundButton homeButton = new RoundButton("Home", Theme.MUTED);
        homeButton.addActionListener(e -> frame.showHome());
        RoundButton cancelButton = new RoundButton("Cancel Order", Theme.DANGER);
        cancelButton.addActionListener(e -> cancelOrder());
        RoundButton placeButton = new RoundButton("Place Order", Theme.SUCCESS);
        placeButton.addActionListener(e -> placeOrder());
        buttons.add(backButton);
        buttons.add(homeButton);
        buttons.add(cancelButton);
        buttons.add(placeButton);

        bottom.add(details);
        bottom.add(totalRow);
        bottom.add(buttons);
        return bottom;
    }

    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.NORMAL_FONT);
        return label;
    }

    // ------------------------------------------------------------------
    //  Cart rows
    // ------------------------------------------------------------------
    /** Rebuilds all rows from the current cart contents. */
    private void refresh() {
        rowsPanel.removeAll();

        if (cart.isEmpty()) {
            JLabel empty = new JLabel("Your cart is empty. Go to the Menu to add some food!");
            empty.setFont(Theme.HEADING_FONT);
            empty.setForeground(Theme.MUTED);
            empty.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
            rowsPanel.add(empty);
        } else {
            for (CartItem ci : cart.getItems()) {
                rowsPanel.add(createRow(ci));
                rowsPanel.add(javax.swing.Box.createVerticalStrut(8));
            }
        }

        totalLabel.setText("Total: " + Theme.money(cart.getTotal()));
        rowsPanel.revalidate();
        rowsPanel.repaint();
    }

    /** One cart row:  name/price  |  [-] qty [+]  |  subtotal  [Remove] */
    private JPanel createRow(CartItem ci) {
        String name = ci.getFood().getName();

        JPanel row = new JPanel(new BorderLayout(15, 0));
        row.setBackground(Theme.CARD);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));

        // Left: name + unit price
        JPanel info = new JPanel(new BorderLayout());
        info.setOpaque(false);
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(Theme.HEADING_FONT);
        JLabel unitLabel = new JLabel(Theme.money(ci.getFood().getPrice()) + " each");
        unitLabel.setFont(Theme.NORMAL_FONT);
        unitLabel.setForeground(Theme.MUTED);
        info.add(nameLabel, BorderLayout.NORTH);
        info.add(unitLabel, BorderLayout.SOUTH);
        info.setPreferredSize(new Dimension(250, 40));

        // Centre: - qty +
        JPanel qty = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        qty.setOpaque(false);
        RoundButton minus = new RoundButton("-", Theme.MUTED);
        minus.setPreferredSize(new Dimension(42, 32));
        minus.setMargin(new java.awt.Insets(0, 0, 0, 0));   // small buttons need no side space
        minus.setEnabled(ci.getQuantity() > 1);
        minus.addActionListener(e -> cart.decrease(name));
        JLabel qtyLabel = new JLabel(String.valueOf(ci.getQuantity()));
        qtyLabel.setFont(Theme.HEADING_FONT);
        RoundButton plus = new RoundButton("+", Theme.PRIMARY);
        plus.setPreferredSize(new Dimension(42, 32));
        plus.setMargin(new java.awt.Insets(0, 0, 0, 0));
        plus.addActionListener(e -> {
            try {
                cart.increase(name);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Limit Reached", JOptionPane.WARNING_MESSAGE);
            }
        });
        qty.add(minus);
        qty.add(qtyLabel);
        qty.add(plus);

        // Right: subtotal + Remove
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 6));
        right.setOpaque(false);
        JLabel subtotal = new JLabel(Theme.money(ci.getSubtotal()));
        subtotal.setFont(Theme.HEADING_FONT);
        subtotal.setForeground(Theme.TEXT);
        RoundButton remove = new RoundButton("Remove", Theme.DANGER);
        remove.addActionListener(e -> cart.remove(name));
        right.add(subtotal);
        right.add(remove);

        row.add(info, BorderLayout.WEST);
        row.add(qty, BorderLayout.CENTER);
        row.add(right, BorderLayout.EAST);
        return row;
    }

    // ------------------------------------------------------------------
    //  Cancel / Place order
    // ------------------------------------------------------------------
    private void cancelOrder() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "There is no order to cancel.",
                    "Cancel Order", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Do you really want to cancel this order?\nAll items will be removed from your cart.",
                "Cancel Order", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) {
            cart.clear();
            nameField.setText("");
            rollField.setText("");
            JOptionPane.showMessageDialog(this, "Your order has been cancelled.",
                    "Order Cancelled", JOptionPane.INFORMATION_MESSAGE);
            frame.showHome();
        }
    }

    private void placeOrder() {
        // 1. Validate
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Your cart is empty. Please add some food first.",
                    "Empty Cart", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String name = nameField.getText().trim();
        String roll = rollField.getText().trim();

        // Name: 2-30 characters, starts with a letter, letters/spaces/dots only
        if (!name.matches("[A-Za-z][A-Za-z .]{1,29}")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid name (2-30 letters, spaces and dots only).",
                    "Invalid Name", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return;
        }
        // Roll number: 4-15 letters or digits
        if (!roll.matches("[A-Za-z0-9]{4,15}")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid roll number (4-15 letters or digits, no spaces).",
                    "Invalid Roll Number", JOptionPane.WARNING_MESSAGE);
            rollField.requestFocus();
            return;
        }

        // 2. Show the order summary and ask for confirmation
        String summary = BillGenerator.generateSummary(name, roll, cart.getItems(), cart.getTotal());
        JTextArea summaryArea = new JTextArea(summary, 16, 42);
        summaryArea.setFont(Theme.BILL_FONT);
        summaryArea.setEditable(false);

        Object[] options = {"Confirm Order", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this, new JScrollPane(summaryArea),
                "Order Summary", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        // 3. Confirm or cancel
        if (choice == 0) {
            Order order = new Order(name, roll, cart.getItems());
            cart.clear();
            nameField.setText("");
            rollField.setText("");
            frame.showBill(order);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Order not placed. Your cart is still saved - you can change it.",
                    "Order Not Placed", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
