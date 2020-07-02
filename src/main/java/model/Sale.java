package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Sale implements Idable<Sale> {
    private int saleId;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;
    private double amountOfSale;
    private ArrayList<Product> productsWithThisSale;
    private transient static ArrayList<Sale> allSales = new ArrayList<>();

    public Sale() {
    }

    private int idSetter() {
        if (allSales.size() == 0) {
            return 1;
        }
        int max = 0;
        for (Sale sale : allSales) {
            if (sale.saleId > max)
                max = sale.saleId;
        }
        return max + 1;
    }

    public static ArrayList<Sale> getAllSales() {
        return allSales;
    }
    public Sale(LocalDateTime beginDate, LocalDateTime endDate, double amountOfSale, ArrayList<Product> productsWithThisSale) {
        this.saleId = idSetter();
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.amountOfSale = amountOfSale;
        this.productsWithThisSale = productsWithThisSale;
    }

    public int getSaleId() {
        return saleId;
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public double getAmountOfSale() {
        return amountOfSale;
    }

    public int getLastSaleId() {
        return this.idSetter();
    }

    public ArrayList<Product> getProductsWithThisSale() {
        return productsWithThisSale;
    }

    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setAmountOfSale(double amountOfSale) {
        this.amountOfSale = amountOfSale;
    }

    public double calculateAmountOfSale(double totalPrice) {
        return totalPrice * (((double) (this.amountOfSale)) / 100);
    }

    public void addProductToThisSale(Product newProduct) {
        productsWithThisSale.add(newProduct);
    }

    public void removeProductFromThisSale(Product specificProduct) {
        productsWithThisSale.removeIf(product -> product.equals(specificProduct));
    }

    public static void removeProductFromItSale(ArrayList<Sale> allSales, Product specificProduct) { //problem detected:deleting from ArrayList in a loop
        for (Sale sale : allSales) {
            if (sale != null && sale.getProductsWithThisSale().contains(specificProduct)) {
                sale.removeProductFromThisSale(specificProduct);
            }
        }
    }

    @Override
    public int getId() {
        return this.saleId;
    }

    @Override
    public Sale getById(int id) {
        for (Sale sale : allSales)
            if (sale.saleId == id)
                return sale;
        return null;
    }
}
