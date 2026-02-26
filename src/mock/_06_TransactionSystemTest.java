package mock;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

enum TransactionType {
    DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT
}

enum TransactionStatus {
    SUCCESS, FAILED, PENDING
}

class Transaction2 {
    String transactionId;
    TransactionType type;
    String userId;
    double amount;
    TransactionStatus status;
    long timestamp;

    public Transaction2() {
    }

    public Transaction2(String txnId, TransactionType type, String userId,
                        double amount, TransactionStatus status) {
        this.transactionId = txnId;
        this.type = type;
        this.userId = userId;
        this.amount = amount;
        this.status = status;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Transaction2{" +
                "transactionId='" + transactionId + '\'' +
                ", type=" + type +
                ", userId='" + userId + '\'' +
                ", amount=" + amount +
                ", status=" + status +
                ", timestamp=" + timestamp +
                '}';
    }

    public String toCSV() {
        // Format: txnId,type,userId,amount,status,timestamp
        return transactionId+","+type+","+userId+","+amount+","+status+","+timestamp;
    }

    public static Transaction2 fromCSV(String line) throws InvalidTransactionException {
        if (line == null || line.trim().isEmpty()) {
            throw new InvalidTransactionException("CSV line is empty");
        }

        String[] parts = line.split(",");

        if (parts.length != 6) {
            throw new InvalidTransactionException(
                    "Invalid CSV format. Expected 6 fields but got " + parts.length
            );
        }

        try {
            String txnId = parts[0].trim();
            TransactionType type = TransactionType.valueOf(parts[1].trim());
            String userId = parts[2].trim();
            double amount = Double.parseDouble(parts[3].trim());
            TransactionStatus status = TransactionStatus.valueOf(parts[4].trim());
            long timestamp = Long.parseLong(parts[5].trim());

            return new Transaction2(
                    txnId, type, userId, amount, status
            );

        } catch (Exception e) {
            throw new InvalidTransactionException(
                    "Error parsing CSV line: " + e.getMessage()
            );
        }
    }
}

class InvalidTransactionException extends Exception {
    public InvalidTransactionException(String message) {
        super(message);
    }
}

interface ITransactionStore {
    void storeTransactions(Queue<Transaction2> transactions) throws IOException;
    Queue<Transaction2> getAllTransactions();
    void persistToFile(String filename) throws IOException;
    void loadFromFile(String filename) throws IOException, InvalidTransactionException;
}

interface ITransactionProcessor {
    void processTransaction(TransactionType type, String userId, double amount,
                            TransactionStatus status);
    int getTotalProcessed();
    int getPendingProcessing();
    void flush() throws IOException;

    // Stream operations
    Map<TransactionType, Long> getTransactionCountByType();
    Map<TransactionStatus, Long> getTransactionsByStatus();
    double getTotalAmountProcessed();

    // Lambda filters
    List<Transaction2> getTransactionsByUser(String userId);
    List<Transaction2> getFailedTransactions();
    List<Transaction2> getTransactionsAboveAmount(double amount);
    double getAverageTransactionAmount();

    // Top queries using Stream
    List<String> getTopSpenders(int n);
    Map<String, Double> getUserTotalSpending();

    // File operations with exception handling
    void exportReport(String filename) throws IOException, ExportException;
    void importTransactions(String filename) throws IOException, InvalidTransactionException;
}

class ExportException extends Exception {
    public ExportException(String message, Throwable cause) {
        super(message, cause);
    }
}

class TransactionStore implements ITransactionStore {
    Queue<Transaction2> transaction2s = new LinkedList<>();

    @Override
    public void storeTransactions(Queue<Transaction2> transactions) throws IOException {
        while(!transactions.isEmpty())
            transaction2s.add(transactions.poll());
    }

    @Override
    public Queue<Transaction2> getAllTransactions() {
        return new LinkedList<>(transaction2s);
    }

    @Override
    public void persistToFile(String filename) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Transaction2 t : transaction2s) {
                writer.write(t.toCSV());
                writer.newLine();
            }
        }

    }

    @Override
    public void loadFromFile(String filename) throws IOException, InvalidTransactionException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = reader.readLine();
        while (line != null){
            transaction2s.add(Transaction2.fromCSV(line));
            line = reader.readLine();
        }
    }
}

class TransactionProcessor implements ITransactionProcessor{
    ITransactionStore transactionStore;
    int batchSize;
    Queue<Transaction2> transaction2s = new LinkedList<>();
    int Id = 1;
    String tId = "Transaction";

    public TransactionProcessor(ITransactionStore t,int batchSize){
        this.transactionStore = t;
        this.batchSize = batchSize;
    }

    @Override
    public void processTransaction(TransactionType type, String userId, double amount, TransactionStatus status) {
        transaction2s.add(new Transaction2(tId+Id++,type,userId,amount,status));
        if(transaction2s.size() >= batchSize){
            try {
                transactionStore.storeTransactions(transaction2s);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public int getTotalProcessed() {
        return transactionStore.getAllTransactions().size();
    }

    @Override
    public int getPendingProcessing() {
        return transaction2s.size();
    }

    @Override
    public void flush() throws IOException {
        transactionStore.storeTransactions(transaction2s);
    }

    @Override
    public Map<TransactionType, Long> getTransactionCountByType() {
        return transactionStore.getAllTransactions()
                .stream()
                .collect(Collectors.groupingBy(
                        t -> t.type,
                        Collectors.counting()
                ));
    }

    @Override
    public Map<TransactionStatus, Long> getTransactionsByStatus() {
//        Map<TransactionStatus, Long> map = new HashMap<>();
//        Queue<Transaction2> stored = transactionStore.getAllTransactions();
//        long count1 = stored.stream().filter(obj -> obj.status == TransactionStatus.SUCCESS).count();
//        long count2 = stored.stream().filter(obj -> obj.status == TransactionStatus.PENDING).count();
//        long count3 = stored.stream().filter(obj -> obj.status == TransactionStatus.FAILED).count();
//        map.put(TransactionStatus.SUCCESS,count1);
//        map.put(TransactionStatus.PENDING,count2);
//        map.put(TransactionStatus.FAILED,count3);
//        return map;
        return transactionStore.getAllTransactions().stream().collect(Collectors.groupingBy(obj-> obj.status,Collectors.counting()));
    }

    @Override
    public double getTotalAmountProcessed() {
        Queue<Transaction2> stored = transactionStore.getAllTransactions();
        return stored.stream().filter(obj -> obj.status == TransactionStatus.SUCCESS).mapToDouble(obj-> obj.amount).sum();
    }

    @Override
    public List<Transaction2> getTransactionsByUser(String userId) {
        Queue<Transaction2> stored = transactionStore.getAllTransactions();
        return stored.stream().filter(obj -> obj.userId.equalsIgnoreCase(userId)).collect(Collectors.toList());
    }

    @Override
    public List<Transaction2> getFailedTransactions() {
        Queue<Transaction2> stored = transactionStore.getAllTransactions();
        return stored.stream().filter(obj -> obj.status == TransactionStatus.FAILED).collect(Collectors.toList());
    }

    @Override
    public List<Transaction2> getTransactionsAboveAmount(double amount) {
        Queue<Transaction2> stored = transactionStore.getAllTransactions();
        return stored.stream().filter(obj -> obj.amount > amount).collect(Collectors.toList());
    }

    @Override
    public double getAverageTransactionAmount() {
        Queue<Transaction2> stored = transactionStore.getAllTransactions();
        return stored.stream().filter(obj -> obj.status == TransactionStatus.SUCCESS)
                .mapToDouble(obj -> obj.amount).average().orElse(0.0);
    }

    @Override
    public List<String> getTopSpenders(int n) {
        return getUserTotalSpending().entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .limit(n)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

    }

    @Override
    public Map<String, Double> getUserTotalSpending() {
        return transactionStore.getAllTransactions().stream().collect(Collectors
                .groupingBy(obj->obj.userId,Collectors.summingDouble(obj->obj.amount)));
    }

    @Override
    public void exportReport(String filename) throws IOException, ExportException {
        Queue<Transaction2> stored = transactionStore.getAllTransactions();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Transaction2 t : stored) {
                writer.write(t.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ExportException("Failed to export report", e);
        }
    }

    @Override
    public void importTransactions(String filename) throws IOException, InvalidTransactionException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Transaction2 t = Transaction2.fromCSV(line);
                transaction2s.add(t);
            }

            if (transaction2s.size() >= batchSize) {
                transactionStore.storeTransactions(transaction2s);
            }
        }
    }
}

public class _06_TransactionSystemTest {

    public static void main(String[] args) {

        try {
            // ---------------- SETUP ----------------
            ITransactionStore store = new TransactionStore();
            ITransactionProcessor processor = new TransactionProcessor(store, 3);

            System.out.println("========== PROCESS TRANSACTIONS ==========");

            processor.processTransaction(TransactionType.DEPOSIT, "user1", 1000, TransactionStatus.SUCCESS);
            processor.processTransaction(TransactionType.WITHDRAWAL, "user1", 300, TransactionStatus.SUCCESS);
            processor.processTransaction(TransactionType.PAYMENT, "user2", 500, TransactionStatus.SUCCESS); // batch flush

            processor.processTransaction(TransactionType.TRANSFER, "user2", 700, TransactionStatus.PENDING);
            processor.processTransaction(TransactionType.PAYMENT, "user3", 1200, TransactionStatus.FAILED);

            System.out.println("Pending (before flush): " + processor.getPendingProcessing());
            processor.flush();
            System.out.println("Pending (after flush): " + processor.getPendingProcessing());

            // ---------------- BASIC COUNTS ----------------
            System.out.println("\n========== BASIC METRICS ==========");
            System.out.println("Total processed: " + processor.getTotalProcessed());
            System.out.println("Total amount processed (SUCCESS): " +
                    processor.getTotalAmountProcessed());

            // ---------------- STREAM OPERATIONS ----------------
            System.out.println("\n========== TRANSACTIONS BY TYPE ==========");
            processor.getTransactionCountByType()
                    .forEach((k, v) -> System.out.println(k + " -> " + v));

            System.out.println("\n========== TRANSACTIONS BY STATUS ==========");
            processor.getTransactionsByStatus()
                    .forEach((k, v) -> System.out.println(k + " -> " + v));

            System.out.println("\n========== FAILED TRANSACTIONS ==========");
            processor.getFailedTransactions()
                    .forEach(System.out::println);

            System.out.println("\n========== TRANSACTIONS BY USER (user1) ==========");
            processor.getTransactionsByUser("user1")
                    .forEach(System.out::println);

            System.out.println("\n========== TRANSACTIONS ABOVE AMOUNT 600 ==========");
            processor.getTransactionsAboveAmount(600)
                    .forEach(System.out::println);

            System.out.println("\n========== AVERAGE SUCCESS TRANSACTION ==========");
            System.out.println(processor.getAverageTransactionAmount());

            // ---------------- TOP SPENDERS ----------------
            System.out.println("\n========== USER TOTAL SPENDING ==========");
            processor.getUserTotalSpending()
                    .forEach((u, amt) -> System.out.println(u + " -> " + amt));

            System.out.println("\n========== TOP 2 SPENDERS ==========");
            System.out.println(processor.getTopSpenders(2));

            // ---------------- FILE EXPORT ----------------
            System.out.println("\n========== EXPORT REPORT ==========");
            processor.exportReport("transactions.csv");
            System.out.println("Exported to transactions.csv");

            // ---------------- IMPORT TEST ----------------
            System.out.println("\n========== IMPORT TRANSACTIONS ==========");
            ITransactionStore newStore = new TransactionStore();
            ITransactionProcessor newProcessor =
                    new TransactionProcessor(newStore, 2);

            newProcessor.importTransactions("transactions.csv");
            newProcessor.flush();

            System.out.println("Imported transactions count: " +
                    newProcessor.getTotalProcessed());

            System.out.println("\n========== IMPORTED DATA ==========");
            newStore.getAllTransactions()
                    .forEach(System.out::println);

            System.out.println("\n🎉 ALL TESTS COMPLETED SUCCESSFULLY 🎉");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}