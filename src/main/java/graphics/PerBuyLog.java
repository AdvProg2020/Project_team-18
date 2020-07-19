package graphics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.BuyLog;
import model.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class PerBuyLog extends Menu implements Initializable {
    private BuyLog buyLog;
    @FXML
    TableView<Product> tableView = new TableView<>();
    @FXML
    TableColumn<Product, Double> priceColumn = new TableColumn<>();
    @FXML
    TableColumn<Product, String> productNameColumn = new TableColumn<>();
    @FXML
    TableColumn<Product, Integer> idColumn = new TableColumn<>();
    @FXML
    TableColumn<Product, String> numberColumn = new TableColumn<>();
    @FXML
    TableColumn<Product, String> brandName = new TableColumn<>();
    @FXML
    TableColumn<Product, String> sellerName = new TableColumn<>();
    @FXML
    Label paidMoney;
    @FXML
    Label amountOfDiscount;
    @FXML
    Label discountUsed;
    @FXML
    Label date;
    @FXML
    Label logCode;
    @FXML
    Label buyLogStatus;

    public PerBuyLog(BuyLog buyLog, Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/PerBuyLog.fxml");
        this.buyLog = buyLog;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final ObservableList<Product> productsInBuyLog = FXCollections.observableArrayList(
                buyLog.getProducts().keySet()
        );
        idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        brandName.setCellValueFactory(new PropertyValueFactory<>("brand"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("numberInCart"));
        sellerName.setCellValueFactory(new PropertyValueFactory<>("sellerName"));
        tableView.setItems(productsInBuyLog);
        paidMoney.setText(Double.toString(buyLog.getPaidMoney()));
        amountOfDiscount.setText(Double.toString(buyLog.getDiscountAmount()));
        if (buyLog.getDiscountUsed().equals("")) {
            discountUsed.setText("No Discounts!");
        } else {
            discountUsed.setText(buyLog.getDiscountUsed());
        }
        date.setText(buyLog.getDate().toString());
        logCode.setText(Integer.toString(buyLog.getBuyCode()));
        buyLogStatus.setText(buyLog.getStatus().toString());
    }

}
