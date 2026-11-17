/**
 * FoodItem - represents ONE item on the canteen menu.
 * It only stores data (name, price, category), so it is called a "model" class.
 */
public class FoodItem {

    private final String name;
    private final int price;        // price in rupees
    private final String category;  // Breakfast, Lunch, Snacks or Beverages

    public FoodItem(String name, int price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName()     { return name; }
    public int getPrice()       { return price; }
    public String getCategory() { return category; }
}
