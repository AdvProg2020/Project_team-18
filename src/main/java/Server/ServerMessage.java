package Server;

public class ServerMessage {
    private MessageType messageType;
    private Object result;

    public ServerMessage(MessageType messageType, Object result) {
        this.messageType = messageType;
        this.result = result;
    }
}
