package graphics;

import controller.CustomerManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import model.Customer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CartMenu extends Menu implements Initializable {
    private CustomerManager customerManager = new CustomerManager();
    @FXML
    TextField productTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void decreaseProduct(){
        String id = productTextField.getText();
        try {
            customerManager.decreaseProduct(id);
            updateTable();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void increaseProduct(){
        String id = productTextField.getText();
        try {
            customerManager.increaseProduct(id);
            updateTable();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void updateTable(){

    }

    @FXML
    private void purchase() throws IOException {
        if (person instanceof Customer){
            if (customerManager.getProductsInCart().isEmpty()){
                System.out.println("Your cart is empty. Nothing to purchase!");
                return;
            }
            System.out.println("Proceed to purchase ");
            FXMLLoader loader = new FXMLLoader(new File("src/main/java/graphics/fxml/PurchasingMenu.fxml").toURI().toURL());
            stage.setScene(new Scene(loader.load(), 600, 600));
        } else{
            System.out.println("First login as customer then purchase.");
            FXMLLoader loader = new FXMLLoader(new File("src/main/java/graphics/fxml/MainMenu.fxml").toURI().toURL());
            stage.setScene(new Scene(loader.load(), 600, 600));
        }
    }

    @FXML
    private void back(){

    }
}