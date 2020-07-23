package graphics;

import Client.ClientCustomerManager;
import Server.ClientMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.Supporter;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController extends Menu implements Initializable {
    private ClientCustomerManager clientCustomerManager = new ClientCustomerManager();
    private Supporter supporter;
    @FXML
    private Label supporterNameLabel;
    @FXML
    private Label customerNameLabel;
    @FXML
    private TextField messageField;
    @FXML
    private TextArea chatBox;

    public ChatController(Menu previousMenu, Supporter supporter) {
        super(previousMenu, "src/main/java/graphics/fxml/ChatRoom.fxml");
        this.supporter = supporter;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        supporterNameLabel.setText(supporter.getUsername());
        customerNameLabel.setText(person.getUsername());
    }

    public void sendMessage() {
        String message = messageField.getText();
        boolean result = false;
        if (message.equals("")) {
            showError("No message to be sent!", 30);
        } else {
            String message1 = person.getUsername() + " : " + message;
            String message2 = "You : " + message;
            result = clientCustomerManager.sendMessage(person.getUsername(), supporter.getUsername(), message1, message2);
        }
//        if (result) {
//            chatBox.append("You : " + message);
//            System.out.println(message);
//        } else {
//            showError("Unable to send your message!", 30);
//        }
    }
}
