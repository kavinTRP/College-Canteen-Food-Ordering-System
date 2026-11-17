import java.util.ArrayList;
import java.util.List;

/**
 * MenuData - the ONE place where the canteen menu and prices are stored.
 *
 * TO CHANGE A PRICE   : edit the number in the add(...) line.
 * TO ADD A NEW ITEM   : copy one add(...) line and change the values.
 * TO REMOVE AN ITEM   : delete its add(...) line.
 * Nothing else in the project needs to be changed.
 */
public class MenuData {

    // The order of this array decides the order of the category buttons.
    public static final String[] CATEGORIES = {"Breakfast", "Lunch", "Snacks", "Beverages"};

    private static final List<FoodItem> ITEMS = new ArrayList<>();

    // This block runs once, when the program starts, and fills the menu.
    static {
        // ---------- Breakfast ----------
        add("Breakfast", "Idli", 20);
        add("Breakfast", "Dosa", 30);
        add("Breakfast", "Poori", 35);
        add("Breakfast", "Pongal", 30);
        add("Breakfast", "Vada", 10);

        // ---------- Lunch ----------
        add("Lunch", "Meals", 70);
        add("Lunch", "Lemon Rice", 40);
        add("Lunch", "Tomato Rice", 40);
        add("Lunch", "Curd Rice", 35);
        add("Lunch", "Veg Biryani", 60);

        // ---------- Snacks ----------
        add("Snacks", "Samosa", 15);
        add("Snacks", "Veg Puff", 20);
        add("Snacks", "Sandwich", 40);
        add("Snacks", "French Fries", 50);

        // ---------- Beverages ----------
        add("Beverages", "Tea", 10);
        add("Beverages", "Coffee", 15);
        add("Beverages", "Fresh Lime Juice", 25);
        add("Beverages", "Water Bottle", 20);
    }

    private static void add(String category, String name, int price) {
        ITEMS.add(new FoodItem(name, price, category));
    }

    /** Returns all items that belong to the given category. */
    public static List<FoodItem> getItemsByCategory(String category) {
        List<FoodItem> result = new ArrayList<>();
        for (FoodItem item : ITEMS) {
            if (item.getCategory().equals(category)) {
                result.add(item);
            }
        }
        return result;
    }
}
