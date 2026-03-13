package mock;

import java.util.*;
import java.io.*;
import java.util.stream.*;

interface IBankAccount {
    void deposit(double amount);
    boolean withdraw(double amount);
    double getBalance();
    String getAccountHolder();
    String getAccountNumber();
}

interface IBankRegistry {
    void addAccount(IBankAccount acc);
    IBankAccount findAccount(String accNo);
    List<IBankAccount> getAll();
    void saveToFile(String filename) throws IOException;
    void loadFromFile(String filename) throws IOException;
    int getTotalAccounts();
}

// ============= IMPLEMENTATIONS =============

class BankAccount implements IBankAccount {
    String accountNumber;
    String accountHolder;
    double balance;

    public BankAccount(String accountNumber, String accountHolder, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    @Override
    public void deposit(double amount) {
        this.balance += amount;
    }

    @Override
    public boolean withdraw(double amount) {
        if(amount > this.balance){
            System.out.println(this.balance +" balance available");
            return false;
        }
        this.balance -= amount;
        return true;
    }

    @Override
    public double getBalance() { return balance; }

    @Override
    public String getAccountHolder() { return accountHolder; }

    @Override
    public String getAccountNumber() { return accountNumber; }

    public String toCSV() {
        // TODO: return "accountNumber,accountHolder,balance"
        return accountNumber+","+accountHolder+","+balance;
    }

    public static BankAccount fromCSV(String line) {
        // TODO: parse CSV and return BankAccount
        String[] splits = line.split(",");
        return new BankAccount(splits[0].trim(),splits[1].trim(),Double.parseDouble(splits[2].trim()));
    }
}

class BankRegistry implements IBankRegistry {
    Map<String,IBankAccount> accounts = new HashMap<>();

    @Override
    public void addAccount(IBankAccount acc) {
        if(accounts.containsKey(acc.getAccountNumber())){
            System.out.println("Account with accno : "+acc.getAccountNumber()+" already exists");
            return;
        }
        accounts.put(acc.getAccountNumber(),acc);
    }

    @Override
    public IBankAccount findAccount(String accNo) {
        return accounts.getOrDefault(accNo,null);
    }

    @Override
    public List<IBankAccount> getAll() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (IBankAccount acc : accounts.values()) {
                writer.write(((BankAccount) acc).toCSV());
                writer.newLine();
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void loadFromFile(String filename) throws IOException {
        // TODO: read CSV lines and populate accounts list
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line = reader.readLine();
            while(line != null){
                BankAccount b = BankAccount.fromCSV(line);
                accounts.put(b.getAccountNumber(),b);
                line = reader.readLine();
            }
        }
        catch (IOException e){
            throw new IOException("File not found or unreadable: " + e.getMessage());
        }
    }

    @Override
    public int getTotalAccounts() {
        return accounts.size();
    }
}

// ============= MAIN =============

public class _15_BankAccount {
    public static void main(String[] args) throws IOException {
        IBankRegistry registry = new BankRegistry();

        // 1. Add 3 accounts and verify count
        registry.addAccount(new BankAccount("ACC001", "Rahul Sharma", 15000.50));
        registry.addAccount(new BankAccount("ACC002", "Priya Das", 8200.00));
        registry.addAccount(new BankAccount("ACC003", "Amit Roy", 5000.00));
        assert registry.getTotalAccounts() == 3
                : "FAIL: Expected 3, got " + registry.getTotalAccounts();
        System.out.println("✔ 1. Total accounts: " + registry.getTotalAccounts());

        // 2. Deposit and check updated balance
        registry.findAccount("ACC001").deposit(5000.00);
        assert registry.findAccount("ACC001").getBalance() == 20000.50
                : "FAIL: Expected 20000.50, got " + registry.findAccount("ACC001").getBalance();
        System.out.println("✔ 2. Balance after deposit: " + registry.findAccount("ACC001").getBalance());

        // 3. Withdraw successfully and verify new balance
        boolean withdrawResult = registry.findAccount("ACC002").withdraw(2000.00);
        assert withdrawResult : "FAIL: Withdraw should succeed";
        assert registry.findAccount("ACC002").getBalance() == 6200.00
                : "FAIL: Expected 6200.00, got " + registry.findAccount("ACC002").getBalance();
        System.out.println("✔ 3. Balance after withdraw: " + registry.findAccount("ACC002").getBalance());

        // 4. Withdraw with insufficient funds (returns false)
        boolean failedWithdraw = registry.findAccount("ACC003").withdraw(99999.00);
        assert !failedWithdraw : "FAIL: Withdraw should fail due to insufficient funds";
        System.out.println("✔ 4. Insufficient withdraw correctly returned false");

        // 5. Find account by number — found case
        IBankAccount found = registry.findAccount("ACC001");
        assert found != null && found.getAccountHolder().equals("Rahul Sharma")
                : "FAIL: Account not found or wrong holder";
        System.out.println("✔ 5. Found account: " + found.getAccountHolder());

        // 6. Find account by number — not found (null check)
        IBankAccount notFound = registry.findAccount("ACC999");
        assert notFound == null : "FAIL: Expected null for non-existent account";
        System.out.println("✔ 6. Non-existent account correctly returned null");

        // 7. getAll() returns defensive copy
        List<IBankAccount> snapshot = registry.getAll();
        snapshot.clear();
        assert registry.getTotalAccounts() == 3
                : "FAIL: getAll() exposed internal map — defensive copy broken";
        System.out.println("✔ 7. getAll() returns a defensive copy");

        // 8. Save to file — verify file is created
        String filename = "bank_accounts.csv";
        registry.saveToFile(filename);
        assert new File(filename).exists() : "FAIL: File was not created";
        System.out.println("✔ 8. File created: " + filename);

        // 9. Clear registry, load from file, verify count restored
        registry = new BankRegistry();
        assert registry.getTotalAccounts() == 0 : "FAIL: Registry should be empty after reset";
        registry.loadFromFile(filename);
        assert registry.getTotalAccounts() == 3
                : "FAIL: Expected 3 after load, got " + registry.getTotalAccounts();
        System.out.println("✔ 9. Count after load: " + registry.getTotalAccounts());

        // 10. Loaded account has correct balance after file round-trip
        IBankAccount loaded = registry.findAccount("ACC001");
        assert loaded != null && loaded.getBalance() == 20000.50
                : "FAIL: Wrong balance after load. Got: " + loaded.getBalance();
        System.out.println("✔ 10. Loaded balance correct: " + loaded.getBalance());

        // 11. Load from non-existent file — graceful handling
        try {
            registry.loadFromFile("nonexistent_file.csv");
            System.out.println("✘ 11. Should have thrown IOException");
        } catch (IOException e) {
            System.out.println("✔ 11. Non-existent file correctly threw IOException");
        }

        // 12. Deposit then save/load — verify persisted value
        registry.findAccount("ACC002").deposit(1000.00);
        double balanceBeforeSave = registry.findAccount("ACC002").getBalance();
        registry.saveToFile(filename);
        registry = new BankRegistry();
        registry.loadFromFile(filename);
        assert registry.findAccount("ACC002").getBalance() == balanceBeforeSave
                : "FAIL: Balance mismatch after save/load";
        System.out.println("✔ 12. Persisted balance correct: " + registry.findAccount("ACC002").getBalance());

        // 13. Total accounts count after load matches saved count
        assert registry.getTotalAccounts() == 3
                : "FAIL: Count mismatch after reload, got " + registry.getTotalAccounts();
        System.out.println("✔ 13. Final count after reload: " + registry.getTotalAccounts());

        System.out.println("\n✅ All 13 test cases passed.");
    }
}