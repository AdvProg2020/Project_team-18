package Client;

import Server.ClientMessage;
import Server.MessageType;
import Server.ServerMessage;
import graphics.Menu;
import model.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientSellerManager extends ClientManager {

    private static HashMap<Integer, ArrayList<Product>> savedProductsInSale = new HashMap<>();

    public ClientSellerManager() {
    }

    public HashMap<Integer, ArrayList<Product>> getSavedProductsInSale() {
        return savedProductsInSale;
    }

    public void addProduct(HashMap<String, String> information, String username) throws Exception{
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        params.add(information);
        ClientMessage clientMessage = new ClientMessage(MessageType.ADD_PRODUCT,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void addAuction(HashMap<String, String> info) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(info);
        ClientMessage clientMessage = new ClientMessage(MessageType.ADD_AUCTION, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if(serverMessage != null && serverMessage.getMessageType() == MessageType.ERROR){
            throw (Exception)serverMessage.getResult();
        }
    }

    public void removeProduct(int productId , String username) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        params.add(productId);
        ClientMessage clientMessage = new ClientMessage(MessageType.REMOVE_PRODUCT_SELLER,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void editProduct(int productId, String field, String updatedVersion , String username) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        params.add(productId);
        params.add(field);
        params.add(updatedVersion);
        ClientMessage clientMessage = new ClientMessage(MessageType.EDIT_PRODUCT,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void addOff(HashMap<String, String> information, ArrayList<Product> productsInOff , String username) throws Exception{
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        params.add(information);
        params.add(productsInOff);
        ClientMessage clientMessage = new ClientMessage(MessageType.ADD_OFF,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void editOff(int offId, String field, String updatedVersion , String username) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        params.add(offId);
        params.add(field);
        params.add(updatedVersion);
        ClientMessage clientMessage = new ClientMessage(MessageType.EDIT_OFF,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public boolean doesSellerHaveProduct(int productId , String username) throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(productId);
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.DOES_SELLER_HAVE_PRODUCT,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
        return (boolean)serverMessage.getResult();
    }

    public boolean doesSellerHaveThisOff(int offId , String username) throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(offId);
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.DOES_SELLER_HAVE_OFF,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
        return (boolean)serverMessage.getResult();
    }

    public void addProductToOff(int offId, int productId , String username) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        params.add(offId);
        params.add(productId);
        ClientMessage clientMessage = new ClientMessage(MessageType.ADD_PRODUCT_TO_OFF,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void removeProductFromOff(int offId, int productId , String username) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        params.add(offId);
        params.add(productId);
        ClientMessage clientMessage = new ClientMessage(MessageType.REMOVE_PRODUCT_FROM_OFF,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public ArrayList<SellLog> getSellerSellHistory(String username) throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.SELLER_SELL_HISTORY,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
        return (ArrayList) serverMessage.getResult();
    }

    public void addBalance(double amount ,String username) throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(amount);
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.SELLER_ADD_BALANCE,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
        setPerson((Person)serverMessage.getResult());
        Menu.setPerson((Person)serverMessage.getResult());
    }

    public boolean doesSellerHasThisSellLog(int sellLogCode , String username) throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(sellLogCode);
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.DOES_SELLER_HAVE_LOG,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
        return (boolean)serverMessage.getResult();
    }

    public ArrayList<Category> viewAllCategories() throws Exception{
        ClientMessage clientMessage = new ClientMessage(MessageType.VIEW_ALL_CATEGORIES, null);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
        return (ArrayList<Category>) serverMessage.getResult();
    }

    public SellLog getSellLogByCode (String code) throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(code);
        ClientMessage clientMessage = new ClientMessage(MessageType.GET_SELL_LOG_BY_CODE,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
        return (SellLog) serverMessage.getResult();
    }

    public void chargeWallet(double money, String username) throws Exception{
        ArrayList<Object> params = new ArrayList<>();
        params.add(money);
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.CHARGE_WALLET, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void withdrawFromWallet(double money, String username) throws Exception{
        ArrayList<Object> params = new ArrayList<>();
        params.add(money);
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.WITHDRAW_WALLET, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }
    public ArrayList<FileProduct> getSoldFileProducts(String username){
        ArrayList <Object> params = new ArrayList<>();
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.GET_SOLD_FILE_PRODUCTS,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        return (ArrayList<FileProduct>)serverMessage.getResult();
    }

    public boolean sendIPPort(String ip, int port,String username) {
        ArrayList <Object> params = new ArrayList<>();
        params.add(ip);
        params.add(port);
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.SEND_IP_PORT,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        return (boolean)serverMessage.getResult();
    }

    public Seller setIPPortNull(String username) {
        ArrayList <Object> params = new ArrayList<>();
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.SET_IP_PORT_NULL,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        return (Seller)serverMessage.getResult();
    }
}
