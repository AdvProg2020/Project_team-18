package model;

import java.util.HashMap;

public class FileProduct extends Product {
    private FileState fileState;
    public FileProduct(HashMap<String, String> information, Seller seller) {
        super(information, seller);
    }
}
