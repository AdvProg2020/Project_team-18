package graphics;

import Client.ClientAdminManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AuctionsMenu extends Menu implements Initializable {

    private ClientAdminManager clientAdminManager = new ClientAdminManager();

    public AuctionsMenu(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/AuctionsMenu.fxml");
    }

    @FXML
    TableView<Auction> auctionsTable = new TableView<>();
    @FXML
    TableColumn<Auction, Integer> auctionId = new TableColumn<>();
    @FXML
    TableColumn<Auction, Double> latestPrice = new TableColumn<>();
    @FXML
    TableColumn<Auction, String> productColumn = new TableColumn<>();
    @FXML
    TableColumn<Auction, LocalDateTime> beginDate = new TableColumn<>();
    @FXML
    TableColumn<Auction, LocalDateTime> endDate = new TableColumn<>();
    @FXML
    TableColumn<Auction, String> sellerColumn = new TableColumn<>();
    @FXML
    TableColumn<Auction, Void> buttonColumn = new TableColumn<>();

    private void updateShownAuctions(ArrayList<Auction> shownProducts) {
        final ObservableList<Auction> data = FXCollections.observableArrayList(
                shownProducts
        );
        beginDate.setCellValueFactory(new PropertyValueFactory<>("beginDate"));
        endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        auctionId.setCellValueFactory(new PropertyValueFactory<>("auctionId"));
        latestPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        productColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        sellerColumn.setCellValueFactory(new PropertyValueFactory<>("sellerName"));
        addButtonToTable(this);
        auctionsTable.setItems(data);
    }

    private void addButtonToTable(AuctionsMenu menu) {
        Callback<TableColumn<Auction, Void>, TableCell<Auction, Void>> cellFactory =
                new Callback<TableColumn<Auction, Void>, TableCell<Auction, Void>>() {
                    @Override
                    public TableCell<Auction, Void> call(final TableColumn<Auction, Void> param) {
                        final TableCell<Auction, Void> cell = new TableCell<Auction, Void>() {

                            private final Button btn = new Button("MORE");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Auction auction = getTableView().getItems().get(getIndex());
                                    AuctionMenu auctionMenu = new AuctionMenu(auction, menu);
                                    auctionMenu.run();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            updateShownAuctions(clientAdminManager.viewAllAuctions());
        } catch (Exception e) {
            showError(e.getMessage(), 20);
        }
    }

}
