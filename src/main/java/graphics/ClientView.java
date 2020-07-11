package graphics;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import controller.FileSaver;
import controller.Storage;
import javafx.application.Application;
import javafx.stage.Stage;


public class ClientView extends Application {
    protected static Socket socket;

    public static Stage mainStage;

    public static Stage getStage() {
        return mainStage;
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void main(String[] args) {
        launch(args);
        try {
             socket = new Socket("localhost",9090);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        MainMenu mainMenu = new MainMenu(null);
        stage.setTitle("TEAM-18");
        stage.setOnCloseRequest(event -> {
            FileSaver fileSaver = new FileSaver(Storage.getStorage());
            fileSaver.dataSaver();
        });
        mainMenu.run();
        stage.show();
    }
}
