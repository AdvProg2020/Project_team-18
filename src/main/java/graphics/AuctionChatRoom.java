package graphics;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AuctionChatRoom extends Menu implements Initializable {
    @FXML
    private Label auctionId;
    @FXML
    private Label customerName;
    @FXML
    private TextField message;
    @FXML
    private TextArea chatBox;

    public AuctionChatRoom(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/AuctionChatRoom.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void sendMessage(){

    }

    public void refresh(){

    }
}
