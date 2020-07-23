package controller;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
        if (findProductInCart(Integer.parseInt(productId),cart.getProductsInCart().keySet())) {
                cart.addNumberOfProductInTheCart(storage.getProductById(Integer.parseInt(productId)));
                System.out.println("proper!");
                return cart;
            }
        cart.addProductToCart(storage.getProductById(Integer.parseInt(productId)));
        System.out.println("again proper!");
        return cart;
    }

    public Cart decreaseProduct(String productId) throws Exception {
        System.out.println(cart.getProductsInCart().keySet().toString());
        System.out.println(storage.getProductById(Integer.parseInt(productId)));
        if (storage.getProductById(Integer.parseInt(productId)) == null)
            throw new Exception("There is not such product!");
        else if (!findProductInCart(Integer.parseInt(productId), cart.getProductsInCart().keySet()))
            throw new Exception("You don't have a product with this Id in your cart!");
        else
            super.cart.decreaseProduct(storage.getProductById(Integer.parseInt(productId)));
        return cart;
    }

    private boolean findProductInCart (int productId, Set<Product> products) {
        if (products.isEmpty())
            return false;
        for (Product product : products) {
            if (product.getId() == productId)
                return true;
        }
        return false;
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
