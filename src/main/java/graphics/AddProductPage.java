package graphics;

import Client.ClientSellerManager;
import controller.SellerManager;
import controller.Storage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.Category;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AddProductPage extends Menu implements Initializable {
    ClientSellerManager sellerManager;
    Storage storage = new Storage();

    public AddProductPage(Menu previousMenu, ClientSellerManager sellerManager) {
        super(previousMenu, "src/main/java/graphics/fxml/AddProductPage.fxml");
        this.sellerManager = sellerManager;
    }

    @FXML
    private TextField name;
    @FXML
    private TextField brand;
    @FXML
    private TextField price;
    @FXML
    private TextField supply;
    @FXML
    private TextField explanation;
    @FXML
    private ChoiceBox<String> categoryField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (!storage.getAllCategories().isEmpty()) {
            for (Category category : storage.getAllCategories()) {
                categoryField.getItems().add(category.getCategoryName());
            }
        } else {
            categoryField.getItems().add("No categories yet!");
        }
        categoryField.setValue("No categories yet!");
    }

    public void addProduct() {
        HashMap<String, String> productInformation = new HashMap<>();
        String productName = name.getText();
        String productBrand = brand.getText();
        String productPrice = price.getText();
        String category = categoryField.getValue();
        String productExplanation = explanation.getText();
        String productCategory;
        if (category.equals("") || category.equals("No categories yet!")) {
            productCategory = "";
        } else {
            productCategory = category;
        }
        if (checkPriceValidity(productPrice)) {
            productInformation.put("name", productName);
            productInformation.put("brand", productBrand);
            productInformation.put("price", productPrice);
            productInformation.put("supply",supply.getText());
            productInformation.put("explanation", productExplanation);
            productInformation.put("categoryName", productCategory);
            try {
                sellerManager.addProduct(productInformation,person.getUsername());
            } catch (Exception e) {
                showError(e.getMessage(),20);
            }
            showMessage();
            back();
        }

    }

    private boolean checkPriceValidity(String price) {
        if (price.equals("")) {
            showError("Please enter price!", 100);
            return false;
        } else if (!price.matches("\\d+\\.?\\d+")) {
            showError("PLease enter valid price!", 100);
            return false;
        } else
            return true;
    }

    public void showMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Request Message");
        alert.setContentText("Your Request has been Successfully sent to admin(s)!");
        alert.setHeaderText(null);
        alert.show();
    }

}
