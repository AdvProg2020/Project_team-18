package Server;

import com.gilecode.yagson.YaGson;
import com.sun.org.apache.bcel.internal.generic.CASTORE;
import controller.CustomerManager;
import controller.FileSaver;
import controller.Manager;
import controller.Storage;
import javafx.scene.media.MediaPlayer;
import model.Customer;
import model.Person;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

public class Server {


    public static void main(String[] args) {
        new ServerImpl().run();
    }

    private static class ServerImpl {
        private void run(){
            try {
                ServerSocket serverSocket = new ServerSocket(9090);

                while (true) {
                    Socket clientSocket;
                    try {
                        clientSocket = serverSocket.accept();
                        System.out.println("client accepted");
                        OutputStream outputStream = clientSocket.getOutputStream();
                        InputStream inputStream = clientSocket.getInputStream();
                        new ClientHandler( outputStream, inputStream, this).start();
                    } catch (Exception e) {
                        System.err.println("Error in accepting client!");
                      /*  FileSaver fileSaver = new FileSaver(Storage.getStorage());
                        fileSaver.dataSaver();*/
                        break;
                    }
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }


    private static class ClientHandler extends Thread{
        private InputStream inputStream;
        private OutputStream outputStream;
        private ClientMessage clientMessage;
        private YaGson yaGson = new YaGson();
        ServerImpl server;
        Manager manager = new Manager();
        CustomerManager customerManager = new CustomerManager();
        Storage storage = new Storage();

        public ClientHandler( OutputStream objectOutputStream, InputStream objectInputStream, ServerImpl server) {
            this.inputStream = objectInputStream;
            this.outputStream = objectOutputStream;
            this.server = server;
        }

        private void handleClient(){
            try {
                while (true) {
                    Scanner scanner = new Scanner(inputStream);
                    clientMessage = yaGson.fromJson(scanner.nextLine(),ClientMessage.class);
                    System.out.println("message received");
                    Formatter formatter = new Formatter(outputStream);
                    formatter.format(yaGson.toJson(interpret(clientMessage))+"\n");
                    formatter.flush();
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        private ServerMessage interpret(ClientMessage clientMessage){
            switch (clientMessage.getMessageType()){
                case DOES_USERNAME_EXIST :
                    String username = (String) clientMessage.getParameters().get(0);
                        return new ServerMessage(MessageType.DOES_USERNAME_EXIST, manager.doesUsernameExist(username));
                case LOGIN:
                    username = (String) clientMessage.getParameters().get(0);
                    String password = (String) clientMessage.getParameters().get(1);
                    try {
                        return new ServerMessage(MessageType.LOGIN,manager.login(username,password));
                    } catch (Exception e) {
                        return new ServerMessage(MessageType.ERROR,e);
                    }
                case ADD_BALANCE:
                    Double money = (Double) clientMessage.getParameters().get(0);
                    customerManager.setPerson(storage.getUserByUsername((String) clientMessage.getParameters().get(1)));
                    customerManager.addBalance(money);
                    break;
                case DECREASE_PRODUCT:
                    String productId = (String) clientMessage.getParameters().get(0);
                    try {
                        customerManager.decreaseProduct(productId);
                    } catch (Exception e){
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                    break;
                case INCREASE_PRODUCT:
                    String id = (String) clientMessage.getParameters().get(0);
                    try {
                        customerManager.increaseProduct(id);
                    } catch (Exception e){
                        return new ServerMessage(MessageType.ERROR, e);
                    }
                    break;
            }
            return null;
        }

        @Override
        public void run() {
            handleClient();
        }
    }
}
