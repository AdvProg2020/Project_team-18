package graphics;

import Client.ClientAdminManager;
import controller.AdminManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AddDiscountPage extends Menu implements Initializable {

    ClientAdminManager adminManager = new ClientAdminManager();

    @FXML private TextField code = new TextField();
    @FXML private TextField percentage = new TextField();
    @FXML private TextField maxUsage = new TextField();
    @FXML private TextField maxAmount = new TextField();
    @FXML private DatePicker beginDate = new DatePicker();
    @FXML private DatePicker endDate = new DatePicker();

    public AddDiscountPage(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/AddDiscountPage.fxml");
    }

    @FXML
    private void addDiscount(){
        LocalDateTime begin = beginDate.getValue().atStartOfDay();
        LocalDateTime end = endDate.getValue().atStartOfDay();
        adminManager.createDiscountCode(code.getText(),begin,end,Integer.parseInt(percentage.getText()),
                Integer.parseInt(maxUsage.getText()),Double.parseDouble(maxAmount.getText()));
        showMessage();
        back();
    }

    public void showMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Discount Creation");
        alert.setContentText("Discount has been Successfully created!");
        alert.setHeaderText(null);
        alert.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
