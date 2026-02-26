package mock;
import java.util.*;

class FoodItem {
    String itemId;
    String name;
    double price;
    boolean isAvailable;

//    public FoodItem(String itemId, String name, double price, boolean isAvailable) {
//        this.itemId = itemId;
//        this.name = name;
//        this.price = price;
//        this.isAvailable = isAvailable;
//    }
}

class Order {
    String orderId;
    String customerName;
    List<FoodItem> items;
    double totalAmount;
    OrderStatus status;

    public Order() {
    }

    public Order(String orderId, String customerName, List<FoodItem> items, double totalAmount, OrderStatus status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", items=" + items +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                '}';
    }
}

enum OrderStatus { PENDING, PREPARING, READY, DELIVERED }

interface IFoodOrder {
    void addMenuItem(FoodItem item);
    String placeOrder(String customerName, List<String> itemIds);
    void updateOrderStatus(String orderId, OrderStatus status);
    double getOrderTotal(String orderId);
    List<Order> getPendingOrders();
}

public class _03_FoodOrderSystem implements IFoodOrder{
    Map<String, FoodItem> foodItemMap = new HashMap<>();
    Map<String, Order> orderMap = new HashMap<>();
    int orderCounter = 1;

    @Override
    public void addMenuItem(FoodItem item) {
        if (foodItemMap.putIfAbsent(item.itemId, item) == null) {
            System.out.println(item.name + " successfully added");
        } else {
            System.out.println(item.name + " already present");
        }
    }


    @Override
    public String placeOrder(String customerName, List<String> itemIds) {

        List<FoodItem> orderedItems = new ArrayList<>();
        double total = 0;

        for (String id : itemIds) {
            FoodItem item = foodItemMap.get(id);
            if (item == null || !item.isAvailable) {
                System.out.println("Item unavailable: " + id);
                return null;
            }
            orderedItems.add(item);
            total += item.price;
        }

        String orderId = "ORD" + orderCounter++;
        Order order = new Order(orderId, customerName, orderedItems, total, OrderStatus.PENDING);

        orderMap.put(orderId, order);

        System.out.println("Order placed successfully: " + orderId);
        return orderId;
    }


    @Override
    public void updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orderMap.get(orderId);
        if (order != null) {
            order.status = status;
            System.out.println("Order status updated");
        } else {
            System.out.println("Order not found");
        }
    }


    @Override
    public double getOrderTotal(String orderId) {
        Order order = orderMap.get(orderId);
        return order != null ? order.totalAmount : 0.0;
    }


    @Override
    public List<Order> getPendingOrders() {
        return orderMap.values()
                .stream()
                .filter(o -> o.status == OrderStatus.PENDING)
                .toList();
    }

}

class FoodOrderTest {

    public static void main(String[] args) {

        IFoodOrder system = new _03_FoodOrderSystem();

        System.out.println("========== ADD MENU ITEMS ==========");
        FoodItem f1 = new FoodItem();
        f1.itemId = "F101";
        f1.name = "Burger";
        f1.price = 120;
        f1.isAvailable = true;

        FoodItem f2 = new FoodItem();
        f2.itemId = "F102";
        f2.name = "Pizza";
        f2.price = 250;
        f2.isAvailable = true;

        FoodItem f3 = new FoodItem();
        f3.itemId = "F103";
        f3.name = "Pasta";
        f3.price = 180;
        f3.isAvailable = false; // unavailable item

        system.addMenuItem(f1);
        system.addMenuItem(f2);
        system.addMenuItem(f3);
        system.addMenuItem(f1); // duplicate check

        System.out.println("\n========== PLACE ORDER (VALID) ==========");
        List<String> order1Items = Arrays.asList("F101", "F102");
        String orderId1 = system.placeOrder("Animesh", order1Items);
        System.out.println("Returned Order ID: " + orderId1);

        System.out.println("\n========== PLACE ORDER (UNAVAILABLE ITEM) ==========");
        List<String> order2Items = Arrays.asList("F101", "F103");
        String orderId2 = system.placeOrder("Rahul", order2Items);
        System.out.println("Returned Order ID: " + orderId2);

        System.out.println("\n========== PLACE ORDER (INVALID ITEM ID) ==========");
        List<String> order3Items = Arrays.asList("F101", "F999");
        system.placeOrder("Rohit", order3Items);

        System.out.println("\n========== GET ORDER TOTAL ==========");
        System.out.println("Total for " + orderId1 + " = " +
                system.getOrderTotal(orderId1));

        System.out.println("\n========== UPDATE ORDER STATUS ==========");
        system.updateOrderStatus(orderId1, OrderStatus.PREPARING);
        system.updateOrderStatus(orderId1, OrderStatus.READY);
        system.updateOrderStatus("ORD999", OrderStatus.DELIVERED); // invalid order

        System.out.println("\n========== PENDING ORDERS ==========");
        List<Order> pendingOrders = system.getPendingOrders();
        for (Order o : pendingOrders) {
            System.out.println(
                    o.orderId + " | " +
                            o.customerName + " | " +
                            o.totalAmount + " | " +
                            o.status
            );
        }

        System.out.println("\n========== FINAL CHECK ==========");
        System.out.println("If no errors & outputs match expectations → SYSTEM IS CORRECT");

        System.out.println("\n🎉 ALL TESTS COMPLETED 🎉");
    }
}
