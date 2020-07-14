package graphics;

import Client.ClientAdminManager;
import Client.ClientProductManager;
import controller.AdminManager;
import controller.ProductManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.Request;
import model.RequestType;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class CustomerRequestsMenu extends Menu implements Initializable {
    private ClientAdminManager adminManager = new ClientAdminManager();
    private ClientProductManager productManager = new ClientProductManager();


    @FXML
    TableView<Request> requestsTable = new TableView<>();
    @FXML
    TableColumn<Request, Integer> idColumn = new TableColumn<>();
    @FXML
    TableColumn<Request, String> statusColumn = new TableColumn<>();
    @FXML
    TableColumn<Request, String> typeColumn = new TableColumn<>();

    public CustomerRequestsMenu(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/CustomerRequestsMenu.fxml");
    }

    @FXML
    private void addComment() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Comment Product");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextField commentField = new TextField();
        commentField.setPromptText("Content");
        TextField productIdField = new TextField();
        productIdField.setPromptText("Product Id");
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Type the product's id :"), productIdField);
        content.getChildren().addAll(new Label("Type your comment :"), titleField, commentField);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        try {
            String title = titleField.getText();
            String body = commentField.getText();
            productManager.addComment(Integer.parseInt(productIdField.getText()), title, body, person.getUsername());
            ArrayList<Request> customerReq = new ArrayList<>();
            for (Request request : adminManager.viewAllRequests()) {
                if (request.getTypeOfRequest() == RequestType.ADD_COMMENT) {
                    if (request.getInformation().get("username") != null && request.getInformation().get("username").equals(person.getUsername()))
                        customerReq.add(request);
                    else if (request.getInformation().get("username") != null && request.getInformation().get("username").equals(person.getUsername()))
                        customerReq.add(request);
                }
            }
            updateShownRequests(customerReq);
        } catch (Exception e) {
            showError(e.getMessage(), 200);
        }
    }

    private void updateShownRequests(ArrayList<Request> shownRequests) {
        final ObservableList<Request> data = FXCollections.observableArrayList(
                shownRequests
        );
        idColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("stateOfRequest"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeOfRequest"));
        requestsTable.setItems(data);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Request> customerReq = new ArrayList<>();
        for (Request request : adminManager.viewAllRequests()) {
            if (request.getTypeOfRequest() == RequestType.ADD_COMMENT) {
                if (request.getInformation().get("username") != null && request.getInformation().get("username").equals(person.getUsername()))
                    customerReq.add(request);
                else if (request.getInformation().get("username") != null && request.getInformation().get("username").equals(person.getUsername()))
                    customerReq.add(request);
            }
        }
        updateShownRequests(customerReq);
    }
}
