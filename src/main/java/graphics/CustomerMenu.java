package graphics;

import Client.ClientCustomerManager;
import Server.*;
import controller.CustomerManager;
import controller.Storage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.*;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerMenu extends Menu implements Initializable {
    private ClientCustomerManager customerManager = new ClientCustomerManager();
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label familyNameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label numberLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label avaLabel;

    public CustomerMenu(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/CustomerMenu.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewPersonalInfo();
    }

    public void viewPersonalInfo() {
        usernameLabel.setText(person.getUsername());
        passwordLabel.setText(person.getPassword());
        nameLabel.setText(person.getName());
        familyNameLabel.setText(person.getFamilyName());
        emailLabel.setText(person.getEmail());
        numberLabel.setText(person.getNumber());
        balanceLabel.setText(Double.toString(person.getBalance()));
        roleLabel.setText("customer");
        avaLabel.setText(person.getAvailability().toString());
    }

    public void editPasswordField() {
        Dialog<ButtonType> dialog = new Dialog<>();
        String updatedVersion;
        dialog.setTitle("Change Personal Information");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        PasswordField passwordField = new PasswordField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter your new password :"), passwordField);
        dialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updatedVersion = passwordField.getText();
            try {
                manager.editField("password", updatedVersion);
                viewPersonalInfo();
            } catch (Exception e) {
                showError(e.getMessage(), 100);
            }
        }
    }

    public void editNameField() {
        Dialog<ButtonType> dialog = new Dialog<>();
        String updatedVersion;
        dialog.setTitle("Change Personal Information");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField = new TextField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter your new name :"), textField);
        dialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updatedVersion = textField.getText();
            try {
                manager.editField("name", updatedVersion);
                viewPersonalInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void editFamilyNameField() {
//        String newFamilyName = editFieldsView("family name");
//        try {
//            manager.editField("familyName", newFamilyName);
//            viewPersonalInfo();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Dialog<ButtonType> dialog = new Dialog<>();
        String updatedVersion;
        dialog.setTitle("Change Personal Information");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField = new TextField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter your new family name :"), textField);
        dialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updatedVersion = textField.getText();
            try {
                manager.editField("familyName", updatedVersion);
                viewPersonalInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void editBalanceField() {
//        String newBalance = editFieldsView("balance");
//        try {
//            sellerManager.addBalance(Double.parseDouble(newBalance));
//            viewPersonalInfo();
//        } catch (Exception e) {
//            showError("Invalid balance!", 100);
//        }
        Dialog<ButtonType> dialog = new Dialog<>();
        String updatedVersion;
        dialog.setTitle("Change Personal Information");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField = new TextField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter amount of money you want to add to you account :"), textField);
        dialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updatedVersion = textField.getText();
            try {
                customerManager.addBalance(Double.parseDouble(updatedVersion), person.getUsername());
                viewPersonalInfo();
            } catch (Exception e) {
                showError(e.getMessage(), 100);
            }
        }
    }

    public void editEmailField() {
//        String newEmail = editFieldsView("email");
//        try {
//            manager.editField("email", newEmail);
//            viewPersonalInfo();
//        } catch (Exception e) {
//            showError("Invalid email address!", 100);
//        }
        Dialog<ButtonType> dialog = new Dialog<>();
        String updatedVersion;
        dialog.setTitle("Change Personal Information");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField = new TextField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter your new e-mail address :"), textField);
        dialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updatedVersion = textField.getText();
            try {
                manager.editField("email", updatedVersion);
                viewPersonalInfo();
            } catch (Exception e) {
                showError(e.getMessage(), 100);
            }
        }
    }

    public void editNumberField() {
//        String newNumber = editFieldsView("phone number");
//        try {
//            manager.editField("number", newNumber);
//            viewPersonalInfo();
//        } catch (Exception e) {
//            showError("Invalid phone number!", 100);
//        }
        Dialog<ButtonType> dialog = new Dialog<>();
        String updatedVersion;
        dialog.setTitle("Change Personal Information");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField = new TextField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter your new number :"), textField);
        dialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updatedVersion = textField.getText();
            try {
                manager.editField("number", updatedVersion);
                viewPersonalInfo();
            } catch (Exception e) {
                showError(e.getMessage(), 100);
            }
        }
    }

//    public String editFieldsView(String field) {
//        Dialog<String> dialog = new Dialog<>();
//        String updatedVersion;
//        dialog.setTitle("Change Personal Information");
//        dialog.setHeaderText(null);
//        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//        if (field.equals("password")) {
//            PasswordField passwordField = new PasswordField();
//            HBox content = new HBox();
//            content.setAlignment(Pos.CENTER_LEFT);
//            content.setSpacing(10);
//            content.getChildren().addAll(new Label("Enter your new password :"), passwordField);
//            dialog.getDialogPane().setContent(content);
//            dialog.showAndWait();
//            updatedVersion = passwordField.getText();
//
//        } else {
//            TextField textField = new TextField();
//            HBox content = new HBox();
//            content.setAlignment(Pos.CENTER_LEFT);
//            content.setSpacing(10);
//            if (field.equals("balance")) {
//                content.getChildren().addAll(new Label("Enter amount of money you want to add to you account :"), textField);
//            } else {
//                content.getChildren().addAll(new Label("Enter your new " + field + " :"), textField);
//            }
//            dialog.getDialogPane().setContent(content);
//            dialog.showAndWait();
//            updatedVersion = textField.getText();
//        }
//        return updatedVersion;
//    }

    public void viewDiscounts() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Your Discounts");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.FINISH);
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMinSize(300, 100);
        ArrayList<Discount> myDiscounts;
        myDiscounts = ((Customer) person).getAllDiscounts();
        if (myDiscounts.isEmpty()) {
            content.getChildren().addAll(new Label("There's not any discounts for you yet!"));
        } else {
            for (int i = 0; i < myDiscounts.size(); i++) {
                content.getChildren().addAll(new Label("YOUR DISCOUNT NUMBER " + (i + 1)));
                content.getChildren().addAll(new Label(myDiscounts.get(i).getDiscountCode()));
                content.getChildren().addAll(new Label("This discount's features are as followed :"));
                content.getChildren().addAll(new Label("Percentage : " + myDiscounts.get(i).getPercentage()));
                content.getChildren().addAll(new Label("Max Amount" + myDiscounts.get(i).getMaxAmount()));
                content.getChildren().addAll(new Label("Begin Date : " + myDiscounts.get(i).getBeginDate().getYear()
                        + "-" + myDiscounts.get(i).getBeginDate().getMonth() + "-" +
                        myDiscounts.get(i).getBeginDate().getDayOfMonth() + ", Time : "
                        + myDiscounts.get(i).getBeginDate().getHour() + ":" + myDiscounts.get(i).getBeginDate().getMinute()
                        + ":" + +myDiscounts.get(i).getBeginDate().getSecond()));
                content.getChildren().addAll(new Label("End Date : " + myDiscounts.get(i).getEndDate().getYear()
                        + "-" + myDiscounts.get(i).getEndDate().getMonth() + "-" +
                        myDiscounts.get(i).getEndDate().getDayOfMonth() + ", Time : "
                        + myDiscounts.get(i).getEndDate().getHour() + ":" + myDiscounts.get(i).getEndDate().getMinute()
                        + ":" + +myDiscounts.get(i).getEndDate().getSecond()));
            }
        }
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();

    }

    public void wallet() {
        Dialog<String> dialog = new Dialog<>();
        Button addBalance = new Button("Add Balance");
        addBalance.setOnAction(e -> addBalanceToWallet());
        dialog.setTitle("Managing your wallet");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.FINISH);
        Label label = new Label("Your wallet's balance is : " + person.getBalance());
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(label , addBalance);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    public void addBalanceToWallet () {
        Dialog<ButtonType> dialog = new Dialog<>();
        String updatedVersion;
        dialog.setTitle("Change Personal Information");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField = new TextField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter amount of money you want to add to you account :"), textField);
        dialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updatedVersion = textField.getText();
            try {
                customerManager.chargeWallet(Double.parseDouble(updatedVersion), person.getUsername());
            } catch (Exception e) {
                showError(e.getMessage(), 100);
            }
        }
    }

    public void goToCartMenu() {
        CartMenu cartMenu = new CartMenu(this);
        cartMenu.run();
    }

    public void goToMainMenu() {
        MainMenu mainMenu = new MainMenu(this);
        mainMenu.run();
    }

    public void goToBuyLogPage() {
        ThisPersonBuyLogs thisPersonBuyLogs = new ThisPersonBuyLogs(this);
        thisPersonBuyLogs.run();
    }

    public void goToChatRoom(){
        ArrayList<Object> params = new ArrayList<>();
        params.add(person.getUsername());
        ClientMessage clientMessage = new ClientMessage(MessageType.CHAT_MESSAGE,params);
        ChatClient client = ((ChatClient)clientMessage.sendAndReceive().getResult());
        StackPane root = new StackPane();
        UI ui = new UI(person.getUsername(), false);
        if(!client.connect()) {
            JOptionPane.showMessageDialog(null,
                    "Connection to " + "localhost" + " failed.",
                    "Chat",
                    JOptionPane.ERROR_MESSAGE);
            //Platform.exit();
        }
        ui.setClient(client);
        Thread clientThread = new Thread(client);
        clientThread.setDaemon(true);
        clientThread.start();
        root.getChildren().add(ui);
        stage.setScene(new Scene(root));
        /*Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Online Supporters");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.FINISH);
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMinSize(300, 100);
        ArrayList<Person> onlineSupporters;

        onlineSupporters = ((Customer) person).getAllDiscounts();
        if (onlineSupporters.isEmpty()) {
            content.getChildren().addAll(new Label("There's not any discounts for you yet!"));
        } else {
            for (int i = 0; i < onlineSupporters.size(); i++) {
                content.getChildren().addAll(new Label("YOUR DISCOUNT NUMBER " + (i + 1)));
                content.getChildren().addAll(new Label(onlineSupporters.get(i).getDiscountCode()));
                content.getChildren().addAll(new Label("This discount's features are as followed :"));
                content.getChildren().addAll(new Label("Percentage : " + onlineSupporters.get(i).getPercentage()));
                content.getChildren().addAll(new Label("Max Amount" + onlineSupporters.get(i).getMaxAmount()));
                content.getChildren().addAll(new Label("Begin Date : " + onlineSupporters.get(i).getBeginDate().getYear()
                        + "-" + onlineSupporters.get(i).getBeginDate().getMonth() + "-" +
                        onlineSupporters.get(i).getBeginDate().getDayOfMonth() + ", Time : "
                        + onlineSupporters.get(i).getBeginDate().getHour() + ":" + onlineSupporters.get(i).getBeginDate().getMinute()
                        + ":" + +onlineSupporters.get(i).getBeginDate().getSecond()));
                content.getChildren().addAll(new Label("End Date : " + onlineSupporters.get(i).getEndDate().getYear()
                        + "-" + onlineSupporters.get(i).getEndDate().getMonth() + "-" +
                        onlineSupporters.get(i).getEndDate().getDayOfMonth() + ", Time : "
                        + onlineSupporters.get(i).getEndDate().getHour() + ":" + onlineSupporters.get(i).getEndDate().getMinute()
                        + ":" + +onlineSupporters.get(i).getEndDate().getSecond()));
            }
        }
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();*/

    }

    public void goToAuctionsMenu(){
        AuctionsMenu auctionsMenu = new AuctionsMenu(this);
        auctionsMenu.run();
    }

    @FXML
    private void customerRequestMenu() {
        CustomerRequestsMenu customerRequestsMenu = new CustomerRequestsMenu(this);
        customerRequestsMenu.run();
    }

    public void logout(ActionEvent actionEvent) {
        ClientView.setToken(null);
        MainMenu mainMenu = new MainMenu(null);
        person.makeOffline();
        person = null;
        mainMenu.run();
    }
}
