package model;

import java.util.ArrayList;

public class Rate implements Idable<Rate> {
    private int id;
    private String user;
    private Product product;
    private double rate;
    private transient static ArrayList<Rate>allRates = new ArrayList<>();

    public Rate() {
    }

    public static ArrayList<Rate> getAllRates() {
        return allRates;
    }
    public Rate(String user, Product product, double rate) {
        this.user = user;
        this.product = product;
        this.rate = rate;
        this.id = idSetter();
    }
    private int idSetter() {
        if (allRates.size() == 0) {
            return 1;
        }
        int max = 0;
        for (Rate rate : allRates) {
            if (rate.id > max)
                max = rate.id;
        }
        return max + 1;
    }

    public String getUser() {
        return user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getRate() {
        return rate;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Rate getById(int id) {
        for(Rate rate : allRates){
            if (rate.id == id)
                return rate;
        }
        return null;
    }
}
