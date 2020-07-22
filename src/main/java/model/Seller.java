package model;


import java.util.ArrayList;
import java.util.HashMap;
public class Seller extends Person {
    private String company;
    private static ArrayList<SellLog> sellHistory = new ArrayList<>();
    private ArrayList<Product> productsToSell;
    private ArrayList<Sale> saleList;
    private Wallet wallet;
    private ArrayList<Auction> thisSellerAuctions;
    // to do : refine in file saver
    private ArrayList<FileProduct> soldFileProducts;
    private transient static ArrayList<Person> allSellers = new ArrayList<>();

    public static ArrayList<Person> getAllSellers() {
        return allSellers;
    }
    public Seller(HashMap<String, String> information) {
        super(information);
        this.company = information.get("company");
        this.productsToSell = new ArrayList<>();
        this.thisSellerAuctions = new ArrayList<>();
        this.saleList = new ArrayList<>();
        this.soldFileProducts = new ArrayList<>();
    }
    public void addToFileProducts(FileProduct fileProduct){
        soldFileProducts.add(fileProduct);
    }

    public ArrayList<FileProduct> getSoldFileProducts() {
        return soldFileProducts;
    }

    public String getCompany() {
        return company;
    }

    public ArrayList<SellLog> getSellHistory() {
        return sellHistory;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
        this.setBalance(this.wallet.getMoney());
    }

    public ArrayList<Product> getProductsToSell() {
        return productsToSell;
    }

    public ArrayList<Auction> getThisSellerAuctions(){
        return this.thisSellerAuctions;
    }

    public ArrayList<Sale> getSaleList() {
        return saleList;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void addProduct(Product newProduct) {
        productsToSell.add(newProduct);
    }

    public void addAuction(Auction auction){
        this.thisSellerAuctions.add(auction);
    }

    public void removeProduct(Product specificProduct) {
        productsToSell.removeIf(product -> product.equals(specificProduct));
    }

    boolean hasProductWithId(String productId) {
        for (Product product : productsToSell) {
            if (product.getProductId() == Integer.parseInt(productId)) {
                return true;
            }
        }
        return false;
    }

    public void addToSellLogs(SellLog newSellLog) {
        sellHistory.add(newSellLog);
    }

    public void addSale(Sale newSale) {
        saleList.add(newSale);
    }

    public void addBalance(double amountToBeAdded) {
        this.setBalance(this.getBalance() + amountToBeAdded);
    }

}