package graphics;

import Client.ClientSellerManager;
import Server.ClientView;
import controller.SellerManager;
import controller.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class SellerMenu extends Menu implements Initializable {
    ClientSellerManager sellerManager = new ClientSellerManager();
    //Storage storage = new Storage();
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
    private Label brandLabel;

    public SellerMenu(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/SellerMenu.fxml");
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
        brandLabel.setText(((Seller) person).getCompany());
        roleLabel.setText("seller");
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

    public void editCompanyField() {
//        String newCompany = editFieldsView("company");
//        try {
//            ((Seller) person).setCompany(newCompany);
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
        content.getChildren().addAll(new Label("Enter your new company name :"), textField);
        dialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updatedVersion = textField.getText();
            try {
                ((Seller) person).setCompany(updatedVersion);
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
                sellerManager.addBalance(Double.parseDouble(updatedVersion), person.getUsername());
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
                showError("Invalid email address!", 100);
            }
        }
    }

//    public String editFieldsView(String field) {
//        Dialog<ButtonType> dialog = new Dialog<>();
//        String updatedVersion = "";
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
//            //dialog.showAndWait();
//            Optional<ButtonType> result = dialog.showAndWait();
//            if (result.isPresent() && result.get() == ButtonType.OK) {
//                updatedVersion = passwordField.getText();
//                try {
//                    manager.editField("password", updatedVersion);
//                    viewPersonalInfo();
//                } catch (Exception e) {
//                    showError("Invalid password format!(Use figures or letters)", 100);
//                }
//            }
//            //updatedVersion = passwordField.getText();
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
//            Optional<ButtonType> result = dialog.showAndWait();
//            if (result.isPresent() && result.get() == ButtonType.OK) {
//                updatedVersion = textField.getText();
//                return updatedVersion;
//            }
//        }
//        return updatedVersion;
//    }

    public void viewProducts() {
        SellerProductMenu sellerProductMenu = new SellerProductMenu(this, this.sellerManager);
        sellerProductMenu.run();
    }

    public void viewOffs() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Your Offs");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.FINISH);
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMinSize(300, 300);
        ArrayList<Sale> mySales;
        mySales = ((Seller) person).getSaleList();
        if (mySales.isEmpty()) {
            content.getChildren().addAll(new Label("You don't have any offs yet!"));
        } else {
            for (int i = 0; i < mySales.size(); i++) {
                content.getChildren().addAll(new Label("OFF NUMBER " + (i + 1)));
                content.getChildren().addAll(new Label("Off Id : " + mySales.get(i).getSaleId()));
                content.getChildren().addAll(new Label("Percentage : " + mySales.get(i).getAmountOfSale()));
                content.getChildren().addAll(new Label("Begin Date : " + mySales.get(i).getBeginDate().getYear()
                        + "-" + mySales.get(i).getBeginDate().getMonth() + "-" +
                        mySales.get(i).getBeginDate().getDayOfMonth() + ", Time : "
                        + mySales.get(i).getBeginDate().getHour() + ":" + mySales.get(i).getBeginDate().getMinute()
                        + ":" + +mySales.get(i).getBeginDate().getSecond()));
                content.getChildren().addAll(new Label("End Date : " + mySales.get(i).getEndDate().getYear()
                        + "-" + mySales.get(i).getEndDate().getMonth() + "-" +
                        mySales.get(i).getEndDate().getDayOfMonth() + ", Time : "
                        + mySales.get(i).getEndDate().getHour() + ":" + mySales.get(i).getEndDate().getMinute()
                        + ":" + +mySales.get(i).getEndDate().getSecond()));
                if (mySales.get(i).getProductsWithThisSale().isEmpty()) {
                    content.getChildren().addAll(new Label("No product with this off yet!"));
                } else {
                    content.getChildren().addAll(new Label("This off products are as followed :"));
                    for (Product product : mySales.get(i).getProductsWithThisSale()) {
                        content.getChildren().add(new Label("Product Id : " + product.getProductId() + " Product Name : "
                                + product.getName() + " Product Brand : " + product.getBrand()));
                    }
                }
            }
        }
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    public void viewAuctions() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Your Auctions");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.FINISH);
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMinSize(300, 300);
        ArrayList<Auction> myAuctions;
        myAuctions = ((Seller) person).getThisSellerAuctions();
        if (myAuctions.isEmpty()) {
            content.getChildren().addAll(new Label("You don't have any auctions yet!"));
        } else {
            for (int i = 0; i < myAuctions.size(); i++) {
                content.getChildren().addAll(new Label("AUCTION NUMBER " + (i + 1)));
                content.getChildren().addAll(new Label("Auction Id : " + myAuctions.get(i).getId()));
                content.getChildren().addAll(new Label("Latest Price : " + myAuctions.get(i).getPrice()));
                content.getChildren().addAll(new Label("Begin Date : " + myAuctions.get(i).getBeginDate().getYear()
                        + "-" + myAuctions.get(i).getBeginDate().getMonth() + "-" +
                        myAuctions.get(i).getBeginDate().getDayOfMonth() + ", Time : "
                        + myAuctions.get(i).getBeginDate().getHour() + ":" + myAuctions.get(i).getBeginDate().getMinute()
                        + ":" + +myAuctions.get(i).getBeginDate().getSecond()));
                content.getChildren().addAll(new Label("End Date : " + myAuctions.get(i).getEndDate().getYear()
                        + "-" + myAuctions.get(i).getEndDate().getMonth() + "-" +
                        myAuctions.get(i).getEndDate().getDayOfMonth() + ", Time : "
                        + myAuctions.get(i).getEndDate().getHour() + ":" + myAuctions.get(i).getEndDate().getMinute()
                        + ":" + +myAuctions.get(i).getEndDate().getSecond()));
                Product product = myAuctions.get(i).getProduct();
                content.getChildren().addAll(new Label("Product : \n" + "Product Id : " + product.getProductId() + " Product Name : "
                        + product.getName() + " Product Brand : " + product.getBrand()));
//                if (myAuctions.get(i).getProductsWithThisSale().isEmpty()) {
//                    content.getChildren().addAll(new Label("No product with this off yet!"));
//                } else {
//                    content.getChildren().addAll(new Label("This off products are as followed :"));
//                    for (Product product : myAuctions.get(i).getProductsWithThisSale()) {
//                        content.getChildren().add(new Label("Product Id : " + product.getProductId() + " Product Name : "
//                                + product.getName() + " Product Brand : " + product.getBrand()));
//                    }
//                }
            }
        }
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

//    public void checkValidityOfOffId() {
//        Dialog<ButtonType> dialog = new Dialog<>();
//        dialog.setTitle("Edit Off");
//        dialog.setHeaderText(null);
//        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//        TextField offId = new TextField();
//        HBox content = new HBox();
//        content.setAlignment(Pos.CENTER_LEFT);
//        content.setSpacing(10);
//        content.getChildren().addAll(new Label("Enter the id of the off you want to edit :"), offId);
//        dialog.getDialogPane().setContent(content);
//        Optional<ButtonType> result = dialog.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.OK) {
//            if (!offId.getText().matches("\\d+")) {
//                showError("Off Id is an integer!", 100);
//            } else {
//                if (sellerManager.doesSellerHaveThisOff(Integer.parseInt(offId.getText()))) {
//                    editOff(Integer.parseInt(offId.getText()));
//                } else {
//                    showError("Oops!You don't have off with this Id!", 100);
//                }
//            }
//        }
//    }

//    public void editOff(int offId) {
//        Dialog<String> dialog = new Dialog<>();
//        dialog.setTitle("Edit Off");
//        dialog.setHeaderText(null);
//        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//        VBox content = new VBox();
//        content.setMinSize(500, 300);
//        content.setAlignment(Pos.CENTER);
//        content.setSpacing(10);
//        content.getChildren().addAll(new Label("Choose the field you want to edit"));
//        MenuItem beginDate = new MenuItem("Begin Date");
//        MenuItem endDate = new MenuItem("End Date");
//        MenuItem amountOfOff = new MenuItem("Amount Of Off");
//        MenuItem addProductToOff = new MenuItem("Add Product To Off");
//        MenuItem removeProductFromOff = new MenuItem("Remove Product From Off");
//        MenuButton fields = new MenuButton("Fields", null, beginDate, endDate, amountOfOff, addProductToOff,
//                removeProductFromOff);
//        content.getChildren().add(fields);
//        dialog.getDialogPane().setContent(content);
//        dialog.show();
//        beginDate.setOnAction(e -> editBeginDate(offId));
//
//        endDate.setOnAction(e -> editEndDate(offId));
//
//        amountOfOff.setOnAction(e -> editAmountOfOff(offId));
//
//        addProductToOff.setOnAction(e -> addProductToOff(offId));
//
//        removeProductFromOff.setOnAction(e -> removeProductFromOff(offId));
//    }
//
//    private void editBeginDate(int offId) {
//        Dialog<String> productDialog = new Dialog<>();
//        String updatedVersion;
//        TextField textField = new TextField();
//        productDialog.setTitle("Edit Begin Date");
//        productDialog.setHeaderText(null);
//        productDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//        HBox content = new HBox();
//        content.getChildren().addAll(new Label("Enter your new Begin Date :(in format yyyy,MM,dd,HH,mm)")
//                , textField);
//        productDialog.getDialogPane().setContent(content);
//        productDialog.showAndWait();
//        updatedVersion = textField.getText();
//        if (!updatedVersion.matches("\\d\\d\\d\\d,\\d\\d,\\d\\d,\\d\\d,\\d\\d")) {
//            showError("Invalid Format of date!", 100);
//        } else {
//            try {
//                sellerManager.editOff(offId, "beginDate", updatedVersion);
//                showMessage();
//            } catch (Exception ex) {
//                showError("Oops!Something went wrong!", 100);
//            }
//        }
//    }
//
//    private void editEndDate(int offId) {
//        Dialog<String> productDialog = new Dialog<>();
//        String updatedVersion;
//        TextField textField = new TextField();
//        productDialog.setTitle("Edit End Date");
//        productDialog.setHeaderText(null);
//        productDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//        HBox content = new HBox();
//        content.getChildren().addAll(new Label("Enter your new End Date :(in format yyyy,MM,dd,HH,mm)")
//                , textField);
//        productDialog.getDialogPane().setContent(content);
//        productDialog.showAndWait();
//        updatedVersion = textField.getText();
//        if (!updatedVersion.matches("\\d\\d\\d\\d,\\d\\d,\\d\\d,\\d\\d,\\d\\d")) {
//            showError("Invalid Format of date!", 100);
//        } else {
//            try {
//                sellerManager.editOff(offId, "endDate", updatedVersion);
//                showMessage();
//            } catch (Exception ex) {
//                showError("Oops!Something went wrong!", 100);
//            }
//        }
//    }
//
//    private void editAmountOfOff(int offId) {
//        Dialog<String> productDialog = new Dialog<>();
//        String updatedVersion;
//        TextField textField = new TextField();
//        productDialog.setTitle("Edit Amount Of Off");
//        productDialog.setHeaderText(null);
//        productDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//        HBox content = new HBox();
//        content.getChildren().addAll(new Label("Enter your new amount of off :")
//                , textField);
//        productDialog.getDialogPane().setContent(content);
//        productDialog.showAndWait();
//        updatedVersion = textField.getText();
//        if (!updatedVersion.matches("\\d+\\.?\\d+")) {
//            showError("Invalid amount!", 100);
//        } else {
//            try {
//                sellerManager.editOff(offId, "amountOfSale", updatedVersion);
//                showMessage();
//            } catch (Exception ex) {
//                showError("Oops!Something went wrong!", 100);
//            }
//        }
//    }
//
//    private void addProductToOff(int offId) {
//        Dialog<String> productDialog = new Dialog<>();
//        String updatedVersion;
//        TextField textField = new TextField();
//        productDialog.setTitle("Add product to off");
//        productDialog.setHeaderText(null);
//        productDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//        HBox content = new HBox();
//        content.getChildren().addAll(new Label("Enter productId you want to add to this off :")
//                , textField);
//        productDialog.getDialogPane().setContent(content);
//        productDialog.showAndWait();
//        updatedVersion = textField.getText();
//        try {
//            sellerManager.addProductToOff(offId, Integer.parseInt(updatedVersion));
//            showMessage();
//        } catch (Exception ex) {
//            showError("Oops!Something went wrong!One of the following errors has happened :\n -There is no product with this Id!\n" +
//                    " -This product is not belonged to you!\n -This product is already added in this sale!", 300);
//        }
//    }
//
//    private void removeProductFromOff(int offId) {
//        Dialog<String> productDialog = new Dialog<>();
//        String updatedVersion;
//        TextField textField = new TextField();
//        productDialog.setTitle("Remove product from off");
//        productDialog.setHeaderText(null);
//        productDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//        HBox content = new HBox();
//        content.getChildren().addAll(new Label("Enter productId you want to remove from this off :")
//                , textField);
//        productDialog.getDialogPane().setContent(content);
//        productDialog.showAndWait();
//        updatedVersion = textField.getText();
//        try {
//            sellerManager.removeProductFromOff(offId, Integer.parseInt(updatedVersion));
//            showMessage();
//        } catch (Exception ex) {
//            showError("Oops!Something went wrong!One of the following errors has happened :\n -There is no product with this Id!\n" +
//                    " -This product is not belonged to you!\n -This product is not assigned to this sale!", 300);
//        }
//    }
//
//    public void addAuction() {
//        AddOffPage addOffPage = new AddOffPage(this, this.sellerManager);
//        addOffPage.run();
//    }

    public void viewCategories() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("All Categories");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.FINISH);
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMinSize(300, 100);
        ArrayList<Category> categories;
        try {
            categories = sellerManager.viewAllCategories();
            if (categories.isEmpty()) {
                content.getChildren().addAll(new Label("There's not any categories yet!"));
            } else {
                for (int i = 0; i < categories.size(); i++) {
                    content.getChildren().addAll(new Label("CATEGORY NUMBER " + (i + 1)));
                    content.getChildren().addAll(new Label(categories.get(i).getCategoryName()));
                    content.getChildren().addAll(new Label("This category's attributes are as followed :"));
                    for (String property : categories.get(i).getProperties().keySet()) {
                        content.getChildren().add(new Label(property + " : " + categories.get(i).getProperties().get(property)));
                    }
                }
            }
        } catch (Exception e) {
            showError(e.getMessage(), 20);
        }
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    public void goToSellLogPage() {
        ThisSellerSellLogs thisSellerSellLogs = new ThisSellerSellLogs(this);
        thisSellerSellLogs.run();
    }

    public void goToMainMenu() {
        MainMenu mainMenu = new MainMenu(this);
        mainMenu.run();

    }

    public void wallet() {
        Dialog<String> dialog = new Dialog<>();
        Button addBalance = new Button("Add Balance");
        addBalance.setOnAction(e -> addBalanceToWallet());
        Button withdraw = new Button("Withdraw from wallet");
        withdraw.setOnAction(e -> withdrawFromWallet());
        dialog.setTitle("Managing your wallet");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.FINISH);
        Label label = new Label("Your wallet's balance is : " + person.getBalance());
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(label, addBalance, withdraw);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    public void addBalanceToWallet() {
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
                sellerManager.chargeWallet(Double.parseDouble(updatedVersion), person.getUsername());
            } catch (Exception e) {
                showError(e.getMessage(), 100);
            }
        }
    }

    public void withdrawFromWallet() {
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
                sellerManager.withdrawFromWallet(Double.parseDouble(updatedVersion), person.getUsername());
            } catch (Exception e) {
                showError(e.getMessage(), 100);
            }
        }
    }

    public void logout(ActionEvent actionEvent) {
        ClientView.setToken(null);
        MainMenu mainMenu = new MainMenu(null);
        try {
            sellerManager.logout(person.getUsername());
        } catch (Exception e) {
            showError("Something went wrong", 30);
        }        person = null;
        mainMenu.run();
    }

    @FXML
    private void goToRequestsMenu() {
        SellerRequestMenu sellerRequestMenu = new SellerRequestMenu(this);
        sellerRequestMenu.run();
    }

    public void GoToMyFiles(ActionEvent actionEvent) {
        System.out.println(sellerManager.getSoldFileProducts(person.getUsername()));
    }
}
