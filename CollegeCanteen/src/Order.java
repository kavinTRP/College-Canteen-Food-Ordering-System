import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Order - a confirmed order. It keeps its own COPY of the cart items,
 * so clearing the cart afterwards does not affect the bill.
 */
public class Order {

    private static int counter = 0;   // counts orders placed since the app started

    private final String orderId;
    private final String studentName;
    private final String rollNo;
    private final LocalDateTime orderTime;
    private final List<CartItem> items = new ArrayList<>();
    private final int total;

    public Order(String studentName, String rollNo, List<CartItem> cartItems) {
        this.studentName = studentName;
        this.rollNo = rollNo;
        this.orderTime = LocalDateTime.now();
        this.orderId = generateOrderId(orderTime);

        int sum = 0;
        for (CartItem ci : cartItems) {
            items.add(new CartItem(ci.getFood(), ci.getQuantity()));
            sum += ci.getSubtotal();
        }
        this.total = sum;
    }

    /** Order ID looks like  ORD-20261023-153045-001  (date, time, running number). */
    private static synchronized String generateOrderId(LocalDateTime time) {
        counter++;
        String stamp = time.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        return "ORD-" + stamp + "-" + String.format("%03d", counter);
    }

    public String getOrderId()          { return orderId; }
    public String getStudentName()      { return studentName; }
    public String getRollNo()           { return rollNo; }
    public LocalDateTime getOrderTime() { return orderTime; }
    public int getTotal()               { return total; }
    public List<CartItem> getItems()    { return Collections.unmodifiableList(items); }
}
