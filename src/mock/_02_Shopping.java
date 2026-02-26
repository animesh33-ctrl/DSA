package mock;
import java.util.*;

class Item {
    String itemId;
    String name;
    double price;
    int quantity;

    public Item(String itemId,String name,double price,int quantity){
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}

interface IShoppingCart {
    void addItem(String itemId, String name, double price, int quantity);
    void removeItem(String itemId);
    void updateQuantity(String itemId, int newQuantity);
    double getTotalPrice();
    List<Item> getAllItems();
    int getTotalItemCount();
}

public class _02_Shopping implements IShoppingCart{
    List<Item> items = new ArrayList<>();

    @Override
    public void addItem(String itemId, String name, double price, int quantity) {
        items.add(new Item(itemId,name,price,quantity));
        System.out.println(name + " successfully added");
    }

    @Override
    public void removeItem(String itemId) {
        items.removeIf(item -> item.itemId.equalsIgnoreCase(itemId));
        System.out.println("Successfully Removed If Present");
    }

    @Override
    public void updateQuantity(String itemId, int newQuantity) {
        items.stream().filter(item -> item.itemId.equalsIgnoreCase(itemId)).findAny().ifPresentOrElse(item -> {
            item.quantity = newQuantity;
            System.out.println("Successfully Updated");
        }, ()-> System.out.println("Item Not Present")
        );
    }


    @Override
    public double getTotalPrice() {
        return items.stream().mapToDouble(item->item.price*item.quantity).sum();
    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(items);
    }

    @Override
    public int getTotalItemCount() {
        return items.stream().mapToInt(item->item.quantity).sum();
    }
}

class ShoppingTest {

    public static void main(String[] args) {

        IShoppingCart cart = new _02_Shopping();

        System.out.println("========== ADD ITEMS ==========");
        cart.addItem("I101", "Apple", 50.0, 2);     // 100
        cart.addItem("I102", "Banana", 10.0, 5);    // 50
        cart.addItem("I103", "Milk", 60.0, 1);      // 60
        cart.addItem("I104", "Bread", 40.0, 3);     // 120

        System.out.println("\n========== ALL ITEMS ==========");
        for (Item item : cart.getAllItems()) {
            System.out.println(
                    item.itemId + " | " +
                            item.name + " | " +
                            item.price + " | " +
                            item.quantity
            );
        }

        System.out.println("\n========== TOTAL PRICE ==========");
        System.out.println("Expected Total = 330");
        System.out.println("Actual Total   = " + cart.getTotalPrice());

        System.out.println("\n========== TOTAL ITEM COUNT ==========");
        System.out.println("Distinct Items = " + cart.getTotalItemCount());

        System.out.println("\n========== UPDATE QUANTITY ==========");
        cart.updateQuantity("I102", 10);   // Banana → 10
        cart.updateQuantity("I103", 4);    // Milk → 4

        System.out.println("\n========== ITEMS AFTER UPDATE ==========");
        for (Item item : cart.getAllItems()) {
            System.out.println(
                    item.itemId + " | " +
                            item.name + " | " +
                            item.price + " | " +
                            item.quantity
            );
        }

        System.out.println("\n========== TOTAL PRICE AFTER UPDATE ==========");
        System.out.println("Expected Total = 560");
        System.out.println("Actual Total   = " + cart.getTotalPrice());

        System.out.println("\n========== REMOVE ITEM ==========");
        cart.removeItem("I101"); // Remove Apple
        cart.removeItem("I999"); // Non-existing item

        System.out.println("\n========== ITEMS AFTER REMOVAL ==========");
        for (Item item : cart.getAllItems()) {
            System.out.println(
                    item.itemId + " | " +
                            item.name + " | " +
                            item.price + " | " +
                            item.quantity
            );
        }

        System.out.println("\n========== FINAL TOTAL PRICE ==========");
        System.out.println(cart.getTotalPrice());

        System.out.println("\n========== FINAL ITEM COUNT ==========");
        System.out.println(cart.getTotalItemCount());

        System.out.println("\n========== EDGE CASE TESTS ==========");
        cart.updateQuantity("I999", 5); // Non-existing
        cart.removeItem("I999");        // Non-existing

        System.out.println("\n========== FINAL STATE ==========");
        for (Item item : cart.getAllItems()) {
            System.out.println(
                    item.itemId + " | " +
                            item.name + " | " +
                            item.price + " | " +
                            item.quantity
            );
        }

        System.out.println("\n🎉 ALL TESTS COMPLETED 🎉");
    }
}
