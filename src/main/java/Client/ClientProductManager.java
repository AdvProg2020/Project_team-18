package Client;

import Server.ClientMessage;
import Server.MessageType;
import Server.ServerMessage;
import model.Category;
import model.Comment;
import model.Product;
import model.Request;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientProductManager extends ClientManager{

    public ArrayList<Comment> showComments(int productId) {
        return storage.getProductById(productId).getComments();
    }

    public void addComment(int productId, String title, String content, String username) throws Exception{
        ArrayList <Object> params = new ArrayList<>();
        params.add(productId);
        params.add(title);
        params.add(content);
        params.add(username);
        ClientMessage clientMessage = new ClientMessage(MessageType.ADD_COMMENT,params);
        ServerMessage serverMessage = clientMessage.sendAndReceive();
        if (serverMessage != null && serverMessage.getMessageType()==MessageType.ERROR){
            throw  (Exception)serverMessage.getResult();
        }
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
        ClientMessage clientMessage = new ClientMessage(MessageType.GET_ALL_PRODUCTS, null);
        return (ArrayList<Product>) clientMessage.sendAndReceive().getResult();
    }

    public HashMap<String, String> viewAttributes(String categoryName) {
        return storage.getCategoryByName(categoryName).getProperties();
    }

    public ArrayList<Product> viewAllProductsWithSale() {
       ClientMessage clientMessage = new ClientMessage(MessageType.GET_ALL_PRODUCTS_IN_SALE, null);
       return (ArrayList<Product>) clientMessage.sendAndReceive().getResult();
    }

    public void addNumberOfSeen(int productId){
        Product seenProduct = storage.getProductById(productId);
        seenProduct.setNumberOfSeen(seenProduct.getNumberOfSeen() + 1);
    }

    public ArrayList<Category> viewAllCategories(){
        ClientMessage clientMessage = new ClientMessage(MessageType.VIEW_ALL_CATEGORIES, null);
        return (ArrayList<Category>) clientMessage.sendAndReceive().getResult();
    }
}
