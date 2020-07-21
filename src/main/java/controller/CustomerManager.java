package controller;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerManager extends Manager {

    private AdminManager adminManager = new AdminManager();

    public boolean CartIsEmpty() {
        if (super.cart.getProductsInCart().isEmpty())
            return true;
        else return false;
    }

    public Cart getCart() {
        return super.cart;
    }

    public HashMap<Product, Integer> getProductsInCart() {
        return super.cart.getProductsInCart();
    }

    public Product getProductInCart(int productId) throws Exception {
        if (!super.cart.getProductsInCart().containsKey(storage.getProductById(productId)))
            throw new Exception("You don't have such product in your cart!");
        else
            return storage.getProductById(productId);
    }

    public boolean doesProductExist(int productId) {
        if (storage.getProductById(productId) == null)
            return false;
        else return true;
    }

    public void addBalance(double money) {
        System.out.println("in function");
        person.setBalance(person.getBalance() + money);
        System.out.println("after function");
    }

    public Cart increaseProduct(String productId) throws Exception {
        System.out.println("in function");
        if (storage.getProductById(Integer.parseInt(productId)) == null) {
            System.out.println("not such product");
            throw new Exception("There is not such product!");
        }
        if (storage.getProductById(Integer.parseInt(productId)).getSupply() == 0) {
            System.out.println("run out of");
            throw new Exception("We have run out of this product!!");
        }
        for (Product product : cart.getProductsInCart().keySet()) {
            if (product.equals(storage.getProductById(Integer.parseInt(productId)))) {
                cart.addNumberOfProductInTheCart(storage.getProductById(Integer.parseInt(productId)));
                System.out.println("proper!");
            }
        }
        cart.addProductToCart(storage.getProductById(Integer.parseInt(productId)));
        System.out.println("again proper!");
        return cart;
    }

    public void decreaseProduct(String productId) throws Exception {
        if (storage.getProductById(Integer.parseInt(productId)) == null)
            throw new Exception("There is not such product!");
        else if (!super.cart.getProductsInCart().containsKey(storage.getProductById(Integer.parseInt(productId))))
            throw new Exception("You don't have a product with this Id in your cart!");
        else
            super.cart.decreaseProduct(storage.getProductById(Integer.parseInt(productId)));
    }

    public double getCartTotalPrice() {
        return cart.getTotalPrice();
    }

    public double getCartTotalPriceWithSale() {
        return cart.getTotalPriceWithSale();
    }

    public BuyLog getOrderWithId(String orderId) {
        return storage.getBuyLogByCode(orderId);
    }

    public void rateProduct(int productId, double rate, String username) throws Exception {
        if (!storage.getProductById(productId).getThisProductsBuyers().contains(person))
            throw new Exception("You can't rate a product which you didn't buy it!!");
        else {
            Rate rateOfThisProduct = new Rate(username, storage.getProductById(productId), rate);
            storage.addRate(rateOfThisProduct);
            storage.getProductById(productId).addRate(rateOfThisProduct);
            storage.getProductById(productId).calculateAverageRate();
        }
    }

    public ArrayList<BuyLog> getCustomerBuyLogs(String username) {
        return ((Customer) storage.getUserByUsername(username)).getBuyHistory();
    }

    public boolean doesCustomerHasThisBuyLog(String username, int logCode) {
        for (BuyLog buyLog : ((Customer) storage.getUserByUsername(username)).getBuyHistory()) {
            if (logCode == buyLog.getBuyCode()) {
                return true;
            }
        }
        return false;
    }

}
