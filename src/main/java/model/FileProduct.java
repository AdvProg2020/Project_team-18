package model;

import java.util.HashMap;

public class FileProduct extends Product {
    private FileState fileState;
    public FileProduct(HashMap<String, String> information, Seller seller) {
        super(information, seller);
    }

    public FileState getFileState() {
        return fileState;
    }

    public void setFileState(FileState fileState) {
        this.fileState = fileState;
    }
}
