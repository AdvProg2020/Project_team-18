package Server;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import Client.ClientManager;
import controller.FileSaver;
import controller.Storage;
import graphics.MainMenu;
import graphics.Menu;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Person;


public class ClientView extends Application {
    protected static Socket socket;
    private static OutputStream outputStream;
    private static InputStream inputStream;
    private static Token token;

    public static Token getToken() {
        return token;
    }

    public static void setToken(Token token) {
        ClientView.token = token;
    }

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
            socket = new Socket("localhost", 9090);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            ClientMessage.scanner = new Scanner(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainMenu mainMenu = new MainMenu(null);

        stage.setTitle("TEAM-18");
        mainMenu.run();
        stage.show();
        //commented temporarily!
        stage.setOnCloseRequest(event -> {
            ClientManager clientManager = new ClientManager();
            if (Menu.getPerson() != null) {
                System.out.println(Menu.getPerson().getUsername());
                clientManager.terminate(Menu.getPerson().getUsername());
            } else {
                clientManager.terminate("");
            }
        });
    }
}
