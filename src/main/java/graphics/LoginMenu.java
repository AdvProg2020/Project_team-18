package graphics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Admin;
import model.Person;
import model.Seller;
import model.Supporter;

public class LoginMenu extends Menu {
    public LoginMenu(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/LoginMenu.fxml");
    }
    @FXML public TextField username;
    @FXML public Label message;
    @FXML public PasswordField password;
    public void goToRegisterMenu(ActionEvent actionEvent) {
        RegisterMenu registerMenu = new RegisterMenu(this);
        registerMenu.run();
    }

    public void login(ActionEvent actionEvent) {
        message.setText("");
        String username = this.username.getText();
        String password = this.password.getText();
        if (!manager.doesUsernameExist(username)) {
            message.setText("username does not exist!");
            return;
        }
        try {
            Menu.setPerson(manager.login(username, password));
            goToAccount(person);

        } catch (Exception e) {
            message.setText(e.getMessage());
        }
    }

    private void goToAccount(Person person){
        if (person == null) {
            System.out.println("You need to login to access account menu.");
            LoginMenu loginRegisterMenu = new LoginMenu(this.getPreviousMenu());
            loginRegisterMenu.run();
        } else if (person instanceof Admin) {
            AdminMenu adminMenu = new AdminMenu(this.getPreviousMenu());
            adminMenu.run();
        } else if (person instanceof Seller) {
            SellerMenu sellerMenu = new SellerMenu(this.getPreviousMenu());
            sellerMenu.run();
        } else if (person instanceof Supporter){
            SupporterMenu supporterMenu = new SupporterMenu(this.getPreviousMenu());
            supporterMenu.run();
        }
        else {
            CustomerMenu customerMenu = new CustomerMenu(this.getPreviousMenu());
            customerMenu.run();
        }
    }
}
