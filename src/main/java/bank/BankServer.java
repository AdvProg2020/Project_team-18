package bank;

import Server.Token;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

public class BankServer {

    public static void main(String[] args) {
        new BankImpl().run();
    }

    private static class BankImpl {

        private void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(8787);
                BankAccount shop;
                File file = new File("./bankDataBase/allBankAccounts.json");
                if (!file.exists()) {
                     shop = new BankAccount("shop", "shop", "shop", "shop");
                } else {
                    BankAccount bankAccount = new BankAccount("temp","temp","temp","temp");
                    shop = bankAccount.getAccountByUsername("shop");
                }
                while (true) {
                    Socket clientSocket;
                    try {
                        clientSocket = serverSocket.accept();
                        System.out.println("client accepted");
                        DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
                        DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                        new ClientHandler(outputStream, inputStream , clientSocket , shop).start();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
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
        BankAccount shoppingCenter;
        Socket clientSocket;
        Token token;
        HashMap<String, String> allAccounts;
        HashMap<String, LocalDateTime> validTokens;
        HashMap<String,String> tokenPerAccount;
        ArrayList<Integer> allAccountIds;
        private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
        private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

        public ClientHandler(DataOutputStream outputStream, DataInputStream inputStream , Socket clientSocket , BankAccount shop) {
            this.outputStream = outputStream;
            this.inputStream = inputStream;
            this.clientSocket = clientSocket;
            this.shoppingCenter = shop;
            allAccounts = new HashMap<>();
            validTokens = new HashMap<>();
            tokenPerAccount = new HashMap<>();
            allAccountIds = new ArrayList<>();
            BankFileSavor bankFileSavor = new BankFileSavor(allAccounts,allAccountIds);
            File file = new File("./bankDataBase/allBankAccounts.json");
            if (file.exists()) {
                allAccountIds = bankFileSavor.readAllAccountIds();
                allAccounts = bankFileSavor.readAllAccounts();
                bankFileSavor.dataReader();
            } else {
                allAccounts.put("shop", "shop");
                allAccountIds.add(1);
            }
            System.out.println(allAccountIds.toString());
            System.out.println(allAccounts.toString());
        }

        private void handleClient() {
            String input = "";
            try {
                while (!input.equals("exit")) {
                    input = inputStream.readUTF();
                    System.out.println(input);
                    if (input.startsWith("create_account")) {
                         String[] inputs = input.split("\\s");
                        if (inputs.length != 6 ) {
                            outputStream.writeUTF("Invalid Input");
                            outputStream.flush();
                        }
                        String firstName = inputs[1];
                        String lastName = inputs[2];
                        String username = inputs[3];
                        String password = inputs[4];
                        String repeatedPassword = inputs[5];
                        createAccount(firstName, lastName, username, password, repeatedPassword);
                    } else if (input.startsWith("get_token")) {
                        String[] inputs = input.split("\\s");
                        if (inputs.length != 3 ) {
                            outputStream.writeUTF("Invalid Input");
                            outputStream.flush();
                        }
                        String username = inputs[1];
                        String password = inputs[2];
                        createToken(username, password);
                    } else if (input.startsWith("create_receipt")) {
                        String[] inputs = input.split("\\s");
                        if (inputs.length != 7 ) {
                            outputStream.writeUTF("Invalid Parameters Passed");
                            outputStream.flush();
                        }
                        String token = inputs[1];
                        String receiptType = inputs[2];
                        String money = inputs[3];
                        String sourceID = inputs[4];
                        String destID = inputs[5];
                        String description = inputs[6];
                        interpret(token,receiptType,money,sourceID,destID,description);
                    } else if (input.startsWith("get_transaction")) {
                        String[] inputs = input.split("\\s");
                        if (inputs.length != 3 ) {
                            outputStream.writeUTF("Invalid Input");
                            outputStream.flush();
                        }
                        String token = inputs[1];
                        String type = inputs[2];
                        getTransactions(token,type);
                    } else if (input.startsWith("pay")) {
                        String[] inputs = input.split("\\s");
                        if (inputs.length != 2 ) {
                            outputStream.writeUTF("Invalid Input");
                            outputStream.flush();
                        }
                        String receiptId = inputs[1];
                        performPayment(receiptId);
                    } else if (input.startsWith("get_balance")) {
                        String[] inputs = input.split("\\s");
                        if (inputs.length != 2 ) {
                            outputStream.writeUTF("Invalid Input");
                            outputStream.flush();
                        }
                        String token = inputs[1];
                        getBalanceByToken(token);
                    } else if (input.equals("terminate")) {
                        System.out.println(allAccounts.keySet().toString());
                        BankFileSavor bankFileSavor = new BankFileSavor(this.allAccounts,this.allAccountIds);
                        bankFileSavor.dataSavor();
                    } else if (input.startsWith("exit")) {
                        outputStream.writeUTF("Successfully Logged out!");
                        outputStream.flush();
                        clientSocket.close();
                        System.out.println("Connection closed!!!");
                        break;
                    }
                }
            } catch (Exception e) {
                try {
                    outputStream.writeUTF("Database Error");
                    outputStream.flush();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
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
                    BankAccount bankAccount = new BankAccount(firstName, lastName, username, password);
                    allAccounts.put(username, password);
                    allAccountIds.add(bankAccount.getAccountId());
                    outputStream.writeUTF("Done " + bankAccount.getAccountId());
                    outputStream.flush();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        private void createToken(String username, String password) {
            try {
                if (!allAccounts.containsKey(username)) {
                    outputStream.writeUTF("Invalid Username");
                    outputStream.flush();
                }
                else if (!allAccounts.get(username).equals(password)){
                    outputStream.writeUTF("Invalid Password");
                    outputStream.flush();
                }
                else {
                    token = new Token(1800000);
                    token.setDisposable();
                    validTokens.put(token.getJWS(),LocalDateTime.now());
                    tokenPerAccount.put(token.getJWS(),username);
                    outputStream.writeUTF(token.getJWS());
                    outputStream.flush();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        private void interpret(String token, String type, String money, String source, String dest, String description){
            if(type.equals("deposit")) {
                if (!isMoneyValidInteger(money)) {
                    try {
                        outputStream.writeUTF("Invalid money");
                        outputStream.flush();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                deposit(token, money, source, dest, description);
            }
            else if (type.equals("withdraw")) {
                withdraw(token, money, source, dest, description);
            }
            else if (type.equals("move")) {
                move(token, money, source, dest, description);
            }
            else {
                try{
                    outputStream.writeUTF("Invalid receipt type");
                    outputStream.flush();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }

        private boolean isMoneyValidInteger (String input) {
            try
            {
                Integer.parseInt(input);
                return true;
            }
            catch (NumberFormatException e)
            {
               return false;
            }
        }

        private void deposit(String token, String money, String source, String dest, String description){
            try {
                if (dest.equals("-1")){
                    outputStream.writeUTF("Invalid Account Id");
                    outputStream.flush();
                }
                else if (!allAccountIds.contains(Integer.parseInt(dest))) {
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
                if (source.equals("-1")){
                    outputStream.writeUTF("Invalid Account Id");
                    outputStream.flush();
                }
                else if (!allAccountIds.contains(Integer.parseInt(source))) {
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
                if (dest.equals("-1") || source.equals("-1")){
                    outputStream.writeUTF("Invalid Account Id");
                    outputStream.flush();
                }
                 else if (!validTokens.containsKey(token)) {
                    outputStream.writeUTF("token is invalid");
                    outputStream.flush();
                } else if (isTokenExpired(token)) {
                     outputStream.writeUTF("token expired");
                     outputStream.flush();
                 } else if (source.equals(dest)){
                     outputStream.writeUTF("equal source and dest account");
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

        private void performPayment(String receiptId) {
            Receipt receipt = new Receipt("temp","temp","temp","temp","temp","temp");
            BankAccount bankAccount = new BankAccount("temp","temp","temp","temp");
            Receipt toBePaid = receipt.getReceiptById(Integer.parseInt(receiptId));
                try {
                    if (toBePaid == null) {
                        outputStream.writeUTF("invalid receipt id");
                        outputStream.flush();
                        return;
                    } else if (toBePaid.isPaid()) {
                        outputStream.writeUTF("receipt is paid before");
                        outputStream.flush();
                        return;
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            assert toBePaid != null;
            if (toBePaid.getReceiptType().equals("deposit"))
                performDepositPayment(toBePaid,bankAccount);
            else if (toBePaid.getReceiptType().equals("withdraw"))
                performWithdrawPayment(toBePaid,bankAccount);
            else
                performMovePayment(toBePaid,bankAccount);
        }

        private void performDepositPayment(Receipt receipt , BankAccount bankAccount) {
            int dest = Integer.parseInt(receipt.getDestAccountID());
            BankAccount destination = bankAccount.getAccountById(dest);
            destination.setValue(destination.getValue() + Double.parseDouble(receipt.getMoney()));
            destination.addDepositTransaction(receipt);
            receipt.setPaid(true);
            try {
                outputStream.writeUTF("done deposit payment ");
                outputStream.flush();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        private void performWithdrawPayment(Receipt receipt , BankAccount bankAccount) {
            int src = Integer.parseInt(receipt.getSourceAccountID());
            BankAccount source = bankAccount.getAccountById(src);
            if (source.getValue() > Double.parseDouble(receipt.getMoney())) {
                source.setValue(source.getValue() - Double.parseDouble(receipt.getMoney()));
                source.addWithdrawalTransaction(receipt);
                receipt.setPaid(true);
                try {
                    outputStream.writeUTF("done withdraw payment ");
                    outputStream.flush();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                try {
                    outputStream.writeUTF("not enough money in source account");
                    outputStream.flush();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        private void performMovePayment(Receipt receipt, BankAccount bankAccount) {
            int dest = Integer.parseInt(receipt.getDestAccountID());
            int src = Integer.parseInt(receipt.getSourceAccountID());
            BankAccount destination = bankAccount.getAccountById(dest);
            System.out.println(destination.getUserName());
            BankAccount source = bankAccount.getAccountById(src);
            if (source.getValue() > Double.parseDouble(receipt.getMoney())) {
                source.setValue(source.getValue() - Double.parseDouble(receipt.getMoney()));
                destination.setValue(destination.getValue() + Double.parseDouble(receipt.getMoney()));
                source.addWithdrawalTransaction(receipt);
                destination.addDepositTransaction(receipt);
                receipt.setPaid(true);
                try {
                    outputStream.writeUTF("done move payment ");
                    outputStream.flush();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                try {
                    outputStream.writeUTF("not enough money in source account");
                    outputStream.flush();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        private void getBalanceByToken(String token) {
            try {
            if (!validTokens.containsKey(token)) {
                outputStream.writeUTF("token is invalid");
                outputStream.flush();
            } else if (isTokenExpired(token)) {
                outputStream.writeUTF("token expired");
                outputStream.flush();
            }
            String username = tokenPerAccount.get(token);
            BankAccount temp = new BankAccount("temp","temp","temp","temp");
            String balance = Double.toString(temp.getValueByUsername(username));
                outputStream.writeUTF(balance);
                outputStream.flush();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        private void getTransactions (String token, String type) {
            try {
                if (!validTokens.containsKey(token)) {
                    outputStream.writeUTF("token is invalid");
                    outputStream.flush();
                    return;
                } else if (isTokenExpired(token)) {
                    outputStream.writeUTF("token expired");
                    outputStream.flush();
                    return;
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            String username = tokenPerAccount.get(token);
            BankAccount temp = new BankAccount("temp","temp","temp","temp");
            Receipt receipt = new Receipt("temp","temp","temp","temp","temp","temp");
            BankAccount account = temp.getAccountByUsername(username);
            try {
                if (type.equals("+")) {
                    outputStream.writeUTF(account.getAllDepositTransactions());
                    outputStream.flush();
                }
                else if (type.equals("-")) {
                    outputStream.writeUTF(account.getAllWithdrawalTransactions());
                    outputStream.flush();
                }
                else if (type.equals("*")) {
                    outputStream.writeUTF(account.getAllTransactions());
                    outputStream.flush();
                }
                else {
                    Receipt desiredReceipt = receipt.getReceiptById(Integer.parseInt(type));
                    if (desiredReceipt == null) {
                        outputStream.writeUTF("invalid receipt id");
                        outputStream.flush();
                    } else {
                        outputStream.writeUTF(desiredReceipt.toString());
                        outputStream.flush();
                    }
                }
            } catch (IOException e){
                e.getMessage();
            }
        }
    }
}
