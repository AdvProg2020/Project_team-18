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

    public String compareTwoProducts(int firstProduct, int secondProduct) throws Exception {
        if (storage.getProductById(secondProduct) == null)
            throw new Exception("There is not a product with this Id!");
        else if (firstProduct == secondProduct)
            throw new Exception("These products are the same!");
        else if (!storage.getProductById(firstProduct).getCategory().getCategoryName().
                equals(storage.getProductById(secondProduct).getCategory().getCategoryName()))
            throw new Exception("These products are from different categories and can't be compared!");
        else {
            Product first = storage.getProductById(firstProduct);
            Product second = storage.getProductById(secondProduct);
            String output = "First Product Name : " + first.getName() +
                    " --- Second Product Name : " + second.getName() + "\n" + "First Product Price : " + first.getPrice()+
            " --- Second Product Price : " + second.getPrice() + "\n" + "First Product Seller Name : " +
                    first.getSeller().getName() + " " + first.getSeller().getFamilyName() + " --- " +
                    "Second Product Seller Name : " + second.getSeller().getName() + " " + second.getSeller().getFamilyName()
                    + "\n" + "First Product Average Rate : " + first.getAverageRate() + " --- Second Product Average" +
                    " Rate : " + second.getAverageRate() + "\n" + "First Product Brand : " + first.getBrand() +
                    " --- Second Product Brand : " + second.getBrand() + "\n" + "First Product Explanation: " +
                    first.getExplanation() + " --- Second Product Explanation : " + second.getExplanation() +
                    "\n" +"First Product Number Of Available Samples : " + first.getSupply() + " --- Second Product" +
                    " Number Of Available Samples : " + second.getSupply();
            return output;
        }
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
