package Server;

import com.gilecode.yagson.YaGson;
import controller.*;
import javafx.scene.media.MediaPlayer;
import model.*;

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
        PurchasingManager purchasingManager = new PurchasingManager();
        SearchingManager searchingManager = new SearchingManager();
        ProductManager productManager = new ProductManager();
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
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case DOES_SELLER_HAVE_LOG:
                    int sellLogCode = (int) clientMessage.getParameters().get(0);
                    try {
                        sellerManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(1)));
                        return new ServerMessage(MessageType.DOES_SELLER_HAVE_LOG, sellerManager.doesSellerHasThisSellLog(sellLogCode));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case SELLER_SELL_HISTORY:
                    try {
                        sellerManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(0)));
                        return new ServerMessage(MessageType.SELLER_SELL_HISTORY, sellerManager.getSellerSellHistory());
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case REMOVE_PRODUCT_FROM_OFF:
                    int offId = (int) clientMessage.getParameters().get(1);
                    int productId = (int) clientMessage.getParameters().get(2);
                    try {
                        sellerManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(0)));
                        sellerManager.removeProductFromOff(offId, productId);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case ADD_PRODUCT_TO_OFF:
                    offId = (int) clientMessage.getParameters().get(1);
                    productId = (int) clientMessage.getParameters().get(2);
                    try {
                        sellerManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(0)));
                        sellerManager.addProductToOff(offId, productId);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case DOES_SELLER_HAVE_OFF:
                    offId = (int) clientMessage.getParameters().get(0);
                    try {
                        sellerManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(1)));
                        return new ServerMessage(MessageType.DOES_SELLER_HAVE_OFF, sellerManager.doesSellerHaveThisOff(offId));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case DOES_SELLER_HAVE_PRODUCT:
                    productId = (int) clientMessage.getParameters().get(0);
                    try {
                        sellerManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(1)));
                        return new ServerMessage(MessageType.DOES_SELLER_HAVE_PRODUCT, sellerManager.doesSellerHaveProduct(productId));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case EDIT_OFF:
                    offId = (int) clientMessage.getParameters().get(1);
                    String field = (String) clientMessage.getParameters().get(2);
                    String updatedVersion = (String) clientMessage.getParameters().get(3);
                    try {
                        sellerManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(0)));
                        sellerManager.editOff(offId, field, updatedVersion);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case ADD_OFF:
                    HashMap<String, String> informationOFF = (HashMap<String, String>) clientMessage.getParameters().get(1);
                    ArrayList<Product> productsInOff = (ArrayList<Product>) clientMessage.getParameters().get(2);
                    try {
                        sellerManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(0)));
                        sellerManager.addOff(informationOFF,productsInOff);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case EDIT_PRODUCT:
                    productId = (int) clientMessage.getParameters().get(1);
                    field = (String) clientMessage.getParameters().get(2);
                    updatedVersion = (String) clientMessage.getParameters().get(3);
                    try {
                        sellerManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(0)));
                        sellerManager.editProduct(productId, field, updatedVersion);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case ADD_PRODUCT:
                    HashMap<String, String> information = (HashMap<String, String>) clientMessage.getParameters().get(1);
                    try {
                        sellerManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(0)));
                        sellerManager.addProduct(information);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case REMOVE_PRODUCT_SELLER:
                    productId = (int) clientMessage.getParameters().get(1);
                    try {
                        sellerManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(0)));
                        sellerManager.removeProduct(productId);
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
                    customerManager.setCart((Cart) clientMessage.getParameters().get(0));
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
                case DOES_CUSTOMER_HAVE_DISCOUNT:
                    String discountCode = (String) clientMessage.getParameters().get(0);
                    try {
                        purchasingManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(1)));
                        return new ServerMessage(MessageType.DOES_CUSTOMER_HAVE_DISCOUNT, purchasingManager.doesCustomerHaveDiscountCode(discountCode));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case UPDATE_DISCOUNT_USAGE:
                    discountCode = (String) clientMessage.getParameters().get(0);
                    try {
                        purchasingManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(1)));
                        purchasingManager.updateDiscountUsagePerPerson(discountCode);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case GET_DISCOUNT_PERCENTAGE:
                     discountCode = (String) clientMessage.getParameters().get(0);
                    try {
                        return new ServerMessage(MessageType.GET_DISCOUNT_PERCENTAGE, purchasingManager.getDiscountPercentage(discountCode));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case DOES_CUSTOMER_HAVE_MONEY:
                    double price = (double) clientMessage.getParameters().get(0);
                    try {
                        purchasingManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(1)));
                        return new ServerMessage(MessageType.DOES_CUSTOMER_HAVE_MONEY, purchasingManager.doesCustomerHaveEnoughMoney(price));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case CHECK_DISCOUNT_VALIDITY:
                    discountCode = (String) clientMessage.getParameters().get(0);
                    try {
                       purchasingManager.checkDiscountValidity(discountCode);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case PERFORM_PAYMENT:
                    HashMap<String, String> receiverInformation = (HashMap<String, String>) clientMessage.getParameters().get(2);
                    double totalPrice = (double) clientMessage.getParameters().get(3);
                    double percentage = (double) clientMessage.getParameters().get(4);
                    String discountUsed = (String) clientMessage.getParameters().get(5);
                    try {
                        purchasingManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(0)));
                        purchasingManager.setCart((Cart) clientMessage.getParameters().get(1));
                        purchasingManager.performPayment(receiverInformation,totalPrice,percentage,discountUsed);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case CALCULATE_TOTAL_PRICE_WITH_DISCOUNT:
                    discountCode = (String) clientMessage.getParameters().get(0);
                    try {
                        purchasingManager.setCart((Cart) clientMessage.getParameters().get(1));
                        return new ServerMessage(MessageType.CALCULATE_TOTAL_PRICE_WITH_DISCOUNT, purchasingManager.calculateTotalPriceWithDiscount(discountCode));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case CALCULATE_TOTAL_PRICE_WITHOUT_DISCOUNT:
                    try {
                        purchasingManager.setCart((Cart) clientMessage.getParameters().get(0));
                        return new ServerMessage(MessageType.CALCULATE_TOTAL_PRICE_WITHOUT_DISCOUNT, purchasingManager.getTotalPriceWithoutDiscount());
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case VIEW_ALL_PRODUCTS:
                    try {
                        return new ServerMessage(MessageType.VIEW_ALL_PRODUCTS, searchingManager.viewAllProducts());
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case PERFORM_FILTER:
                    String filterTag = (String) clientMessage.getParameters().get(0);
                    String info = (String) clientMessage.getParameters().get(1);
                    try {
                        return new ServerMessage(MessageType.PERFORM_FILTER, searchingManager.performFilter(filterTag,info));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case PERFORM_SORT:
                    String sortTag = (String) clientMessage.getParameters().get(0);
                    try {
                        return new ServerMessage(MessageType.PERFORM_SORT, searchingManager.performSort(sortTag));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case DISABLE_FILTER:
                     filterTag = (String) clientMessage.getParameters().get(0);
                     info = (String) clientMessage.getParameters().get(1);
                    try {
                        return new ServerMessage(MessageType.DISABLE_FILTER, searchingManager.disableFilter(filterTag,info));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case DISABLE_SORT:
                     sortTag = (String) clientMessage.getParameters().get(0);
                    try {
                        return new ServerMessage(MessageType.DISABLE_SORT, searchingManager.disableSort(sortTag));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case DEFAULT_SORT:
                    ArrayList<Product> productsOfSort = (ArrayList<Product>) clientMessage.getParameters().get(0);
                    try {
                        return new ServerMessage(MessageType.DEFAULT_SORT, searchingManager.performDefaultSort(productsOfSort));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case VIEW_ALL_CATEGORIES:
                    try {
                        return new ServerMessage(MessageType.VIEW_ALL_CATEGORIES, searchingManager.viewAllCategories());
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case ADD_COMMENT:
                    productManager.addComment((Integer) clientMessage.getParameters().get(0), (String) clientMessage.getParameters().get(1),
                            (String) clientMessage.getParameters().get(2), (String) clientMessage.getParameters().get(3));
                case GET_ALL_PRODUCTS:
                    return new ServerMessage(MessageType.GET_ALL_PRODUCTS, productManager.viewAllProducts());
                case GET_ALL_PRODUCTS_IN_SALE:
                    return new ServerMessage(MessageType.GET_ALL_PRODUCTS_IN_SALE, productManager.viewAllProductsWithSale());
            }
            return null;
        }

        @Override
        public void run() {
            handleClient();
        }
    }
}
