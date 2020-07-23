package graphics;

import Client.ClientAdminManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.Supporter;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SupportersTable extends Menu implements Initializable {
    ClientAdminManager clientAdminManager = new ClientAdminManager();
    @FXML
    TableView<Supporter> supportersTable = new TableView<>();
    @FXML
    TableColumn<Supporter, String> usernameColumn = new TableColumn<>();
    @FXML
    TableColumn<Supporter, String> availabilityColumn = new TableColumn<>();
    @FXML
    TableColumn<Supporter, Void> buttonColumn = new TableColumn<>();

    public SupportersTable(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/SupportersTable.fxml");
}

    private void updateShownSupporters(ArrayList<Supporter> shownProducts) {
        final ObservableList<Supporter> data = FXCollections.observableArrayList(
                shownProducts
        );
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
        addButtonToTable(this);
        supportersTable.setItems(data);
    }

    private void addButtonToTable(SupportersTable menu) {
        Callback<TableColumn<Supporter, Void>, TableCell<Supporter, Void>> cellFactory =
                new Callback<TableColumn<Supporter, Void>, TableCell<Supporter, Void>>() {
                    @Override
                    public TableCell<Supporter, Void> call(final TableColumn<Supporter, Void> param) {
                        final TableCell<Supporter, Void> cell = new TableCell<Supporter, Void>() {

                            private final Button btn = new Button("Go To ChatRoom");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    Supporter supporter = getTableView().getItems().get(getIndex());
                                    ChatController chatController = new ChatController(menu, supporter);
                                    chatController.run();
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
            updateShownSupporters(clientAdminManager.viewOnlineSupporters());
        } catch (Exception e) {
            showError(e.getMessage(), 20);
        }
    }

}
