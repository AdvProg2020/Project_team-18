package Server;

import com.gilecode.yagson.YaGson;
import controller.*;
import javafx.scene.media.MediaPlayer;
import model.*;

import javax.print.DocFlavor;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Scanner;

public class Server {


    public static void main(String[] args) {
        new ServerImpl().run();
        new ChatServer(8080);
    }

    private static class ServerImpl {
        DataInputStream bankDataInputStream ;
        DataOutputStream bankDataOutputStream;
        private void run() {
            try {
                Socket bankSocket = new Socket("127.0.0.1", 8787);
                bankDataInputStream = new DataInputStream(bankSocket.getInputStream());
                bankDataOutputStream = new DataOutputStream(bankSocket.getOutputStream());
                bankDataOutputStream.writeUTF("connected");
                bankDataOutputStream.flush();
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
        AdminManager adminManager = new AdminManager();
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
                    break;
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
                    break;
                case ADD_PRODUCT_TO_OFF:
                    offId = (int) clientMessage.getParameters().get(1);
                    productId = (int) clientMessage.getParameters().get(2);
                    try {
                        sellerManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(0)));
                        sellerManager.addProductToOff(offId, productId);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                    break;
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
                    break;
                case ADD_OFF:
                    HashMap<String, String> informationOFF = (HashMap<String, String>) clientMessage.getParameters().get(1);
                    ArrayList<Product> productsInOff = (ArrayList<Product>) clientMessage.getParameters().get(2);
                    try {
                        sellerManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(0)));
                        sellerManager.addOff(informationOFF,productsInOff);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                    break;
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
                    break;
                case ADD_PRODUCT:
                    HashMap<String, String> information = (HashMap<String, String>) clientMessage.getParameters().get(1);
                    try {
                        sellerManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(0)));
                        sellerManager.addProduct(information);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                    break;
                case REMOVE_PRODUCT_SELLER:
                    productId = (int) clientMessage.getParameters().get(1);
                    try {
                        sellerManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(0)));
                        sellerManager.removeProduct(productId);
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                    break;
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
                    try {
                        System.out.println("I'm here!");
                        customerManager.setCart((Cart) clientMessage.getParameters().get(0));
                        customerManager.increaseProduct((String) clientMessage.getParameters().get(1));
                        System.out.println("done!");
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case GET_CART:
                    //customerManager.setCart((Cart) clientMessage.getParameters().get(0));
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
                    break;
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
                    break;
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
                        purchasingManager.setPerson(storage.getUserByUsername((String)clientMessage.getParameters().get(1)));
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
                    break;
                case PERFORM_PAYMENT:
                    HashMap<String, String> receiverInformation = (HashMap<String, String>) clientMessage.getParameters().get(2);
                    double totalPrice = (double) clientMessage.getParameters().get(3);
                    double percentage = (double) clientMessage.getParameters().get(4);
                    String discountUsed = (String) clientMessage.getParameters().get(5);
                    try {
                        purchasingManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(0)));
                        purchasingManager.setCart((Cart) clientMessage.getParameters().get(1));
                        purchasingManager.performPayment(receiverInformation,totalPrice,percentage,discountUsed);
                        System.out.println("Im in server");
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                    break;
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
                    break;
                case GET_ALL_PRODUCTS:
                    return new ServerMessage(MessageType.GET_ALL_PRODUCTS, productManager.viewAllProducts());
                case GET_ALL_PRODUCTS_IN_SALE:
                    return new ServerMessage(MessageType.GET_ALL_PRODUCTS_IN_SALE, productManager.viewAllProductsWithSale());
                case GET_PRODUCT_BY_ID:
                    productId = (int) clientMessage.getParameters().get(0);
                    try {
                        return new ServerMessage(MessageType.GET_PRODUCT_BY_ID, manager.getProductById(productId));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case GET_SELL_LOG_BY_CODE:
                        return new ServerMessage(MessageType.GET_SELL_LOG_BY_CODE, storage.getSellLogByCode((String)clientMessage.getParameters().get(0)));
                case GET_BUY_LOG_BY_CODE:
                    return new ServerMessage(MessageType.GET_BUY_LOG_BY_CODE, storage.getBuyLogByCode((String)clientMessage.getParameters().get(0)));
                case REGISTER:
                    try {
                        manager.register((HashMap<String, String>)clientMessage.getParameters().get(0));
                        createBankAccount((HashMap<String, String>)clientMessage.getParameters().get(0));
                        break;
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case DOES_ANY_ADMIN_EXIST:
                    return new ServerMessage(MessageType.DOES_ANY_ADMIN_EXIST,manager.doesAnyAdminExist());
                case EDIT_FIELD:
                    field = (String)clientMessage.getParameters().get(0);
                    updatedVersion = (String)clientMessage.getParameters().get(1);
                    try {
                        manager.editField(field,updatedVersion);
                        break;
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case GET_ALL_USERS:
                    return new ServerMessage(MessageType.GET_ALL_USERS,adminManager.viewAllUsers());
                case DELETE_USER:
                    try {
                        adminManager.deleteUser((String) clientMessage.getParameters().get(0));
                        break;
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case REMOVE_PRODUCT:
                    try {
                        adminManager.removeProduct((String) clientMessage.getParameters().get(0));
                        break;
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case ADD_CATEGORY:

                    try {
                        adminManager.addCategory((String) clientMessage.getParameters().get(0),(String) clientMessage.getParameters().get(1));
                        break;
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case VIEW_ALL_DISCOUNT_CODES:
                    return new ServerMessage(MessageType.VIEW_ALL_DISCOUNT_CODES,adminManager.viewAllDiscountCodes());
                case VIEW_ALL_REQUESTS:
                    return new ServerMessage(MessageType.VIEW_ALL_REQUESTS,adminManager.viewAllRequests());
                case VIEW_DISCOUNT_CODE:
                    try {
                        adminManager.viewDiscountCode((String) clientMessage.getParameters().get(0));
                        break;
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case ADD_CUSTOMER_TO_DISCOUNT:
                    try {
                        adminManager.addCustomerToDiscount((String) clientMessage.getParameters().get(0),(Discount) clientMessage.getParameters().get(1));
                        break;
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case REMOVE_CUSTOMER_FROM_DISCOUNT:
                    try {
                        adminManager.removeCustomerFromDiscount((Discount) clientMessage.getParameters().get(0),(String) clientMessage.getParameters().get(1));
                        break;
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case EDIT_DISCOUNT_FIELD:
                    adminManager.editDiscountField((Discount) clientMessage.getParameters().get(0),(String) clientMessage.getParameters().get(1),(String) clientMessage.getParameters().get(2));
                    break;
                case CREATE_DISCOUNT_CODE:
                    String code =(String) clientMessage.getParameters().get(0);
                    LocalDateTime startDate = (LocalDateTime)clientMessage.getParameters().get(1);
                    LocalDateTime endDate = (LocalDateTime)clientMessage.getParameters().get(2);
                    int percentage1 = (int)clientMessage.getParameters().get(3);
                    int usagePerCustomer = (int)clientMessage.getParameters().get(4);
                    double maxAmount = (double)clientMessage.getParameters().get(5);
                    adminManager.createDiscountCode(code,startDate,endDate,percentage1,usagePerCustomer,maxAmount);
                    break;
                case REMOVE_DISCOUNT_CODE:
                    try {
                        adminManager.removeDiscountCode((String) clientMessage.getParameters().get(0));
                        break;
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case REMOVE_CATEGORY:
                    try {
                        adminManager.removeCategory((String) clientMessage.getParameters().get(0));
                        break;
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case ACCEPT_REQUEST:
                    try {
                        adminManager.acceptRequest((String) clientMessage.getParameters().get(0));
                        break;
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case DECLINE_REQUEST:
                    try {
                        adminManager.declineRequest((String) clientMessage.getParameters().get(0));
                        break;
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case EDIT_CATEGORY_BY_NAME:
                        adminManager.editCategoryByName((String) clientMessage.getParameters().get(0),(String) clientMessage.getParameters().get(1));
                        break;
                case EDIT_CATEGORY_BY_PROPERTIES:
                        adminManager.editCategoryByProperties((Category) clientMessage.getParameters().get(0),(String) clientMessage.getParameters().get(1),(String) clientMessage.getParameters().get(2));
                        break;
                case VIEW_CATEGORY:
                    try {
                        return new ServerMessage(MessageType.VIEW_CATEGORY,adminManager.viewCategory((String) clientMessage.getParameters().get(0)));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case VIEW_ALL_BUY_LOGS:
                    return new ServerMessage(MessageType.VIEW_ALL_BUY_LOGS, adminManager.viewAllBuyLogs());

                case SEND_PURCHASE:
                    try {
                        adminManager.sendPurchase((String) clientMessage.getParameters().get(0));
                        break;
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                case CHAT_MESSAGE:
                    ChatClient client = new ChatClient((String)clientMessage.getParameters().get(0));
                    return new ServerMessage(MessageType.CHAT_MESSAGE, client);
                case TERMINATE:
                    manager.terminate();
                    break;
                case CHARGE_WALLET:
                    Person person = storage.getUserByUsername((String) clientMessage.getParameters().get(1));
                    Customer customer = (Customer) person;
                    String charge = "get_token " + customer.getWallet().getBankAccountUsername() + " " +
                            customer.getWallet().getBankAccountPassword();
                    try {
                        System.out.println(charge);
                        String token = getTokenFromBank(charge);
                        int receipt = withdraw(token , (double) clientMessage.getParameters().get(0) ,
                                customer.getWallet().getAccountId() ,"charging_wallet");
                        boolean wasPaid = pay(receipt);
                        if (wasPaid) {
                            customer.setBalance(customer.getBalance() + (double) clientMessage.getParameters().get(0));
                            server.bankDataOutputStream.writeUTF("get_transactions " + token + " -");
                            System.out.println(server.bankDataInputStream.readUTF());
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
            }
            return null;
        }

        private void createBankAccount(HashMap<String, String>information) throws Exception{
            if(!information.get("role").equals("admin")) {
                String username = information.get("username") + information.get("role");
                String password = "im" + information.get("username");
                String string = "create_account " + information.get("name") + " " + information.get("familyName") +
                        " " + username + " " + password + " " + password;
                try {
                    server.bankDataOutputStream.writeUTF(string);
                    server.bankDataOutputStream.flush();
                    String result = server.bankDataInputStream.readUTF();
                    System.out.println(result);
                    if (result.startsWith("Done"))
                        setWallet(information,username,password,result.substring(result.indexOf(" ")+1));
                    else if (result.equals("Passwords do not match"))
                        throw new Exception ("Passwords do not match");
                    else if (result.equals("Username is not available"))
                        throw new Exception("Username is not available");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        private void setWallet(HashMap<String,String> information , String accountUsername , String accountPassword , String id){
            int accountId = Integer.parseInt(id);
            Person person = storage.getUserByUsername(information.get("username"));
            if (information.get("role").equals("seller"))
                ((Seller) person).setWallet(new Wallet(50.0 , accountUsername , accountPassword , accountId, ""));
            if (information.get("role").equals("customer"))
                ((Customer) person).setWallet(new Wallet(50.0 , accountUsername , accountPassword ,accountId, ""));
        }

        private String getTokenFromBank(String info) {
            try {
                server.bankDataOutputStream.writeUTF(info);
                server.bankDataOutputStream.flush();
                return server.bankDataInputStream.readUTF();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }

        private int withdraw(String token, double money, int srcId, String description){
            String request = "create_receipt " + token + " withdraw " + money + " " + srcId + " -1 " + description;
            System.out.println(request);
            try {
                server.bankDataOutputStream.writeUTF(request);
                server.bankDataOutputStream.flush();
                int id = Integer.parseInt(server.bankDataInputStream.readUTF());
                System.out.println(id);
                return id;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return 0;
        }

        private boolean pay(int id){
            try {
                server.bankDataOutputStream.writeUTF("pay " + id);
                server.bankDataOutputStream.flush();
                String result = server.bankDataInputStream.readUTF();
                System.out.println(result);
                if (result.startsWith("done"))
                    return true;
                else return false;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return false;
        }

        @Override
        public void run() {
            handleClient();
        }
    }
}
