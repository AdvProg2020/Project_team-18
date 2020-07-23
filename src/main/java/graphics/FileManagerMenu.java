package graphics;

import Client.ClientCustomerManager;
import Client.ClientSellerManager;
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
import model.FileProduct;
import model.Person;
import model.Seller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FileManagerMenu extends Menu implements Initializable {
    private ClientSellerManager clientSellerManager = new ClientSellerManager();
    private ClientCustomerManager clientCustomerManager = new ClientCustomerManager();
    public FileManagerMenu(Menu previousMenu) {
        super(previousMenu, "src/main/java/graphics/fxml/FileManagerMenu.fxml");
    }
    @FXML
    TableView userTable = new TableView();
    @FXML
    TableColumn<FileProduct, String> fileNameColumn = new TableColumn<>();
    @FXML TableColumn<FileProduct, String> sellerNameColumn = new TableColumn<>();
    @FXML TableColumn<FileProduct, String> stateColumn = new TableColumn<>();
    @FXML TableColumn<FileProduct, Void> buttonColumn = new TableColumn<>();
    private void updateShownProducts(ArrayList<FileProduct> shownProducts){
        final ObservableList<FileProduct> data = FXCollections.observableArrayList(
                shownProducts
        );
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        sellerNameColumn.setCellValueFactory(new PropertyValueFactory<>("sellerName"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("fileState"));
        addButtonToTable();
        userTable.setItems(data);
    }

    private void addButtonToTable() {
        Callback<TableColumn<FileProduct, Void>, TableCell<FileProduct, Void>> cellFactory =
                new Callback<TableColumn<FileProduct, Void>, TableCell<FileProduct, Void>>() {
                    @Override
                    public TableCell<FileProduct, Void> call(final TableColumn<FileProduct, Void> param) {
                        final TableCell<FileProduct, Void> cell = new TableCell<FileProduct, Void>() {

                            private final Button btn = new Button("Download");

                            {
                                btn.setOnAction((ActionEvent event) -> {
                                    FileProduct fileProduct = getTableView().getItems().get(getIndex());
                                    download(fileProduct);
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

    private void download(FileProduct fileProduct) {
        openDownloadDialog(fileProduct);
    }

    private void customerDownload(String filePath,String sellerUsername,FileProduct fileProduct) throws Exception {
        String ip = clientCustomerManager.getSellerIP(sellerUsername);
        int port = clientCustomerManager.getSellerPort(sellerUsername);
        Socket socket = new Socket(ip,port);
        System.out.println("customer socket created");
        clientCustomerManager.setFileDownloading(fileProduct.getProductId(),person.getUsername());
        updateShownProducts(clientCustomerManager.getPayedFileProducts(person.getUsername()));
        FileOutputStream fos = new FileOutputStream(filePath);
        BufferedOutputStream out = new BufferedOutputStream(fos);
        byte[] buffer = new byte[1024];
        InputStream in = socket.getInputStream();
        while(in.read(buffer) >0){
            fos.write(buffer);
        }
        fos.close();
        socket.close();
        clientCustomerManager.setFileDownloaded(fileProduct.getProductId(),person.getUsername());
        updateShownProducts(clientCustomerManager.getPayedFileProducts(person.getUsername()));

    }

    private void sellerDownload(String filePath,String ip,int port,FileProduct fileProduct) throws Exception {
        File myFile = new File(filePath);
       if (!clientSellerManager.sendIPPort(ip, port, person.getUsername())){
           throw new Exception("unsuccessful operation!");
       }
        ServerSocket serverSocket = new ServerSocket(port);
        new SellerConnectionHandler(serverSocket,myFile).start();
    }
    private class SellerConnectionHandler extends Thread{
        ServerSocket serverSocket;
        File myFile;
        public SellerConnectionHandler(ServerSocket serverSocket,File myFile){
            this.serverSocket = serverSocket;
            this.myFile=myFile;
        }

        @Override
        public void run() {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("seller socket created!");
                Thread.sleep(500);
                updateShownProducts(clientSellerManager.getSoldFileProducts(person.getUsername()));
                OutputStream out = socket.getOutputStream();
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(myFile));
                int count;
                byte[] buffer = new byte[1024];
                while ((count = in.read(buffer)) > 0) {
                    out.write(buffer, 0, count);
                    out.flush();
                }
                socket.close();
                Thread.sleep(500);
                updateShownProducts(clientSellerManager.getSoldFileProducts(person.getUsername()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void openDownloadDialog(FileProduct fileProduct){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Download Manager");
        dialog.setHeaderText("File "+fileProduct.getName()+" Download");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField filePathField = new TextField();
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Enter file path :"), filePathField);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        try {
            if (person instanceof Seller){
                sellerDownload(filePathField.getText(),"localhost",9000,fileProduct);
            }else {
                customerDownload(filePathField.getText(),fileProduct.getSellerName(),fileProduct);
            }
        } catch (Exception e) {
            showError(e.getMessage(),20);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (person instanceof Seller){
            updateShownProducts(clientSellerManager.getSoldFileProducts(person.getUsername()));
        }else updateShownProducts(clientCustomerManager.getPayedFileProducts(person.getUsername()));
    }
}