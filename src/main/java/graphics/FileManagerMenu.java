package graphics;

import Client.ClientCustomerManager;
import Client.ClientSellerManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.FileProduct;
import model.Person;
import model.Seller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FileManagerMenu extends Menu implements Initializable {
    private ClientSellerManager clientSellerManager = new ClientSellerManager();
    private ClientCustomerManager clientCustomerManager = new ClientCustomerManager();
    public FileManagerMenu(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/FileManagerMenu.fxml");
    }
    @FXML
    TableView userTable = new TableView();
    @FXML
    TableColumn<FileProduct, String> fileNameColumn = new TableColumn<>();
    @FXML TableColumn<FileProduct, String> sellerNameColumn = new TableColumn<>();
    @FXML TableColumn<FileProduct, String> stateColumn = new TableColumn<>();
    @FXML TableColumn<FileProduct, Void> buttonColumn = new TableColumn<>();
    private void updateShownUsers(ArrayList<FileProduct> shownProducts){
        final ObservableList<FileProduct> data = FXCollections.observableArrayList(
                shownProducts
        );
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        sellerNameColumn.setCellValueFactory(new PropertyValueFactory<>("sellerName"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("fileState"));
        addButtonToTable();
        userTable.setItems(data);
    }

    private void addButtonToTable() {
        Callback<TableColumn<FileProduct, Void>, TableCell<FileProduct, Void>> cellFactory =
                new Callback<TableColumn<FileProduct, Void>, TableCell<FileProduct, Void>>() {
                    @Override
                    public TableCell<FileProduct, Void> call(final TableColumn<FileProduct, Void> param) {
                        final TableCell<FileProduct, Void> cell = new TableCell<FileProduct, Void>() {

                            private final Button btn = new Button("Download");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    FileProduct fileProduct = getTableView().getItems().get(getIndex());
                                    download(fileProduct);
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

    private void download(FileProduct fileProduct) {
        if (person instanceof Seller){
            sellerDownload();
        }else {
            customerDownload();
        }

    }

    private void customerDownload() {
    }

    private void sellerDownload() {
    }
    private void openDownloadDialog(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Download Manager");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField fileNameField = new TextField();
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter file path :"), fileNameField);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        try {
            System.out.println("112 file manager menu");

            //adminManager.addCategory(categoryNameField.getText(),imageField.getText());
            //updateShownCategories(adminManager.viewAllCategories());
        } catch (Exception e) {
            showError(e.getMessage(),20);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (person instanceof Seller){
            updateShownUsers(clientSellerManager.getSoldFileProducts(person.getUsername()));
        }else updateShownUsers(clientCustomerManager.getPayedFileProducts(person.getUsername()));
    }
}
