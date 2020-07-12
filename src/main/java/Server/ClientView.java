package Server;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import controller.FileSaver;
import controller.Storage;
import graphics.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;


public class ClientView extends Application {
    protected static Socket socket;
    private static OutputStream outputStream;
    private static InputStream inputStream;

    public static Stage mainStage;

    public static Stage getStage() {
        return mainStage;
    }

    public static OutputStream getOutputStream() {
        return outputStream;
    }

    public static InputStream getInputStream() {
        return inputStream;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        try {
            socket = new Socket("localhost",9090);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainMenu mainMenu = new MainMenu(null);
        stage.setTitle("TEAM-18");
      /*  stage.setOnCloseRequest(event -> {
            FileSaver fileSaver = new FileSaver(Storage.getStorage());
            fileSaver.dataSaver();
        });*/
        mainMenu.run();
        stage.show();
    }
}
