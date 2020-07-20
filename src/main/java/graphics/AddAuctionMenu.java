package graphics;

import Client.ClientSellerManager;
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

public class AddAuctionMenu extends Menu {

    private ClientSellerManager sellerManager;


    public AddAuctionMenu(Menu previousMenu, ClientSellerManager sellerManager) {
        super(previousMenu, "src/main/java/graphics/fxml/AddAuctionMenu.fxml");
        this.sellerManager = sellerManager;
    }


    @FXML
    private TextField beginDateField;
    @FXML
    private TextField endDateField;
    @FXML
    private TextField initialPrice;
    @FXML
    private TextField productToBeAuctioned;

    public void addAuction() {
        HashMap<String, String> productInformation = new HashMap<>();
        String beginDate = beginDateField.getText();
        String endDate = endDateField.getText();
        String initialPriceText = initialPrice.getText();
        String productIdToBeAuctioned = productToBeAuctioned.getText();
        if (checkPriceValidity(initialPriceText) && checkDateValidity(beginDate, "begin") && checkDateValidity(endDate, "end") && checkProductIdValidity(productIdToBeAuctioned))
        {
            productInformation.put("beginDate", beginDate);
            productInformation.put("endDate", endDate);
            productInformation.put("price", initialPriceText);
            productInformation.put("productId", productIdToBeAuctioned);
            productInformation.put("seller", person.getUsername());
            try {
                sellerManager.addAuction(productInformation);
            } catch (Exception e) {
                showError(e.getMessage(), 20);
            }
            showMessage();
            back();
        }
    }

    private boolean checkPriceValidity(String amount) {
        if (amount.equals("")) {
            showError("Please enter initial price!", 100);
            return false;
        } else if (!amount.matches("\\d+\\.?\\d+")) {
            showError("PLease enter valid initial price!", 100);
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

    private boolean checkProductIdValidity(String productId) {
        if (!productId.matches("\\d+")) {
            showError("Product Id is an integer!", 100);
            return false;
        } else {
            try {
                if (sellerManager.getProductById(Integer.parseInt(productId)) == null) {
                    showError("There's not such product!", 100);
                    return false;
                } else {
                    try {
                        if (!sellerManager.doesSellerHaveProduct(Integer.parseInt(productId), person.getUsername())) {
                            showError("Oops!You don't have off with this Id!", 100);
                            return false;
                        }
                    } catch (Exception e) {
                        showError(e.getMessage(), 20);
                        return false;
                    }
                }
            } catch (Exception e) {
                showError(e.getMessage(), 20);
                return false;
            }
            return true;
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
