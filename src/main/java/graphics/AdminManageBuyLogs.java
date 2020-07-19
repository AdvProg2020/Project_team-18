package graphics;

import Client.ClientAdminManager;
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
import model.BuyLog;
import model.BuyLogStatus;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AdminManageBuyLogs extends Menu implements Initializable {

    ClientAdminManager adminManager = new ClientAdminManager();

    @FXML
    TableView<BuyLog> buyLogsTable = new TableView<>();
    @FXML
    TableColumn<BuyLog, Integer> idColumn = new TableColumn<>();
    @FXML
    TableColumn<BuyLog, String> statusColumn = new TableColumn<>();
    @FXML
    TableColumn<BuyLog, Void> respondColumn = new TableColumn<>();
    @FXML
    TableColumn<BuyLog, Void> viewMoreColumn = new TableColumn<>();

    public AdminManageBuyLogs(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/AdminManageBuyLogs.fxml");
    }

    private void updateShownBuyLogs(ArrayList<BuyLog> shownBuyLogs) {
        final ObservableList<BuyLog> data = FXCollections.observableArrayList(
                shownBuyLogs
        );
        idColumn.setCellValueFactory(new PropertyValueFactory<>("buyCode"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("buyLogStatus"));
        addRespondButtonToTable(this);
        addViewMoreButtonToTable(this);
        buyLogsTable.setItems(data);
    }

    private void addRespondButtonToTable(AdminManageBuyLogs menu) {
        Callback<TableColumn<BuyLog, Void>, TableCell<BuyLog, Void>> cellFactory =
                new Callback<TableColumn<BuyLog, Void>, TableCell<BuyLog, Void>>() {
                    @Override
                    public TableCell<BuyLog, Void> call(final TableColumn<BuyLog, Void> param) {
                        final TableCell<BuyLog, Void> cell = new TableCell<BuyLog, Void>() {

                            private final Button btn = new Button("Respond");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    BuyLog buyLog = getTableView().getItems().get(getIndex());
                                    respond(buyLog);
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

    private void respond(BuyLog buyLog) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Respond BuyLog");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Label userInfo = new Label("What do you want to do with this BuyLog?");
        VBox content = new VBox();
        Button accept = new Button("Send");
        accept.setOnAction(e -> {
            try {
                adminManager.sendPurchase(Integer.toString(buyLog.getBuyCode()));
                ArrayList<BuyLog> newBuyLogs = new ArrayList<>();
                for (BuyLog buyLog1 : adminManager.viewAllBuyLogs()) {
                    if (buyLog1.getStatus() == BuyLogStatus.WAITING)
                        newBuyLogs.add(buyLog1);
                }
                updateShownBuyLogs(newBuyLogs);
            } catch (Exception ex) {
                showError(ex.getMessage(), 20);
            }
        });
        Button takeNoAction = new Button("Take No Action");
        takeNoAction.setOnAction(e -> back());
        content.getChildren().addAll(accept, takeNoAction, userInfo);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    private void addViewMoreButtonToTable(AdminManageBuyLogs menu) {
        Callback<TableColumn<BuyLog, Void>, TableCell<BuyLog, Void>> cellFactory =
                new Callback<TableColumn<BuyLog, Void>, TableCell<BuyLog, Void>>() {
                    @Override
                    public TableCell<BuyLog, Void> call(final TableColumn<BuyLog, Void> param) {
                        final TableCell<BuyLog, Void> cell = new TableCell<BuyLog, Void>() {

                            private final Button btn = new Button("MORE");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    BuyLog buyLog = getTableView().getItems().get(getIndex());
                                    viewMore(buyLog);
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

    private void viewMore(BuyLog buyLog) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Respond BuyLog");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Label moreInfo = new Label();
        HashMap<String, String> info = buyLog.getCustomerInfo();
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
        ArrayList<BuyLog> newRequests = new ArrayList<>();
        for (BuyLog buyLog : adminManager.viewAllBuyLogs()) {
            if (buyLog.getStatus() == BuyLogStatus.WAITING)
                newRequests.add(buyLog);
        }
        updateShownBuyLogs(newRequests);
    }
}
