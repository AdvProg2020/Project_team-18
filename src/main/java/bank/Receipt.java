package bank;

import java.util.ArrayList;

public class Receipt {
    String token;
    String receiptType;
    String sourceAccountID;
    String destAccountID;
    String money;
    String description;
    int receiptId;
    boolean paid;
    static ArrayList<Receipt> allReceipts = new ArrayList<>();

    public Receipt(String token, String type, String sourceAccountID, String destAccountID, String money, String description) {
        this.token = token;
        this.receiptType = type;
        this.sourceAccountID = sourceAccountID;
        this.destAccountID = destAccountID;
        this.money = money;
        this.description = description;
        this.receiptId = idSetter();
        this.paid = false;
        if (type != null)
            allReceipts.add(this);
    }

    private int idSetter (){
        if (allReceipts.size() == 0){
            return 1;
        }
        int max = 0;
        for (Receipt receipt : allReceipts){
            if (receipt.receiptId>max)
                max = receipt.receiptId;
        }
        return max+1;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public Receipt getReceiptById (int id) {
        for (Receipt receipt : allReceipts) {
            if(receipt.getReceiptId() == id)
                return receipt;
        }
        return null;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public String getToken() {
        return token;
    }

    public String getSourceAccountID() {
        return sourceAccountID;
    }

    public String getDestAccountID() {
        return destAccountID;
    }

    public String getMoney() {
        return money;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "token='" + token + '\'' +
                ", receiptType='" + receiptType + '\'' +
                ", sourceAccountID='" + sourceAccountID + '\'' +
                ", destAccountID='" + destAccountID + '\'' +
                ", money='" + money + '\'' +
                ", description='" + description + '\'' +
                ", receiptId=" + receiptId +
                ", paid=" + paid +
                '}';
    }
}
