import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * BillGenerator - builds the text of the order summary and the final bill.
 * It is shown in a monospaced font, so the columns line up neatly.
 */
public class BillGenerator {

    private static final int WIDTH = 40;
    private static final String DOUBLE_LINE = "=".repeat(WIDTH);
    private static final String SINGLE_LINE = "-".repeat(WIDTH);
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy  hh:mm a");

    /** Final bill shown after the order is confirmed. */
    public static String generateBill(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append(DOUBLE_LINE).append("\n");
        sb.append(center("COLLEGE CANTEEN")).append("\n");
        sb.append(center("Food Order Bill")).append("\n");
        sb.append(DOUBLE_LINE).append("\n");
        sb.append("Order ID : ").append(order.getOrderId()).append("\n");
        sb.append("Date     : ").append(order.getOrderTime().format(DATE_FORMAT)).append("\n");
        sb.append("Student  : ").append(order.getStudentName()).append("\n");
        sb.append("Roll No  : ").append(order.getRollNo()).append("\n");
        sb.append(SINGLE_LINE).append("\n");
        appendItemTable(sb, order.getItems());
        sb.append(SINGLE_LINE).append("\n");
        sb.append(String.format("%-25s%15s", "TOTAL AMOUNT", Theme.money(order.getTotal()))).append("\n");
        sb.append(DOUBLE_LINE).append("\n");
        sb.append(center("Thank you! Visit again.")).append("\n");
        return sb.toString();
    }

    /** Summary shown BEFORE the order is confirmed. */
    public static String generateSummary(String studentName, String rollNo,
                                         List<CartItem> items, int total) {
        StringBuilder sb = new StringBuilder();
        sb.append(DOUBLE_LINE).append("\n");
        sb.append(center("ORDER SUMMARY")).append("\n");
        sb.append(DOUBLE_LINE).append("\n");
        sb.append("Student  : ").append(studentName).append("\n");
        sb.append("Roll No  : ").append(rollNo).append("\n");
        sb.append(SINGLE_LINE).append("\n");
        appendItemTable(sb, items);
        sb.append(SINGLE_LINE).append("\n");
        sb.append(String.format("%-25s%15s", "TOTAL AMOUNT", Theme.money(total))).append("\n");
        sb.append(DOUBLE_LINE).append("\n");
        sb.append("Click 'Confirm Order' to place it.\n");
        return sb.toString();
    }

    private static void appendItemTable(StringBuilder sb, List<CartItem> items) {
        sb.append(String.format("%-18s %3s %7s %9s", "Item", "Qty", "Price", "Amount")).append("\n");
        sb.append(SINGLE_LINE).append("\n");
        for (CartItem ci : items) {
            sb.append(String.format("%-18s %3d %7s %9s",
                    ci.getFood().getName(),
                    ci.getQuantity(),
                    Theme.money(ci.getFood().getPrice()),
                    Theme.money(ci.getSubtotal()))).append("\n");
        }
    }

    private static String center(String text) {
        int spaces = Math.max(0, (WIDTH - text.length()) / 2);
        return " ".repeat(spaces) + text;
    }
}
