package graphics;

import Client.ClientCustomerManager;
import controller.CustomerManager;
import controller.Storage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.BuyLog;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ThisPersonBuyLogs extends Menu implements Initializable {
    ClientCustomerManager customerManager = new ClientCustomerManager();
    Storage storage = new Storage();
    @FXML
    TableView<BuyLog> tableView = new TableView<>();
    @FXML
    TableColumn<BuyLog, String> buyLogCode = new TableColumn<>();
    @FXML
    TableColumn<BuyLog, Double> totalPriceColumn = new TableColumn<>();
    @FXML
    TableColumn<BuyLog, Double> amountOfDiscountColumn = new TableColumn<>();
    @FXML
    TableColumn<BuyLog, LocalDateTime> dateColumn = new TableColumn<>();
    @FXML
    TextField logCode = new TextField();

    public ThisPersonBuyLogs(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/ThisPersonBuyLogs.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTable(tableView);
    }

    public void setTable(TableView<BuyLog> tableView) {
        buyLogCode.setCellValueFactory(new PropertyValueFactory<>("buyCode"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("paidMoney"));
        amountOfDiscountColumn.setCellValueFactory(new PropertyValueFactory<>("discountAmount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        ObservableList<BuyLog> thisPersonBuyLogs = FXCollections.observableArrayList(returnThisPersonBuyLogs());
        tableView.setItems(thisPersonBuyLogs);
    }

    public ArrayList<BuyLog> returnThisPersonBuyLogs() {
        return ((Customer) person).getBuyHistory();
    }

    public void showBuyLog() throws IOException {
        if (logCode.getText().equals("")) {
            showError("Please Enter a code!");
        } else if (!logCode.getText().matches("\\d+")) {
            showError("Buy log code is an integer!");
        } else if (customerManager.getCustomerBuyLogs(person.getUsername()).isEmpty()) {
            showError("You have not purchase anything!");
        } else if (!customerManager.doesCustomerHasThisBuyLog(person.getUsername(), Integer.parseInt(logCode.getText()))) {
            showError("Oops!You don't have this buy log!");
        } else {
            showWantedBuyLog(logCode.getText());
        }
    }

    private void showWantedBuyLog(String code) throws IOException {
        BuyLog thisBuyLog = storage.getBuyLogByCode(code);
        PerBuyLog buyLog = new PerBuyLog(thisBuyLog, this);
        buyLog.run();
    }

    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(null);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.show();
    }
}
