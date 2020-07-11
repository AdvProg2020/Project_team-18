package Client;

import controller.AdminManager;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientCustomerManager extends ClientManager{

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
        person.setBalance(person.getBalance() + money);
    }

    public void increaseProduct(String productId) throws Exception {
        if (storage.getProductById(Integer.parseInt(productId)) == null)
            throw new Exception("There is not such product!");
        if (storage.getProductById(Integer.parseInt(productId)).getSupply() == 0)
            throw new Exception("We have run out of this product!!");
        for (Product product : cart.getProductsInCart().keySet()) {
            if (product.equals(storage.getProductById(Integer.parseInt(productId)))) {
                super.cart.addNumberOfProductInTheCart(storage.getProductById(Integer.parseInt(productId)));
                return;
            }
        }
        cart.addProductToCart(storage.getProductById(Integer.parseInt(productId)));
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

    public void rateProduct(int productId, double rate) throws Exception {
        if (!storage.getProductById(productId).getThisProductsBuyers().contains(person))
            throw new Exception("You can't rate a product which you didn't buy it!!");
        else {
            Rate rateOfThisProduct = new Rate(person.getUsername(), storage.getProductById(productId), rate);
            storage.addRate(rateOfThisProduct);
            storage.getProductById(productId).addRate(rateOfThisProduct);
            storage.getProductById(productId).calculateAverageRate();
        }
    }

    public ArrayList<BuyLog> getCustomerBuyLogs() {
        return ((Customer) person).getBuyHistory();
    }

    public boolean doesCustomerHasThisBuyLog(int logCode) {
        for (BuyLog buyLog : ((Customer) person).getBuyHistory()) {
            if (logCode == buyLog.getBuyCode()) {
                return true;
            }
        }
        return false;
    }

}
