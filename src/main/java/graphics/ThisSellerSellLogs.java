package graphics;

import controller.SellerManager;
import controller.Storage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.SellLog;
import model.Seller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ThisSellerSellLogs extends Menu implements Initializable {
    SellerManager sellerManager = new SellerManager();
    Storage storage = new Storage();
    @FXML
    TableView<SellLog> tableView = new TableView<>();
    @FXML
    TableColumn<SellLog, String> sellLogCode= new TableColumn<>();
    @FXML
    TableColumn<SellLog, Double> receivedMoney = new TableColumn<>();
    @FXML
    TableColumn<SellLog, Double> amountOfSale = new TableColumn<>();
    @FXML
    TableColumn<SellLog, LocalDateTime> dateColumn = new TableColumn<>();
    @FXML
    TextField logCode;

    public ThisSellerSellLogs(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/ThisSellerSellLogs.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTable(tableView);
    }

    public void setTable(TableView<SellLog> tableView) {
        sellLogCode.setCellValueFactory(new PropertyValueFactory<>("sellCode"));
        receivedMoney.setCellValueFactory(new PropertyValueFactory<>("receivedMoney"));
        amountOfSale.setCellValueFactory(new PropertyValueFactory<>("saleAmount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        ObservableList<SellLog> thisSellerSellLogs = FXCollections.observableArrayList(returnThisSellerSellLogs());
        tableView.setItems(thisSellerSellLogs);
    }
    public ArrayList<SellLog> returnThisSellerSellLogs(){
        return ((Seller) person).getSellHistory();
    }

    public void showBuyLog() throws IOException {
        if (logCode.getText().equals("")) {
            showError("Please Enter a code!");
        } else if(!logCode.getText().matches("\\d+")){
            showError("Buy log code is an integer!");
        } else if (sellerManager.getSellerSellHistory().isEmpty()) {
            showError("You have not sold anything!");
        } else if (!sellerManager.doesSellerHasThisSellLog(Integer.parseInt(logCode.getText()))) {
            showError("Oops!You don't have this sell log!");
        } else {
            showWantedSellLog(logCode.getText());
        }
    }

    private void showWantedSellLog(String code) throws IOException {
        SellLog thisSellLog = storage.getSellLogByCode(code);
        PerSellLog sellLog =new PerSellLog(thisSellLog, this);
        sellLog.run();
        /*FXMLLoader loader = new FXMLLoader(new File("src/main/java/graphics/fxml/PerBuyLog.fxml").toURI().toURL());
        stage.setScene(new Scene(loader.load(), 600, 600));*/
    }

    public void showError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(null);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.show();
    }
}
