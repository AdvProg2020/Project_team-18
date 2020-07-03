package graphics;

import controller.AdminManager;
import controller.ProductManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import model.Product;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminMangeProducts extends Menu implements Initializable {

    ProductManager productManager = new ProductManager();
    AdminManager adminManager = new AdminManager();

    @FXML
    TableView productTable = new TableView();
    @FXML
    TableColumn<Product, Double> priceColumn = new TableColumn<>();
    @FXML TableColumn<Product, String> categoryColumn = new TableColumn<>();
    @FXML TableColumn<Product, String> nameColumn = new TableColumn<>();
    @FXML TableColumn<Product, Image> imageColumn = new TableColumn<>();
    @FXML TableColumn<Product, Double> averageRateColumn = new TableColumn<>();
    @FXML TableColumn<Product, String> sellerColumn = new TableColumn<>();
    @FXML TableColumn<Product, Void> buttonColumn = new TableColumn<>();

    public AdminMangeProducts(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/AdminMangeProducts.fxml");
    }

    private void updateShownProducts(ArrayList<Product> shownProducts){
        final ObservableList<Product> data = FXCollections.observableArrayList(
                shownProducts
        );
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        categoryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategory().getCategoryName()));
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
        averageRateColumn.setCellValueFactory(new PropertyValueFactory<>("averageRate"));
        sellerColumn.setCellValueFactory(new PropertyValueFactory<>("SellerName"));
        addButtonToTable(this);
        productTable.setItems(data);
    }

    private void addButtonToTable(AdminMangeProducts menu) {
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory =
                new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
                    @Override
                    public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                        final TableCell<Product, Void> cell = new TableCell<Product, Void>() {

                            private final Button btn = new Button("Click");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Product product = getTableView().getItems().get(getIndex());
                                    ProductMenu productMenu = new ProductMenu(menu,product);
                                    productMenu.run();
                                });
                            }

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(btn);
                                }
                            }
                        };
                        return cell;
                    }
                };

        buttonColumn.setCellFactory(cellFactory);

    }

    @FXML
    private void removeProduct(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Remove Product");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField removedField = new TextField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter the product's id :"), removedField);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        try {
            adminManager.removeProduct(removedField.getText());
            updateShownProducts(productManager.viewAllProducts());
        } catch (Exception e) {
            showError(e.getMessage(),20);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateShownProducts(productManager.viewAllProducts());
    }
}
