package bank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;

public class BankClientHandler {

    public static void main(String[] args) {
        new BankImpl().run();
    }

    private static class BankImpl {

        private void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(8787);

                while (true) {
                    Socket clientSocket;
                    try {
                        clientSocket = serverSocket.accept();
                        System.out.println("client accepted");
                        DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
                        DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                        new ClientHandler(outputStream, inputStream).start();
                    } catch (Exception e) {
                        System.err.println("Error in accepting client!");
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static class ClientHandler extends Thread {
        DataOutputStream outputStream;
        DataInputStream inputStream;
        HashMap<String, String> allAccounts;
        HashMap<String, LocalDateTime> validTokens;
        private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
        private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

        public ClientHandler(DataOutputStream outputStream, DataInputStream inputStream) {
            this.outputStream = outputStream;
            this.inputStream = inputStream;
            allAccounts = new HashMap<>();
            validTokens = new HashMap<>();
        }

        private void handleClient() {
            String input = "";
            try {
                while (true) {
                    input = inputStream.readUTF();
                    System.out.println(input);
                    if (input.startsWith("create_account")) {
                         String[] inputs = input.split("\\s");
                        String firstName = inputs[1];
                        String lastName = inputs[2];
                        String username = inputs[3];
                        String password = inputs[4];
                        String repeatedPassword = inputs[5];
                        createAccount(firstName, lastName, username, password, repeatedPassword);
                        break;
                    } else if (input.startsWith("get_token")) {
                        String[] inputs = input.split("\\s");
                        String username = inputs[1];
                        String password = inputs[2];
                        createToken(username, password);
                        break;
                    } else if (input.startsWith("create_receipt")) {
                        String[] inputs = input.split("\\s");
                        String token = inputs[1];
                        String receiptType = inputs[2];
                        String money = inputs[3];
                        String sourceID = inputs[4];
                        String destID = inputs[5];
                        String description = inputs[6];
                        interpret(token,receiptType,money,sourceID,destID,description);
                    } else if (input.startsWith("get_transaction")) {

                    } else if (input.startsWith("pay")) {

                    } else if (input.startsWith("get_balance")) {

                    } else if (input.startsWith("exit")) {

                    } else {

                    }
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        @Override
        public void run() {
            handleClient();
        }

        private void createAccount(String firstName, String lastName, String username, String password, String repeat) {
            try {
                if (!password.equals(repeat)) {
                    outputStream.writeUTF("Passwords do not match");
                    outputStream.flush();
                } else if (allAccounts.containsKey(username)) {
                    outputStream.writeUTF("Username is not available");
                    outputStream.flush();
                } else {
                    new BankAccount(firstName, lastName, username, password);
                    allAccounts.put(username, password);
                    outputStream.writeUTF("Done");
                    outputStream.flush();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        private void createToken(String username, String password) {
            try {
                if (!allAccounts.containsKey(username)) {
                    outputStream.writeUTF("Username is wrong");
                    outputStream.flush();
                }
                else if (!allAccounts.get(username).equals(password)){
                    outputStream.writeUTF("Password is wrong");
                    outputStream.flush();
                }
                else {
                    String token = "";
                    byte[] randomBytes = new byte[24];
                    secureRandom.nextBytes(randomBytes);
                    token = base64Encoder.encodeToString(randomBytes);
                    validTokens.put(token,LocalDateTime.now());
                    outputStream.writeUTF(token);
                    outputStream.flush();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        private void interpret(String token, String type, String money, String source, String dest, String description){
            if(type.equals("deposit"))
                deposit(token,money,source,dest,description);
            else if (type.equals("withdraw"))
                withdraw(token,money,source,dest,description);
            else if (type.equals("move"))
                move(token,money,source,dest,description);
            else {
                try{
                    outputStream.writeUTF("Invalid receipt type");
                    outputStream.flush();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }

        private void deposit(String token, String money, String source, String dest, String description){
            try {
                if (!allAccounts.containsKey(dest)) {
                    outputStream.writeUTF("dest account id is invalid");
                    outputStream.flush();
                } else if (!validTokens.containsKey(token)) {
                    outputStream.writeUTF("token is invalid");
                    outputStream.flush();
                } else if (isTokenExpired(token)) {
                    outputStream.writeUTF("token expired");
                    outputStream.flush();
                } else {
                    Receipt receipt = new Receipt(token,"deposit",source,dest,money,description);
                    String id = Integer.toString(receipt.getReceiptId());
                    outputStream.writeUTF(id);
                    outputStream.flush();
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        private void withdraw(String token, String money, String source, String dest, String description){
            try {
                if (!allAccounts.containsKey(source)) {
                    outputStream.writeUTF("source account id is invalid");
                    outputStream.flush();
                } else if (!validTokens.containsKey(token)) {
                    outputStream.writeUTF("token is invalid");
                    outputStream.flush();
                } else if (isTokenExpired(token)) {
                    outputStream.writeUTF("token expired");
                    outputStream.flush();
                } else {
                    Receipt receipt = new Receipt(token,"withdraw",source,dest,money,description);
                    String id = Integer.toString(receipt.getReceiptId());
                    outputStream.writeUTF(id);
                    outputStream.flush();
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        private void move(String token, String money, String source, String dest, String description){
            try {
                 if (!validTokens.containsKey(token)) {
                    outputStream.writeUTF("token is invalid");
                    outputStream.flush();
                } else if (isTokenExpired(token)) {
                     outputStream.writeUTF("token expired");
                     outputStream.flush();
                 } else {
                     Receipt receipt = new Receipt(token,"move",source,dest,money,description);
                     String id = Integer.toString(receipt.getReceiptId());
                     outputStream.writeUTF(id);
                     outputStream.flush();
                 }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        private boolean isTokenExpired (String token){
            LocalDateTime fromDateTime = validTokens.get(token);
            LocalDateTime toDateTime = LocalDateTime.now();

            LocalDateTime tempDateTime = LocalDateTime.from( fromDateTime );

            long years = tempDateTime.until( toDateTime, ChronoUnit.YEARS );
            tempDateTime = tempDateTime.plusYears( years );

            long months = tempDateTime.until( toDateTime, ChronoUnit.MONTHS );
            tempDateTime = tempDateTime.plusMonths( months );

            long days = tempDateTime.until( toDateTime, ChronoUnit.DAYS );
            tempDateTime = tempDateTime.plusDays( days );


            long hours = tempDateTime.until( toDateTime, ChronoUnit.HOURS );

            if (years == 0 && months == 0 && days == 0 && hours < 1)
                return false;
            else return true;
        }
    }
}
