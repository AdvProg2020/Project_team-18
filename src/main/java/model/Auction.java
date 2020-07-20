package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Auction implements Idable<Auction> {
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

    public String getProductName() {
        return productName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public Auction(LocalDateTime beginDate, LocalDateTime endDate, Product product, Double price, Seller seller) {
        this.auctionId = idSetter();
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.product = product;
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

    public void setCustomer(Customer customer){
        this.customer = customer;
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

    public Customer getCustomer(){
        return this.customer;
    }

    public static ArrayList<Auction> getAllAuctions() {
        return allAuctions;
    }
}
