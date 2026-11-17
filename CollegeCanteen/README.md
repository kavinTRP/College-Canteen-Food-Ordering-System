# College Canteen Food Ordering System (Java Swing)

A complete mini project: view the menu, add food to a cart, change quantities, place an order, and get a formatted bill.
Tested: it compiles with JDK 21 and a 26-step automated test of every feature passed.

---

## 1. Technology choice

| Choice | Why it suits a beginner |
|---|---|
| **Java Swing** (not JavaFX) | Swing is **built into the JDK**. No extra downloads, no module setup, runs in VS Code with a single click. JavaFX needs separate SDK files and extra configuration, which is the #1 reason beginner projects fail to start. |
| **Plain OOP classes** | Each class has one job (see section 3), which is easy to explain in a viva. |
| **No database yet** | Menu lives in `MenuData.java`. A MySQL upgrade path is in section 10. |

OOP concepts used (good for your viva): classes and objects, encapsulation (private fields and getters), composition (`Cart` has `CartItem`s, `CartItem` has a `FoodItem`), inheritance (`MainFrame extends JFrame`, `RoundButton extends JButton`), static methods and blocks, collections (`List`, `Map`), exception handling (`try/catch`, `IllegalArgumentException`), lambda expressions for button clicks.

---

## 2. Screens

1. **Home**: logo placeholder, project name, category line, **Order Now**.
2. **Menu**: category buttons (Breakfast / Lunch / Snacks / Beverages), food cards (picture, name, price, quantity box, **Add to Cart**), and a mini cart on the right.
3. **Cart**: change quantities with **-** / **+**, **Remove**, enter name and roll number, live total, **Cancel Order**, **Place Order**.
4. **Order Summary** (pop-up before checkout): **Confirm Order** or **Cancel**.
5. **Confirmation + Bill**: green banner, unique Order ID, formatted bill, **Save Bill**, **Order More Food**, **Back to Home**.

A navigation bar (Home, Menu, Cart (n)) is visible on every screen.

---

## 3. Folder structure

```
CollegeCanteen/
├── src/
│   ├── Main.java              <- START HERE (run this file)
│   ├── MainFrame.java         main window + navigation bar + page switching
│   ├── HomePanel.java         home page
│   ├── MenuPanel.java         menu page (food cards + mini cart)
│   ├── CartPanel.java         cart page, validation, order summary
│   ├── BillPanel.java         confirmation page + bill + save to file
│   ├── FoodItem.java          model: one menu item
│   ├── MenuData.java          THE MENU AND PRICES (edit here)
│   ├── CartItem.java          model: one cart line (item + quantity)
│   ├── Cart.java              cart logic: add / increase / decrease / remove / total
│   ├── Order.java             model: confirmed order + unique Order ID
│   ├── BillGenerator.java     builds the bill / summary text
│   ├── Theme.java             colours, fonts, rupee formatting
│   ├── RoundButton.java       custom rounded button
│   ├── LogoPanel.java         round LOGO placeholder
│   └── FoodImagePanel.java    food picture (or coloured placeholder)
├── images/                    optional food photos (see images/README.txt)
├── bills/                     saved bills appear here
├── run.bat                    Windows: double-click to run
├── run.sh                     Mac/Linux: ./run.sh
└── README.md
```

---

## 4. Software required

1. **JDK 17 or newer** (the Java compiler; JRE alone is NOT enough)
2. **Visual Studio Code**
3. **Extension Pack for Java** (VS Code extension by Microsoft)

---

## 5. Installation (Windows, step by step)

### Step 1: Install the JDK
1. Open https://adoptium.net and download **Temurin 21 (LTS)**, Windows `.msi`.
2. Run the installer. On the "Custom Setup" screen, enable **"Set JAVA_HOME variable"** and **"Add to PATH"**.
3. Finish the install, then **close and reopen** any Command Prompt.

### Step 2: Verify
Open Command Prompt and type:
```
java -version
javac -version
```
Both must print a version number (17 or higher). If `javac` says "not recognized", the PATH option was missed: re-run the installer and enable it.

### Step 3: Install VS Code and the Java extension
1. Install VS Code from https://code.visualstudio.com
2. Open VS Code, click the **Extensions** icon (left bar, four squares), search **Extension Pack for Java**, click **Install**.
3. Wait until the bottom-right "Java: Ready" message appears.

---

## 6. Setting up the project

1. Unzip `CollegeCanteen.zip` to e.g. `D:\Projects\CollegeCanteen`.
2. In VS Code: **File > Open Folder...** and select the `CollegeCanteen` folder (the one containing `src`).
3. Click **Trust** if VS Code asks.
4. Wait for the Java extension to finish loading (spinner at bottom-left).

Do not move files out of `src`. VS Code detects that folder automatically.

---

## 7. Running the project

**Option A: VS Code (easiest)**
1. In the Explorer panel, open `src/Main.java`.
2. Click the **Run** link above `public static void main`, or press **Ctrl+F5**.
3. The window opens.

**Option B: Windows double-click:** double-click `run.bat`.

**Option C: Command line** (from inside the `CollegeCanteen` folder):
```
mkdir out
javac -d out src\*.java
java -cp out Main
```
On Mac/Linux use `src/*.java`.

> Food photos and saved bills use relative folders (`images/`, `bills/`), so run from the **project root** (Options A, B and C all do).

---

## 8. Changing the menu, prices and pictures

**Open `src/MenuData.java`.** Every item is one line:
```java
add("Breakfast", "Idli", 20);
```
- Change a price: edit the number.
- Add an item: copy a line, change category/name/price.
- Remove an item: delete its line.
- Add a category: add its name to `CATEGORIES` and add items with that category name. Also add a colour for it in `Theme.categoryColor`.

**Pictures:** save photos in `images/` using lowercase names with underscores (`veg_biryani.png`). Full list in `images/README.txt`. No photo means a coloured placeholder is shown.

**Colours:** edit the constants at the top of `Theme.java`.

---

## 9. Testing every feature

| # | Feature | How to test | Expected result |
|---|---|---|---|
| 1 | Home page | Launch app | Logo, "College Canteen / Food Ordering System", Order Now |
| 2 | Menu display | Click **Order Now** | Breakfast cards with names and prices |
| 3 | Categories | Click Lunch, Snacks, Beverages | Cards change; selected tab turns orange |
| 4 | Quantity | Set Qty to 3 on Dosa | Spinner changes 1-20 |
| 5 | Add to cart | Click **Add to Cart** | Green message, mini cart updates, nav shows `Cart (3)` |
| 6 | Quantity validation | Type `abc` in a Qty box, Add to Cart | "Invalid Quantity" pop-up, nothing added |
| 7 | Limit | Add Idli until above 20 total | "Cannot Add Item" pop-up |
| 8 | Increase / decrease | In Cart page click **+** and **-** | Qty and total change; **-** is disabled at 1 |
| 9 | Remove | Click **Remove** | Row disappears, total drops |
| 10 | Auto total | Add several items | Total = sum of price x quantity |
| 11 | Student validation | Click **Place Order** with blank name, then roll `ab` | "Invalid Name" / "Invalid Roll Number" |
| 12 | Order summary | Valid name (`Ravi Kumar`) + roll no (`CSE2024`), **Place Order** | Summary pop-up with items and total |
| 13 | Cancel before confirming | In summary click **Cancel** | Cart kept; or use **Cancel Order** button, confirm Yes, cart is emptied |
| 14 | Confirm + Order ID | Click **Confirm Order** | Green "Order Confirmed!" page with ID like `ORD-20261023-153045-001` |
| 15 | Bill | Look at the bill area | Aligned columns, date, student, total |
| 16 | Save bill | Click **Save Bill** | File created in `bills/` |
| 17 | Unique IDs | Place a second order | New, different Order ID |
| 18 | Empty cart | Open Cart page empty, **Place Order** | "Empty Cart" warning |
| 19 | Home button | Click **Back to Home** (or nav **Home**) | Home page |

---

## 10. Troubleshooting

| Problem | Fix |
|---|---|
| `javac` is not recognized | JDK not on PATH. Reinstall Temurin and tick "Add to PATH". |
| VS Code shows no Run link | Wait for "Java: Ready"; make sure you opened the **CollegeCanteen** folder, not its parent. |
| `Main` class not found | Run from the project root; recompile with `javac -d out src\*.java`. |
| `₹` shows as a box | Your system font lacks it. In `Theme.java` set `RUPEE = "Rs. "`. |
| Yellow warnings about `serialVersionUID` | Harmless; ignore. |
| Images not showing | Check lowercase file name with underscores, and that `images/` is next to `src/`. |

---

## 11. Future enhancements

### 11.1 Student login and registration
1. Create `Student.java` (`id`, `name`, `rollNo`, `passwordHash`).
2. Create `LoginPanel.java` and `RegisterPanel.java` with `JTextField` / `JPasswordField` and add them to `MainFrame` as new CardLayout pages (`"LOGIN"`, `"REGISTER"`).
3. Show Login first; on success remember the student in `MainFrame` (`private Student currentStudent;`).
4. Remove the Name/Roll No fields from `CartPanel` and use `currentStudent` instead.
5. **Never store plain passwords.** Hash them (SHA-256 with a salt, or BCrypt library).

### 11.2 MySQL database integration
1. Install **MySQL Server** + **MySQL Workbench** (https://dev.mysql.com/downloads/installer/).
2. Create the database:
```sql
CREATE DATABASE canteen_db;
USE canteen_db;

CREATE TABLE food_items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE,
  category VARCHAR(20) NOT NULL,
  price INT NOT NULL,
  available BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE students (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  roll_no VARCHAR(15) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL
);

CREATE TABLE orders (
  order_id VARCHAR(30) PRIMARY KEY,
  student_id INT NOT NULL,
  total INT NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'PLACED',
  created_at DATETIME NOT NULL,
  FOREIGN KEY (student_id) REFERENCES students(id)
);

CREATE TABLE order_items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  order_id VARCHAR(30) NOT NULL,
  food_name VARCHAR(50) NOT NULL,
  quantity INT NOT NULL,
  unit_price INT NOT NULL,
  FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
```
3. Download **MySQL Connector/J** (`mysql-connector-j-x.x.x.jar`) from https://dev.mysql.com/downloads/connector/j/ and put it in a new `lib/` folder. In VS Code, open **Java Projects** (left panel) > **Referenced Libraries** > **+** and select the jar.
4. Create `Database.java`:
```java
import java.sql.*;

public class Database {
    private static final String URL  = "jdbc:mysql://localhost:3306/canteen_db";
    private static final String USER = "root";
    private static final String PASS = "your_mysql_password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
```
5. Change `MenuData.getItemsByCategory` to run `SELECT name, price FROM food_items WHERE category = ? AND available = TRUE` using a `PreparedStatement`, and build `FoodItem` objects from the result.
6. In `CartPanel.placeOrder`, after `new Order(...)`, insert rows into `orders` and `order_items`.
7. Always use `PreparedStatement` (never string concatenation) to prevent SQL injection.

### 11.3 Order history
After step 11.2: add `OrderHistoryPanel.java` with a `JTable`, loaded with `SELECT * FROM orders WHERE student_id = ? ORDER BY created_at DESC`. Add an **My Orders** button to the nav bar. Double-click a row to rebuild the bill from `order_items`.

### 11.4 Admin dashboard
Create `AdminLoginPanel` (fixed admin account) and `AdminPanel` with tabs: **Today's Orders** (JTable), **Manage Menu** (add/edit/delete food rows), **Sales Summary** (`SELECT SUM(total) ...`). Keep it in a separate window class `AdminFrame` so students cannot reach it.

### 11.5 Food availability management
The `available` column above is already there. In the admin Manage Menu tab add an **Available** checkbox that runs `UPDATE food_items SET available = ? WHERE id = ?`. In `FoodItem` add `boolean available`; in `MenuPanel.createFoodCard` disable the Add button and show "Sold Out" when false.

### 11.6 Payment integration
- **Step 1 (easy):** add a payment choice (Cash / UPI) in the summary dialog and store it in `orders`.
- **Step 2 (UPI QR):** generate a QR code for `upi://pay?pa=<your-upi-id>&pn=Canteen&am=<total>&cu=INR` with the **ZXing** library and show it in a dialog.
- **Step 3 (real gateway):** use a gateway such as Razorpay's Java SDK. This needs a server and test keys, so do it as a web version later.

### 11.7 Order status tracking
Statuses: `PLACED -> PREPARING -> READY -> COLLECTED`. The `status` column exists. The admin dashboard updates it with a dropdown. Add `TrackOrderPanel` for students: enter Order ID, query `SELECT status FROM orders WHERE order_id = ?`, and refresh every 10 seconds with `javax.swing.Timer`.

---

## 12. How the code flows (for your viva)

1. `Main` starts Swing and opens `MainFrame`.
2. `MainFrame` creates **one** `Cart` and gives the same object to `MenuPanel` and `CartPanel`.
3. `MenuPanel` reads food from `MenuData`, draws cards, and calls `cart.addItem(...)`.
4. `Cart` changes its data, then calls its **listeners** (this is the Observer pattern). The nav badge, mini cart and cart page refresh themselves.
5. `CartPanel.placeOrder` validates input, shows `BillGenerator.generateSummary`, and on Confirm creates an `Order` (which makes the unique ID) and clears the cart.
6. `MainFrame.showBill` passes the `Order` to `BillPanel`, which prints `BillGenerator.generateBill`.
