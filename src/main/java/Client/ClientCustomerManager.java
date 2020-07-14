package Client;

import Server.ClientMessage;
import Server.MessageType;
import Server.ServerMessage;
import controller.AdminManager;
import javafx.fxml.Initializable;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientCustomerManager extends ClientManager {

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
        ArrayList<Object> params = new ArrayList<>();
        params.add(cart);
        ClientMessage clientMessage = new ClientMessage(MessageType.GET_CART, params);
        return (HashMap<Product, Integer>) clientMessage.sendAndReceive().getResult();
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

    public void increaseProduct(Cart cart, String productId) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(cart);
        params.add(productId);
        ClientMessage clientMessage = new ClientMessage(MessageType.INCREASE_PRODUCT, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void decreaseProduct(String productId) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(productId);
        ClientMessage clientMessage = new ClientMessage(MessageType.DECREASE_PRODUCT, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public double getCartTotalPrice() {
        ArrayList<Object> params = new ArrayList<>();
        ClientMessage clientMessage = new ClientMessage(MessageType.GET_CART_PRICE, params);
        return (Double) clientMessage.sendAndReceive().getResult();
    }

    public double getCartTotalPriceWithSale() {
        return cart.getTotalPriceWithSale();
    }

    public BuyLog getOrderWithId(String orderId) {
        return storage.getBuyLogByCode(orderId);
    }

    public void rateProduct(int productId, double rate, String username) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(productId);
        params.add(rate);
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.ADD_RATE, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public ArrayList<BuyLog> getCustomerBuyLogs(String username) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.GET_ALL_BUY_LOGS, params);
        return (ArrayList<BuyLog>) clientMessage.sendAndReceive().getResult();
    }

    public boolean doesCustomerHasThisBuyLog(String username, int logCode) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(logCode);
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.DOES_CUSTOMER_HAVE_BUY_LOG, params);
        return (boolean) clientMessage.sendAndReceive().getResult();
    }

    public BuyLog getBuyLogByCode (String code) throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(code);
        ClientMessage clientMessage = new ClientMessage(MessageType.GET_BUY_LOG_BY_CODE,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
        return (BuyLog) serverMessage.getResult();
    }

}
