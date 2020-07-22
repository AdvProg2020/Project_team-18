package graphics;

import Client.ClientCustomerManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Customer;
import model.Supporter;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatController extends Menu implements Initializable {
    @FXML private TextField message;
    @FXML private TextArea chatBox;
    @FXML private Label supporterName;
    @FXML private Label customerName;
    private ClientCustomerManager clientCustomerManager = new ClientCustomerManager();
    private Supporter supporter;
    public ChatController(Menu previousMenu, Supporter supporter) {
        super(previousMenu, "src/main/java/graphics/fxml/ChatRoom.fxml");
        this.supporter = supporter;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        supporterName.setText(supporter.getUsername());
        customerName.setText(person.getUsername());
        updateMessages();
    }

    public void sendMessage() {
        String message3 = message.getText();
        boolean result = false;
        if (message3.equals("")) {
            showError("No message to be sent!", 30);
        } else {
            String message1 = person.getUsername() + " : " + message3;
            String message2 = "You : " + message3;
            clientCustomerManager.sendMessage(person.getUsername(), supporter.getUsername(), message1, message2);
        }
//        if (result) {
//            chatBox.append("You : " + message);
//            System.out.println(message);
//        } else {
//            showError("Unable to send your message!", 30);
//        }
    }

    public void updateMessages(){
        ArrayList<Integer> indexes = new ArrayList<>();
        for (String username : ((Customer) person).getSupportersSender()) {
            if (username.equals(this.supporter.getUsername())){
                indexes.add(((Customer) person).getSupportersSender().indexOf(username));
            }
        }
        for (Integer index : indexes) {
            chatBox.appendText(((Customer) person).getSupportersSender().get(index) + "\n");
        }
    }
}
