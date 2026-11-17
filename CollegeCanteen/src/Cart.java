import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Cart - the shopping cart. All cart rules live here:
 * adding, increasing, decreasing, removing, clearing and calculating the total.
 *
 * Screens ("panels") register a listener so they refresh automatically
 * whenever the cart changes.
 */
public class Cart {

    public static final int MAX_QTY = 20;   // maximum quantity allowed for one item

    // LinkedHashMap keeps items in the order they were added. Key = food name.
    private final Map<String, CartItem> items = new LinkedHashMap<>();
    private final List<Runnable> listeners = new ArrayList<>();

    public void addChangeListener(Runnable listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    /** Adds an item. Throws IllegalArgumentException (with a friendly message) if invalid. */
    public void addItem(FoodItem food, int quantity) {
        if (food == null) {
            throw new IllegalArgumentException("No food item selected.");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }
        CartItem existing = items.get(food.getName());
        int newQuantity = quantity + (existing == null ? 0 : existing.getQuantity());
        if (newQuantity > MAX_QTY) {
            throw new IllegalArgumentException(
                "You can order at most " + MAX_QTY + " of each item.\n"
                + "Your cart already has " + (existing == null ? 0 : existing.getQuantity())
                + " x " + food.getName() + ".");
        }
        if (existing == null) {
            items.put(food.getName(), new CartItem(food, quantity));
        } else {
            existing.setQuantity(newQuantity);
        }
        notifyListeners();
    }

    public void increase(String foodName) {
        CartItem item = items.get(foodName);
        if (item == null) return;
        if (item.getQuantity() >= MAX_QTY) {
            throw new IllegalArgumentException("You can order at most " + MAX_QTY + " of each item.");
        }
        item.setQuantity(item.getQuantity() + 1);
        notifyListeners();
    }

    /** Decreases by one. The quantity never goes below 1 (use Remove to delete the item). */
    public void decrease(String foodName) {
        CartItem item = items.get(foodName);
        if (item == null) return;
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            notifyListeners();
        }
    }

    public void remove(String foodName) {
        if (items.remove(foodName) != null) {
            notifyListeners();
        }
    }

    public void clear() {
        items.clear();
        notifyListeners();
    }

    /** Returns a copy of the list so outside code cannot break the cart. */
    public List<CartItem> getItems() {
        return new ArrayList<>(items.values());
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getTotal() {
        int total = 0;
        for (CartItem item : items.values()) {
            total += item.getSubtotal();
        }
        return total;
    }

    /** Total number of food pieces in the cart (used for the "Cart (3)" badge). */
    public int getTotalQuantity() {
        int count = 0;
        for (CartItem item : items.values()) {
            count += item.getQuantity();
        }
        return count;
    }
}
