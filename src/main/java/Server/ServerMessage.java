package Server;

import java.io.Serializable;

public class ServerMessage implements Serializable {
    private MessageType messageType;
    private Object result;

    public ServerMessage(MessageType messageType, Object result) {
        this.messageType = messageType;
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public MessageType getMessageType() {
        return messageType;
    }
}
