package graphics;

import Client.ClientCustomerManager;
import Client.ClientProductManager;
import controller.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class ProductMenu extends Menu implements Initializable {

    private Product product;
    private ClientCustomerManager customerManager = new ClientCustomerManager();
    private ClientProductManager productManager = new ClientProductManager();
    final DoubleProperty zoomProperty = new SimpleDoubleProperty(200);

    @FXML
    public ImageView statusImageView;
    @FXML
    public Label productNameLabel;
    @FXML
    public ChoiceBox choiceBox;
    @FXML
    public ImageView imageview;
    @FXML
    public ListView listView;
    @FXML
    private ScrollPane scrollpane = new ScrollPane();
    @FXML
    private TableView<Product> similarProducts = new TableView<>();
    @FXML private TableColumn<Product,Integer> idColumn = new TableColumn<>();
    @FXML private TableColumn<Product,String> nameColumn = new TableColumn<>();
    @FXML private TableColumn<Product,Void> viewMoreColumn = new TableColumn<>();

    public ProductMenu(Menu previousMenu , Product product) {
        super(previousMenu, "src/main/java/graphics/fxml/ProductMenu.fxml");
        this.product = product;
    }

    public void button(){
        try {
            customerManager.increaseProduct(currentCart, Integer.toString(this.product.getProductId()));
        } catch (Exception e){
            showError(e.getMessage(), 200);
        }
    }

    public void choiceBoxAction() throws Exception {
        switch (choiceBox.getValue().toString()){
            case "Comments":
                comment();
                return;
            case "Attributes":
                attributes();
                return;
            case "Compare":
                compareAction();
                return;
            case "Rate":
                rate();
                return;
            case "More Options":
                showError("No Action Selected", 100);
        }
    }

    private void rate(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Rate Product");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField rateField = new TextField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter a number between 0-5 :"), rateField);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        try {
            customerManager.rateProduct(product.getProductId(),Double.parseDouble(rateField.getText()), person.getUsername());
        } catch (Exception e) {
            showError(e.getMessage(), 200);
        }
    }

    private void comment(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Comments");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TableView tableView = new TableView();
        TableColumn<String, String> commentColumn = new TableColumn<>("Comment");
        tableView.getColumns().addAll(commentColumn);
        Collection<String> list = new ArrayList<>();
        for (Comment comment : product.getComments()) {
            list.add(comment.getCommentTitle() + " : " + comment.getCommentBody());
        }
        ObservableList<String> details = FXCollections.observableArrayList(list);
        commentColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        tableView.setItems(details);
        VBox content = new VBox(tableView);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        Button addComment = new Button("Add a new comment");
        addComment.setOnAction(e -> addComment());
        content.getChildren().addAll(addComment);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    private void attributes(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.FINISH);
        Label label = new Label(product.getCategory().getProperties().toString());
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(label);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    private void addComment(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Comment Product");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField titleField = new TextField("Title : ");
        TextField commentField = new TextField("Content : ");
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Type your comment :"),titleField,commentField);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        try {
            String title = titleField.getText().substring(titleField.getText().indexOf(":") + 1);
            String body = commentField.getText().substring(titleField.getText().indexOf(":") + 1);
            if (person != null){
                productManager.addComment(product.getProductId(), title, body, person.getUsername());
        } else {
                productManager.addComment(product.getProductId(), title, body, "anonymous");
            }
        } catch (Exception e) {
            showError(e.getMessage(), 200);
        }
    }

    private void compareAction() throws Exception {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Compare Product");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField compareField = new TextField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter second product's id :"), compareField);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        CompareMenu compareMenu = new CompareMenu(this , product.getProductId() , Integer.parseInt(compareField.getText()));
        compareMenu.run();
    }

    private void listViewContents() throws Exception{
        String id = Integer.toString(product.getProductId());
        String name = product.getName();
        String brand = product.getBrand();
        String price = Double.toString(product.getPrice());
        String supply = Integer.toString(product.getSupply());
        String category = product.getCategory().getCategoryName();
        String rate = Double.toString(product.getAverageRate());
        listView.getItems().addAll("productId    | " + id, "Name          | " + name , "Brand          | " + brand ,
                "Price           | " + price , "supply         | " + supply , "Category     | " + category ,
                "Average Rate | " + rate);
    }

    private void setStatusImageView(){
        if(product.getSupply() == 0){
            statusImageView.setImage(new Image("file:src/main/java/graphics/fxml/images/finished.jpg"));
        } else if(product.getSale() == null){
            statusImageView.setImage(new Image("file:src/main/java/graphics/fxml/images/available.png"));
        } else{
            statusImageView.setImage(new Image("file:src/main/java/graphics/fxml/images/sale.jpg"));
        }
    }

    public void goToMyAccount(){
        if (person == null){
            LoginMenu loginMenu = new LoginMenu(this);
            loginMenu.run();
        } else if (person instanceof Admin){
            AdminMenu adminMenu = new AdminMenu(this);
            adminMenu.run();
        } else if (person instanceof Seller){
            SellerMenu sellerMenu = new SellerMenu(this);
            sellerMenu.run();
        } else if (person instanceof Customer){
            CustomerMenu customerMenu = new CustomerMenu(this);
            customerMenu.run();
        }
    }

    private void updateShownProducts(ArrayList<Product> shownProducts){
        final ObservableList<Product> data = FXCollections.observableArrayList(
                shownProducts
        );
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        addButtonToTable(this);
        similarProducts.setItems(data);
    }

    private void addButtonToTable(ProductMenu menu) {
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory =
                new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
                    @Override
                    public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                        final TableCell<Product, Void> cell = new TableCell<Product, Void>() {

                            private final Button btn = new Button("Click");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Product product = getTableView().getItems().get(getIndex());
                                    ProductMenu productMenu = new ProductMenu(menu,product);
                                    productMenu.run();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productNameLabel.setText(product.getName());
        imageview.setImage(new Image(product.getImagePath()));
        setStatusImageView();
        choiceBox.getItems().addAll("Compare", "Attributes" , "Comments" , "Rate" , "More Options");
        choiceBox.setValue("More Options");
        updateShownProducts(product.getCategory().getThisCategoryProducts());
        zoomProperty.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable arg0) {
                imageview.setFitWidth(zoomProperty.get() * 4);
                imageview.setFitHeight(zoomProperty.get() * 3);
            }
        });

        scrollpane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() > 0) {
                    zoomProperty.set(zoomProperty.get() * 1.1);
                } else if (event.getDeltaY() < 0) {
                    zoomProperty.set(zoomProperty.get() / 1.1);
                }
            }
        });
        imageview.preserveRatioProperty().set(true);
        scrollpane.setContent(imageview);
        try {
            listViewContents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

