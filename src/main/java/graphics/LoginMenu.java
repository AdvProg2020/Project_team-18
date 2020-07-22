package graphics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.*;

public class LoginMenu extends Menu {
    public LoginMenu(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/LoginMenu.fxml");
    }

    @FXML
    public TextField username;
    @FXML
    public Label message;
    @FXML
    public PasswordField password;

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
            Menu.setCurrentCart(Cart.getCart());
            goToAccount(person);

        } catch (Exception e) {
            message.setText(e.getMessage());
            person.makeOffline();
        }
    }

    private void goToAccount(Person person) {
        if (person == null) {
            System.out.println("You need to login to access account menu.");
            LoginMenu loginRegisterMenu = new LoginMenu(this.getPreviousMenu());
            loginRegisterMenu.run();
        } else if (person instanceof Admin) {
//            System.out.println(person.getAvailability());
//            person.makeOnline();
//            System.out.println(person.getAvailability());
            AdminMenu adminMenu = new AdminMenu(this.getPreviousMenu());
            adminMenu.run();
        } else if (person instanceof Seller) {
//            System.out.println(person.getAvailability());
//            person.makeOnline();
//            System.out.println(person.getAvailability());
            SellerMenu sellerMenu = new SellerMenu(this.getPreviousMenu());
            sellerMenu.run();
        } else if (person instanceof Supporter) {
//            System.out.println(person.getAvailability());
//            person.makeOnline();
//            System.out.println(person.getAvailability());
            SupporterMenu supporterMenu = new SupporterMenu(this.getPreviousMenu());
            supporterMenu.run();
        } else {
//            System.out.println(person.getAvailability());
//            System.out.println(person.getAvailability());
            CustomerMenu customerMenu = new CustomerMenu(this.getPreviousMenu());
            customerMenu.run();
        }
    }
}
