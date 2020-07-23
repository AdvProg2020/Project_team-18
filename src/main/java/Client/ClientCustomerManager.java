package Client;

import Server.ClientMessage;
import Server.MessageType;
import Server.Server;
import Server.ServerMessage;
import controller.AdminManager;
import graphics.Menu;
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

    public HashMap<Product, Integer> getProductsInCart(Cart cart) {
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
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        setPerson((Person) serverMessage.getResult());
        Menu.setPerson((Person) serverMessage.getResult());
        //return (boolean)clientMessage.sendAndReceive().getResult();
        // person.setBalance(person.getBalance() + money);
    }

    public void chargeWallet(double money, String person) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(money);
        params.add(person);
        ClientMessage clientMessage = new ClientMessage(MessageType.CHARGE_WALLET, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
    }

    public Cart increaseProduct(Cart cart, String productId) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(cart);
        params.add(productId);
        ClientMessage clientMessage = new ClientMessage(MessageType.INCREASE_PRODUCT, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
        Cart cart1 = (Cart) serverMessage.getResult();
        return cart1;
    }

    public void decreaseProduct(String productId) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(productId);
        ClientMessage clientMessage = new ClientMessage(MessageType.DECREASE_PRODUCT, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
    }

    public double getCartTotalPrice(Cart cart) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(cart);
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
        if (serverMessage != null && serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
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

    public BuyLog getBuyLogByCode(String code) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(code);
        ClientMessage clientMessage = new ClientMessage(MessageType.GET_BUY_LOG_BY_CODE, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
        return (BuyLog) serverMessage.getResult();
    }

    public void bidding(Auction auction, String updatedVersion, String username) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(auction);
        params.add(updatedVersion);
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.BIDDING, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
    }

    public ArrayList<FileProduct> getPayedFileProducts(String username) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.GET_PAYED_FILE_PRODUCTS, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        return (ArrayList<FileProduct>) serverMessage.getResult();
    }

    public void sendMessage(String sender, String receiver, String message1, String message2) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(sender);
        params.add(receiver);
        params.add(message1);
        params.add(message2);
        System.out.println(message1);
        ClientMessage clientMessage = new ClientMessage(MessageType.SEND_MESSAGE_FROM_CUSTOMER, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
    }

    public void sendMessageFromSupporter(String sender, String receiver, String message) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(sender);
        params.add(receiver);
        params.add(message);
        ClientMessage clientMessage = new ClientMessage(MessageType.SENT_MESSAGE_FROM_SUPPORTER, params);
        clientMessage.sendAndReceive();
    }

    public String getSellerIP(String sellerUsername) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(sellerUsername);
        ClientMessage clientMessage = new ClientMessage(MessageType.GET_SELLER_IP, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
        return (String) serverMessage.getResult();
    }

    public int getSellerPort(String sellerUsername) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(sellerUsername);
        ClientMessage clientMessage = new ClientMessage(MessageType.GET_SELLER_PORT, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
        return (int) serverMessage.getResult();
    }

    public void setFileDownloading(int productId) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(productId);
        ClientMessage clientMessage = new ClientMessage(MessageType.SET_FILE_DOWNLOADING, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
    }
}
