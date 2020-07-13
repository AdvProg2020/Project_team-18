package Client;

import Server.ClientMessage;
import Server.MessageType;
import Server.ServerMessage;
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
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void removeProduct(int productId , String username) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        params.add(productId);
        ClientMessage clientMessage = new ClientMessage(MessageType.REMOVE_PRODUCT_SELLER,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
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
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void addOff(HashMap<String, String> information, ArrayList<Product> productsInOff) {
        Sale sale = new Sale(null, null, 0, null);
        int offId = sale.getLastSaleId();
        savedProductsInSale.put(offId, productsInOff);
        information.put("seller", person.getUsername());
        information.put("offId", Integer.toString(offId));
        storage.addRequest(new Request("add sale", information));
    }

    public void editOff(int offId, String field, String updatedVersion , String username) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        params.add(offId);
        params.add(field);
        params.add(updatedVersion);
        ClientMessage clientMessage = new ClientMessage(MessageType.EDIT_OFF,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
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
        if (serverMessage.getMessageType()==MessageType.ERROR){
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
        if (serverMessage.getMessageType()==MessageType.ERROR){
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
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
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
}
