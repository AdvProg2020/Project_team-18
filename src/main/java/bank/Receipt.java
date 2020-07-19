package bank;

import model.Request;

import java.util.ArrayList;

public class Receipt {
    String token;
    String type;
    String sourceAccount;
    String destAccount;
    String money;
    String description;
    int receiptId;
    static ArrayList<Receipt> allReceipts = new ArrayList<>();

    public Receipt(String token, String type, String sourceAccount, String destAccount, String money, String description) {
        this.token = token;
        this.type = type;
        this.sourceAccount = sourceAccount;
        this.destAccount = destAccount;
        this.money = money;
        this.description = description;
        this.receiptId = idSetter();
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
}
