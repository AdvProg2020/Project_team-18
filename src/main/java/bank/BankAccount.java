package bank;

import com.gilecode.yagson.com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class BankAccount {
    String firstName;
    String lastName;
    String userName;
    String passWord;
    Double value;
    int accountId;
    static ArrayList<BankAccount> allAccounts = new ArrayList<>();
    ArrayList<Receipt> withdrawalTransactions;
    ArrayList<Receipt> depositTransactions;
    ArrayList<Receipt> allTransactions;

    public BankAccount(String firstName, String lastName, String userName, String passWord) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.passWord = passWord;
        if (userName.equals("shop") || userName.equals("temp"))
            value = 0.0;
        else {
            value = 50.0;
            allAccounts.get(0).setValue(allAccounts.get(0).getValue() + 50);
        }
        this.accountId = idSetter();
        if(!firstName.equals("temp"))
            allAccounts.add(this);
        withdrawalTransactions = new ArrayList<>();
        depositTransactions = new ArrayList<>();
        allTransactions = new ArrayList<>();
    }

    private int idSetter (){
        if (allAccounts.size() == 0){
            return 1;
        }
        int max = 0;
        for (BankAccount bankAccount : allAccounts){
            if (bankAccount.accountId>max)
                max = bankAccount.accountId;
        }
        return max+1;
    }

    public int getAccountId() {
        return accountId;
    }

    public BankAccount getAccountById(int id){
        for (BankAccount account : allAccounts) {
            if(account.getAccountId() == id)
                return account;
        }
        return null;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public String getUserName() {
        return userName;
    }

    public double getValueByUsername (String username) {
        for (BankAccount account : allAccounts) {
            if (account.getUserName().equals(username))
                return account.getValue();
        }
        return 0.0;
    }

    public void addWithdrawalTransaction (Receipt receipt) {
        this.withdrawalTransactions.add(receipt);
        this.allTransactions.add(receipt);
    }

    public void addDepositTransaction (Receipt receipt) {
        this.depositTransactions.add(receipt);
        this.allTransactions.add(receipt);
    }

    public BankAccount getAccountByUsername (String username) {
        for (BankAccount account : allAccounts) {
            if (account.getUserName().equals(username))
                return account ;
        }
        return null ;
    }

    public String getAllDepositTransactions() {
        return convertArrayListToGsonString(this.depositTransactions);
    }

    public String getAllWithdrawalTransactions() {
        return convertArrayListToGsonString(this.withdrawalTransactions);
    }

    public String getAllTransactions() {
        return convertArrayListToGsonString(this.allTransactions);
    }

    private String convertArrayListToGsonString (ArrayList<Receipt> receipts) {
        Gson gson = new Gson();
        String json = "";
        for (Receipt receipt : receipts) {
             json += gson.toJson(receipt);
             json += "*";
        }
        return json;
    }

    public ArrayList<BankAccount> getAllAccounts() {
        return allAccounts;
    }

    public void showAccounts () {
        System.out.println("*****");
        for (BankAccount account : allAccounts) {
            System.out.println(account.getUserName() + " " + account.getAccountId());
        }
        System.out.println("*****");
    }
}
