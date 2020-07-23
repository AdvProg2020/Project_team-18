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
    @FXML
    private TextField message;
    @FXML
    private TextArea chatBox;
    @FXML
    private Label supporterName;
    @FXML
    private Label customerName;
    private ClientCustomerManager clientCustomerManager = new ClientCustomerManager();
    private Supporter supporter;

    public ChatController(Menu previousMenu, Supporter supporter) {
        super(previousMenu, "src/main/java/graphics/fxml/ChatRoom.fxml");
        this.supporter = supporter;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPerson(clientCustomerManager.getPersonByUsername(person.getUsername()));
        supporterName.setText(supporter.getUsername());
        customerName.setText(person.getUsername());
        updateMessages();
    }

    public void sendMessage() {
        String message3 = message.getText();
        if (message3.equals("")) {
            showError("No message to be sent!", 30);
        } else {
            String message1 = person.getUsername() + " : " + message3;
            String message2 = "You : " + message3;
            clientCustomerManager.sendMessage(person.getUsername(), supporter.getUsername(), message1, message2);
            message.clear();
        }
    }

    public void updateMessages() {
        ArrayList<Integer> indexes = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < ((Customer) person).getSupportersSender().size(); i++) {
            if (((Customer) person).getSupportersSender().get(i).equals(this.supporter.getUsername())) {
                indexes.add(i);
                System.out.println(indexes.get(j));
                j++;
            }
        }
        for (Integer index : indexes) {
            System.out.println((((Customer) person).getCombinedMessages().get(index).charAt(((Customer) person).getCombinedMessages().get(index).length() - 1)));
            if ((((Customer) person).getCombinedMessages().get(index).charAt(((Customer) person).getCombinedMessages().get(index).length() - 2) == '>')){
                chatBox.appendText(((Customer) person).getCombinedMessages().get(index).concat("Not responded yet!"));
            } else {
                chatBox.appendText(((Customer) person).getCombinedMessages().get(index) + "\n");
            }
        }
    }

    public void refresh(){
        chatBox.clear();
        setPerson(clientCustomerManager.getPersonByUsername(person.getUsername()));
        updateMessages();
    }
}
