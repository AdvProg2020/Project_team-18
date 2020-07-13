package graphics;

import Client.ClientAdminManager;
import controller.AdminManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.Category;
import model.Product;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AdminManageCategoriesMenu extends Menu implements Initializable {

    private ClientAdminManager adminManager = new ClientAdminManager();

    @FXML
    TableView categoriesTable = new TableView();
    @FXML TableColumn<Category, Integer> idColumn = new TableColumn<>();
    @FXML TableColumn<Category, String> nameColumn = new TableColumn<>();
    @FXML TableColumn<Category, Void> propertiesColumn = new TableColumn<>();
    @FXML TableColumn<Category, Void> productsColumn = new TableColumn<>();

    public AdminManageCategoriesMenu(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/AdminManageCategoriesMenu.fxml");
    }

    private void updateShownCategories(ArrayList<Category> shownCategories){
        final ObservableList<Category> data = FXCollections.observableArrayList(
                shownCategories
        );
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        addPropertiesButtonToTable(this);
        addProductsButtonToTable(this);
        categoriesTable.setItems(data);
    }

    private void addProductsButtonToTable(AdminManageCategoriesMenu menu) {
        Callback<TableColumn<Category, Void>, TableCell<Category, Void>> cellFactory =
                new Callback<TableColumn<Category, Void>, TableCell<Category, Void>>() {
                    @Override
                    public TableCell<Category, Void> call(final TableColumn<Category, Void> param) {
                        final TableCell<Category, Void> cell = new TableCell<Category, Void>() {

                            private final Button btn = new Button("Click");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Category category = getTableView().getItems().get(getIndex());
                                    showProducts(category);
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

        productsColumn.setCellFactory(cellFactory);

    }

    private void showProducts(Category category){
        ArrayList<Product> products = category.getThisCategoryProducts();
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Category's Products");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TableView tableView = new TableView();
        TableColumn<String, String> productColumn = new TableColumn<>("Product");
        tableView.getColumns().addAll(productColumn);
        Collection<String> list = new ArrayList<>();
        for (Product product : products) {
            list.add("Product Id : " + product.getProductId() + " Product Name : " + product.getName());
        }

        ObservableList<String> details = FXCollections.observableArrayList(list);
        productColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        tableView.setItems(details);
        VBox content = new VBox(tableView);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    private void addPropertiesButtonToTable(AdminManageCategoriesMenu menu) {
        Callback<TableColumn<Category, Void>, TableCell<Category, Void>> cellFactory =
                new Callback<TableColumn<Category, Void>, TableCell<Category, Void>>() {
                    @Override
                    public TableCell<Category, Void> call(final TableColumn<Category, Void> param) {
                        final TableCell<Category, Void> cell = new TableCell<Category, Void>() {

                            private final Button btn = new Button("Click");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Category category = getTableView().getItems().get(getIndex());
                                    showProperties(category);
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

        propertiesColumn.setCellFactory(cellFactory);

    }

    private void showProperties(Category category){
        HashMap<String,String> properties = category.getProperties();
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Category's Properties");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TableView tableView = new TableView();
        TableColumn<String, String> propertyColumn = new TableColumn<>("Property");
        tableView.getColumns().addAll(propertyColumn);
        Collection<String> list = new ArrayList<>();
        for (String str : properties.keySet()) {
            list.add(str + " : " + properties.get(str));
        }

        ObservableList<String> details = FXCollections.observableArrayList(list);
        propertyColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        tableView.setItems(details);
        VBox content = new VBox(tableView);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    @FXML
    private void removeCategory(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Remove Category");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField categoryNameField = new TextField();
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter category's name :"), categoryNameField);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        try {
            adminManager.removeCategory(categoryNameField.getText());
            updateShownCategories(adminManager.viewAllCategories());
        } catch (Exception e) {
            showError(e.getMessage(),20);
        }
    }

    @FXML
    private void addCategory(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add Category");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField categoryNameField = new TextField();
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        Button button = new Button("Category's Image");
        button.setOnAction(e -> showCategoryImages());
        content.getChildren().addAll(new Label("Enter new category's name :"), categoryNameField);
        TextField imageField = new TextField();
        content.getChildren().addAll(new Label("Enter category's image code : ") , imageField,button);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        try {
            adminManager.addCategory(categoryNameField.getText(),imageField.getText());
            updateShownCategories(adminManager.viewAllCategories());
        } catch (Exception e) {
            showError(e.getMessage(),20);
        }
    }

    private void showCategoryImages(){
        ShowImages showImages = new ShowImages(this);
        showImages.run();
    }

    @FXML
    private void editCategory(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Category");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane content = new GridPane();
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Edit Name" , "Edit Properties");
        content.add(choiceBox,0,0);
        Label label1 = new Label("Enter Category's name");
        content.add(label1,0,1);
        TextField categoryNameField = new TextField();
        content.add(categoryNameField,1,1);
        Label label2 = new Label("If you are not editing category's name, enter the property that you want to add or edit");
        content.add(label2,0,2);
        TextField categoryPropertyField = new TextField();
        content.add(categoryPropertyField,1,2);
        Label label3 = new Label("Now enter the updated value of the edited field");
        content.add(label3,0,3);
        TextField categoryNewField = new TextField();
        content.add(categoryNewField,1,3);
        content.setAlignment(Pos.CENTER_LEFT);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
            if(choiceBox.getValue().equals("Edit Name")){
                adminManager.editCategoryByName(categoryNameField.getText() , categoryNewField.getText());
                updateShownCategories(adminManager.viewAllCategories());
            } else if(choiceBox.getValue().equals("Edit Properties")){
                try {
                    adminManager.editCategoryByProperties(adminManager.viewCategory(categoryNameField.getText()),categoryPropertyField.getText(),categoryNewField.getText());
                    updateShownCategories(adminManager.viewAllCategories());
                } catch (Exception ex) {
                    showError(ex.getMessage(),20);
                }
            }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateShownCategories(adminManager.viewAllCategories());
    }
}
