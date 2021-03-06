package graphics;

import Server.ClientView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import model.Seller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminMenu extends Menu implements Initializable {
    @FXML
    public Label usernameLabel;
    @FXML
    public Label passwordLabel;
    @FXML
    public Label nameLabel;
    @FXML
    public Label familyNameLabel;
    @FXML
    public Label emailLabel;
    @FXML
    public Label numberLabel;
    @FXML
    public Label roleLabel;


    public AdminMenu(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/AdminMenu.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewPersonalInfo();
    }

    public void viewPersonalInfo() {
        System.out.println("****" + person);
        usernameLabel.setText(person.getUsername());
        passwordLabel.setText(person.getPassword());
        nameLabel.setText(person.getName());
        familyNameLabel.setText(person.getFamilyName());
        emailLabel.setText(person.getEmail());
        numberLabel.setText(person.getNumber());
        roleLabel.setText("admin");
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
//
//        if (field.equals("password")) {
//            PasswordField passwordField = new PasswordField();
//            HBox content = new HBox();
//            content.setAlignment(Pos.CENTER_LEFT);
//            content.setSpacing(10);
//            content.getChildren().addAll(new Label("Enter your new password :"), passwordField);
//            dialog.getDialogPane().setContent(content);
//            dialog.showAndWait();
//            updatedVersion = passwordField.getText();
//        } else {
//            TextField textField = new TextField();
//            HBox content = new HBox();
//            content.setAlignment(Pos.CENTER_LEFT);
//            content.setSpacing(10);
//
//            content.getChildren().addAll(new Label("Enter your new " + field + " :"), textField);
//
//            dialog.getDialogPane().setContent(content);
//            dialog.showAndWait();
//            updatedVersion = textField.getText();
//        }
//        return updatedVersion;
//    }


    public void goToManageProductsMenu(ActionEvent actionEvent) {
        AdminMangeProducts adminMangeProducts = new AdminMangeProducts(this);
        adminMangeProducts.run();
    }

    public void goToManageUsersMenu(ActionEvent actionEvent) {
        AdminManageUsersMenu adminManageUsersMenu = new AdminManageUsersMenu(this);
        adminManageUsersMenu.run();
    }

    public void goToManageCodesMenu(ActionEvent actionEvent) {
        AdminManageCodesMenu adminManageCodesMenu = new AdminManageCodesMenu(this);
        adminManageCodesMenu.run();
    }

    public void goToManageRequestsMenu(ActionEvent actionEvent) {
        AdminManageRequestsMenu adminManageRequestsMenu = new AdminManageRequestsMenu(this);
        adminManageRequestsMenu.run();
    }

    public void goToManageCategoryMenu(ActionEvent actionEvent) {
        AdminManageCategoriesMenu adminManageCategoriesMenu = new AdminManageCategoriesMenu(this);
        adminManageCategoriesMenu.run();
    }

    public void logout(ActionEvent actionEvent) {
        ClientView.setToken(null);
        MainMenu mainMenu = new MainMenu(null);
        try {
            manager.logout(person.getUsername());
        } catch (Exception e) {
            showError("Something went wrong", 30);
        }        person = null;
        mainMenu.run();
    }

    public void confirmCenterSettings () {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Center Settings");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        double money = manager.getShopAccountBalance();
        Button wage = new Button("Set the center's wage");
        wage.setOnAction(e -> setWage());
        Button minBalance = new Button("Set the minimum amount in wallets");
        minBalance.setOnAction(e -> setMinBalance());
        String shopAccountBalance = Double.toString(money);
        Label label = new Label("Shopping Center's money in account : " + shopAccountBalance);
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(label, wage,minBalance);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    private void setWage() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Set Wage");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField = new TextField();
        Label label = new Label("Enter percentage of shop's wage");
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(label, textField);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        try {
            manager.setWage(Integer.parseInt(textField.getText()));
        } catch (Exception e) {
            showError(e.getMessage(),20);
        }
    }

    private void setMinBalance() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Set Minimum Balance");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField = new TextField();
        Label label = new Label("Enter minimum balance that must be in wallets.");
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(label, textField);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        try {
            manager.setMinBalance(Double.parseDouble(textField.getText()));
        } catch (Exception e) {
            showError(e.getMessage(),20);
        }
    }

    public void goToMainMenu() {
        MainMenu mainMenu = new MainMenu(this);
        mainMenu.run();
    }

    public void goToManageBuyLogs() {
        AdminManageBuyLogs adminManageBuyLogs = new AdminManageBuyLogs(this);
        adminManageBuyLogs.run();
    }
}
