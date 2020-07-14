package Server;

import com.gilecode.yagson.YaGson;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

public class ClientMessage implements Serializable {
    private MessageType messageType;
    private ArrayList<Object> parameters;
    private static YaGson yaGson = new YaGson();
    public  static Scanner scanner;


    public ClientMessage(MessageType messageType, ArrayList<Object> parameters) {
        this.messageType = messageType;
        this.parameters = parameters;
       /* try {
            objectInputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e){
            System.out.println(e.getMessage());
        }*/
    }

    public synchronized ServerMessage sendAndReceive() {
        Formatter formatter = new Formatter(ClientView.getOutputStream());
        formatter.format(yaGson.toJson(this)+"\n");
        formatter.flush();
        String tempServerMessage = scanner.nextLine();
       ServerMessage temp = yaGson.fromJson(tempServerMessage,ServerMessage.class);
        return temp;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public ArrayList<Object> getParameters() {
        return parameters;
    }
}
