package bank;

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

    public BankAccount(String firstName, String lastName, String userName, String passWord) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.passWord = passWord;
        value = 50.0;
        this.accountId = idSetter();
        allAccounts.add(this);
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
}
