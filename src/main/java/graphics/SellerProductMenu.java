package graphics;

import Client.ClientSellerManager;
import controller.SellerManager;
import controller.Storage;
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
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.Category;
import model.Product;
import model.Seller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SellerProductMenu extends Menu implements Initializable {
    private ClientSellerManager sellerManager;
    private Storage storage = new Storage();

    public SellerProductMenu(Menu previousMenu, ClientSellerManager sellerManager) {
        super(previousMenu, "src/main/java/graphics/fxml/SellerProductMenu.fxml");
        this.sellerManager = sellerManager;
    }

    @FXML
    TableView<Product> productTable = new TableView<>();
    @FXML
    TableColumn<Product, Integer> idColumn = new TableColumn<>();
    @FXML
    TableColumn<Product, Double> priceColumn = new TableColumn<>();
    @FXML
    TableColumn<Product, String> categoryColumn = new TableColumn<>();
    @FXML
    TableColumn<Product, String> nameColumn = new TableColumn<>();
    @FXML
    TableColumn<Product, Image> imageColumn = new TableColumn<>();
    @FXML
    TableColumn<Product, Double> averageRateColumn = new TableColumn<>();
    @FXML
    TableColumn<Product, String> brandColumn = new TableColumn<>();
    @FXML
    TableColumn<Product, Integer> supplyColumn = new TableColumn<>();
    @FXML
    TableColumn<Product, Void> buttonColumn = new TableColumn<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final ObservableList<Product> data = FXCollections.observableArrayList(((Seller) person).getProductsToSell());
        System.out.println(((Seller) person).getProductsToSell().toString());
        idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        supplyColumn.setCellValueFactory(new PropertyValueFactory<>("supply"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
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
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        averageRateColumn.setCellValueFactory(new PropertyValueFactory<>("averageRate"));
        addButtonToTable();
        productTable.setItems(data);
    }

    private void addButtonToTable() {
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory =
                new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
                    @Override
                    public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                        final TableCell<Product, Void> cell = new TableCell<Product, Void>() {

                            private final Button btn = new Button("Edit");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    int productId = getTableView().getItems().get(getIndex()).getProductId();
                                    editProduct(productId);
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

    public void addProduct() {
        AddProductPage addProductPage = new AddProductPage(this, this.sellerManager);
        addProductPage.run();
    }

    public void removeProduct() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Remove Product");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField productId = new TextField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter the id of the product you want to remove :"), productId);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        if (!productId.getText().matches("\\d+")) {
            showError("Product Id is an integer!", 100);
        } else {
            try {
                if (sellerManager.doesSellerHaveProduct(Integer.parseInt(productId.getText()),person.getUsername())) {
                    try {
                        sellerManager.removeProduct(Integer.parseInt(productId.getText()),person.getUsername());
                        showMessage();
                    } catch (Exception e) {
                        showError("Oops!Something went wrong!", 100);
                    }
                } else {
                    showError("Oops!You don't have product with this Id!", 100);
                }
            } catch (Exception e) {
                showError(e.getMessage(),20);
            }
        }
    }

    public void editProduct(int productId) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Product");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        VBox content = new VBox();
        content.setMinSize(500, 300);
        content.setAlignment(Pos.CENTER);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Choose the field you want to edit"));
        MenuItem name = new MenuItem("Name");
        MenuItem brand = new MenuItem("Brand");
        MenuItem price = new MenuItem("Price");
        MenuItem category = new MenuItem("Category");
        MenuItem explanation = new MenuItem("Explanation");
        MenuButton fields = new MenuButton("Fields", null, name, brand, price, category, explanation);
        content.getChildren().add(fields);
        dialog.getDialogPane().setContent(content);
        dialog.show();
        name.setOnAction(e -> editUsualFields(productId, "name"));

        brand.setOnAction(e -> editUsualFields(productId, "brand"));

        price.setOnAction(e -> editUsualFields(productId, "price"));

        category.setOnAction(e -> editCategoryOfProduct(productId));

        explanation.setOnAction(e -> editUsualFields(productId, "explanation"));

    }

    private void editPrice(int productId) {
        Dialog<String> productDialog = new Dialog<>();
        String updatedVersion;
        TextField textField = new TextField();
        productDialog.setTitle("Edit Price");
        productDialog.setHeaderText(null);
        productDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        HBox content = new HBox();
        content.getChildren().addAll(new Label("Enter your new price for product :")
                , textField);
        productDialog.getDialogPane().setContent(content);
        productDialog.showAndWait();
        updatedVersion = textField.getText();
        if (!updatedVersion.matches("\\d+\\.?\\d+")) {
            showError("Invalid amount!", 100);
        } else {
            try {
                sellerManager.editOff(productId, "price", updatedVersion,person.getUsername());
            } catch (Exception ex) {
                showError("Oops!Something went wrong!", 100);
            }
            showMessage();
        }
    }

    private void editCategoryOfProduct(int productId) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit category");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        ChoiceBox<String> content = new ChoiceBox<>();
        ArrayList<String> categoryNames = getCategoryNames();
        if (categoryNames.isEmpty()) {
            content.getItems().add("No Categories yet!");
        } else {
            content.getItems().addAll(categoryNames);
        }
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        String selectedCategory = content.getValue();
        if (!categoryNames.isEmpty()) {
            try {
                sellerManager.editProduct(productId, "category", selectedCategory,person.getUsername());
                showMessage();
            } catch (Exception e) {
                showError("Oops!Something went wrong!", 100);
            }
        } else {
            showError("There's no categories!", 100);
        }
    }

    public void editUsualFields(int productId, String field) {
        Dialog<String> productDialog = new Dialog<>();
        String updatedVersion;
        TextField textField = new TextField();
        productDialog.setTitle("Edit " + field);
        productDialog.setHeaderText(null);
        productDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        HBox content = new HBox();
        content.getChildren().addAll(new Label("Enter new " + field + " for product :"), textField);
        productDialog.getDialogPane().setContent(content);
        productDialog.showAndWait();
        updatedVersion = textField.getText();
        if (field.equals("price")) {
            if (!updatedVersion.matches("\\d+\\.?\\d+")) {
                showError("Invalid price!", 100);
            } else {
                try {
                    sellerManager.editProduct(productId, field, updatedVersion,person.getUsername());
                } catch (Exception ex) {
                    showError("Oops!Something went wrong!", 100);
                }
                showMessage();
            }
        } else {
            try {
                sellerManager.editProduct(productId, field, updatedVersion, person.getUsername());
            } catch (Exception ex) {
                showError("Oops!Something went wrong!", 100);
            }
            showMessage();
        }
    }

    public void showMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Request Message");
        alert.setContentText("Your Request has been Successfully sent to admin(s)!");
        alert.setHeaderText(null);
        alert.show();
    }

    private ArrayList<String> getCategoryNames() {
        ArrayList<String> categoryName = new ArrayList<>();
        if (!storage.getAllCategories().isEmpty()) {
            for (Category category : storage.getAllCategories()) {
                categoryName.add(category.getCategoryName());
            }
        }
        return categoryName;
    }
}
