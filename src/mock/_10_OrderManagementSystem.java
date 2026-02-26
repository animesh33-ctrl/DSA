package mock;

import java.util.*;
import java.io.*;
import java.util.stream.*;
import java.time.*;

enum OrderStatus2 {
    PLACED, CONFIRMED, PACKED, SHIPPED, OUT_FOR_DELIVERY, DELIVERED, CANCELLED, RETURNED
}

enum PaymentMethod2 {
    CASH_ON_DELIVERY, CREDIT_CARD, DEBIT_CARD, UPI, WALLET, NET_BANKING
}

enum PaymentStatus2 {
    PENDING, SUCCESS, FAILED, REFUNDED
}

class OrderItem {
    String productId;
    String productName;
    int quantity;
    double pricePerUnit;

    public OrderItem(String productId, String productName, int qty, double price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = qty;
        this.pricePerUnit = price;
    }

    public double getSubtotal() {
        return quantity * pricePerUnit;
    }

    // Serialize to compact format
    public String serialize() {
        return productId + ":" + productName + ":" + quantity + ":" + pricePerUnit;
    }

    // Deserialize from compact format
    public static OrderItem deserialize(String data) {
        String[] parts = data.split(":");
        return new OrderItem(parts[0], parts[1],
                Integer.parseInt(parts[2]),
                Double.parseDouble(parts[3]));
    }
}

class Order2 {
    String orderId;
    String customerId;
    List<OrderItem> items;
    OrderStatus2 status;
    PaymentMethod2 paymentMethod;
    PaymentStatus2 paymentStatus;
    double totalAmount;
    double discountAmount;
    double deliveryCharges;
    long orderTimestamp;
    long deliveryTimestamp;
    String deliveryAddress;

    public Order2(String orderId, String customerId, List<OrderItem> items,
                 PaymentMethod2 paymentMethod, String address) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.paymentMethod = paymentMethod;
        this.deliveryAddress = address;
        this.status = OrderStatus2.PLACED;
        this.paymentStatus = PaymentStatus2.PENDING;
        this.orderTimestamp = System.currentTimeMillis();
        this.discountAmount = 0.0;
        this.deliveryCharges = 50.0;
        calculateTotal();
    }

    private void calculateTotal() {
        double subtotal = items.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
        this.totalAmount = subtotal - discountAmount + deliveryCharges;
    }

    public void applyDiscount(double amount) {
        this.discountAmount = amount;
        calculateTotal();
    }

    public String toCSV() {
        // Serialize items as pipe-separated list
        String itemsStr = items.stream()
                .map(OrderItem::serialize)
                .collect(Collectors.joining("|"));

        return orderId + "," +
                customerId + "," +
                itemsStr + "," +
                status.name() + "," +
                paymentMethod.name() + "," +
                paymentStatus.name() + "," +
                totalAmount + "," +
                discountAmount + "," +
                deliveryCharges + "," +
                orderTimestamp + "," +
                deliveryTimestamp + "," +
                deliveryAddress.replace(",", ";");
    }

    public static Order2 fromCSV(String line) throws InvalidOrderException {
        if (line == null || line.trim().isEmpty()) {
            throw new InvalidOrderException("CSV line is empty");
        }

        String[] parts = line.split(",");

        if (parts.length != 12) {
            throw new InvalidOrderException(
                    "Invalid CSV format. Expected 12 fields but got " + parts.length
            );
        }

        try {
            String orderId = parts[0].trim();
            String customerId = parts[1].trim();

            // Deserialize items
            String itemsStr = parts[2].trim();
            List<OrderItem> items = new ArrayList<>();
            if (!itemsStr.isEmpty()) {
                String[] itemParts = itemsStr.split("\\|");
                for (String itemStr : itemParts) {
                    items.add(OrderItem.deserialize(itemStr));
                }
            }

            OrderStatus2 status = OrderStatus2.valueOf(parts[3].trim());
            PaymentMethod2 paymentMethod = PaymentMethod2.valueOf(parts[4].trim());
            PaymentStatus2 paymentStatus = PaymentStatus2.valueOf(parts[5].trim());
            double totalAmount = Double.parseDouble(parts[6].trim());
            double discountAmount = Double.parseDouble(parts[7].trim());
            double deliveryCharges = Double.parseDouble(parts[8].trim());
            long orderTimestamp = Long.parseLong(parts[9].trim());
            long deliveryTimestamp = Long.parseLong(parts[10].trim());
            String deliveryAddress = parts[11].trim().replace(";", ",");

            Order2 order = new Order2(orderId, customerId, items, paymentMethod, deliveryAddress);
            order.status = status;
            order.paymentStatus = paymentStatus;
            order.totalAmount = totalAmount;
            order.discountAmount = discountAmount;
            order.deliveryCharges = deliveryCharges;
            order.orderTimestamp = orderTimestamp;
            order.deliveryTimestamp = deliveryTimestamp;

            return order;

        } catch (Exception e) {
            throw new InvalidOrderException("Error parsing CSV: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Order2{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", items=" + items.size() +
                ", status=" + status +
                ", totalAmount=" + totalAmount +
                '}';
    }
}

class InvalidOrderException extends Exception {
    public InvalidOrderException(String message) {
        super(message);
    }
}

interface IOrderStore {
    void storeOrders(Queue<Order2> orders) throws IOException;
    Queue<Order2> getAllOrders();
    void saveToFile(String filename) throws IOException;
    void loadFromFile(String filename) throws IOException, InvalidOrderException;
}

interface IOrderManagementSystem {
    void placeOrder(String customerId, List<OrderItem> items,
                    PaymentMethod2 paymentMethod, String address);
    void updateOrderStatus(String orderId, OrderStatus2 status);
    void updatePaymentStatus(String orderId, PaymentStatus2 status);
    void applyDiscount(String orderId, double amount);
    void cancelOrder(String orderId);

    int getTotalOrders();
    int getPendingOrders();
    void flush() throws IOException;

    // Stream-based analytics
    Map<OrderStatus2, Long> getOrdersByStatus();
    Map<PaymentMethod2, Long> getOrdersByPaymentMethod();
    Map<PaymentStatus2, Long> getOrdersByPaymentStatus();
    double getTotalRevenue();
    double getAverageOrderValue();

    // Lambda filters
    List<Order2> getOrdersByCustomer(String customerId);
    List<Order2> getOrdersByStatus(OrderStatus2 status);
    List<Order2> getFailedPayments();
    List<Order2> getPendingDeliveries();
    List<Order2> getOrdersAboveAmount(double minAmount);
    List<Order2> getOrdersInDateRange(long start, long end);

    // Advanced analytics
    Map<String, Double> getRevenueByCustomer();
    Map<String, Long> getOrderCountByCustomer();
    List<String> getTopCustomers(int n); // By revenue
    List<String> getMostFrequentCustomers(int n); // By order count
    Map<String, Long> getProductSalesCount(); // productId -> quantity sold
    List<String> getTopSellingProducts(int n);
    double getAverageDeliveryTime(); // In hours
    double getCancellationRate();

    // File operations
    void exportOrders(String filename) throws IOException;
    void importOrders(String filename) throws IOException, InvalidOrderException;
    void generateCustomerReport(String customerId, String filename)
            throws IOException, ReportException3;
    void generateSalesReport(LocalDate start, LocalDate end, String filename)
            throws IOException, ReportException3;
}

class ReportException3 extends Exception {
    public ReportException3(String message) {
        super(message);
    }
}


class OrderStore implements IOrderStore {
    private Queue<Order2> orderQueue = new LinkedList<>();

    @Override
    public void storeOrders(Queue<Order2> orders) throws IOException {
        while (!orders.isEmpty()) {
            orderQueue.add(orders.poll());
        }
        System.out.println("✓ Orders stored successfully");
    }

    @Override
    public Queue<Order2> getAllOrders() {
        return new LinkedList<>(orderQueue);
    }

    @Override
    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Order2 order : orderQueue) {
                writer.write(order.toCSV());
                writer.newLine();
            }
        }
    }

    @Override
    public void loadFromFile(String filename) throws IOException, InvalidOrderException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                orderQueue.add(Order2.fromCSV(line));
            }
        }
    }
}

class OrderManagementSystem implements IOrderManagementSystem {
    private Queue<Order2> buffer = new LinkedList<>();
    private IOrderStore store;
    private int batchSize;
    private int orderCounter = 1;

    public OrderManagementSystem(IOrderStore store, int batchSize) {
        this.store = store;
        this.batchSize = batchSize;
    }

    @Override
    public void placeOrder(String customerId, List<OrderItem> items,
                           PaymentMethod2 paymentMethod, String address) {
        String orderId = "ORD-" + String.format("%06d", orderCounter++);
        Order2 order = new Order2(orderId, customerId, items, paymentMethod, address);
        buffer.add(order);

        if (buffer.size() >= batchSize) {
            try {
                store.storeOrders(buffer);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store orders: " + e.getMessage());
            }
        }
    }

    @Override
    public void updateOrderStatus(String orderId, OrderStatus2 status) {
        // Update in buffer
        buffer.stream()
                .filter(o -> o.orderId.equals(orderId))
                .forEach(o -> o.status = status);

        // Update in store
        store.getAllOrders().stream()
                .filter(o -> o.orderId.equals(orderId))
                .forEach(o -> {
                    o.status = status;
                    if (status == OrderStatus2.DELIVERED) {
                        o.deliveryTimestamp = System.currentTimeMillis();
                    }
                });
    }

    @Override
    public void updatePaymentStatus(String orderId, PaymentStatus2 status) {
        buffer.stream()
                .filter(o -> o.orderId.equals(orderId))
                .forEach(o -> o.paymentStatus = status);

        store.getAllOrders().stream()
                .filter(o -> o.orderId.equals(orderId))
                .forEach(o -> o.paymentStatus = status);
    }

    @Override
    public void applyDiscount(String orderId, double amount) {
        buffer.stream()
                .filter(o -> o.orderId.equals(orderId))
                .forEach(o -> o.applyDiscount(amount));

        store.getAllOrders().stream()
                .filter(o -> o.orderId.equals(orderId))
                .forEach(o -> o.applyDiscount(amount));
    }

    @Override
    public void cancelOrder(String orderId) {
        updateOrderStatus(orderId, OrderStatus2.CANCELLED);
    }

    @Override
    public int getTotalOrders() {
        return store.getAllOrders().size();
    }

    @Override
    public int getPendingOrders() {
        return buffer.size();
    }

    @Override
    public void flush() throws IOException {
        if (!buffer.isEmpty()) {
            store.storeOrders(buffer);
        }
    }

    // Stream-based analytics
    @Override
    public Map<OrderStatus2, Long> getOrdersByStatus() {
        return store.getAllOrders().stream()
                .collect(Collectors.groupingBy(
                        o -> o.status,
                        Collectors.counting()
                ));
    }

    @Override
    public Map<PaymentMethod2, Long> getOrdersByPaymentMethod() {
        return store.getAllOrders().stream()
                .collect(Collectors.groupingBy(
                        o -> o.paymentMethod,
                        Collectors.counting()
                ));
    }

    @Override
    public Map<PaymentStatus2, Long> getOrdersByPaymentStatus() {
        return store.getAllOrders().stream()
                .collect(Collectors.groupingBy(
                        o -> o.paymentStatus,
                        Collectors.counting()
                ));
    }

    @Override
    public double getTotalRevenue() {
        return store.getAllOrders().stream()
                .filter(o -> o.status == OrderStatus2.DELIVERED &&
                        o.paymentStatus == PaymentStatus2.SUCCESS)
                .mapToDouble(o -> o.totalAmount)
                .sum();
    }

    @Override
    public double getAverageOrderValue() {
        return store.getAllOrders().stream()
                .mapToDouble(o -> o.totalAmount)
                .average()
                .orElse(0.0);
    }

    // Lambda filters
    @Override
    public List<Order2> getOrdersByCustomer(String customerId) {
        return store.getAllOrders().stream()
                .filter(o -> o.customerId.equals(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order2> getOrdersByStatus(OrderStatus2 status) {
        return store.getAllOrders().stream()
                .filter(o -> o.status == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order2> getFailedPayments() {
        return store.getAllOrders().stream()
                .filter(o -> o.paymentStatus == PaymentStatus2.FAILED)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order2> getPendingDeliveries() {
        return store.getAllOrders().stream()
                .filter(o -> o.status == OrderStatus2.SHIPPED ||
                        o.status == OrderStatus2.OUT_FOR_DELIVERY)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order2> getOrdersAboveAmount(double minAmount) {
        return store.getAllOrders().stream()
                .filter(o -> o.totalAmount > minAmount)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order2> getOrdersInDateRange(long start, long end) {
        return store.getAllOrders().stream()
                .filter(o -> o.orderTimestamp >= start && o.orderTimestamp <= end)
                .collect(Collectors.toList());
    }

    // Advanced analytics
    @Override
    public Map<String, Double> getRevenueByCustomer() {
        return store.getAllOrders().stream()
                .filter(o -> o.status == OrderStatus2.DELIVERED &&
                        o.paymentStatus == PaymentStatus2.SUCCESS)
                .collect(Collectors.groupingBy(
                        o -> o.customerId,
                        Collectors.summingDouble(o -> o.totalAmount)
                ));
    }

    @Override
    public Map<String, Long> getOrderCountByCustomer() {
        return store.getAllOrders().stream()
                .collect(Collectors.groupingBy(
                        o -> o.customerId,
                        Collectors.counting()
                ));
    }

    @Override
    public List<String> getTopCustomers(int n) {
        return getRevenueByCustomer()
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getMostFrequentCustomers(int n) {
        return getOrderCountByCustomer()
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> getProductSalesCount() {
        return store.getAllOrders().stream()
                .filter(o -> o.status == OrderStatus2.DELIVERED)
                .flatMap(o -> o.items.stream())
                .collect(Collectors.groupingBy(
                        item -> item.productId,
                        Collectors.summingLong(item -> item.quantity)
                ));
    }

    @Override
    public List<String> getTopSellingProducts(int n) {
        return getProductSalesCount()
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public double getAverageDeliveryTime() {
        return store.getAllOrders().stream()
                .filter(o -> o.status == OrderStatus2.DELIVERED && o.deliveryTimestamp > 0)
                .mapToLong(o -> o.deliveryTimestamp - o.orderTimestamp)
                .average()
                .orElse(0.0) / (1000.0 * 60 * 60); // Convert to hours
    }

    @Override
    public double getCancellationRate() {
        long total = store.getAllOrders().size();
        if (total == 0) return 0.0;

        long cancelled = store.getAllOrders().stream()
                .filter(o -> o.status == OrderStatus2.CANCELLED)
                .count();

        return (cancelled * 100.0) / total;
    }

    // File operations
    @Override
    public void exportOrders(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Order2 order : store.getAllOrders()) {
                writer.write(order.toCSV());
                writer.newLine();
            }
        }
    }

    @Override
    public void importOrders(String filename) throws IOException, InvalidOrderException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.add(Order2.fromCSV(line));

                if (buffer.size() >= batchSize) {
                    store.storeOrders(buffer);
                }
            }
        }
    }

    @Override
    public void generateCustomerReport(String customerId, String filename)
            throws IOException, ReportException3 {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("=== CUSTOMER REPORT ===\n\n");
            writer.write("Customer ID: " + customerId + "\n\n");

            List<Order2> customerOrders = getOrdersByCustomer(customerId);

            writer.write("Total Orders: " + customerOrders.size() + "\n");

            double totalSpent = customerOrders.stream()
                    .filter(o -> o.status == OrderStatus2.DELIVERED)
                    .mapToDouble(o -> o.totalAmount)
                    .sum();
            writer.write("Total Spent: ₹" + String.format("%.2f", totalSpent) + "\n");

            double avgOrder = customerOrders.stream()
                    .mapToDouble(o -> o.totalAmount)
                    .average()
                    .orElse(0.0);
            writer.write("Average Order2 Value: ₹" + String.format("%.2f", avgOrder) + "\n\n");

            writer.write("--- Order2 History ---\n");
            for (Order2 order : customerOrders) {
                writer.write(String.format("%s | %s | ₹%.2f | %s\n",
                        order.orderId, order.status, order.totalAmount,
                        new Date(order.orderTimestamp)));
            }

        } catch (IOException e) {
            throw new ReportException3("Failed to generate customer report: " + e.getMessage());
        }
    }

    @Override
    public void generateSalesReport(LocalDate start, LocalDate end, String filename)
            throws IOException, ReportException3 {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("=== SALES REPORT ===\n\n");
            writer.write("Period: " + start + " to " + end + "\n\n");

            long startMs = start.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long endMs = end.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            List<Order2> periodOrders = getOrdersInDateRange(startMs, endMs);

            writer.write("Total Orders: " + periodOrders.size() + "\n");

            double revenue = periodOrders.stream()
                    .filter(o -> o.status == OrderStatus2.DELIVERED)
                    .mapToDouble(o -> o.totalAmount)
                    .sum();
            writer.write("Total Revenue: ₹" + String.format("%.2f", revenue) + "\n\n");

            writer.write("--- Orders by Status ---\n");
            periodOrders.stream()
                    .collect(Collectors.groupingBy(o -> o.status, Collectors.counting()))
                    .forEach((status, count) -> {
                        try {
                            writer.write(status + ": " + count + "\n");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

        } catch (IOException e) {
            throw new ReportException3("Failed to generate sales report: " + e.getMessage());
        }
    }
}

public class _10_OrderManagementSystem {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║    E-COMMERCE ORDER MANAGEMENT SYSTEM TEST          ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");

        try {
            IOrderStore store = new OrderStore();
            IOrderManagementSystem system = new OrderManagementSystem(store, 3);

            // Test: Place orders
            printSection("PLACE ORDERS");

            List<OrderItem> order1Items = Arrays.asList(
                    new OrderItem("P001", "Laptop", 1, 50000),
                    new OrderItem("P002", "Mouse", 2, 500)
            );
            system.placeOrder("C001", order1Items, PaymentMethod2.UPI, "123 Street, Mumbai");
            System.out.println("✓ Order2 1 placed");

            List<OrderItem> order2Items = Arrays.asList(
                    new OrderItem("P003", "Phone", 1, 30000)
            );
            system.placeOrder("C002", order2Items, PaymentMethod2.CREDIT_CARD, "456 Avenue, Delhi");
            System.out.println("✓ Order2 2 placed");

            List<OrderItem> order3Items = Arrays.asList(
                    new OrderItem("P001", "Laptop", 2, 50000),
                    new OrderItem("P004", "Keyboard", 1, 1500)
            );
            system.placeOrder("C001", order3Items, PaymentMethod2.WALLET, "123 Street, Mumbai");
            System.out.println("✓ Order2 3 placed (Batch flushed)");

            system.flush();

            // Test: Update statuses
            printSection("UPDATE STATUSES");
            Queue<Order2> allOrders = store.getAllOrders();
            List<Order2> orderList = new ArrayList<>(allOrders);

            if (orderList.size() >= 2) {
                system.updatePaymentStatus(orderList.get(0).orderId, PaymentStatus2.SUCCESS);
                system.updateOrderStatus(orderList.get(0).orderId, OrderStatus2.DELIVERED);
                System.out.println("✓ Order2 1: Payment SUCCESS, Status DELIVERED");

                system.updatePaymentStatus(orderList.get(1).orderId, PaymentStatus2.FAILED);
                System.out.println("✓ Order2 2: Payment FAILED");
            }

            // Test: Analytics
            printSection("ANALYTICS");

            System.out.println("Orders by Status:");
            system.getOrdersByStatus().forEach((status, count) ->
                    System.out.println("  " + status + ": " + count)
            );

            System.out.println("\nTotal Revenue: ₹" + String.format("%.2f", system.getTotalRevenue()));
            System.out.println("Average Order2 Value: ₹" + String.format("%.2f", system.getAverageOrderValue()));
            System.out.println("Cancellation Rate: " + String.format("%.1f", system.getCancellationRate()) + "%");

            System.out.println("\nTop Customers:");
            system.getTopCustomers(2).forEach(System.out::println);

            System.out.println("\nProduct Sales Count:");
            system.getProductSalesCount().forEach((product, qty) ->
                    System.out.println("  " + product + ": " + qty + " units")
            );

            // Test: Export
            printSection("FILE OPERATIONS");
            system.exportOrders("orders_export.csv");
            System.out.println("✓ Exported to orders_export.csv");

            system.generateCustomerReport("C001", "customer_C001_report.txt");
            System.out.println("✓ Generated customer report");

            System.out.println("\n╔══════════════════════════════════════════════════════╗");
            System.out.println("║          ALL TESTS COMPLETED SUCCESSFULLY ✅         ║");
            System.out.println("╚══════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printSection(String title) {
        System.out.println("\n" + "═".repeat(50));
        System.out.println(title);
        System.out.println("═".repeat(50) + "\n");
    }
}

