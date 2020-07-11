package Server;

import graphics.ClientView;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientMessage {
    protected static Socket socket = ClientView.getSocket();
    private MessageType messageType;
    private ArrayList<Object> parameters;

    public ClientMessage(MessageType messageType, ArrayList<Object> parameters) {
        this.messageType = messageType;
        this.parameters = parameters;
    }

    public void send() {

    }
}
