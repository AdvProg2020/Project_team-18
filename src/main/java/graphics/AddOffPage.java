package graphics;

import Client.ClientSellerManager;
import controller.SellerManager;
import controller.Storage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class AddOffPage extends Menu {
    private ClientSellerManager sellerManager;
    private Storage storage = new Storage();

    public AddOffPage(Menu previousMenu, ClientSellerManager sellerManager) {
        super(previousMenu, "src/main/java/graphics/fxml/AddOffPage.fxml");
        this.sellerManager = sellerManager;
    }

    @FXML
    private TextField beginDateField;
    @FXML
    private TextField endDateField;
    @FXML
    private TextField amountOfOffField;

    private ArrayList<Product> productsInSale = new ArrayList<>();

    public void addOff() {
        HashMap<String, String> productInformation = new HashMap<>();
        String beginDate = beginDateField.getText();
        String endDate = endDateField.getText();
        String amountOfOff = amountOfOffField.getText();
        if (checkPriceValidity(amountOfOff) && checkDateValidity(beginDate, "begin") && checkDateValidity(endDate, "end")) {
            productInformation.put("beginDate", beginDate);
            productInformation.put("endDate", endDate);
            productInformation.put("amountOfSale", amountOfOff);
            try {
                sellerManager.addOff(productInformation, productsInSale,person.getUsername());
            } catch (Exception e) {
                showError(e.getMessage(),20);
            }
            showMessage();
            back();
        }
    }

    private boolean checkPriceValidity(String amount) {
        if (amount.equals("")) {
            showError("Please enter price!", 100);
            return false;
        } else if (!amount.matches("\\d+\\.?\\d+")) {
            showError("PLease enter valid price!", 100);
            return false;
        } else
            return true;
    }

    private boolean checkDateValidity(String date, String type) {
        if (date.equals("")) {
            showError("Please enter " + type + " date!", 100);
            return false;
        } else if (!date.matches("\\d\\d\\d\\d,\\d\\d,\\d\\d,\\d\\d,\\d\\d")) {
            showError("PLease enter valid " + type + " date!", 100);
            return false;
        } else
            return true;
    }

    public void addProductToSale() {
        javafx.scene.control.Dialog<ButtonType> productDialog = new javafx.scene.control.Dialog<>();
        String productId;
        TextField textField = new TextField();
        productDialog.setTitle("Add product to off");
        productDialog.setHeaderText(null);
        productDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        HBox content = new HBox();
        content.getChildren().addAll(new Label("Enter productId you want to add to this off :"), textField);
        productDialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = productDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            productId = textField.getText();
            if (!productId.matches("\\d+")) {
                showError("ProductId is an integer!", 100);
            } else {
                if (storage.getProductById(Integer.parseInt(productId)) == null) {
                    showError("There's not such product!", 100);
                } else if (!person.getUsername().equals(storage.getProductById(Integer.parseInt(productId)).getSeller().getUsername())) {
                    showError("You don't have this product!", 100);
                } else {
                    productsInSale.add(storage.getProductById(Integer.parseInt(productId)));
                }
            }
        }
    }

    public void showMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Request Message");
        alert.setContentText("Your Request has been Successfully sent to admin(s)!");
        alert.setHeaderText(null);
        alert.show();
    }
}
