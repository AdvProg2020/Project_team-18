package graphics;

import Client.ClientCustomerManager;
import Client.ClientManager;
import Server.ClientMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Auction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AuctionChatRoom extends Menu implements Initializable {
    private ClientManager clientManager = new ClientManager();
    private Auction auction;
    @FXML
    private Label auctionId;
    @FXML
    private Label customerName;
    @FXML
    private TextField message;
    @FXML
    private TextArea chatBox;

    public AuctionChatRoom(Menu previousMenu, Auction auction) {
        super(previousMenu, "src/main/java/graphics/fxml/AuctionChatRoom.fxml");
        this.auction = auction;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPerson(clientManager.getPersonByUsername(person.getUsername()));
        this.auction = clientManager.getAuctionById(auction.getId());
        auctionId.setText(Integer.toString(auction.getId()));
        updateMessages();
    }

    public void sendMessage() {
        String message3 = message.getText();
        if (message3.equals("")) {
            showError("No message to be sent!", 30);
        } else {
            String message1 = person.getUsername() + " : " + message3;
            clientManager.sendMessageToChatInbox(person.getUsername(), auction.getId(), message1);
            message.clear();
        }
    }

    public void refresh() {
        this.auction = clientManager.getAuctionById(auction.getId());
        chatBox.clear();
        updateMessages();
    }

    @FXML
    private void updateMessages() {
        this.auction = clientManager.getAuctionById(auction.getId());
        for (int i = 0; i < auction.getThisAuctionChat().size(); i++) {
            if (person.getUsername().equals(auction.getSenders().get(i))) {
                String previous = auction.getThisAuctionChat().get(i).substring(3);
                String newString = "You" + previous;
                chatBox.appendText(newString + "\n");
            } else {
                chatBox.appendText(auction.getThisAuctionChat().get(i) + "\n");
            }
        }
    }
}
