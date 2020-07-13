package Server;

import com.gilecode.yagson.YaGson;
import controller.CustomerManager;
import controller.FileSaver;
import controller.Manager;
import controller.SellerManager;
import controller.Storage;
import javafx.scene.media.MediaPlayer;
import model.BuyLog;
import model.Customer;
import model.Person;
import model.Product;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Scanner;

public class Server {


    public static void main(String[] args) {
        new ServerImpl().run();
    }

    private static class ServerImpl {
        private void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(9090);

                while (true) {
                    Socket clientSocket;
                    try {
                        clientSocket = serverSocket.accept();
                        System.out.println("client accepted");
                        OutputStream outputStream = clientSocket.getOutputStream();
                        InputStream inputStream = clientSocket.getInputStream();
                        new ClientHandler(outputStream, inputStream, this).start();
                    } catch (Exception e) {
                        System.err.println("Error in accepting client!");
                      /*  FileSaver fileSaver = new FileSaver(Storage.getStorage());
                        fileSaver.dataSaver();*/
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


    private static class ClientHandler extends Thread {
        private InputStream inputStream;
        private OutputStream outputStream;
        private ClientMessage clientMessage;
        private YaGson yaGson = new YaGson();
        ServerImpl server;
        Manager manager = new Manager();
        SellerManager sellerManager = new SellerManager();
        CustomerManager customerManager = new CustomerManager();
        Storage storage = new Storage();

        public ClientHandler(OutputStream objectOutputStream, InputStream objectInputStream, ServerImpl server) {
            this.inputStream = objectInputStream;
            this.outputStream = objectOutputStream;
            this.server = server;
        }

        private void handleClient() {
            try {
                while (true) {
                    Scanner scanner = new Scanner(inputStream);
                    clientMessage = yaGson.fromJson(scanner.nextLine(), ClientMessage.class);
                    System.out.println("message received");
                    Formatter formatter = new Formatter(outputStream);
                    formatter.format(yaGson.toJson(interpret(clientMessage)) + "\n");
                    formatter.flush();
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        private ServerMessage interpret(ClientMessage clientMessage) {
            switch (clientMessage.getMessageType()) {
                case DOES_USERNAME_EXIST:
                    String username = (String) clientMessage.getParameters().get(0);
                    return new ServerMessage(MessageType.DOES_USERNAME_EXIST, manager.doesUsernameExist(username));
                case LOGIN:
                    username = (String) clientMessage.getParameters().get(0);
                    String password = (String) clientMessage.getParameters().get(1);
                    try {
                        return new ServerMessage(MessageType.LOGIN, manager.login(username, password));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case SELLER_ADD_BALANCE:
                    double amount = (double) clientMessage.getParameters().get(0);
                    try {
                        sellerManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(1)));
                        sellerManager.addBalance(amount);
                        return new ServerMessage(MessageType.SELLER_ADD_BALANCE, true);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case DOES_SELLER_HAVE_LOG:
                    username = (String) clientMessage.getParameters().get(1);
                    int sellLogCode = (int) clientMessage.getParameters().get(0);
                    try {
                        return new ServerMessage(MessageType.DOES_SELLER_HAVE_LOG, sellerManager.doesSellerHasThisSellLog(sellLogCode, username));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case SELLER_SELL_HISTORY:
                    username = (String) clientMessage.getParameters().get(0);
                    try {
                        return new ServerMessage(MessageType.SELLER_SELL_HISTORY, sellerManager.getSellerSellHistory(username));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case REMOVE_PRODUCT_FROM_OFF:
                    username = (String) clientMessage.getParameters().get(0);
                    int offId = (int) clientMessage.getParameters().get(1);
                    int productId = (int) clientMessage.getParameters().get(2);
                    try {
                        sellerManager.removeProductFromOff(offId, productId, username);
                        return new ServerMessage(MessageType.REMOVE_PRODUCT_FROM_OFF, null);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case ADD_PRODUCT_TO_OFF:
                    username = (String) clientMessage.getParameters().get(0);
                    offId = (int) clientMessage.getParameters().get(1);
                    productId = (int) clientMessage.getParameters().get(2);
                    try {
                        sellerManager.addProductToOff(offId, productId, username);
                        return new ServerMessage(MessageType.ADD_PRODUCT_TO_OFF, null);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case DOES_SELLER_HAVE_OFF:
                    username = (String) clientMessage.getParameters().get(1);
                    offId = (int) clientMessage.getParameters().get(0);
                    try {
                        return new ServerMessage(MessageType.DOES_SELLER_HAVE_OFF, sellerManager.doesSellerHaveThisOff(offId, username));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case DOES_SELLER_HAVE_PRODUCT:
                    username = (String) clientMessage.getParameters().get(1);
                    productId = (int) clientMessage.getParameters().get(0);
                    try {
                        return new ServerMessage(MessageType.DOES_SELLER_HAVE_PRODUCT, sellerManager.doesSellerHaveProduct(productId, username));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case EDIT_OFF:
                    username = (String) clientMessage.getParameters().get(0);
                    offId = (int) clientMessage.getParameters().get(1);
                    String field = (String) clientMessage.getParameters().get(2);
                    String updatedVersion = (String) clientMessage.getParameters().get(3);
                    try {
                        sellerManager.editOff(offId, field, updatedVersion, username);
                        return new ServerMessage(MessageType.EDIT_OFF, null);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case EDIT_PRODUCT:
                    username = (String) clientMessage.getParameters().get(0);
                    productId = (int) clientMessage.getParameters().get(1);
                    field = (String) clientMessage.getParameters().get(2);
                    updatedVersion = (String) clientMessage.getParameters().get(3);
                    try {
                        sellerManager.editProduct(productId, field, updatedVersion, username);
                        return new ServerMessage(MessageType.EDIT_PRODUCT, null);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case ADD_PRODUCT:
                    username = (String) clientMessage.getParameters().get(0);
                    HashMap<String, String> information = (HashMap<String, String>) clientMessage.getParameters().get(1);
                    try {
                        sellerManager.addProduct(information, username);
                        return new ServerMessage(MessageType.ADD_PRODUCT, null);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case REMOVE_PRODUCT_SELLER:
                    username = (String) clientMessage.getParameters().get(0);
                    productId = (int) clientMessage.getParameters().get(1);
                    try {
                        sellerManager.removeProduct(productId, username);
                        return new ServerMessage(MessageType.REMOVE_PRODUCT_SELLER, null);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case ADD_BALANCE:
                    Double money = (Double) clientMessage.getParameters().get(0);
                    customerManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(1)));
                    customerManager.addBalance(money);
                    break;
                case DECREASE_PRODUCT:
                    String productId1 = (String) clientMessage.getParameters().get(0);
                    try {
                        customerManager.decreaseProduct(productId1);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                    break;
                case INCREASE_PRODUCT:
                    String id = (String) clientMessage.getParameters().get(0);
                    try {
                        customerManager.increaseProduct(id);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                    break;
                case GET_CART:
                    HashMap<Product, Integer> products = customerManager.getProductsInCart();
                    return new ServerMessage(MessageType.GET_CART, products);
                case GET_CART_PRICE:
                    Double totalCartPrice = customerManager.getCartTotalPrice();
                    return new ServerMessage(MessageType.GET_CART_PRICE, totalCartPrice);
                case ADD_RATE:
                    int idOfProduct =  (Integer) clientMessage.getParameters().get(0);
                    double rate = (Double) clientMessage.getParameters().get(1);
                    String userId = (String) clientMessage.getParameters().get(2);
                    try {
                        customerManager.rateProduct(idOfProduct, rate, userId);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                }
                case GET_ALL_BUY_LOGS:
                    ArrayList<BuyLog> buyLogs = customerManager.getCustomerBuyLogs((String) clientMessage.getParameters().get(0));
                    return new ServerMessage(MessageType.GET_ALL_BUY_LOGS, buyLogs);
                case DOES_CUSTOMER_HAVE_BUY_LOG:
                    return new ServerMessage(MessageType.DOES_CUSTOMER_HAVE_BUY_LOG,
                            customerManager.doesCustomerHasThisBuyLog((String) clientMessage.getParameters().get(1),
                                    (Integer) clientMessage.getParameters().get(0)));
            }
            return null;
        }

        @Override
        public void run() {
            handleClient();
        }
    }
}
