package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class SellLog extends Log {
    private int sellCode;
    private double receivedMoney;
    private double saleAmount;
    private Customer customer;
    private transient static ArrayList<Integer> allSellCodes = new ArrayList<>();
    private transient static ArrayList<Log>allSellLogs = new ArrayList<>();
    private HashMap<Product,Integer> sellerProductsInCart = new HashMap<>();

    public static ArrayList<Log> getAllSellLogs() {
        return allSellLogs;
    }
    private int idSetter() {
        if (allSellLogs.size() == 0) {
            return 1;
        }
        int max = 0;
        for (Log sellLog : allSellLogs) {
            if (((SellLog)sellLog).getSellCode() > max)
                max = ((SellLog)sellLog).getSellCode();
        }
        return max + 1;
    }

    public SellLog(LocalDateTime date, double receivedMoney, double saleAmount, Customer customer,HashMap<Product,Integer>products) {
        super(date);
        this.sellCode = idSetter();
        this.receivedMoney = receivedMoney;
        this.saleAmount = saleAmount;
        this.customer = customer;
        for (Product product : products.keySet()) {
            this.sellerProductsInCart.put(product,products.get(product));
        }
        allSellCodes.add(sellCode);
    }

    public Customer getCustomer() {
        return customer;
    }
    public int getSellCode(){
        return this.sellCode;
    }

    public static ArrayList<Integer> getAllSellCodes() {
        return allSellCodes;
    }

    public double getReceivedMoney(){
        return this.receivedMoney;
    }

    public double getSaleAmount(){
        return this.saleAmount;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public HashMap<Product, Integer> getSellerProductsInCart(){
        return sellerProductsInCart;
    }
    @Override
    public String toString() {
        return "SellLog{" +
                "sellCode=" + sellCode +
                ", receivedMoney=" + receivedMoney +
                ", saleAmount=" + saleAmount +
                ", customer=" + customer.getUsername() +
                '}';
    }
}