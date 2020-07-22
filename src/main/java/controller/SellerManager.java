package controller;

import java.util.ArrayList;
import java.util.HashMap;

import model.*;

public class SellerManager extends Manager {

    private static HashMap<Integer, ArrayList<Product>> savedProductsInSale = new HashMap<>();
    private double minBalance;

    public SellerManager() {
    }

    public HashMap<Integer, ArrayList<Product>> getSavedProductsInSale() {
        return savedProductsInSale;
    }

    public Product viewSellerProduct(int productId) throws Exception {
        if (storage.getProductById(productId) == null)
            throw new Exception("A product with this Id does not exist!!");
        else if (!storage.getProductById(productId).getSeller().equals(person))
            throw new Exception("You don't have this product!!");
        else
            return storage.getProductById(productId);
    }

    public void addProduct(HashMap<String, String> information) {
        information.put("seller", person.getUsername());
        storage.addRequest(new Request("add product", information));
    }

    public void removeProduct(int productId) throws Exception {
        if (storage.getProductById(productId) == null)
            throw new Exception("There is not such product!");
        else if (!storage.getProductById(productId).getSeller().equals(person))
            throw new Exception("You don't have such product!");
        else {
            HashMap<String, String> information = new HashMap<>();
            information.put("productId", Integer.toString(productId));
            information.put("seller", person.getUsername());
            storage.addRequest(new Request("remove product", information));
        }
    }

    public ArrayList<Customer> viewProductBuyers(int productId) throws Exception {
        if (storage.getProductById(productId) == null)
            throw new Exception("There is not such product!");
        else if (!storage.getProductById(productId).getSeller().equals(person))
            throw new Exception("You don't have such product!");
        else
            return storage.getProductById(productId).getThisProductsBuyers();
    }

    public void editProduct(int productId, String field, String updatedVersion) throws Exception {
        if (storage.getProductById(productId) == null)
            throw new Exception("There is not such product!");
        else if (!storage.getProductById(productId).getSeller().equals(person))
            throw new Exception("You don't have such product!");
        else {
            HashMap<String, String> information = new HashMap<>();
            information.put("field", field);
            information.put("updatedVersion", updatedVersion);
            information.put("productId", Integer.toString(productId));
            information.put("seller", person.getUsername());
            storage.addRequest(new Request("edit product", information));
        }
    }

    public ArrayList<Sale> viewSellerOffs() {
        return ((Seller) person).getSaleList();
    }

    public ArrayList<Product> viewSellerProducts() {
        return ((Seller) person).getProductsToSell();
    }

    public void addAuction(HashMap<String, String> info){
        storage.addRequest(new Request("add auction", info));
    }

    public void addOff(HashMap<String, String> information, ArrayList<Product> productsInOff) {
        Sale sale = new Sale(null, null, 0, null);
        int offId = sale.getLastSaleId();
        savedProductsInSale.put(offId, productsInOff);
        information.put("seller", person.getUsername());
        information.put("offId", Integer.toString(offId));
        storage.addRequest(new Request("add sale", information));
    }

    public void editOff(int offId, String field, String updatedVersion) throws Exception {
        if (storage.getSaleById(offId) == null)
            throw new Exception("There is no such off!");
        else if (!((Seller) person).getSaleList().contains(storage.getSaleById(offId)))
            throw new Exception("You don't have such off!");
        else {
            HashMap<String, String> information = new HashMap<>();
            information.put("field", field);
            information.put("updatedVersion", updatedVersion);
            information.put("offId", Integer.toString(offId));
            information.put("seller", person.getUsername());
            storage.addRequest(new Request("edit sale", information));
        }
    }

    public boolean doesSellerHaveProduct(int productId) {
        if (storage.getProductById(productId).getSeller().equals(person)) return true;
        else return false;
    }

    public boolean doesProductExist(int productId) {
        if (storage.getProductById(productId) == null)
            return false;
        else return true;
    }

    public Product getSellerProductById(int productId) {
        return storage.getProductById(productId);
    }

    public boolean doesSellerHaveThisOff(int offId) {
        if (((Seller) person).getSaleList().contains(storage.getSaleById(offId)))
            return true;
        else return false;
    }

    public void addProductToOff(int offId, int productId) throws Exception {
        if (storage.getProductById(productId) == null)
            throw new Exception("There is no product with this Id!");
        else if (storage.getSaleById(offId) == null)
            throw new Exception("There is no off with this Id");
        else if (!storage.getProductById(productId).getSeller().getUsername().equals(person.getUsername()))
            throw new Exception("This product is not belonged to you");
        else if (!((Seller) person).getSaleList().contains(storage.getSaleById(offId)))
            throw new Exception("You don't have this off");
        else if (storage.getSaleById(offId).getProductsWithThisSale().contains(storage.getProductById(productId)))
            throw new Exception("This product is already added in this sale!");
        else {
            HashMap<String, String> information = new HashMap<>();
            information.put("offId", Integer.toString(offId));
            information.put("productId", Integer.toString(productId));
            information.put("seller", person.getUsername());
            storage.addRequest(new Request("add product to sale", information));
        }
    }

    public void removeProductFromOff(int offId, int productId) throws Exception {
        if (storage.getProductById(productId) == null)
            throw new Exception("There is no product with this Id!");
        else if (storage.getSaleById(offId) == null)
            throw new Exception("There is no off with this Id");
        else if (!((Seller) person).getSaleList().contains(storage.getSaleById(offId)))
            throw new Exception("You don't have this off");
        else if (!storage.getSaleById(offId).getProductsWithThisSale().contains(storage.getProductById(productId)))
            throw new Exception("This product is not assigned to this sale!");
        else {
            HashMap<String, String> information = new HashMap<>();
            information.put("offId", Integer.toString(offId));
            information.put("productId", Integer.toString(productId));
            information.put("seller", person.getUsername());
            storage.addRequest(new Request("remove product from sale", information));
        }
    }

    public Sale viewSingleOff(int offId) throws Exception {
        if (storage.getSaleById(offId) == null)
            throw new Exception("There is not such off!");
        else if (!((Seller) person).getSaleList().contains(storage.getSaleById(offId)))
            throw new Exception("You don't have this off!");
        else
            return storage.getSaleById(offId);
    }

    public ArrayList<SellLog> getSellerSellHistory() {
        return ((Seller) person).getSellHistory();
    }

    public void addBalance(double amount) {
        person.setBalance(person.getBalance() + amount);
    }

    public boolean doesSellerHasThisSellLog(int sellLogCode) {
        for (SellLog sellLog : ((Seller) person).getSellHistory()) {
            if (sellLogCode == sellLog.getSellCode())
                return true;
        }
        return false;
    }

    public double getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(double minBalance) {
        this.minBalance = minBalance;
    }
}
