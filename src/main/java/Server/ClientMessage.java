package Server;

import graphics.ClientView;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientMessage {
    protected static Socket socket = ClientView.getSocket();
    private MessageType messageType;
    private ArrayList<Object> parameters;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public ClientMessage(MessageType messageType, ArrayList<Object> parameters) {
        this.messageType = messageType;
        this.parameters = parameters;
        try {
            objectInputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public synchronized Object sendAndReceive() throws Exception {
            objectOutputStream.writeObject(this);
            return ((ServerMessage)objectInputStream.readObject()).getResult();
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public ArrayList<Object> getParameters() {
        return parameters;
    }
}
