package controller;

import com.google.gson.*;
import model.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class File_Saver {
    private GsonBuilder gsonBuilder = new GsonBuilder();
    public Gson getCustomeGson(){
        gsonBuilder.registerTypeAdapter(Admin.class,new AdminSerializer());
        gsonBuilder.registerTypeAdapter(Seller.class,new SellerSerializer());
        gsonBuilder.registerTypeAdapter(Customer.class,new CustomerSerializer());
        gsonBuilder.registerTypeAdapter(BuyLog.class,new BuyLogSerializer());
        gsonBuilder.registerTypeAdapter(SellLog.class,new SellLogSerializer());
        gsonBuilder.registerTypeAdapter(Category.class,new CategorySerializer());
        gsonBuilder.registerTypeAdapter(Comment.class,new CommentSerializer());
        gsonBuilder.registerTypeAdapter(Rate.class,new RateSerializer());
        gsonBuilder.registerTypeAdapter(Sale.class,new SaleSerializer());
        gsonBuilder.registerTypeAdapter(Product.class,new ProductSerializer());
        Gson customGson = gsonBuilder.create();
        return customGson;
    }
    public String test(Seller seller){
        return getCustomeGson().toJson(seller);
    }



}
class AdminSerializer implements JsonSerializer<Admin> {
    Gson gsonDefault = new Gson();
    private ArrayList<Integer> getAllRequestIds(ArrayList<Request> allRequestIds){
        ArrayList<Integer> temp = new ArrayList<>();
        for (Request request : allRequestIds){
            temp.add(request.getRequestId());
        }
        return temp;
    }
    @Override
    public JsonElement serialize(Admin admin, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement jsonElement = gsonDefault.toJsonTree(admin);
        JsonElement allRequests = gsonDefault.toJsonTree(getAllRequestIds(admin.getAllRequests()));
        jsonElement.getAsJsonObject().add("allRequests",allRequests);
        return jsonElement;
    }
}
class SellerSerializer implements JsonSerializer<Seller> {
    Gson gsonDefault = new Gson();
    private ArrayList<Integer> getAllProductId(ArrayList<Product> productsToSell){
        ArrayList<Integer> temp = new ArrayList<>();
        for (Product product : productsToSell){
            temp.add(product.getProductId());
        }
        return temp;
    }
    private ArrayList<Integer> getAllSaleId(ArrayList<Sale> saleList){
        ArrayList<Integer> temp = new ArrayList<>();
        for (Sale sale : saleList){
            temp.add(sale.getSaleId());
        }
        return temp;
    }

    @Override
    public JsonElement serialize(Seller seller, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement jsonElement = gsonDefault.toJsonTree(seller);
        JsonElement products = gsonDefault.toJsonTree(getAllProductId(seller.getProductsToSell()));
        JsonElement sales = gsonDefault.toJsonTree(getAllSaleId(seller.getSaleList()));
        jsonElement.getAsJsonObject().add("productsToSell",products);
        jsonElement.getAsJsonObject().add("saleList",sales);
        return jsonElement;
    }
}
class CustomerSerializer implements JsonSerializer<Customer> {
    Gson gsonDefault = new Gson();
    private ArrayList<Integer> getAllDiscountId(ArrayList<Discount> allDiscounts){
        ArrayList<Integer> temp = new ArrayList<>();
        for (Discount discount : allDiscounts){
            temp.add(discount.getDiscountId());
        }
        return temp;
    }
    private ArrayList<Integer> getAllBuyLogId(ArrayList<BuyLog> buyHistory){
        ArrayList<Integer> temp = new ArrayList<>();
        for (BuyLog buyLog : buyHistory){
            temp.add(buyLog.getBuyCode());
        }
        return temp;
    }

    @Override
    public JsonElement serialize(Customer customer, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement jsonElement = gsonDefault.toJsonTree(customer);
        JsonElement buyLog = gsonDefault.toJsonTree(getAllBuyLogId(customer.getBuyHistory()));
        JsonElement discount = gsonDefault.toJsonTree(getAllDiscountId(customer.getAllDiscounts()));
        jsonElement.getAsJsonObject().add("allDiscounts",discount);
        jsonElement.getAsJsonObject().add("buyHistory",buyLog);
        return jsonElement;
    }
}
class BuyLogSerializer implements JsonSerializer<BuyLog> {
    Gson gsonDefault = new Gson();
    private ArrayList<String> getAllSellerIds(ArrayList<Seller> sellers){
        ArrayList<String> temp = new ArrayList<>();
        for (Seller seller : sellers){
            temp.add(seller.getUsername());
        }
        return temp;
    }
    @Override
    public JsonElement serialize(BuyLog buyLog, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement jsonElement = gsonDefault.toJsonTree(buyLog);
        JsonElement allSeller = gsonDefault.toJsonTree(getAllSellerIds(buyLog.getSeller()));
        jsonElement.getAsJsonObject().add("seller",allSeller);
        return jsonElement;
    }
}
class SellLogSerializer implements JsonSerializer<SellLog> {
    Gson gsonDefault = new Gson();
    @Override
    public JsonElement serialize(SellLog sellLog, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement jsonElement = gsonDefault.toJsonTree(sellLog);
        jsonElement.getAsJsonObject().addProperty("customer",sellLog.getCustomer().getUsername());
        return jsonElement;
    }
}
class CategorySerializer implements JsonSerializer<Category> {
    Gson gsonDefault = new Gson();
    private ArrayList<Integer> getAllProductId(ArrayList<Product> products) {
        ArrayList<Integer> temp = new ArrayList<>();
        for (Product product : products) {
            temp.add(product.getProductId());
        }
        return temp;
    }
        @Override
    public JsonElement serialize(Category category, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement jsonElement = gsonDefault.toJsonTree(category);
        JsonElement products = gsonDefault.toJsonTree(getAllProductId(category.getThisCategoryProducts()));
        jsonElement.getAsJsonObject().add("thisCategoryProducts",products);
        return jsonElement;
    }
}
class CommentSerializer implements JsonSerializer<Comment> {
    Gson gsonDefault = new Gson();
    @Override
    public JsonElement serialize(Comment comment, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement jsonElement = gsonDefault.toJsonTree(comment);
        jsonElement.getAsJsonObject().addProperty("customer",comment.getProduct().getProductId());
        return jsonElement;
    }
}
class RateSerializer implements JsonSerializer<Rate> {
    Gson gsonDefault = new Gson();
    @Override
    public JsonElement serialize(Rate rate, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement jsonElement = gsonDefault.toJsonTree(rate);
        jsonElement.getAsJsonObject().addProperty("product",rate.getProduct().getProductId());
        return jsonElement;
    }
}
class SaleSerializer implements JsonSerializer<Sale> {
    Gson gsonDefault = new Gson();
    private ArrayList<Integer> getAllProductId(ArrayList<Product> products) {
        ArrayList<Integer> temp = new ArrayList<>();
        for (Product product : products) {
            temp.add(product.getProductId());
        }
        return temp;
    }
    @Override
    public JsonElement serialize(Sale sale, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement jsonElement = gsonDefault.toJsonTree(sale);
        JsonElement products = gsonDefault.toJsonTree(getAllProductId(sale.getProductsWithThisSale()));
        jsonElement.getAsJsonObject().add("productsWithThisSale",products);
        return jsonElement;
    }
}
class ProductSerializer implements JsonSerializer<Product> {
    Gson gsonDefault = new Gson();
    private ArrayList<String> getAllBuyerIds(ArrayList<Customer> customers){
        ArrayList<String> temp = new ArrayList<>();
        for (Customer customer : customers){
            temp.add(customer.getUsername());
        }
        return temp;
    }
    private ArrayList<Double> getAllRates(ArrayList<Rate> rates){
        ArrayList<Double> temp = new ArrayList<>();
        for (Rate rate : rates){
            temp.add(rate.getRate());
        }
        return temp;
    }
    private ArrayList<String> getAllCommentContents(ArrayList<Comment> comments){
        ArrayList<String> temp = new ArrayList<>();
        for (Comment comment : comments){
            temp.add(comment.getCommentBody());
        }
        return temp;
    }
    @Override
    public JsonElement serialize(Product product, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement jsonElement = gsonDefault.toJsonTree(product);
        jsonElement.getAsJsonObject().addProperty("seller",product.getSeller().getUsername());
        jsonElement.getAsJsonObject().addProperty("category",product.getCategory().getCategoryName());
        jsonElement.getAsJsonObject().addProperty("sale",product.getSale().getSaleId());
        JsonElement allBuyer = gsonDefault.toJsonTree(getAllBuyerIds(product.getThisProductsBuyers()));
        jsonElement.getAsJsonObject().add("buyers",allBuyer);
        JsonElement allRates = gsonDefault.toJsonTree(getAllRates(product.getRates()));
        jsonElement.getAsJsonObject().add("rates",allRates);
        JsonElement allComments = gsonDefault.toJsonTree(getAllCommentContents(product.getComments()));
        jsonElement.getAsJsonObject().add("comments",allComments);
        return jsonElement;
    }
}
//product
class AdminDeserializer implements JsonDeserializer<Admin>{

    @Override
    public Admin deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }
}


