/**
 * CartItem - one line in the shopping cart: a food item + how many the student wants.
 */
public class CartItem {

    private final FoodItem food;
    private int quantity;

    public CartItem(FoodItem food, int quantity) {
        this.food = food;
        this.quantity = quantity;
    }

    public FoodItem getFood()  { return food; }
    public int getQuantity()   { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    /** Price of this line = unit price x quantity. */
    public int getSubtotal() {
        return food.getPrice() * quantity;
    }
}
