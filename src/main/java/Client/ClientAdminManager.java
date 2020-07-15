package Client;

import Server.ClientMessage;
import Server.MessageType;
import Server.ServerMessage;
import controller.SellerManager;
import model.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class ClientAdminManager extends ClientManager{

    private SellerManager tempSellerManager = new SellerManager();


    public ArrayList<Person> viewAllUsers (){
        ClientMessage clientMessage = new ClientMessage(MessageType.GET_ALL_USERS,null);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        return (ArrayList<Person>)serverMessage.getResult();

    }


    public void deleteUser (String username) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.DELETE_USER,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage!=null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void removeProduct (String productId) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(productId);
        ClientMessage clientMessage = new ClientMessage(MessageType.REMOVE_PRODUCT,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage!=null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void addCategory (String name,String imageOption) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(name);
        params.add(imageOption);
        ClientMessage clientMessage = new ClientMessage(MessageType.ADD_CATEGORY,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage!=null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public ArrayList<Discount> viewAllDiscountCodes (){
        ClientMessage clientMessage = new ClientMessage(MessageType.VIEW_ALL_DISCOUNT_CODES,null);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        return (ArrayList<Discount>)serverMessage.getResult();
    }

    public ArrayList<Request> viewAllRequests (){
        ClientMessage clientMessage = new ClientMessage(MessageType.VIEW_ALL_REQUESTS,null);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        return (ArrayList<Request>)serverMessage.getResult();
    }

    public Discount viewDiscountCode (String code) throws Exception{
        ArrayList<Object> params = new ArrayList<>();
        params.add(code);
        ClientMessage clientMessage = new ClientMessage(MessageType.VIEW_DISCOUNT_CODE,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage!=null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
        return (Discount) serverMessage.getResult();
    }

    public void addCustomerToDiscount(String username , Discount discount) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        params.add(discount);
        ClientMessage clientMessage = new ClientMessage(MessageType.ADD_CUSTOMER_TO_DISCOUNT,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage!=null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void removeCustomerFromDiscount (Discount discount , String username) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(discount);
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.REMOVE_CUSTOMER_FROM_DISCOUNT,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage!=null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void editDiscountField ( Discount discount,String field , String updatedVersion ){
        ArrayList<Object> params = new ArrayList<>();
        params.add(discount);
        params.add(field);
        params.add(updatedVersion);
        ClientMessage clientMessage = new ClientMessage(MessageType.EDIT_DISCOUNT_FIELD,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
    }

    public void createDiscountCode (String code, LocalDateTime startDate, LocalDateTime endDate,
                                    int percentage, int usagePerCustomer, double maxAmount){
        ArrayList<Object> params = new ArrayList<>();
        params.add(code);
        params.add(startDate);
        params.add(endDate);
        params.add(percentage);
        params.add(usagePerCustomer);
        params.add(maxAmount);
        ClientMessage clientMessage = new ClientMessage(MessageType.CREATE_DISCOUNT_CODE,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
    }

    public void removeDiscountCode (String code) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(code);
        ClientMessage clientMessage = new ClientMessage(MessageType.REMOVE_DISCOUNT_CODE,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage!=null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void removeCategory (String name) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(name);
        ClientMessage clientMessage = new ClientMessage(MessageType.REMOVE_CATEGORY,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage!=null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void editCategoryByName (String oldName , String newName){
        ArrayList<Object> params = new ArrayList<>();
        params.add(oldName);
        params.add(newName);
        ClientMessage clientMessage = new ClientMessage(MessageType.EDIT_CATEGORY_BY_NAME,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
    }

    public void editCategoryByProperties (Category category ,String property, String newValue){
        ArrayList<Object> params = new ArrayList<>();
        params.add(category);
        params.add(property);
        params.add(newValue);
        ClientMessage clientMessage = new ClientMessage(MessageType.EDIT_CATEGORY_BY_PROPERTIES,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
    }

    public void acceptRequest (String requestId) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(requestId);
        ClientMessage clientMessage = new ClientMessage(MessageType.ACCEPT_REQUEST,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage!=null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }

    public void declineRequest (String requestId) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(requestId);
        ClientMessage clientMessage = new ClientMessage(MessageType.DECLINE_REQUEST,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage!=null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
    }
    public ArrayList<Category> viewAllCategories() {
        ClientMessage clientMessage = new ClientMessage(MessageType.VIEW_ALL_CATEGORIES, null);
        return (ArrayList<Category>) clientMessage.sendAndReceive().getResult();

    }

    public Category viewCategory(String text) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(text);
        ClientMessage clientMessage = new ClientMessage(MessageType.VIEW_CATEGORY,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage!=null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
        return (Category) serverMessage.getResult();
    }
}
