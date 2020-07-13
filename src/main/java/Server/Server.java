package Server;

import controller.FileSaver;
import controller.Manager;
import controller.Storage;
import model.Person;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
        private Socket clientSocket;
        private InputStream inputStream;
        private OutputStream outputStream;
        private ClientMessage clientMessage;
        ServerImpl server;
        Manager manager = new Manager();

        public ClientHandler( OutputStream objectOutputStream, InputStream objectInputStream, ServerImpl server) {
            this.inputStream = objectInputStream;
            this.outputStream = objectOutputStream;
            this.server = server;
        }

        private void handleClient(){
            try {
                while (true) {
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                    clientMessage = (ClientMessage) objectInputStream.readObject();
                    System.out.println("message received");
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(interpret(clientMessage));
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
            }
            return null;
        }

        @Override
        public void run() {
            handleClient();
        }
    }
}
