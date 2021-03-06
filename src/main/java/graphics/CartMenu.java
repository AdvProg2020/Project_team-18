package graphics;

import Client.ClientCustomerManager;
import controller.CustomerManager;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CartMenu extends Menu implements Initializable {
    private ClientCustomerManager customerManager = new ClientCustomerManager();
    @FXML
    TextField productTextField;
    @FXML TableView<Product> tableView = new TableView<>(FXCollections.observableList(FXCollections.observableArrayList(
            customerManager.getProductsInCart(currentCart).keySet()
    )));
    @FXML TableColumn<Product, Double> priceColumn = new TableColumn<>();
    @FXML TableColumn<Product, Number> totalPriceColumn = new TableColumn<>();
    @FXML TableColumn<Product, Integer> idColumn = new TableColumn<>();
    @FXML TableColumn<Product, Image> imageColumn = new TableColumn<>();
    @FXML TableColumn<Product, Integer> numberColumn = new TableColumn<>();
    @FXML Label totalPriceLabel = new Label();

    public CartMenu(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/CartMenu.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTable();
    }

    public void decreaseProduct(){
        String id = productTextField.getText();
        try {
            currentCart = customerManager.decreaseProduct(currentCart , id);
            updateTable();
        }catch (Exception e){
            showError(e.getMessage(), 200);
        }
    }

    public void increaseProduct(){
        String id = productTextField.getText();
        try {
            currentCart = customerManager.increaseProduct(currentCart, id);
           //System.out.println(currentCart.getProductsInCart().keySet().toString());
            updateTable();
        }catch (Exception e){
            showError(e.getMessage(), 200);
        }
    }

    private void updateTable(){

        final ObservableList<Product> data = FXCollections.observableArrayList(
                customerManager.getProductsInCart(currentCart).keySet()
        );
        idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("numberInCart"));
        imageColumn.setCellFactory(param -> {
            //Set up the ImageView
            final ImageView imageview = new ImageView();
            imageview.setFitHeight(50);
            imageview.setFitWidth(50);

            //Set up the Table
            TableCell<Product, Image> cell = new TableCell<Product, Image>() {
                public void updateItem(Image item, boolean empty) {
                    if (item != null) {
                        imageview.setImage(item);
                    }
                }
            };
            // Attach the imageView to the cell
            cell.setGraphic(imageview);
            return cell;
        });
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("Image"));
        totalPriceColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            return Bindings.createDoubleBinding(
                    () -> {
                        try {
                            double price = product.getPrice();
                            int quantity = interpretHashMap(customerManager.getProductsInCart(currentCart),product);
                            return price * quantity ;
                        } catch (NumberFormatException nfe) {
                            return Double.valueOf(0);
                        }
                    },
                    product.priceProperty(),
                    product.numberInCartProperty()
            );
        });
        if (currentCart.getProductsInCart().isEmpty())
            data.clear();
        tableView.setItems(data);
        totalPriceLabel.setText("Total Price OF Cart : " + customerManager.getCartTotalPrice(currentCart));
    }

    private int interpretHashMap (HashMap<Product,Integer> products , Product desiredProduct){
        for (Product product : products.keySet()) {
            if(product.getId() == desiredProduct.getId())
                return products.get(product);
        }
        return 0;
    }

    @FXML
    private void purchase() throws IOException {
        if (person instanceof Customer){
            if (customerManager.getProductsInCart(currentCart).isEmpty()){
                showError("Your cart is empty. Nothing to purchase!", 100);
                return;
            }
            PurchasingMenu purchasingMenu = new PurchasingMenu(this);
            purchasingMenu.run();
        } else{
            showError("First login as customer then purchase.", 100);
            LoginMenu loginMenu = new LoginMenu(this);
            loginMenu.run();
        }
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
