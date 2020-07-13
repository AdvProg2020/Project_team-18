package Client;

import Server.ClientMessage;
import Server.MessageType;
import Server.ServerMessage;
import controller.AdminManager;
import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientPurchasingManager extends ClientManager {

    private int buyLogCode = 0;
    private AdminManager adminManager = new AdminManager();

    public ClientPurchasingManager() {
    }

    public void performPayment(HashMap<String, String> receiverInformation, double totalPrice, double discountPercentage,
                               String discountUsed , String username , Cart cart) throws Exception {
        ArrayList<Object> params = new ArrayList<>();
        params.add(username);
        params.add(cart);
        params.add(receiverInformation);
        params.add(totalPrice);
        params.add(discountPercentage);
        params.add(discountUsed);
        ClientMessage clientMessage = new ClientMessage(MessageType.PERFORM_PAYMENT,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage!= null && serverMessage.getMessageType()==MessageType.ERROR){
            throw (Exception)serverMessage.getResult();
        }
    }

    public void checkDiscountValidity(String discountCode) throws Exception {
        ArrayList <Object> params = new ArrayList<>();
        params.add(discountCode);
        ClientMessage clientMessage = new ClientMessage(MessageType.CHECK_DISCOUNT_VALIDITY,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage!= null && serverMessage.getMessageType()==MessageType.ERROR){
            throw (Exception)serverMessage.getResult();
        }
    }

    public boolean doesCustomerHaveEnoughMoney(double price, String username) throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(price);
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.DOES_CUSTOMER_HAVE_MONEY,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
        return (boolean)serverMessage.getResult();
    }

    public double calculateTotalPriceWithDiscount(String discountCode , Cart cart) throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(discountCode);
        params.add(cart);
        ClientMessage clientMessage = new ClientMessage(MessageType.CALCULATE_TOTAL_PRICE_WITH_DISCOUNT,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
        return (double)serverMessage.getResult();
    }

    public double getDiscountPercentage(String discountCode) throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(discountCode);
        ClientMessage clientMessage = new ClientMessage(MessageType.GET_DISCOUNT_PERCENTAGE,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
        return (double)serverMessage.getResult();
    }

    public double getTotalPriceWithoutDiscount(Cart cart) throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(cart);
        ClientMessage clientMessage = new ClientMessage(MessageType.CALCULATE_TOTAL_PRICE_WITHOUT_DISCOUNT,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
        return (double)serverMessage.getResult();
    }

    public void updateDiscountUsagePerPerson(String discountCode , String username) throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(discountCode);
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.UPDATE_DISCOUNT_USAGE,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage!= null && serverMessage.getMessageType()==MessageType.ERROR){
            throw (Exception)serverMessage.getResult();
        }
    }

    public boolean doesCustomerHaveDiscountCode(String discountCode , String username) throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(discountCode);
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.DOES_CUSTOMER_HAVE_DISCOUNT,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
        return (boolean)serverMessage.getResult();
    }
}
