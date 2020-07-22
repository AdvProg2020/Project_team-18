package Client;

import Server.ClientMessage;
import Server.ClientView;
import Server.MessageType;
import Server.ServerMessage;
import controller.FileSaver;
import controller.Storage;
import graphics.Menu;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientManager {
    protected Storage storage;
    protected static Person person;
    protected Cart cart;
    protected static ArrayList<Filter> currentFilters = new ArrayList<>();
    protected static ArrayList<Sort> currentSorts = new ArrayList<>();

    public boolean doesAnyAdminExist() {
        ClientMessage clientMessage = new ClientMessage(MessageType.DOES_ANY_ADMIN_EXIST, null);
        return (boolean) clientMessage.sendAndReceive().getResult();
    }

    public void register(HashMap<String, String> information) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(information);
        ClientMessage clientMessage = new ClientMessage(MessageType.REGISTER, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }

    }

    public Person login(String username, String password) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        params.add(password);
        ClientMessage clientMessage = new ClientMessage(MessageType.LOGIN, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
        ClientView.setToken(serverMessage.getToken());
        return (Person) serverMessage.getResult();
    }


    public void editField(String field, String updatedVersion) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(field);
        params.add(updatedVersion);
        ClientMessage clientMessage = new ClientMessage(MessageType.EDIT_FIELD, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
        setPerson((Person) serverMessage.getResult());
        Menu.setPerson((Person) serverMessage.getResult());

    }


    public boolean doesUsernameExist(String username) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.DOES_USERNAME_EXIST, params);
        return (boolean) clientMessage.sendAndReceive().getResult();

    }

    public Product getProductById(int productId) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(productId);
        ClientMessage clientMessage = new ClientMessage(MessageType.GET_PRODUCT_BY_ID, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
        return (Product) serverMessage.getResult();
    }

    public void setPerson(Person person1) {
        person = person1;
    }

    public void logout(String username) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.LOGOUT, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
    }

    public void terminate(String username) {
        ArrayList<Object> params = new ArrayList<>();
        if (username.equals("")) {
            ClientMessage clientMessage = new ClientMessage(MessageType.TERMINATE, null);
            ServerMessage serverMessage = clientMessage.sendAndReceive();
        } else {
            params.add(username);
            ClientMessage clientMessage = new ClientMessage(MessageType.TERMINATE, params);
            ServerMessage serverMessage = clientMessage.sendAndReceive();
        }
    }

    public double getShopAccountBalance() {
        ClientMessage clientMessage = new ClientMessage(MessageType.GET_SHOP_BALANCE, null);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        return Double.parseDouble((String) serverMessage.getResult());
    }

    public void setWage(int percentage) throws Exception{
        ArrayList<Object> params = new ArrayList<>();
        params.add(percentage);
        ClientMessage clientMessage = new ClientMessage(MessageType.SET_WAGE, params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType() == MessageType.ERROR) {
            throw (Exception) serverMessage.getResult();
        }
    }
}
