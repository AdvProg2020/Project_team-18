package graphics;

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
import model.Customer;
import model.Discount;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AdminManageCodesMenu extends Menu implements Initializable {

    AdminManager adminManager = new AdminManager();

    @FXML
    TableView<Discount> discountTable = new TableView<>();
    @FXML TableColumn<Discount, Integer> idColumn = new TableColumn<>();
    @FXML TableColumn<Discount, String> codeColumn = new TableColumn<>();
    @FXML TableColumn<Discount, String> beginDateColumn = new TableColumn<>();
    @FXML TableColumn<Discount, String> endDateColumn = new TableColumn<>();
    @FXML TableColumn<Discount, Double> maxAmountColumn = new TableColumn<>();
    @FXML TableColumn<Discount, Double> percentageColumn = new TableColumn<>();
    @FXML TableColumn<Discount, Integer> maxUsageColumn = new TableColumn<>();
    @FXML TableColumn<Discount, Void> viewMoreColumn = new TableColumn<>();

    public AdminManageCodesMenu(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/AdminManageCodesMenu.fxml");
    }

    private void updateShownDiscounts(ArrayList<Discount> shownDiscounts){
        final ObservableList<Discount> data = FXCollections.observableArrayList(
                shownDiscounts
        );
        idColumn.setCellValueFactory(new PropertyValueFactory<>("discountId"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("discountCode"));
        beginDateColumn.setCellValueFactory(new PropertyValueFactory<>("beginDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        maxAmountColumn.setCellValueFactory(new PropertyValueFactory<>("maxAmount"));
        percentageColumn.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        maxUsageColumn.setCellValueFactory(new PropertyValueFactory<>("usagePerCustomer"));
        addViewMoreButtonToTable(this);
        discountTable.setItems(data);
    }

    private void addViewMoreButtonToTable(AdminManageCodesMenu menu) {
        Callback<TableColumn<Discount, Void>, TableCell<Discount, Void>> cellFactory =
                new Callback<TableColumn<Discount, Void>, TableCell<Discount, Void>>() {
                    @Override
                    public TableCell<Discount, Void> call(final TableColumn<Discount, Void> param) {
                        final TableCell<Discount, Void> cell = new TableCell<Discount, Void>() {

                            private final Button btn = new Button("Click");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Discount discount = getTableView().getItems().get(getIndex());
                                    viewMore(discount);
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

    private void viewMore(Discount discount){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(" Customers Using Discount");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Label moreInfo = new Label();
        HashMap<String,Integer> details = new HashMap<>();
        for (Customer customer : discount.getCustomersWithThisDiscount().keySet()) {
            details.put(customer.getUsername(),discount.getCustomersWithThisDiscount().get(customer));
        }
        moreInfo.setText(details.toString());
        VBox content = new VBox();
        content.getChildren().addAll(moreInfo);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    @FXML
    private void addDiscount(){
        AddDiscountPage addDiscountPage = new AddDiscountPage(this);
        addDiscountPage.run();
    }

    @FXML
    private void removeDiscount(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Remove Discount Code");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField codeField = new TextField();
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter discount code :"), codeField);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        try {
            adminManager.removeDiscountCode(codeField.getText());
            updateShownDiscounts(adminManager.viewAllDiscountCodes());
        } catch (Exception e) {
            showError(e.getMessage(),20);
        }
    }

    @FXML
    private void editDiscount(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Discount Code");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        CheckBox startDate = new CheckBox("Start Date");
        CheckBox endDate = new CheckBox("End Date");
        CheckBox percentage = new CheckBox("Percentage");
        CheckBox maxAmount = new CheckBox("Max Amount");
        CheckBox maxUsage = new CheckBox("Max Usage");
        CheckBox addCustomer = new CheckBox("Add customer to discount");
        CheckBox removeCustomer = new CheckBox("Remove customer from discount");
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Choose one of these fields to edit :"),startDate,endDate,
                percentage,maxAmount,maxUsage,addCustomer,removeCustomer);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        if(startDate.isSelected())
            editStartDate();
        else if(endDate.isSelected())
            editEndDate();
        else if (percentage.isSelected())
            editPercentage();
        else if (maxAmount.isSelected())
            editMaxAMount();
        else if(maxUsage.isSelected())
            editMaxUsage();
        else if (addCustomer.isSelected())
            addCustomerToDiscount();
        else if (removeCustomer.isSelected())
            removeCustomerFromDiscount();
    }

    private void editStartDate(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Start Date");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        TextField discountCode = new TextField();
        content.getChildren().addAll(new Label("Enter discount code:"),discountCode);
        DatePicker datePicker = new DatePicker();
        content.getChildren().addAll(new Label("Enter updated start date:"),datePicker);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        String month = String.format("%02d",datePicker.getValue().getMonthValue());
        String day = String.format("%02d",datePicker.getValue().getDayOfMonth());
        String year = Integer.toString(datePicker.getValue().getYear());
        String updatedDate = year +"," + month +","+ day + ",00,00";
        try {
            adminManager.editDiscountField(adminManager.viewDiscountCode(discountCode.getText()),"beginDate",updatedDate);
        } catch (Exception e) {
            showError(e.getMessage(),20);
        }
        updateShownDiscounts(adminManager.viewAllDiscountCodes());
    }

    private void editEndDate(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit End Date");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        TextField discountCode = new TextField();
        content.getChildren().addAll(new Label("Enter discount code:"),discountCode);
        DatePicker datePicker = new DatePicker();
        content.getChildren().addAll(new Label("Enter updated end date:"),datePicker);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        String month = String.format("%02d",datePicker.getValue().getMonthValue());
        String day = String.format("%02d",datePicker.getValue().getDayOfMonth());
        String year = Integer.toString(datePicker.getValue().getYear());
        String updatedDate = year +"," + month +","+ day + ",00,00";
        try {
            adminManager.editDiscountField(adminManager.viewDiscountCode(discountCode.getText()),"endDate",updatedDate);
        } catch (Exception e) {
            showError(e.getMessage(),20);
        }
        updateShownDiscounts(adminManager.viewAllDiscountCodes());
    }

    private void editPercentage(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Percentage");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        TextField discountCode = new TextField();
        content.getChildren().addAll(new Label("Enter discount code:"),discountCode);
        TextField percentage = new TextField();
        content.getChildren().addAll(new Label("Enter updated percentage:"),percentage);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        try {
            adminManager.editDiscountField(adminManager.viewDiscountCode(discountCode.getText()),"percentage",percentage.getText());
        } catch (Exception e) {
            showError(e.getMessage(),20);
        }
        updateShownDiscounts(adminManager.viewAllDiscountCodes());
    }

    private void editMaxAMount(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Max Amount");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        TextField discountCode = new TextField();
        content.getChildren().addAll(new Label("Enter discount code:"),discountCode);
        TextField maxAmount = new TextField();
        content.getChildren().addAll(new Label("Enter updated max amount:"),maxAmount);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        try {
            adminManager.editDiscountField(adminManager.viewDiscountCode(discountCode.getText()),"maxAmount",maxAmount.getText());
        } catch (Exception e) {
            showError(e.getMessage(),20);
        }
        updateShownDiscounts(adminManager.viewAllDiscountCodes());
    }

    private void editMaxUsage(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Max Usage");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        TextField discountCode = new TextField();
        content.getChildren().addAll(new Label("Enter discount code:"),discountCode);
        TextField maxUsage = new TextField();
        content.getChildren().addAll(new Label("Enter updated max usage:"),maxUsage);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        try {
            adminManager.editDiscountField(adminManager.viewDiscountCode(discountCode.getText()),"usagePerCustomer",maxUsage.getText());
        } catch (Exception e) {
            showError(e.getMessage(),20);
        }
        updateShownDiscounts(adminManager.viewAllDiscountCodes());
    }

    private void addCustomerToDiscount(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add Customer To Discount");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        TextField discountCode = new TextField();
        content.getChildren().addAll(new Label("Enter discount code:"),discountCode);
        TextField username = new TextField();
        content.getChildren().addAll(new Label("Enter customer's username:"),username);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        try {
            adminManager.addCustomerToDiscount(username.getText(),adminManager.viewDiscountCode(discountCode.getText()));
        } catch (Exception e) {
            showError(e.getMessage(),20);
        }
        updateShownDiscounts(adminManager.viewAllDiscountCodes());
    }

    private void removeCustomerFromDiscount(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Remove Customer From Discount");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        TextField discountCode = new TextField();
        content.getChildren().addAll(new Label("Enter discount code:"),discountCode);
        TextField username = new TextField();
        content.getChildren().addAll(new Label("Enter customer's username:"),username);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        try {
            adminManager.removeCustomerFromDiscount(adminManager.viewDiscountCode(discountCode.getText()),username.getText());
        } catch (Exception e) {
            showError(e.getMessage(),20);
        }
        updateShownDiscounts(adminManager.viewAllDiscountCodes());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateShownDiscounts(adminManager.viewAllDiscountCodes());
    }
}
