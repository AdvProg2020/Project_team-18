package graphics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class RegisterMenu extends Menu implements Initializable {
    @FXML
    public TextField username;
    @FXML
    public PasswordField password;
    @FXML
    public TextField name;
    @FXML
    public TextField familyName;
    @FXML
    public TextField email;
    @FXML
    public TextField number;
    @FXML
    public Label message;
    @FXML
    public Label companyLabel;
    @FXML
    public TextField company;
    @FXML
    public CheckBox adminRole;
    @FXML
    public CheckBox sellerRole;
    @FXML
    public CheckBox customerRole;
    @FXML
    public CheckBox supporterRole;

    private Menu previousMenu;

    public RegisterMenu(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/RegisterMenu.fxml");
        this.previousMenu = previousMenu;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        companyLabel.visibleProperty().bind(sellerRole.selectedProperty());
        company.visibleProperty().bind(sellerRole.selectedProperty());
    }

    public void register(ActionEvent actionEvent) {
        message.setText("");
        String username = this.username.getText();
        String password = this.password.getText();
        String name = this.name.getText();
        String familyName = this.familyName.getText();
        String email = this.email.getText();
        String number = this.number.getText();
        if (!adminRole.isSelected() && !sellerRole.isSelected() && !customerRole.isSelected() && !supporterRole.isSelected()) {
            message.setText("please choose your role!");
            return;
        }
        if (adminRole.isSelected() && manager.doesAnyAdminExist() && !(previousMenu instanceof AdminManageUsersMenu)) {
            message.setText("Only admins can add admins!");
            return;
        }
        if (supporterRole.isSelected() && !(previousMenu instanceof AdminManageUsersMenu)) {
            message.setText("Only admins can add supporters!");
            return;
        }
        if (manager.doesUsernameExist(username)) {
            message.setText("username already exists!");
            return;
        }
        HashMap<String, String> data = new HashMap<>();
        if (sellerRole.isSelected()) {
            String company = this.company.getText();
            data.put("company", company);
        }

        data.put("username", username);
        data.put("password", password);
        data.put("role", roleGetter());
        data.put("name", name);
        data.put("familyName", familyName);
        data.put("email", email);
        data.put("number", number);
        try {
            manager.register(data);
            message.setText("register successful!");
        } catch (Exception e) {
            message.setText(e.getMessage());
        }
    }

    private String roleGetter() {
        if (sellerRole.isSelected()) {
            return "seller";
        } else if (customerRole.isSelected()) {
            return "customer";
        } else if (supporterRole.isSelected()) {
            return "supporter";
        }
        return "admin";
    }

}


