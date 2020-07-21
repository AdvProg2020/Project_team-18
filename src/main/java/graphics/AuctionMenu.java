package graphics;

import model.Auction;

public class AuctionMenu extends Menu {
    Auction auction;
    public AuctionMenu(Auction auction, Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/AuctionMenu.fxml");
        this.auction = auction;
    }
}
