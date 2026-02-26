package mock;

import java.util.*;

enum PaymentStatus { PENDING, SUCCESS, FAILED, REFUNDED }
enum PaymentType { CREDIT_CARD, UPI, WALLET }

class Transaction {
    String transactionId;
    String userId;
    double amount;
    PaymentStatus status;
    long timestamp;
    PaymentType type;
    public Transaction(String transactionId, String userId, double amount,
                       PaymentStatus status, PaymentType type) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.amount = amount;
        this.status = status;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }
    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + transactionId + '\'' +
                ", amount=" + amount +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}

interface IPayment {
    boolean processPayment(double amount);
    String getTransactionId();
    PaymentStatus getStatus();
}
interface ICreditCardPayment extends IPayment {
    boolean validate3DSecure(String otp);
    void setCardDetails(String cardNumber, String cvv, String expiry);
}
interface IUPIPayment extends IPayment {
    boolean verifyUpiId(String upiId);
    void setUpiId(String upiId);
}
interface IWalletPayment extends IPayment {
    double getWalletBalance(String userId);
    boolean addMoneyToWallet(String userId, double amount);
}
interface IPaymentProcessor {
    void registerPaymentMethod(String userId, IPayment payment);
    boolean executePayment(String userId, double amount, PaymentType type);
    List<Transaction> getTransactionHistory(String userId);
    double getTotalSpent(String userId);
    boolean refundPayment(String transactionId);
}


// class credit card
class CreditCardPayment implements ICreditCardPayment {
    private String transactionId;
    private PaymentStatus status;
    private String cardNumber;
    private String cvv;
    private String expiry;
    private boolean is3DSecureValidated;

    public CreditCardPayment() {
        this.transactionId = "TXN-CC-" + UUID.randomUUID().toString().substring(0, 8);
        this.status = PaymentStatus.PENDING;
        this.is3DSecureValidated = false;
    }

    @Override
    public void setCardDetails(String cardNumber, String cvv, String expiry) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiry = expiry;
    }

    @Override
    public boolean validate3DSecure(String otp) {
        // Simulate OTP validation
        if (otp != null && otp.length() == 6) {
            this.is3DSecureValidated = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean processPayment(double amount) {
        if (cardNumber == null || cvv == null || expiry == null) {
            System.out.println("Card details not set!");
            this.status = PaymentStatus.FAILED;
            return false;
        }
        if (!is3DSecureValidated) {
            System.out.println("3D Secure validation required!");
            this.status = PaymentStatus.FAILED;
            return false;
        }

        System.out.println("Processing credit card payment of Rs." + amount);

        if (Math.random() < 0.9) {
            this.status = PaymentStatus.SUCCESS;
            System.out.println("✓ Credit Card payment successful!");
            return true;
        }
        else {
            this.status = PaymentStatus.FAILED;
            System.out.println("✗ Credit Card payment failed!");
            return false;
        }
    }

    @Override
    public String getTransactionId() {
        return transactionId;
    }

    @Override
    public PaymentStatus getStatus() {
        return status;
    }

}
// 2. UPI PAYMENT
class UPIPayment implements IUPIPayment {
    private String transactionId;
    private PaymentStatus status;
    private String upiId;
    private boolean isVerified;

    public UPIPayment() {
        this.transactionId = "TXN-UPI-" + UUID.randomUUID().toString().substring(0, 8);
        this.status = PaymentStatus.PENDING;
        this.isVerified = false;
    }

    @Override
    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public boolean verifyUpiId(String upiId) {
        if (upiId != null && upiId.contains("@")) {
            this.isVerified = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean processPayment(double amount) {
        if (upiId == null) {
            System.out.println("UPI ID not set!");
            this.status = PaymentStatus.FAILED;
            return false;
        }
        if (!isVerified) {
            System.out.println("UPI ID not verified!");
            this.status = PaymentStatus.FAILED;
            return false;
        }
        System.out.println("Processing UPI payment of Rs." + amount + " to " + upiId);
        if (Math.random() < 0.95) {
            this.status = PaymentStatus.SUCCESS;
            System.out.println("✓ UPI payment successful!");
            return true;
        } else {
            this.status = PaymentStatus.FAILED;
            System.out.println("✗ UPI payment failed!");
            return false;
        }
    }

    @Override
    public String getTransactionId() {
        return transactionId;
    }

    @Override
    public PaymentStatus getStatus() {
        return status;
    }
}

class WalletPayment implements IWalletPayment {
    private String transactionId;
    private PaymentStatus status;
    private static Map<String, Double> walletBalances = new HashMap<>();
    public WalletPayment() {
        this.transactionId = "TXN-WALLET-" + UUID.randomUUID().toString().substring(0, 8);
        this.status = PaymentStatus.PENDING;
    }
    @Override
    public double getWalletBalance(String userId) {
        return walletBalances.getOrDefault(userId, 0.0);
    }
    @Override
    public boolean addMoneyToWallet(String userId, double amount) {
        if (amount <= 0) {
            return false;
        }
        double currentBalance = getWalletBalance(userId);
        walletBalances.put(userId, currentBalance + amount);
        System.out.println("Added Rs." + amount + " to wallet. New balance: Rs." +
                walletBalances.get(userId));
        return true;
    }
    @Override
    public boolean processPayment(double amount) {
        this.status = PaymentStatus.SUCCESS;
        return true;
    }
    public boolean processPayment(String userId, double amount) {
        double balance = getWalletBalance(userId);
        if (balance < amount) {
            System.out.println("Insufficient wallet balance! Required: Rs." + amount +
                    ", Available: Rs." + balance);
            this.status = PaymentStatus.FAILED;
            return false;
        }
        walletBalances.put(userId, balance - amount);
        this.status = PaymentStatus.SUCCESS;
        System.out.println("✓ Wallet payment successful! Remaining balance: Rs." +
                walletBalances.get(userId));
        return true;
    }
    @Override
    public String getTransactionId() {
        return transactionId;
    }
    @Override
    public PaymentStatus getStatus() {
        return status;
    }
}

class PaymentProcessor implements IPaymentProcessor {
    private Map<String, Map<PaymentType, IPayment>> userPaymentMethods;
    private List<Transaction> allTransactions;
    private Map<String, List<Transaction>> userTransactions;

    public PaymentProcessor() {
        this.userPaymentMethods = new HashMap<>();
        this.allTransactions = new ArrayList<>();
        this.userTransactions = new HashMap<>();
    }

    @Override
    public void registerPaymentMethod(String userId, IPayment payment) {
        userPaymentMethods.putIfAbsent(userId, new HashMap<>());
        PaymentType type = null;
        if (payment instanceof ICreditCardPayment) {
            type = PaymentType.CREDIT_CARD;
        } else if (payment instanceof IUPIPayment) {
            type = PaymentType.UPI;
        } else if (payment instanceof IWalletPayment) {
            type = PaymentType.WALLET;
        }
        if (type != null) {
            userPaymentMethods.get(userId).put(type, payment);
            System.out.println("Registered " + type + " for user: " + userId);
        }
    }

    @Override
    public boolean executePayment(String userId, double amount, PaymentType type) {
        // Check if user has registered this payment method
        if (!userPaymentMethods.containsKey(userId) ||
                !userPaymentMethods.get(userId).containsKey(type)) {
            System.out.println("Payment method not registered for user!");
            return false;
        }
        IPayment payment = userPaymentMethods.get(userId).get(type);
        boolean success;
        // Special handling for wallet (needs userId)
        if (payment instanceof WalletPayment) {
            success = ((WalletPayment) payment).processPayment(userId, amount);
        } else {
            success = payment.processPayment(amount);
        }
        // Create transaction record
        Transaction transaction = new Transaction(
                payment.getTransactionId(),
                userId,
                amount,
                payment.getStatus(),
                type
        );
        allTransactions.add(transaction);
        userTransactions.putIfAbsent(userId, new ArrayList<>());
        userTransactions.get(userId).add(transaction);
        return success;
    }

    @Override
    public List<Transaction> getTransactionHistory(String userId) {
        return userTransactions.getOrDefault(userId, new ArrayList<>());
    }

    @Override
    public double getTotalSpent(String userId) {
        List<Transaction> transactions = getTransactionHistory(userId);
        double total = 0.0;
        for (Transaction txn : transactions) {
            if (txn.status == PaymentStatus.SUCCESS) {
                total += txn.amount;
            }
        }
        return total;
    }

    @Override
    public boolean refundPayment(String transactionId) {
        for (Transaction txn : allTransactions) {
            if (txn.transactionId.equals(transactionId)) {
                if (txn.status == PaymentStatus.SUCCESS) {
                    txn.status = PaymentStatus.REFUNDED;
                    // If wallet payment, add money back
                    if (txn.type == PaymentType.WALLET) {
                        IPayment payment = userPaymentMethods.get(txn.userId).get(PaymentType.WALLET);
                        if (payment instanceof WalletPayment) {
                            ((WalletPayment) payment).addMoneyToWallet(txn.userId, txn.amount);
                        }
                    }
                    System.out.println("✓ Refund successful for transaction: " + transactionId);
                    return true;
                }
            }
        }
        System.out.println("✗ Refund failed. Transaction not found or already refunded.");
        return false;
    }
}


public class PaymentGatewayMain {
    public static void main(String[] args) {
        IPaymentProcessor processor = new PaymentProcessor();
        String user1 = "Alice";
        String user2 = "Bob";
        System.out.println("========== PAYMENT GATEWAY DEMO ==========\n");
        // ===== CREDIT CARD SETUP =====
        System.out.println("--- Setting up Credit Card ---");
        ICreditCardPayment creditCard = new CreditCardPayment();
        creditCard.setCardDetails("1234-5678-9012-3456", "123", "12/26");
        creditCard.validate3DSecure("123456");
        processor.registerPaymentMethod(user1, creditCard);
        // ===== UPI SETUP =====
        System.out.println("\n--- Setting up UPI ---");
        IUPIPayment upi = new UPIPayment();
        upi.setUpiId("alice@paytm");
        upi.verifyUpiId("alice@paytm");
        processor.registerPaymentMethod(user1, upi);
        // ===== WALLET SETUP =====
        System.out.println("\n--- Setting up Wallet ---");
        IWalletPayment wallet = new WalletPayment();
        wallet.addMoneyToWallet(user1, 5000.0);
        processor.registerPaymentMethod(user1, wallet);
        // ===== PROCESS PAYMENTS =====
        System.out.println("\n========== PROCESSING PAYMENTS ==========\n");
        System.out.println("1. Credit Card Payment:");
        processor.executePayment(user1, 1500.0, PaymentType.CREDIT_CARD);
        System.out.println("\n2. UPI Payment:");
        processor.executePayment(user1, 800.0, PaymentType.UPI);
        System.out.println("\n3. Wallet Payment:");
        processor.executePayment(user1, 2000.0, PaymentType.WALLET);
        System.out.println("\n4. Insufficient Wallet Balance:");
        processor.executePayment(user1, 5000.0, PaymentType.WALLET);
        // ===== TRANSACTION HISTORY =====
        System.out.println("\n========== TRANSACTION HISTORY ==========");
        List<Transaction> history = processor.getTransactionHistory(user1);
        for (Transaction txn : history) {
            System.out.println(txn);
        }
        // ===== TOTAL SPENT =====
        System.out.println("\n========== TOTAL SPENT ==========");
        double totalSpent = processor.getTotalSpent(user1);
        System.out.println("Total spent by " + user1 + ": Rs." + totalSpent);
        // ===== REFUND =====
        System.out.println("\n========== REFUND TEST ==========");
        if (!history.isEmpty()) {
            String refundTxnId = history.get(0).transactionId;
            processor.refundPayment(refundTxnId);
        }
        // ===== RETRY FAILED PAYMENT =====
        System.out.println("\n========== RETRY MECHANISM ==========");
        System.out.println("Retrying UPI payment (max 3 attempts):");
        boolean success = false;
        for (int attempt = 1; attempt <= 3; attempt++) {
            System.out.println("Attempt " + attempt + ":");
            IUPIPayment retryUpi = new UPIPayment();
            retryUpi.setUpiId("alice@paytm");
            retryUpi.verifyUpiId("alice@paytm");
            processor.registerPaymentMethod(user1, retryUpi);
            success = processor.executePayment(user1, 500.0, PaymentType.UPI);
            if (success) {
                System.out.println("Payment succeeded on attempt " + attempt);
                break;
            }
        }
        if (!success) {
            System.out.println("Payment failed after 3 attempts");
        }
        System.out.println("\n========== FINAL STATS ==========");
        System.out.println("Total transactions: " + processor.getTransactionHistory(user1).size());
        System.out.println("Total spent: Rs." + processor.getTotalSpent(user1));
        System.out.println("Wallet balance: Rs." + wallet.getWalletBalance(user1));
    }
}
