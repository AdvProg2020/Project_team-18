package graphics;

import Client.ClientCustomerManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import model.Auction;
import model.Customer;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class AuctionMenu extends Menu implements Initializable {
    private ClientCustomerManager customerManager = new ClientCustomerManager();

    private Auction auction;
    @FXML
    private Label idLabel;
    @FXML
    private Label productLabel;
    @FXML
    private Label sellerLabel;
    @FXML
    private Label beginDateLabel;
    @FXML
    private Label endDateLabel;
    @FXML
    private Label latestPriceLabel;
    @FXML
    private Label latestCustomerLabel;

    public AuctionMenu(Auction auction, Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/AuctionMenu.fxml");
        this.auction = auction;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.auction = customerManager.getAuctionById(auction.getId());
        setPerson((Customer) customerManager.getPersonByUsername(person.getUsername()));
        idLabel.setText(Integer.toString(auction.getId()));
        productLabel.setText(auction.getProductName());
        sellerLabel.setText(auction.getSellerName());
        beginDateLabel.setText(auction.getBeginDate().toString());
        endDateLabel.setText(auction.getEndDate().toString());
        latestPriceLabel.setText(Double.toString(auction.getPrice()));
        if (auction.getCustomer() == null){
            latestCustomerLabel.setText("no customer");
        } else
        latestCustomerLabel.setText(auction.getCustomer().getUsername());
    }

    @FXML
    private void makeAnOffer() {
        Dialog<ButtonType> dialog = new Dialog<>();
        String updatedVersion;
        dialog.setTitle("Make An Offer");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        javafx.scene.control.TextField textField = new javafx.scene.control.TextField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new javafx.scene.control.Label("Enter amount of money you want to offer :"), textField);
        dialog.getDialogPane().setContent(content);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            updatedVersion = textField.getText();
            if (LocalDateTime.now().isAfter(auction.getEndDate())) {
                showError("This auction has been finished!", 30);
            } else {
                if (!updatedVersion.matches("\\d+\\.?\\d+")) {
                    showError("PLease enter valid price!", 30);
                } else {
                    if (Double.parseDouble(updatedVersion) < auction.getPrice()) {
                        showError("If you want to offer, you should offer more money than the price shown in the table", 30);
                    } else if (Double.parseDouble(updatedVersion) > ((Customer) person).getWallet().getMoney()) {
                        showError("Oops!You don't have enough money in your wallet!!", 30);
                    } else {
                        try {
                            customerManager.bidding(auction.getId(), updatedVersion, person.getUsername());
                            this.auction = customerManager.getAuctionById(auction.getId());
                            setPerson((Customer) customerManager.getPersonByUsername(person.getUsername()));
                            idLabel.setText(Integer.toString(auction.getId()));
                            productLabel.setText(auction.getProductName());
                            sellerLabel.setText(auction.getSellerName());
                            beginDateLabel.setText(auction.getBeginDate().toString());
                            endDateLabel.setText(auction.getEndDate().toString());
                            latestPriceLabel.setText(Double.toString(auction.getPrice()));
                            if (auction.getCustomer() == null){
                                latestCustomerLabel.setText("no customer");
                            } else
                                latestCustomerLabel.setText(auction.getCustomer().getUsername());
                        } catch (Exception e) {
                            showError(e.getMessage(), 30);
                        }
//                    auction.setPrice(Double.parseDouble(updatedVersion));
//                    auction.setCustomer((Customer) person);
                    }
                }
//            try {
////                customerManager.addBalance(Double.parseDouble(updatedVersion), person.getUsername());
////                viewPersonalInfo();
//            } catch (Exception e) {
//                showError(e.getMessage(), 100);
//            }
            }
        }
    }

    @FXML
    private void goToThisAuctionChatRoom() {
        AuctionChatRoom auctionChatRoom = new AuctionChatRoom(this, this.auction);
        auctionChatRoom.run();
    }
}
