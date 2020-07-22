package controller;

import model.Comment;
import model.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductManager extends Manager {

    public ArrayList<Comment> showComments(int productId) {
        return storage.getProductById(productId).getComments();
    }

    public void addComment(int productId, String title, String content, String username) {
        HashMap<String,String> information = new HashMap<>();
        information.put("productId",Integer.toString(productId));
        information.put("title",title);
        information.put("content",content);
        information.put("username",username);
        storage.addRequest(new Request("add comment",information));
    }

    public ArrayList<Product> viewAllProducts() {
        return storage.getAllProducts();
    }

    public HashMap<String, String> viewAttributes(String categoryName) {
        return storage.getCategoryByName(categoryName).getProperties();
    }

    public ArrayList<Product> viewAllProductsWithSale() {
        ArrayList<Product> finalProducts = new ArrayList<>();
        for (Product product : storage.getAllProducts()) {
            if (product.getSale() != null) {
                finalProducts.add(product);
            }
        }
        return finalProducts;
    }

    public void addNumberOfSeen(int productId){
        Product seenProduct = storage.getProductById(productId);
        seenProduct.setNumberOfSeen(seenProduct.getNumberOfSeen() + 1);
    }
}
