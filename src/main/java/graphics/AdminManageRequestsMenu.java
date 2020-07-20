package graphics;

import Client.ClientAdminManager;
import controller.AdminManager;
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
import model.Request;
import model.StateType;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AdminManageRequestsMenu extends Menu implements Initializable {

    ClientAdminManager adminManager = new ClientAdminManager();

    @FXML
    TableView<Request> requestsTable = new TableView<>();
    @FXML
    TableColumn<Request, Integer> idColumn = new TableColumn<>();
    @FXML
    TableColumn<Request, String> statusColumn = new TableColumn<>();
    @FXML
    TableColumn<Request, String> typeColumn = new TableColumn<>();
    @FXML
    TableColumn<Request, Void> respondColumn = new TableColumn<>();
    @FXML
    TableColumn<Request, Void> viewMoreColumn = new TableColumn<>();

    public AdminManageRequestsMenu(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/AdminManageRequestsMenu.fxml");
    }

    private void updateShownRequests(ArrayList<Request> shownRequests) {
        final ObservableList<Request> data = FXCollections.observableArrayList(
                shownRequests
        );
        idColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("stateOfRequest"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeOfRequest"));
        addRespondButtonToTable(this);
        addViewMoreButtonToTable(this);
        requestsTable.setItems(data);
    }

    private void addRespondButtonToTable(AdminManageRequestsMenu menu) {
        Callback<TableColumn<Request, Void>, TableCell<Request, Void>> cellFactory =
                new Callback<TableColumn<Request, Void>, TableCell<Request, Void>>() {
                    @Override
                    public TableCell<Request, Void> call(final TableColumn<Request, Void> param) {
                        final TableCell<Request, Void> cell = new TableCell<Request, Void>() {

                            private final Button btn = new Button("Click");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Request request = getTableView().getItems().get(getIndex());
                                    respond(request);
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

        respondColumn.setCellFactory(cellFactory);

    }

    private void respond(Request request) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Respond Request");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Label userInfo = new Label("What do you want to do with this request?");
        VBox content = new VBox();
        Button accept = new Button("accept");
        accept.setOnAction(e -> {
            try {
                adminManager.acceptRequest(Integer.toString(request.getRequestId()));
                ArrayList<Request> newRequests = new ArrayList<>();
                for (Request request1 : adminManager.viewAllRequests()) {
                    if (request1.getStateOfRequest() == StateType.PROCESSING)
                        newRequests.add(request1);
                }
                updateShownRequests(newRequests);
            } catch (Exception ex) {
                showError(ex.getMessage(), 20);
            }
        });
        Button decline = new Button("decline");
        decline.setOnAction(e -> {
            try {
                adminManager.declineRequest(Integer.toString(request.getRequestId()));
                ArrayList<Request> newRequests = new ArrayList<>();
                for (Request request2 : adminManager.viewAllRequests()) {
                    if (request2.getStateOfRequest() == StateType.PROCESSING)
                        newRequests.add(request2);
                }
                updateShownRequests(newRequests);
            } catch (Exception ex) {
                showError(ex.getMessage(), 20);
            }
        });
        Button takeNoAction = new Button("Take No Action");
        takeNoAction.setOnAction(e -> back());
        content.getChildren().addAll(accept, decline, takeNoAction, userInfo);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    private void addViewMoreButtonToTable(AdminManageRequestsMenu menu) {
        Callback<TableColumn<Request, Void>, TableCell<Request, Void>> cellFactory =
                new Callback<TableColumn<Request, Void>, TableCell<Request, Void>>() {
                    @Override
                    public TableCell<Request, Void> call(final TableColumn<Request, Void> param) {
                        final TableCell<Request, Void> cell = new TableCell<Request, Void>() {

                            private final Button btn = new Button("Click");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Request request = getTableView().getItems().get(getIndex());
                                    viewMore(request);
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

        viewMoreColumn.setCellFactory(cellFactory);

    }

    private void viewMore(Request request) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Respond Request");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Label moreInfo = new Label();
        HashMap<String, String> info = request.getInformation();
        moreInfo.setText(info.toString());
        VBox content = new VBox();
        content.getChildren().addAll(moreInfo);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Request> newRequests = new ArrayList<>();
        for (Request request : adminManager.viewAllRequests()) {
            if (request.getStateOfRequest() == StateType.PROCESSING)
                newRequests.add(request);
        }
        updateShownRequests(newRequests);
    }
}
