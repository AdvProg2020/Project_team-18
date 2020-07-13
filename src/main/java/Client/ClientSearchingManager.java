package Client;

import Server.ClientMessage;
import Server.MessageType;
import Server.ServerMessage;
import controller.ProductManager;
import model.Category;
import model.Filter;
import model.Product;
import model.Sort;

import java.util.ArrayList;

public class ClientSearchingManager extends ClientManager {

    private ProductManager productManager = new ProductManager();

    public ClientSearchingManager() {

    }

    public ArrayList<Product> viewAllProducts() throws Exception {
        ClientMessage clientMessage = new ClientMessage(MessageType.VIEW_ALL_PRODUCTS, null);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
        return (ArrayList<Product>) serverMessage.getResult();
    }

    public ArrayList<Product> performFilter(String filterTag, String info) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(filterTag);
        params.add(info);
        ClientMessage clientMessage = new ClientMessage(MessageType.PERFORM_FILTER, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
        return (ArrayList<Product>) serverMessage.getResult();
    }

    public ArrayList<Product> performSort(String sortTag) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(sortTag);
        ClientMessage clientMessage = new ClientMessage(MessageType.PERFORM_SORT, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
        return (ArrayList<Product>) serverMessage.getResult();
    }

    public ArrayList<Product> disableFilter(String filterTag, String info) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(filterTag);
        params.add(info);
        ClientMessage clientMessage = new ClientMessage(MessageType.DISABLE_FILTER, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
        return (ArrayList<Product>) serverMessage.getResult();
    }


    public ArrayList<Product> disableSort(String sortTag) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(sortTag);
        ClientMessage clientMessage = new ClientMessage(MessageType.DISABLE_SORT, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
        return (ArrayList<Product>) serverMessage.getResult();
    }

    public ArrayList<Product> performDefaultSort(ArrayList<Product> products) throws Exception{
        ArrayList<Object> params = new ArrayList<>();
        params.add(products);
        ClientMessage clientMessage = new ClientMessage(MessageType.DEFAULT_SORT, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
        return (ArrayList<Product>) serverMessage.getResult();
    }

    public ArrayList<Category> viewAllCategories() throws Exception{
        ClientMessage clientMessage = new ClientMessage(MessageType.VIEW_ALL_CATEGORIES, null);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
        return (ArrayList<Category>) serverMessage.getResult();
    }
}
