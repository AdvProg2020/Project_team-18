package Server;

import controller.Manager;

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
                ServerSocket serverSocket = new ServerSocket(0);
                while (true) {
                    Socket clientSocket;
                    try {
                        clientSocket = serverSocket.accept();
                        ObjectOutputStream clientDataOutputStream = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
                        ObjectInputStream clientDataInputStream = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                        new ClientHandler(clientSocket, clientDataOutputStream, clientDataInputStream, this).start();
                    } catch (Exception e) {
                        System.err.println("Error in accepting client!");
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
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;
        private ClientMessage clientMessage;
        ServerImpl server;
        Manager manager = new Manager();

        public ClientHandler(Socket clientSocket, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, ServerImpl server) {
            this.clientSocket = clientSocket;
            this.objectInputStream = objectInputStream;
            this.objectOutputStream = objectOutputStream;
            this.server = server;
        }

        private void handleClient(){
            try {
                while (true) {
                    clientMessage = (ClientMessage) objectInputStream.readObject();
                    System.out.println(clientMessage.toString());
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
