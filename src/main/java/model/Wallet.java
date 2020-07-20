package model;

public class Wallet {

    private double money;
    private String bankAccountUsername;
    private String bankAccountPassword;
    private String currentBankToken;
    private int accountId;

    public Wallet(double money, String bankAccountUsername, String bankAccountPassword, int id, String currentBankToken) {
        this.money = money;
        this.bankAccountUsername = bankAccountUsername;
        this.bankAccountPassword = bankAccountPassword;
        this.currentBankToken = currentBankToken;
        this.accountId = id;
    }

    public double getMoney() {
        return money;
    }

    public String getBankAccountUsername() {
        return bankAccountUsername;
    }

    public String getBankAccountPassword() {
        return bankAccountPassword;
    }

    public String getCurrentBankToken() {
        return currentBankToken;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
