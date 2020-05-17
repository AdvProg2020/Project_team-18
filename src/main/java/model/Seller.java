package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Seller extends Person {
    private String company;
    private static ArrayList<SellLog> sellHistory = new ArrayList<>();
    private ArrayList<Product> productsToSell;
    private ArrayList<Sale> saleList;
    private static ArrayList<Person> allSellers = new ArrayList<>();

    public static ArrayList<Person> getAllSellers() {
        return allSellers;
    }

    public Seller(HashMap<String, String> information) {
        super(information);
        this.company = information.get("company");
        this.productsToSell = new ArrayList<>();
        this.saleList = new ArrayList<>();
    }

    public String getCompany() {
        return company;
    }

    public ArrayList<SellLog> getSellHistory() {
        return sellHistory;
    }

    public ArrayList<Product> getProductsToSell() {
        return productsToSell;
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