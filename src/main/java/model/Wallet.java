package model;

public class Wallet {

    private double money;
    private String bankAccountUsername;
    private String bankAccountPassword;
    private String currentBankToken;

    public Wallet(double money, String bankAccountUsername, String bankAccountPassword, String currentBankToken) {
        this.money = money;
        this.bankAccountUsername = bankAccountUsername;
        this.bankAccountPassword = bankAccountPassword;
        this.currentBankToken = currentBankToken;
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
}
