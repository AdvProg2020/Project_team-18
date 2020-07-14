package graphics;
import Client.ClientProductManager;
import Client.ClientSearchingManager;
import controller.ProductManager;
import controller.SearchingManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import model.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AllOffsMenu extends Menu implements Initializable {

    private ClientProductManager productManager = new ClientProductManager();
    private ClientSearchingManager searchingManager = new ClientSearchingManager();

    @FXML TableView<Product> productTable = new TableView<>();
    @FXML TableColumn<Product, Double> priceColumn = new TableColumn<>();
    @FXML TableColumn<Product, Integer> salePercentColumn = new TableColumn<>();
    @FXML TableColumn<Product, String> nameColumn = new TableColumn<>();
    @FXML TableColumn<Product, Image> imageColumn = new TableColumn<>();
    @FXML TableColumn<Product, Double> averageRateColumn = new TableColumn<>();
    @FXML TableColumn<Product, Void> buttonColumn = new TableColumn<>();
    @FXML TableColumn<Product, String> beginDateColumn = new TableColumn<>();
    @FXML TableColumn<Product, String> endDateColumn = new TableColumn<>();
    @FXML TableColumn<Product, String> timeLeftColumn = new TableColumn<>();
    @FXML TextField priceTextField = new TextField();
    @FXML TextField nameTextField = new TextField();
    @FXML ChoiceBox categoryChoiceBox = new ChoiceBox();
    @FXML CheckBox priceCheckBox = new CheckBox();
    @FXML CheckBox averageRateCheckBox = new CheckBox();


    public AllOffsMenu(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/AllOffsMenu.fxml");
    }

    private void populatingChoiceBoxes(){
        for (Category category : productManager.viewAllCategories()) {
            categoryChoiceBox.getItems().add(category.getCategoryName());
        }
        categoryChoiceBox.getItems().add("Choose Category");
        categoryChoiceBox.setValue("Choose Category");
    }

    private void updateShownProducts(ArrayList<Product> shownProducts){
        final ObservableList<Product> data = FXCollections.observableArrayList(
                shownProducts
        );
        for (Product product : shownProducts) {
            Sale sale = product.getSale();
            product.setSalePercent(sale.getAmountOfSale());
            product.setSaleBeginDate(sale.getBeginDate().toString());
            product.setSaleEndDAte(sale.getEndDate().toString());
            product.setSaleTimeLeft(calculateTimeLeft(sale.getBeginDate(),sale.getEndDate()));
        }
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        salePercentColumn.setCellValueFactory(new PropertyValueFactory<>("salePercent"));
        beginDateColumn.setCellValueFactory(new PropertyValueFactory<>("saleBeginDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("saleEndDate"));
        timeLeftColumn.setCellValueFactory(new PropertyValueFactory<>("saleTimeLeft"));
        imageColumn.setCellFactory(param -> {
            //Set up the ImageView
            final ImageView imageview = new ImageView();
            imageview.setFitHeight(50);
            imageview.setFitWidth(50);


            //Set up the Table
            TableCell<Product, Image> cell = new TableCell<Product, Image>() {
                public void updateItem(Image item, boolean empty) {
                    if (item != null) {
                        imageview.setImage(item);
                    }
                }
            };
            // Attach the imageView to the cell
            cell.setGraphic(imageview);
            return cell;
        });
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("Image"));
        averageRateColumn.setCellValueFactory(new PropertyValueFactory<>("averageRate"));
        addButtonToTable(this);
        productTable.setItems(data);
    }

    private String calculateTimeLeft(LocalDateTime beginDate, LocalDateTime endDate){
        LocalDateTime tempDateTime = LocalDateTime.from( beginDate );

        long years = tempDateTime.until( endDate, ChronoUnit.YEARS );
        tempDateTime = tempDateTime.plusYears( years );

        long months = tempDateTime.until( endDate, ChronoUnit.MONTHS );
        tempDateTime = tempDateTime.plusMonths( months );

        long days = tempDateTime.until( endDate, ChronoUnit.DAYS );
        tempDateTime = tempDateTime.plusDays( days );


        long hours = tempDateTime.until( endDate, ChronoUnit.HOURS );
        tempDateTime = tempDateTime.plusHours( hours );

        long minutes = tempDateTime.until( endDate, ChronoUnit.MINUTES );
        tempDateTime = tempDateTime.plusMinutes( minutes );

        String result = Long.toString(years) +","+  Long.toString(months) +","+ Long.toString(days) +","+
                Long.toString(hours) +","+ Long.toString(minutes);
        return result;
    }

    private void addButtonToTable(AllOffsMenu menu) {
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

        buttonColumn.setCellFactory(cellFactory);

    }

    @FXML
    private void performFilter(){
        ArrayList<Product> updatedProducts = new ArrayList<>();
        if(!priceTextField.getText().equals("")) {
            try {
                updatedProducts.addAll(searchingManager.performFilter("price" , priceTextField.getText()));
            } catch (Exception e) {
                showError(e.getMessage() , 20);
            }
        }
        if(!nameTextField.getText().equals("")){
            try {
                for (Product product : searchingManager.performFilter("name", nameTextField.getText())) {
                    if(!updatedProducts.contains(product))
                        updatedProducts.add(product);
                }
            } catch (Exception e) {
                showError(e.getMessage(),20);
            }
        }
        if(!categoryChoiceBox.getValue().equals("Choose Category")){
            try {
                for (Product product : searchingManager.performFilter("name", nameTextField.getText())) {
                    if(!updatedProducts.contains(product))
                        updatedProducts.add(product);
                }
            } catch (Exception e) {
                showError(e.getMessage(),20);
            }
        }
        updateShownProducts(updatedProducts);
    }

    @FXML
    private void disableFilter(){
        ArrayList<Product> updatedProducts = new ArrayList<>();
        if(!priceTextField.getText().equals("")){
            try {
                updatedProducts.addAll(searchingManager.disableFilter("price",priceTextField.getText()));
            } catch (Exception e) {
                showError(e.getMessage(),20);
            }
        }
        if(!nameTextField.getText().equals("")){
            try {
                updatedProducts.addAll(searchingManager.disableFilter("name",nameTextField.getText()));
            } catch (Exception e) {
                showError(e.getMessage(),20);
            }
        }
        if(!categoryChoiceBox.getValue().equals("Choose Category")){
            try {
                updatedProducts.addAll(searchingManager.disableFilter("category",categoryChoiceBox.getValue().toString()));
            } catch (Exception e) {
                showError(e.getMessage(),20);
            }
        }
        categoryChoiceBox.setValue("Choose Category");
        priceTextField.clear();
        nameTextField.clear();
        updateShownProducts(updatedProducts);
    }

    @FXML
    private void performSort() {
        ArrayList<Product> updatedProducts = new ArrayList<>();
        if(priceCheckBox.isSelected()) {
            try {
                updatedProducts.addAll(searchingManager.performSort("price"));
            } catch (Exception e) {
                showError(e.getMessage(),20);
            }
        }
        if(averageRateCheckBox.isSelected()) {
            try {
                for (Product product : searchingManager.performSort("average rate")) {
                    if(!updatedProducts.contains(product))
                        updatedProducts.add(product);
                }
            } catch (Exception e) {
                showError(e.getMessage(),20);
            }
        }
        updateShownProducts(updatedProducts);
    }

    @FXML
    private void disableSort() throws Exception{
        ArrayList<Product> updatedProducts = new ArrayList<>();
        if(priceCheckBox.isSelected()){
            priceCheckBox.setSelected(false);
            updatedProducts.addAll(searchingManager.disableSort("price"));
        }
        if(averageRateCheckBox.isSelected()){
            priceCheckBox.setSelected(false);
            updatedProducts.addAll(searchingManager.disableSort("average rate"));
        }
        updateShownProducts(updatedProducts);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populatingChoiceBoxes();
        updateShownProducts(productManager.viewAllProductsWithSale());
    }
}
