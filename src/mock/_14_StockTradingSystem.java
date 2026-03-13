package mock;
import java.util.*;
import java.io.*;

class Stock {
    String stockId;
    String companyName;
    String symbol;
    double currentPrice;
    boolean isListed;

}

class Trader {
    String traderId;
    String name;
    String email;
    double walletBalance;
}

class Trade {
    String tradeId;
    Trader trader;
    Stock stock;
    int quantity;
    double priceAtTrade;
    TradeType type;
    TradeStatus status;

    public Trade() {}

    public Trade(String tradeId, Trader trader, Stock stock, int quantity,
                 double priceAtTrade, TradeType type, TradeStatus status) {
        this.tradeId = tradeId;
        this.trader = trader;
        this.stock = stock;
        this.quantity = quantity;
        this.priceAtTrade = priceAtTrade;
        this.type = type;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Trade{tradeId='" + tradeId + "', trader=" + trader.name +
                ", stock=" + stock.symbol + ", qty=" + quantity +
                ", price=" + priceAtTrade + ", type=" + type + ", status=" + status + '}';
    }
}

enum TradeType   { BUY, SELL }
enum TradeStatus { PENDING, EXECUTED, CANCELLED }

interface IStockTradingSystem {

    // Register stock; print "[symbol] listed" or "[symbol] already listed"
    void listStock(Stock stock);

    // Register trader; print "[name] registered" or "[name] already exists"
    void registerTrader(Trader trader);

    /**
     * Place a BUY order:
     * - Stock must exist and be listed
     * - Trader must exist
     * - Trader wallet must have enough balance: quantity * currentPrice
     * - Assign tradeId ("TRD1", "TRD2", ...)
     * - Deduct amount from wallet, status = EXECUTED
     * - Print "BUY executed: [tradeId] | [symbol] x[qty] @ ₹[price]"
     * - Return tradeId or null on failure
     */
    String buyStock(String traderId, String stockId, int quantity);

    /**
     * Place a SELL order:
     * - Trader must have enough quantity of that stock from previous BUY trades (EXECUTED)
     * - Add proceeds to wallet (quantity * currentPrice)
     * - status = EXECUTED
     * - Print "SELL executed: [tradeId] | [symbol] x[qty] @ ₹[price]"
     * - Return tradeId or null on failure
     */
    String sellStock(String traderId, String stockId, int quantity);

    /**
     * Update stock price:
     * - Stock must exist
     * - New price must be > 0
     * - Print "[symbol] price updated to ₹[newPrice]"
     */
    void updateStockPrice(String stockId, double newPrice);

    /**
     * Cancel a trade:
     * - Only PENDING trades can be cancelled (treat as order not yet filled)
     * - If BUY cancelled → refund wallet
     * - If SELL cancelled → nothing to refund
     * - status = CANCELLED
     */
    void cancelTrade(String tradeId);

    // Return net profit/loss for a trader:
    // sum of (SELL priceAtTrade * qty) - sum of (BUY priceAtTrade * qty) for EXECUTED trades
    double getNetProfitLoss(String traderId);

    // Return total quantity of a stock currently HELD by a trader
    // (sum of BUY qty - sum of SELL qty for EXECUTED trades)
    int getHoldingQuantity(String traderId, String stockId);

    // Return list of all EXECUTED trades for a trader, sorted by tradeId
    List<Trade> getTradeHistory(String traderId);

    // Return the trader with highest wallet balance; null if none
    Trader getRichestTrader();

    /**
     * Save all traders to file
     * Format: traderId,name,email,walletBalance
     * Print "Traders saved to [filename]"
     */
    void saveTradersToFile(String filename);

    /**
     * Load traders from file
     * Skip malformed lines and duplicate traderIds
     * Print "Loaded [n] traders from [filename]"
     * Print "File not found: [filename]" if missing
     */
    void loadTradersFromFile(String filename);

    /**
     * Save all EXECUTED trades to file
     * Format: tradeId,traderId,stockId,quantity,priceAtTrade,type,status
     * Print "Trades saved to [filename]"
     */
    void saveTradestoFile(String filename);

    /**
     * Generate portfolio report for a trader
     * Format:
     * === PORTFOLIO: [traderName] ===
     * Wallet Balance: ₹[balance]
     * Total Trades: [n]
     * Total BUY Value: ₹[amount]
     * Total SELL Value: ₹[amount]
     * Net P&L: ₹[amount]
     * Holdings:
     *   [symbol]: [qty] units
     *   ...                          ← only stocks with qty > 0
     * Print "Portfolio saved to [filename]"
     */
    void generatePortfolioReport(String traderId, String filename);
}

public class _14_StockTradingSystem implements IStockTradingSystem {

    // TODO: Add fields

    @Override public void listStock(Stock stock) { /* TODO */ }
    @Override public void registerTrader(Trader trader) { /* TODO */ }
    @Override public String buyStock(String traderId, String stockId, int quantity) { return null; }
    @Override public String sellStock(String traderId, String stockId, int quantity) { return null; }
    @Override public void updateStockPrice(String stockId, double newPrice) { /* TODO */ }
    @Override public void cancelTrade(String tradeId) { /* TODO */ }
    @Override public double getNetProfitLoss(String traderId) { return 0; }
    @Override public int getHoldingQuantity(String traderId, String stockId) { return 0; }
    @Override public List<Trade> getTradeHistory(String traderId) { return null; }
    @Override public Trader getRichestTrader() { return null; }
    @Override public void saveTradersToFile(String filename) { /* TODO */ }
    @Override public void loadTradersFromFile(String filename) { /* TODO */ }
    @Override public void saveTradestoFile(String filename) { /* TODO */ }
    @Override public void generatePortfolioReport(String traderId, String filename) { /* TODO */ }
}

class StockTradingTest {
    public static void main(String[] args) {

        IStockTradingSystem system = new _14_StockTradingSystem();

        System.out.println("========== LIST STOCKS ==========");
        Stock st1 = new Stock(); st1.stockId="ST001"; st1.companyName="Tata Motors";
        st1.symbol="TATAMOTORS"; st1.currentPrice=800.0; st1.isListed=true;

        Stock st2 = new Stock(); st2.stockId="ST002"; st2.companyName="Infosys";
        st2.symbol="INFY"; st2.currentPrice=1500.0; st2.isListed=true;

        Stock st3 = new Stock(); st3.stockId="ST003"; st3.companyName="Reliance";
        st3.symbol="RIL"; st3.currentPrice=2500.0; st3.isListed=true;

        system.listStock(st1); system.listStock(st2); system.listStock(st3);
        system.listStock(st1); // duplicate

        System.out.println("\n========== REGISTER TRADERS ==========");
        Trader t1 = new Trader(); t1.traderId="T001"; t1.name="Animesh";
        t1.email="a@trade.com"; t1.walletBalance=100000.0;

        Trader t2 = new Trader(); t2.traderId="T002"; t2.name="Rahul";
        t2.email="r@trade.com"; t2.walletBalance=50000.0;

        system.registerTrader(t1); system.registerTrader(t2);
        system.registerTrader(t1); // duplicate

        System.out.println("\n========== BUY STOCKS ==========");
        // 10 * 800 = ₹8000 deducted
        String trd1 = system.buyStock("T001", "ST001", 10);
        System.out.println("Trade: " + trd1); // TRD1

        // 5 * 1500 = ₹7500 deducted
        String trd2 = system.buyStock("T001", "ST002", 5);
        System.out.println("Trade: " + trd2); // TRD2

        // Insufficient balance: 100 * 2500 = ₹250000 — should FAIL
        String trd3 = system.buyStock("T002", "ST003", 100);
        System.out.println("Trade (null): " + trd3);

        // Valid: 5 * 2500 = ₹12500
        String trd4 = system.buyStock("T002", "ST003", 5);
        System.out.println("Trade: " + trd4); // TRD3

        // Invalid stock
        String trd5 = system.buyStock("T001", "ST999", 1);
        System.out.println("Trade (null): " + trd5);

        System.out.println("\n========== SELL STOCKS ==========");
        // Price updated before sell
        system.updateStockPrice("ST001", 1000.0);

        // Sell 5 of TATAMOTORS @ ₹1000 → +₹5000 to wallet
        String trd6 = system.sellStock("T001", "ST001", 5);
        System.out.println("Trade: " + trd6); // TRD4

        // Try to sell more than held (T001 holds 5 TATAMOTORS now) — FAIL
        String trd7 = system.sellStock("T001", "ST001", 10);
        System.out.println("Trade (null): " + trd7);

        // T002 never bought INFY — FAIL
        String trd8 = system.sellStock("T002", "ST002", 1);
        System.out.println("Trade (null): " + trd8);

        System.out.println("\n========== NET P&L ==========");
        // T001: SELL(5*1000=5000) - BUY(10*800=8000 + 5*1500=7500) = 5000-15500 = -10500
        System.out.println("P&L T001: " + system.getNetProfitLoss("T001")); // -10500.0

        System.out.println("\n========== HOLDINGS ==========");
        System.out.println("T001 TATAMOTORS: " + system.getHoldingQuantity("T001","ST001")); // 5
        System.out.println("T001 INFY: "       + system.getHoldingQuantity("T001","ST002")); // 5
        System.out.println("T002 RIL: "        + system.getHoldingQuantity("T002","ST003")); // 5

        System.out.println("\n========== TRADE HISTORY ==========");
        List<Trade> history = system.getTradeHistory("T001");
        for (Trade t : history) System.out.println(t.tradeId + " | " + t.type + " | " + t.stock.symbol);

        System.out.println("\n========== RICHEST TRADER ==========");
        Trader rich = system.getRichestTrader();
        System.out.println("Richest: " + (rich != null ? rich.name + " ₹" + rich.walletBalance : "None"));

        System.out.println("\n========== FILE OPERATIONS ==========");
        system.saveTradersToFile("traders.csv");
        system.saveTradestoFile("trades.csv");
        system.generatePortfolioReport("T001", "portfolio_T001.txt");

        // Load into fresh system
        IStockTradingSystem system2 = new _14_StockTradingSystem();
        system2.loadTradersFromFile("traders.csv");
        system2.loadTradersFromFile("traders.csv");        // duplicates skipped
        system2.loadTradersFromFile("no_file.csv");        // file not found

        System.out.println("\n🎉 ALL TESTS COMPLETED 🎉");
    }
}