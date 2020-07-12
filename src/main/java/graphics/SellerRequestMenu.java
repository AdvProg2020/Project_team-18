package graphics;

import Client.ClientSellerManager;
import Server.ClientMessage;
import controller.AdminManager;
import controller.SellerManager;
import controller.Storage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Category;
import model.Request;
import model.RequestType;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class SellerRequestMenu extends Menu implements Initializable {

    AdminManager adminManager = new AdminManager();
    ClientSellerManager sellerManager = new ClientSellerManager();
    private Storage storage = new Storage();

    @FXML
    TableView<Request> requestsTable = new TableView<>();
    @FXML
    TableColumn<Request, Integer> idColumn = new TableColumn<>();
    @FXML
    TableColumn<Request, String> statusColumn = new TableColumn<>();
    @FXML
    TableColumn<Request, String> typeColumn = new TableColumn<>();

    private void updateShownRequests(ArrayList<Request> shownRequests) {
        final ObservableList<Request> data = FXCollections.observableArrayList(
                shownRequests
        );
        idColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("stateOfRequest"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeOfRequest"));
        requestsTable.setItems(data);
    }

    public SellerRequestMenu(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/SellerRequestMenu.fxml");
    }

    @FXML
    private void addProduct() {
        AddProductPage addProductPage = new AddProductPage(this, this.sellerManager);
        addProductPage.run();
    }

    @FXML
    private void editProduct() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Product");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField productId = new TextField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter the id of the product you want to edit :"), productId);
        dialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (!productId.getText().matches("\\d+")) {
                showError("Off Id is an integer!", 100);
            } else {
                if (storage.getProductById(Integer.parseInt(productId.getText())) == null) {
                    showError("There's not such product!", 100);
                } else {
                    try {
                        if (!sellerManager.doesSellerHaveProduct(Integer.parseInt(productId.getText()))) {
                            showError("Oops!You don't have off with this Id!", 100);
                        } else {
                            editProductProcess(Integer.parseInt(productId.getText()));
                        }
                    } catch (Exception e) {
                        showError(e.getMessage(),20);
                    }
                }
            }
        }
    }

    private void editProductProcess(int productId) {
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

    private void editCategoryOfProduct(int productId) {
        Dialog<ButtonType> dialog = new Dialog<>();
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
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String selectedCategory = content.getValue();
            if (!categoryNames.isEmpty()) {
                try {
                    sellerManager.editProduct(productId, "category", selectedCategory);
                    ArrayList<Request> sellerReq = new ArrayList<>();
                    for (Request request : adminManager.viewAllRequests()) {
                        if (!(request.getTypeOfRequest() == RequestType.ADD_COMMENT)) {
                            if (request.getInformation().get("seller") != null && request.getInformation().get("seller").equals(person.getUsername()))
                                sellerReq.add(request);
                            else if (request.getInformation().get("username") != null && request.getInformation().get("username").equals(person.getUsername()))
                                sellerReq.add(request);
                        }
                    }
                    updateShownRequests(sellerReq);
                    showMessage();
                } catch (Exception e) {
                    showError("Oops!Something went wrong!", 100);
                }
            } else {
                showError("There's no categories!", 100);
            }
        }
    }

    public void editUsualFields(int productId, String field) {
        Dialog<ButtonType> productDialog = new Dialog<>();
        String updatedVersion;
        TextField textField = new TextField();
        productDialog.setTitle("Edit " + field);
        productDialog.setHeaderText(null);
        productDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        HBox content = new HBox();
        content.getChildren().addAll(new Label("Enter new " + field + " for product :"), textField);
        productDialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = productDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updatedVersion = textField.getText();
            if (field.equals("price")) {
                if (!updatedVersion.matches("\\d+\\.?\\d+")) {
                    showError("Invalid price!", 100);
                } else {
                    try {
                        sellerManager.editProduct(productId, field, updatedVersion);
                        ArrayList<Request> sellerReq = new ArrayList<>();
                        for (Request request : adminManager.viewAllRequests()) {
                            if (!(request.getTypeOfRequest() == RequestType.ADD_COMMENT)) {
                                if (request.getInformation().get("seller") != null && request.getInformation().get("seller").equals(person.getUsername()))
                                    sellerReq.add(request);
                                else if (request.getInformation().get("username") != null && request.getInformation().get("username").equals(person.getUsername()))
                                    sellerReq.add(request);
                            }
                        }
                        updateShownRequests(sellerReq);
                    } catch (Exception ex) {
                        showError("Oops!Something went wrong!", 100);
                    }
                    showMessage();
                }
            } else {
                try {
                    sellerManager.editProduct(productId, field, updatedVersion);
                    ArrayList<Request> sellerReq = new ArrayList<>();
                    for (Request request : adminManager.viewAllRequests()) {
                        if (!(request.getTypeOfRequest() == RequestType.ADD_COMMENT)) {
                            if (request.getInformation().get("seller") != null && request.getInformation().get("seller").equals(person.getUsername()))
                                sellerReq.add(request);
                            else if (request.getInformation().get("username") != null && request.getInformation().get("username").equals(person.getUsername()))
                                sellerReq.add(request);
                        }
                    }
                    updateShownRequests(sellerReq);
                } catch (Exception ex) {
                    showError("Oops!Something went wrong!", 100);
                }
                showMessage();
            }
        }
    }

    @FXML
    private void removeProduct() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Remove Product");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField productId = new TextField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter the id of the product you want to remove :"), productId);
        dialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (!productId.getText().matches("\\d+")) {
                showError("Product Id is an integer!", 100);
            } else {
                if (storage.getProductById(Integer.parseInt(productId.getText())) == null) {
                    showError("There's not such product!", 100);
                } else {
                    try {
                        if (!sellerManager.doesSellerHaveProduct(Integer.parseInt(productId.getText()))) {
                            showError("Oops!You don't have product with this Id!", 100);
                        } else {
                            try {
                                sellerManager.removeProduct(Integer.parseInt(productId.getText()));
                                showMessage();
                            } catch (Exception e) {
                                showError("Oops!Something went wrong!", 100);
                            }
                        }
                    } catch (Exception e) {
                        showError(e.getMessage(),20);
                    }
                }
            }
        }
    }

    @FXML
    private void addOff() {
        AddOffPage addOffPage = new AddOffPage(this, this.sellerManager);
        addOffPage.run();
    }

    @FXML
    private void editOff() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Off");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField offId = new TextField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter the id of the off you want to edit :"), offId);
        dialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (!offId.getText().matches("\\d+")) {
                showError("Off Id is an integer!", 100);
            } else {
                try {
                    if (sellerManager.doesSellerHaveThisOff(Integer.parseInt(offId.getText()))) {
                        editOffProcess(Integer.parseInt(offId.getText()));
                    } else {
                        showError("Oops!You don't have off with this Id!", 100);
                    }
                } catch (Exception e) {
                    showError(e.getMessage(),20);
                }
            }
        }
    }

    private void editOffProcess(int offId) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Off");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        VBox content = new VBox();
        content.setMinSize(500, 300);
        content.setAlignment(Pos.CENTER);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Choose the field you want to edit"));
        MenuItem beginDate = new MenuItem("Begin Date");
        MenuItem endDate = new MenuItem("End Date");
        MenuItem amountOfOff = new MenuItem("Amount Of Off");
        MenuItem addProductToOff = new MenuItem("Add Product To Off");
        MenuItem removeProductFromOff = new MenuItem("Remove Product From Off");
        MenuButton fields = new MenuButton("Fields", null, beginDate, endDate, amountOfOff, addProductToOff,
                removeProductFromOff);
        content.getChildren().add(fields);
        dialog.getDialogPane().setContent(content);
        dialog.show();
        beginDate.setOnAction(e -> editBeginDate(offId));

        endDate.setOnAction(e -> editEndDate(offId));

        amountOfOff.setOnAction(e -> editAmountOfOff(offId));

        addProductToOff.setOnAction(e -> addProductToOff(offId));

        removeProductFromOff.setOnAction(e -> removeProductFromOff(offId));
    }

    private void editBeginDate(int offId) {
        Dialog<ButtonType> productDialog = new Dialog<>();
        String updatedVersion;
        TextField textField = new TextField();
        productDialog.setTitle("Edit Begin Date");
        productDialog.setHeaderText(null);
        productDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        HBox content = new HBox();
        content.getChildren().addAll(new Label("Enter your new Begin Date :(in format yyyy,MM,dd,HH,mm)")
                , textField);
        productDialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = productDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updatedVersion = textField.getText();
            if (!updatedVersion.matches("\\d\\d\\d\\d,\\d\\d,\\d\\d,\\d\\d,\\d\\d")) {
                showError("Invalid Format of date!", 100);
            } else {
                try {
                    sellerManager.editOff(offId, "beginDate", updatedVersion);
                    ArrayList<Request> sellerReq = new ArrayList<>();
                    for (Request request : adminManager.viewAllRequests()) {
                        if (!(request.getTypeOfRequest() == RequestType.ADD_COMMENT)) {
                            if (request.getInformation().get("seller") != null && request.getInformation().get("seller").equals(person.getUsername()))
                                sellerReq.add(request);
                            else if (request.getInformation().get("username") != null && request.getInformation().get("username").equals(person.getUsername()))
                                sellerReq.add(request);
                        }
                    }
                    updateShownRequests(sellerReq);
                    showMessage();
                } catch (Exception ex) {
                    showError("Oops!Something went wrong!", 100);
                }
            }
        }
    }

    private void editEndDate(int offId) {
        Dialog<ButtonType> productDialog = new Dialog<>();
        String updatedVersion;
        TextField textField = new TextField();
        productDialog.setTitle("Edit End Date");
        productDialog.setHeaderText(null);
        productDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        HBox content = new HBox();
        content.getChildren().addAll(new Label("Enter your new End Date :(in format yyyy,MM,dd,HH,mm)")
                , textField);
        productDialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = productDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updatedVersion = textField.getText();
            if (!updatedVersion.matches("\\d\\d\\d\\d,\\d\\d,\\d\\d,\\d\\d,\\d\\d")) {
                showError("Invalid Format of date!", 100);
            } else {
                try {
                    sellerManager.editOff(offId, "endDate", updatedVersion);
                    ArrayList<Request> sellerReq = new ArrayList<>();
                    for (Request request : adminManager.viewAllRequests()) {
                        if (!(request.getTypeOfRequest() == RequestType.ADD_COMMENT)) {
                            if (request.getInformation().get("seller") != null && request.getInformation().get("seller").equals(person.getUsername()))
                                sellerReq.add(request);
                            else if (request.getInformation().get("username") != null && request.getInformation().get("username").equals(person.getUsername()))
                                sellerReq.add(request);
                        }
                    }
                    updateShownRequests(sellerReq);
                    showMessage();
                } catch (Exception ex) {
                    showError("Oops!Something went wrong!", 100);
                }
            }
        }
    }

    private void editAmountOfOff(int offId) {
        Dialog<ButtonType> productDialog = new Dialog<>();
        String updatedVersion;
        TextField textField = new TextField();
        productDialog.setTitle("Edit Amount Of Off");
        productDialog.setHeaderText(null);
        productDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        HBox content = new HBox();
        content.getChildren().addAll(new Label("Enter your new amount of off :")
                , textField);
        productDialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = productDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updatedVersion = textField.getText();
            if (!updatedVersion.matches("\\d+\\.?\\d+")) {
                showError("Invalid amount!", 100);
            } else {
                try {
                    sellerManager.editOff(offId, "amountOfSale", updatedVersion);
                    ArrayList<Request> sellerReq = new ArrayList<>();
                    for (Request request : adminManager.viewAllRequests()) {
                        if (!(request.getTypeOfRequest() == RequestType.ADD_COMMENT)) {
                            if (request.getInformation().get("seller") != null && request.getInformation().get("seller").equals(person.getUsername()))
                                sellerReq.add(request);
                            else if (request.getInformation().get("username") != null && request.getInformation().get("username").equals(person.getUsername()))
                                sellerReq.add(request);
                        }
                    }
                    updateShownRequests(sellerReq);
                    showMessage();
                } catch (Exception ex) {
                    showError("Oops!Something went wrong!", 100);
                }
            }
        }
    }

    private void addProductToOff(int offId) {
        Dialog<ButtonType> productDialog = new Dialog<>();
        String updatedVersion;
        TextField textField = new TextField();
        productDialog.setTitle("Add product to off");
        productDialog.setHeaderText(null);
        productDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        HBox content = new HBox();
        content.getChildren().addAll(new Label("Enter productId you want to add to this off :")
                , textField);
        productDialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = productDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updatedVersion = textField.getText();
            try {
                sellerManager.addProductToOff(offId, Integer.parseInt(updatedVersion));
                ArrayList<Request> sellerReq = new ArrayList<>();
                for (Request request : adminManager.viewAllRequests()) {
                    if (!(request.getTypeOfRequest() == RequestType.ADD_COMMENT)) {
                        if (request.getInformation().get("seller") != null && request.getInformation().get("seller").equals(person.getUsername()))
                            sellerReq.add(request);
                        else if (request.getInformation().get("username") != null && request.getInformation().get("username").equals(person.getUsername()))
                            sellerReq.add(request);
                    }
                }
                updateShownRequests(sellerReq);
                showMessage();
            } catch (Exception ex) {
                showError("Oops!Something went wrong!One of the following errors has happened :\n -There is no product with this Id!\n" +
                        " -This product is not belonged to you!\n -This product is already added in this sale!", 300);
            }
        }
    }

    private void removeProductFromOff(int offId) {
        Dialog<ButtonType> productDialog = new Dialog<>();
        String updatedVersion;
        TextField textField = new TextField();
        productDialog.setTitle("Remove product from off");
        productDialog.setHeaderText(null);
        productDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        HBox content = new HBox();
        content.getChildren().addAll(new Label("Enter productId you want to remove from this off :")
                , textField);
        productDialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = productDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updatedVersion = textField.getText();
            try {
                sellerManager.removeProductFromOff(offId, Integer.parseInt(updatedVersion));
                ArrayList<Request> sellerReq = new ArrayList<>();
                for (Request request : adminManager.viewAllRequests()) {
                    if (!(request.getTypeOfRequest() == RequestType.ADD_COMMENT)) {
                        if (request.getInformation().get("seller") != null && request.getInformation().get("seller").equals(person.getUsername()))
                            sellerReq.add(request);
                        else if (request.getInformation().get("username") != null && request.getInformation().get("username").equals(person.getUsername()))
                            sellerReq.add(request);
                    }
                }
                updateShownRequests(sellerReq);
                showMessage();
            } catch (Exception ex) {
                showError("Oops!Something went wrong!One of the following errors has happened :\n -There is no product with this Id!\n" +
                        " -This product is not belonged to you!\n -This product is not assigned to this sale!", 300);
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

    private ArrayList<String> getCategoryNames() {
        ArrayList<String> categoryName = new ArrayList<>();
        if (!storage.getAllCategories().isEmpty()) {
            for (Category category : storage.getAllCategories()) {
                categoryName.add(category.getCategoryName());
            }
        }
        return categoryName;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Request> sellerReq = new ArrayList<>();
        for (Request request : adminManager.viewAllRequests()) {
            if (!(request.getTypeOfRequest() == RequestType.ADD_COMMENT)) {
                if (request.getInformation().get("seller") != null && request.getInformation().get("seller").equals(person.getUsername()))
                    sellerReq.add(request);
                else if (request.getInformation().get("username") != null && request.getInformation().get("username").equals(person.getUsername()))
                    sellerReq.add(request);
            }
        }
        updateShownRequests(sellerReq);
    }
}
