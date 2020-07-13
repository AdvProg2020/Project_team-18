package Client;

import Server.ClientMessage;
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

    public boolean doesAnyAdminExist(){
        return false;
    }

    public void register(HashMap<String, String> information) throws Exception {

    }

    public Person login(String username, String password) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        params.add(password);
        ClientMessage clientMessage = new ClientMessage(MessageType.LOGIN,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
           throw  (Exception)serverMessage.getResult();
        }
        return (Person)serverMessage.getResult();
    }


    public void editField(String field, String updatedVersion) throws Exception {

    }


    public boolean doesUsernameExist(String username) {
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.DOES_USERNAME_EXIST,params);
        return (boolean)clientMessage.sendAndReceive().getResult();

    }




    public Product getProductById(int productId) throws Exception {
        return null;
    }

    public void setPerson(Person person1){
        person = person1;
    }
}
