import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * BillPanel - the order confirmation page. Shows a success message,
 * the formatted bill, and buttons to save the bill / order again / go home.
 */
public class BillPanel extends JPanel {

    private final JLabel messageLabel = new JLabel();
    private final JLabel orderIdLabel = new JLabel();
    private final JTextArea billArea = new JTextArea();
    private Order currentOrder;

    public BillPanel(MainFrame frame) {
        setLayout(new BorderLayout(0, 12));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // Green confirmation banner
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(Theme.SUCCESS);
        banner.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel heading = new JLabel("Order Confirmed!");
        heading.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 26));
        heading.setForeground(Color.WHITE);
        messageLabel.setFont(Theme.NORMAL_FONT);
        messageLabel.setForeground(Color.WHITE);
        orderIdLabel.setFont(Theme.HEADING_FONT);
        orderIdLabel.setForeground(Color.WHITE);
        JPanel texts = new JPanel(new BorderLayout());
        texts.setOpaque(false);
        texts.add(heading, BorderLayout.NORTH);
        texts.add(messageLabel, BorderLayout.CENTER);
        texts.add(orderIdLabel, BorderLayout.SOUTH);
        banner.add(texts, BorderLayout.CENTER);
        add(banner, BorderLayout.NORTH);

        // The bill
        billArea.setFont(Theme.BILL_FONT);
        billArea.setEditable(false);
        billArea.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JScrollPane scroll = new JScrollPane(billArea);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        add(scroll, BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));
        buttons.setOpaque(false);
        RoundButton saveButton = new RoundButton("Save Bill", Theme.PRIMARY);
        saveButton.addActionListener(e -> saveBill());
        RoundButton newOrderButton = new RoundButton("Order More Food", Theme.SUCCESS);
        newOrderButton.addActionListener(e -> frame.showMenu());
        RoundButton homeButton = new RoundButton("Back to Home", Theme.MUTED);
        homeButton.addActionListener(e -> frame.showHome());
        buttons.add(saveButton);
        buttons.add(newOrderButton);
        buttons.add(homeButton);
        add(buttons, BorderLayout.SOUTH);
    }

    /** Called by MainFrame to fill the page with a new order. */
    public void showOrder(Order order) {
        currentOrder = order;
        messageLabel.setText("Thank you, " + order.getStudentName()
                + "! Your order has been placed successfully.");
        orderIdLabel.setText("Order ID: " + order.getOrderId());
        billArea.setText(BillGenerator.generateBill(order));
        billArea.setCaretPosition(0);   // scroll to the top
    }

    /** Saves the bill as a text file inside the "bills" folder. */
    private void saveBill() {
        if (currentOrder == null) return;
        try {
            File folder = new File("bills");
            folder.mkdirs();   // creates the folder if it does not exist
            File file = new File(folder, currentOrder.getOrderId() + ".txt");
            Files.writeString(file.toPath(), billArea.getText(), StandardCharsets.UTF_8);
            JOptionPane.showMessageDialog(this,
                    "Bill saved as:\n" + file.getAbsolutePath(),
                    "Bill Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not save the bill:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
