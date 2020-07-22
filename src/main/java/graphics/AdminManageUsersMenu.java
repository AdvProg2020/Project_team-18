package graphics;

import Client.ClientAdminManager;
import controller.AdminManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.Person;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminManageUsersMenu extends Menu implements Initializable {

    ClientAdminManager adminManager = new ClientAdminManager();

    @FXML
    TableView userTable = new TableView();
    @FXML
    TableColumn<Person, Double> userNameColumn = new TableColumn<>();
    @FXML TableColumn<Person, String> roleColumn = new TableColumn<>();
    @FXML TableColumn<Person, String> firstNameColumn = new TableColumn<>();
    @FXML TableColumn<Person, Image> lastNameColumn = new TableColumn<>();
    @FXML TableColumn<Person, Double> emailColumn = new TableColumn<>();
    @FXML TableColumn<Person, String> numberColumn = new TableColumn<>();
    @FXML TableColumn<Person, Double> balanceColumn = new TableColumn<>();
    @FXML TableColumn<Person, Void> buttonColumn = new TableColumn<>();
    @FXML TableColumn<Person, String> availabilityColumn = new TableColumn<>();
    public AdminManageUsersMenu(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/AdminManageUsersMenu.fxml");
    }

    private void updateShownUsers(ArrayList<Person> shownUsers){
        final ObservableList<Person> data = FXCollections.observableArrayList(
                shownUsers
        );
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("familyName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
        addButtonToTable(this);
        userTable.setItems(data);
    }

    private void addButtonToTable(AdminManageUsersMenu menu) {
        Callback<TableColumn<Person, Void>, TableCell<Person, Void>> cellFactory =
                new Callback<TableColumn<Person, Void>, TableCell<Person, Void>>() {
                    @Override
                    public TableCell<Person, Void> call(final TableColumn<Person, Void> param) {
                        final TableCell<Person, Void> cell = new TableCell<Person, Void>() {

                            private final Button btn = new Button("Click");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Person person = getTableView().getItems().get(getIndex());
                                    viewSinglePerson(person);
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

    private void viewSinglePerson(Person person){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("User Panel");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Label userInfo = new Label();
        userInfo.setText(person.toString());
        VBox content = new VBox(userInfo);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
    }

    @FXML
    private void removeUser(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Remove User");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField removedField = new TextField();
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter the Users's username :"), removedField);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        try {
            adminManager.deleteUser(removedField.getText());
            updateShownUsers(adminManager.viewAllUsers());
        } catch (Exception e) {
            showError(e.getMessage(),20);
        }
    }

    @FXML
    private void createManagerAccount(){
        RegisterMenu registerMenu = new RegisterMenu(this);
        registerMenu.run();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateShownUsers(adminManager.viewAllUsers());
    }
}
