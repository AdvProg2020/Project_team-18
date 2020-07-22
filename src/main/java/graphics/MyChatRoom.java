package graphics;

import Client.ClientCustomerManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Supporter;

import java.net.URL;
import java.util.ResourceBundle;

public class MyChatRoom extends Menu implements Initializable {
    ClientCustomerManager clientCustomerManager = new ClientCustomerManager();
    @FXML
    private TextField messageField;
    @FXML
    private TextField receiverName;
    @FXML
    private TextArea chatBox;
    @FXML
    private Label supporterName;

    public MyChatRoom(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/MyChatRoom.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        supporterName.setText(person.getUsername() + " as supporter");
        for (String inbox : ((Supporter) person).getInbox()) {
            chatBox.appendText(inbox + "\n");
        }
    }

    public void sendMessage() {
        String receiver = receiverName.getText();
        String message = messageField.getText();
        if (receiver.equals("")) {
            showError("No recipient!", 50);
        }else if (message.equals("")) {
            showError("No message!", 50);
        } else if (!((Supporter) person).getSenders().contains(receiver)){
            showError("This customer has not contacted you yet!", 50);
        }  else {
            clientCustomerManager.sendMessageFromSupporter(person.getUsername(), receiver, message);
        }
    }
}
