package model;


import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends Person {
    private ArrayList<BuyLog> buyHistory;
    private ArrayList<Discount> allDiscounts;
    private double amountOfAllPurchasing;
    private Wallet wallet;
    // to do : refine in file saver
    private ArrayList<FileProduct> payedFileProducts;
    private transient static ArrayList<Person> allCustomers = new ArrayList<>();
    private ArrayList<String> supportersSender = new ArrayList<>();
    private ArrayList<String> inbox = new ArrayList<>();
    private ArrayList<String> sentMessages = new ArrayList<>();
    private ArrayList<String> combinedMessages = new ArrayList<>();

    public static ArrayList<Person> getAllCustomers() {
        return allCustomers;
    }

    public Customer() {
    }

    public Customer(HashMap<String, String> information) {
        super(information);
        this.buyHistory = new ArrayList<>();
        this.allDiscounts = new ArrayList<>();
        this.payedFileProducts = new ArrayList<>();
    }

    public void addToFileProducts(FileProduct fileProduct) {
        payedFileProducts.add(fileProduct);
    }

    public ArrayList<FileProduct> getPayedFileProducts() {
        return payedFileProducts;
    }

    public ArrayList<BuyLog> getBuyHistory() {
        return buyHistory;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
        this.setBalance(this.wallet.getMoney());
    }

    public ArrayList<Discount> getAllDiscounts() {
        return allDiscounts;
    }

    public void addToAllDiscounts(Discount newDiscount) {
        this.allDiscounts.add(newDiscount);
    }

    public void addToBuyLogs(BuyLog newBuyLog) {
        buyHistory.add(newBuyLog);
    }

    public void addAmountOfAllPurchasing(double amountToBeAdded) {
        this.amountOfAllPurchasing += amountToBeAdded;
    }

    public void setAmountOfAllPurchasing(double amountOfAllPurchasing) {
        this.amountOfAllPurchasing = amountOfAllPurchasing;
    }

    public double getAmountOfAllPurchasing() {
        return amountOfAllPurchasing;
    }

    public void addToInbox(String supporterSender, String receivedMessage) {
        supportersSender.add(supporterSender);
        inbox.add(receivedMessage);
    }

    public void addToCombinedMessages(String username, String combinedMessage) {
        supportersSender.add(username);
        combinedMessages.add(combinedMessage);
    }

    public ArrayList<String> getSupportersSender() {
        return this.supportersSender;
    }

    public ArrayList<String> getCombinedMessages() {
        return this.combinedMessages;
    }

    public void addMessage(String sender, String message){
        int supporterIndex = supportersSender.lastIndexOf(sender);
        String previous = this.getCombinedMessages().get(supporterIndex);
        this.combinedMessages.remove(supporterIndex);
        this.combinedMessages.add(previous + message);
    }
}
