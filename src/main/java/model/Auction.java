package model;

import controller.Storage;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class Auction extends TimerTask implements Idable<Auction> {
    private int auctionId;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;
    private Product product;
    private String productName;
    private Double price;
    private Seller seller;
    private String sellerName;
    private Customer customer;
    private transient static ArrayList<Auction> allAuctions = new ArrayList<>();
    private Storage storage = new Storage();
    private ArrayList<String> thisAuctionChat = new ArrayList<>();
    private ArrayList<String> senders = new ArrayList<>();

    public String getProductName() {
        return productName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public Auction() {
    }

    public Auction(LocalDateTime beginDate, LocalDateTime endDate, Product product, Double price, Seller seller) {
        this.auctionId = idSetter();
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.product = product;
        this.product.setSupply(this.product.getSupply() - 1);
        this.productName = product.getName();
        this.price = price;
        this.seller = seller;
        this.sellerName = seller.getUsername();
        this.customer = null;
    }

    private int idSetter() {
        if (allAuctions.size() == 0) {
            return 1;
        }
        int max = 0;
        for (Auction auction : allAuctions) {
            if (auction.auctionId > max)
                max = auction.auctionId;
        }
        return max + 1;
    }

    @Override
    public int getId() {
        return this.auctionId;
    }

    @Override
    public Auction getById(int id) {
        for (Auction auction : allAuctions) {
            if (auction.getId() == id) {
                return auction;
            }
        }
        return null;
    }

    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.finishAuction();
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Product getProduct() {
        return product;
    }

    public Double getPrice() {
        return price;
    }

    public Seller getSeller() {
        return seller;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public static ArrayList<Auction> getAllAuctions() {
        return allAuctions;
    }

    public ArrayList<String> getThisAuctionChat() {
        return this.thisAuctionChat;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public ArrayList<String> getSenders() {
        return this.senders;
    }

    public void addToThisAuctionChat(String username, String message) {
        this.senders.add(username);
        this.thisAuctionChat.add(message);
    }

    public void finishAuction() {
        System.out.println("hellooooo");
        Timer timer = new Timer();
        TimerTask task = new Auction();
        Date date = Date.from(this.getEndDate().atZone(ZoneId.systemDefault()).toInstant());
        try {
            timer.schedule(task, date);
        } catch (Exception e) {
            timer.cancel();
            this.getProduct().setSupply(this.getProduct().getSupply() + 1);
        }
    }

    @Override
    public void run() {
        if (this.getCustomer() == null) {
            System.out.println("error");
            return;
        } else {
            this.getCustomer().setBalance(this.getCustomer().getBalance() - this.getPrice());
            this.getSeller().addBalance(this.getPrice() * 0.9);
            ArrayList<Seller> sellers = new ArrayList<>();
            sellers.add(this.getSeller());
            HashMap<String, String> info = new HashMap<>();
            info.put("receiverName", this.getCustomer().getName());
            info.put("address", "No address defined!");
            info.put("phoneNumber", this.getCustomer().getNumber());
            HashMap<Product, Integer> product = new HashMap<>();
            product.put(this.getProduct(), 1);
            BuyLog buyLog = new BuyLog(this.getEndDate(), this.getPrice(), 0, sellers, info, product, "");
            storage.addBuyLog(buyLog);
            (this.getCustomer()).addToBuyLogs(buyLog);
            SellLog sellLog = new SellLog(this.getEndDate(), this.getPrice(), 0, this.getCustomer(), product);
            storage.addSellLog(sellLog);
            seller.addToSellLogs(sellLog);
        }
    }
}
