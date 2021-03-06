package model;


import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class BuyLog extends Log {
    private int buyCode;
    private double paidMoney;
    private double discountAmount;
    private ArrayList<Seller> seller;
    private HashMap<String, String> customerInfo;
    private transient static ArrayList<Integer> allBuyCodes = new ArrayList<>();
    private transient static ArrayList<Log> allBuyLogs = new ArrayList<>();
    private String discountUsed;
    private BuyLogStatus status;

    public static ArrayList<Log> getAllBuyLogs() {
        return allBuyLogs;
    }

    public BuyLog() {
    }

    public BuyLog(LocalDateTime date, HashMap<String, String> info) {
        super(date);
    }

    private int idSetter() {
        if (allBuyLogs.size() == 0) {
            return 1;
        }
        int max = 0;
        for (Log buyLog : allBuyLogs) {
            if (((BuyLog) buyLog).getBuyCode() > max)
                max = ((BuyLog) buyLog).getBuyCode();
        }
        return max + 1;
    }

    public BuyLog(LocalDateTime date, double paidMoney, double discountAmount, ArrayList<Seller> sellers,
                  HashMap<String, String> customerInfo, HashMap<Product, Integer> productsInThisBuyLog, String discountUsed) {
        super(date);
        this.paidMoney = paidMoney;
        this.discountAmount = discountAmount;
        this.seller = sellers;
        this.customerInfo = customerInfo;
        this.buyCode = idSetter();
        for (Product product : productsInThisBuyLog.keySet()) {
            this.products.put(product, productsInThisBuyLog.get(product));
        }
        this.discountUsed = discountUsed;
        allBuyCodes.add(buyCode);
        this.status = BuyLogStatus.WAITING;
    }

    public int getBuyCode() {
        return buyCode;
    }

    public double getPaidMoney() {
        return paidMoney;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public String getDiscountUsed() {
        return discountUsed;
    }

    public ArrayList<Seller> getSeller() {
        return seller;
    }

    public ArrayList<Integer> getAllBuyCodes() {
        return allBuyCodes;
    }

    public HashMap<String, String> getCustomerInfo(){
        return customerInfo;
    }

    public BuyLogStatus getStatus(){
        return this.status;
    }

    public void sendPurchase() {
        this.status = BuyLogStatus.SENT;
    }
}
