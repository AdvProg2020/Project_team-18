package Server;

import java.io.Serializable;

public class ServerMessage implements Serializable {
    private Token token;
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

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
