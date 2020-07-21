package graphics;


import Client.ClientPurchasingManager;
import controller.PurchasingManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.Admin;
import model.Cart;
import model.Customer;
import model.Seller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PurchasingMenu extends Menu {

    private ClientPurchasingManager purchasingManager = new ClientPurchasingManager();
    private HashMap<String, String> receivedInfo = new HashMap<>();
    private double finalPrice;
    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private TextField number;
    @FXML
    private TextField discountCodeField;

    public PurchasingMenu(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/PurchasingMenu.fxml");
    }

    private boolean checkName() {
        if (name.getText().matches("")) {
            showError("All fields are essential except Discount Code!", 100);
            return false;
        }
        return true;
    }

    private boolean checkAddress() {
        if (address.getText().matches("")) {
            showError("All fields are essential except Discount Code!", 100);
            return false;
        }
        return true;
    }

    private boolean checkNumber() {
        if (number.getText().matches("")) {
            showError("All fields are essential except Discount Code!", 100);
            return false;
        } else if (!number.getText().matches("\\d+")) {
            showError("Invalid phone number!", 100);
            return false;
        }
        return true;
    }

    private boolean checkValidityOfDiscountCode() {
        String discountCode = discountCodeField.getText();
        if (!discountCode.equals("")) {
            try {
                if (!purchasingManager.doesCustomerHaveDiscountCode(discountCode,person.getUsername())) {
                    System.out.println(discountCode);
                    showError("Sorry!You don't have this discount!", 100);
                    return false;
                }
            } catch (Exception e) {
                showError(e.getMessage() , 20);
            }
            try {
                purchasingManager.checkDiscountValidity(discountCode);
                finalPrice = purchasingManager.calculateTotalPriceWithDiscount(discountCode,currentCart);
                return true;
            } catch (Exception e) {
                showError("You can't use this discount for one of following reasons:\n -This discount is expired!\n" +
                        " -This discount is not available yet!\n -You used this discount before and it's not available anymore!", 200);
                return false;
            }
        } else {
            try {
                finalPrice = purchasingManager.getTotalPriceWithoutDiscount(currentCart);
            } catch (Exception e) {
                showError(e.getMessage() , 20);
            }
            return true;
        }
    }

    public void paymentWithWallet() throws Exception {
        if (!checkName()) {
            return;
        } else if (!checkAddress()) {
            return;
        } else if (!checkNumber()) {
            return;
        } else if (!checkValidityOfDiscountCode()) {
            return;
        }
        boolean nameValidity = checkName();
        boolean addressValidity = checkAddress();
        boolean numberValidity = checkNumber();
        boolean discountCodeValidity = checkValidityOfDiscountCode();
        if (nameValidity && addressValidity && numberValidity && discountCodeValidity) {
            if (!purchasingManager.doesCustomerHaveEnoughMoney(finalPrice,person.getUsername())) {
                showError("Oops!You don't have enough money in your wallet!", 100);
            } else {
                receivedInfo.put("receiverName", name.getText());
                receivedInfo.put("address", address.getText());
                receivedInfo.put("phoneNumber", name.getText());
                Cart result = purchasingManager.performPayment(receivedInfo, finalPrice,
                        purchasingManager.getDiscountPercentage(discountCodeField.getText()), discountCodeField.getText(),
                        person.getUsername() ,currentCart);
                if (!discountCodeField.getText().equals("")) {
                    purchasingManager.updateDiscountUsagePerPerson(discountCodeField.getText() , person.getUsername());
                }
                currentCart = result;
                showMessage();
                back();
            }
        }
    }

    public void paymentWithAccount() throws Exception{
        if (!checkName()) {
            return;
        } else if (!checkAddress()) {
            return;
        } else if (!checkNumber()) {
            return;
        } else if (!checkValidityOfDiscountCode()) {
            return;
        }
        boolean nameValidity = checkName();
        boolean addressValidity = checkAddress();
        boolean numberValidity = checkNumber();
        boolean discountCodeValidity = checkValidityOfDiscountCode();
        if (nameValidity && addressValidity && numberValidity && discountCodeValidity) {
                receivedInfo.put("receiverName", name.getText());
                receivedInfo.put("address", address.getText());
                receivedInfo.put("phoneNumber", name.getText());
                purchasingManager.performPaymentWithBankAccount(receivedInfo, finalPrice,
                        purchasingManager.getDiscountPercentage(discountCodeField.getText()), discountCodeField.getText(),
                        person.getUsername() ,currentCart);
                if (!discountCodeField.getText().equals("")) {
                    purchasingManager.updateDiscountUsagePerPerson(discountCodeField.getText() , person.getUsername());
                }
                showMessage();
                back();
        }
    }

    public void goToPreviousPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(new File("src/main/java/graphics/fxml/CartMenu.fxml").toURI().toURL());
        stage.setScene(new Scene(loader.load(), 600, 600));
    }

    public void showMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Buying Message");
        alert.setContentText("Thanks for buying from us:)");
        alert.setHeaderText(null);
        alert.show();
    }

    public void goToMyAccount(){
        if (person == null){
            LoginMenu loginMenu = new LoginMenu(this);
            loginMenu.run();
        } else if (person instanceof Admin){
            AdminMenu adminMenu = new AdminMenu(this);
            adminMenu.run();
        } else if (person instanceof Seller){
            SellerMenu sellerMenu = new SellerMenu(this);
            sellerMenu.run();
        } else if (person instanceof Customer){
            CustomerMenu customerMenu = new CustomerMenu(this);
            customerMenu.run();
        }
    }
}
