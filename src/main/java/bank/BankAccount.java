package bank;

import java.util.ArrayList;
import java.util.HashMap;

public class BankAccount {
    String firstName;
    String lastName;
    String userName;
    String passWord;
    Double value;
    static ArrayList<BankAccount> allAccounts = new ArrayList<>();

    public BankAccount(String firstName, String lastName, String userName, String passWord) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.passWord = passWord;
        value = 50.0;
        allAccounts.add(this);
    }
}
