package Client;

import Server.ClientMessage;
import Server.MessageType;
import Server.ServerMessage;
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

    public void addBalance(double money, String username) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(money);
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.ADD_BALANCE, params);
        clientMessage.sendAndReceive();
            //return (boolean)clientMessage.sendAndReceive().getResult();
       // person.setBalance(person.getBalance() + money);
    }

    public void increaseProduct(String productId) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(productId);
        ClientMessage clientMessage = new ClientMessage(MessageType.INCREASE_PRODUCT, params);
        clientMessage.sendAndReceive();
    }

    public void decreaseProduct(String productId) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(productId);
        ClientMessage clientMessage = new ClientMessage(MessageType.DECREASE_PRODUCT, params);
        clientMessage.sendAndReceive();
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
