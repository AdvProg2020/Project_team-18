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

    public void addProduct(HashMap<String, String> information) throws Exception{
        ArrayList<Object> params = new ArrayList<>();
        params.add(person.getUsername());
        params.add(information);
        ClientMessage clientMessage = new ClientMessage(MessageType.ADD_PRODUCT,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void removeProduct(int productId) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(person.getUsername());
        params.add(productId);
        ClientMessage clientMessage = new ClientMessage(MessageType.REMOVE_PRODUCT_SELLER,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void editProduct(int productId, String field, String updatedVersion) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(person.getUsername());
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

    public void editOff(int offId, String field, String updatedVersion) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(person.getUsername());
        params.add(offId);
        params.add(field);
        params.add(updatedVersion);
        ClientMessage clientMessage = new ClientMessage(MessageType.EDIT_OFF,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public boolean doesSellerHaveProduct(int productId) throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(productId);
        params.add(person.getUsername());
        ClientMessage clientMessage = new ClientMessage(MessageType.DOES_SELLER_HAVE_PRODUCT,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
        return (boolean)serverMessage.getResult();
    }

    public boolean doesSellerHaveThisOff(int offId) throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(offId);
        params.add(person.getUsername());
        ClientMessage clientMessage = new ClientMessage(MessageType.DOES_SELLER_HAVE_OFF,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
        return (boolean)serverMessage.getResult();
    }

    public void addProductToOff(int offId, int productId) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(person.getUsername());
        params.add(offId);
        params.add(productId);
        ClientMessage clientMessage = new ClientMessage(MessageType.ADD_PRODUCT_TO_OFF,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void removeProductFromOff(int offId, int productId) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(person.getUsername());
        params.add(offId);
        params.add(productId);
        ClientMessage clientMessage = new ClientMessage(MessageType.REMOVE_PRODUCT_FROM_OFF,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public ArrayList<SellLog> getSellerSellHistory() throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(person.getUsername());
        ClientMessage clientMessage = new ClientMessage(MessageType.SELLER_SELL_HISTORY,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
        return (ArrayList) serverMessage.getResult();
    }

    public void addBalance(double amount) throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(amount);
        params.add(person.getUsername());
        ClientMessage clientMessage = new ClientMessage(MessageType.SELLER_ADD_BALANCE,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public boolean doesSellerHasThisSellLog(int sellLogCode) throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(sellLogCode);
        params.add(person.getUsername());
        ClientMessage clientMessage = new ClientMessage(MessageType.DOES_SELLER_HAVE_LOG,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
        return (boolean)serverMessage.getResult();
    }
}
