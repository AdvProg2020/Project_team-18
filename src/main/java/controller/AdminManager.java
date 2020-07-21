package controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import model.*;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class AdminManager extends Manager {

    private SellerManager tempSellerManager = new SellerManager();
    private static int lastAwardedIndex = 1;
    private static int lastRandomIndex = 1;
    public AdminManager() {
    }

    public ArrayList<Person> viewAllUsers (){
        return storage.getAllUsers();
    }

    public ArrayList<BuyLog> viewAllBuyLogs(){
        ArrayList<BuyLog> allBuyLogs = new ArrayList<>();
        for (Log log : storage.getAllBuyLogs()) {
            allBuyLogs.add((BuyLog) log);
        }
        return allBuyLogs;
    }

    public Person viewUser (String username) throws Exception {
        if (storage.getUserByUsername(username) == null)
            throw new Exception("There is not such user!!");
        else
        return storage.getUserByUsername(username);
    }

    public void deleteUser (String username) throws Exception {
        if (storage.getUserByUsername(username) == null)
            throw new Exception("There is not such user!!");
        else
        storage.deleteUser(storage.getUserByUsername(username));
    }

    public void createManager (HashMap<String,String> information) throws Exception {
        if (!checkValidity(information.get("username")))
            throw new Exception("Username is not valid!!");
        else if (!checkValidity(information.get("password")))
            throw new Exception("Password is not valid!!");
        else if (!checkEmailValidity(information.get("email")))
            throw new Exception("Email is not valid");
        else if (!checkPhoneNumberValidity(information.get("number")))
            throw new Exception("Phone Number is not valid");
        else
            storage.addUser(new Admin (information));
    }

    public void removeProduct (String productId) throws Exception {
        if(storage.getProductById(Integer.parseInt(productId)) == null)
            throw new Exception("There is not such product!!");
        else {
            Sale sale = null;
            Product removedProduct = storage.getProductById(Integer.parseInt(productId));
            storage.deleteProduct(removedProduct);
            removedProduct.getSeller().removeProduct(removedProduct);
            sale.removeProductFromItSale(storage.getAllSales(),removedProduct);
        }
    }

    public void addCategory (String name,String imageOption) throws Exception {
        if (storage.getCategoryByName(name) != null)
            throw new Exception("Category with this name already exists!!");
        else
            storage.addCategory(new Category(name,imageOption));
    }

    public ArrayList<Discount> viewAllDiscountCodes (){
        return storage.getAllDiscounts();
    }

    public ArrayList<Request> viewAllRequests (){
        return storage.getAllRequests();
    }

    public Discount viewDiscountCode (String code) throws Exception{
        if (storage.getDiscountByCode(code) == null)
            throw new Exception("There is no such discount");
        return storage.getDiscountByCode(code);
    }

    public void addCustomerToDiscount(String username , Discount discount) throws Exception {
        if (storage.getUserByUsername(username) == null)
            throw new Exception("There is not a user with this username!");
        else if (discount.getCustomersWithThisDiscount().containsKey(storage.getUserByUsername(username)))
            throw new Exception("This customer is already added to this discount!");
        else {
            discount.addCustomer((Customer) storage.getUserByUsername(username));
            ((Customer) storage.getUserByUsername(username)).addToAllDiscounts(discount);
        }
    }

    public void removeCustomerFromDiscount (Discount discount , String username) throws Exception {
        if (storage.getUserByUsername(username) == null)
            throw new Exception("There is not a user with this username!");
        else if (!discount.getCustomersWithThisDiscount().containsKey(storage.getUserByUsername(username)))
            throw new Exception("This customer does not have this discount!!");
        else
            discount.removeCustomer((Customer) storage.getUserByUsername(username),discount.getUsagePerCustomer());
    }

    public void editDiscountField ( Discount discount,String field , String updatedVersion ){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm");
        switch (field){
            case "percentage" :
                discount.setPercentage(Integer.parseInt(updatedVersion));
                return;
            case "usagePerCustomer":
                discount.setUsageCount(Integer.parseInt(updatedVersion));
                return;
            case "beginDate":
                LocalDateTime beginDate = LocalDateTime.parse(updatedVersion,formatter);
                discount.setBeginDate(beginDate);
                return;
            case "endDate":
                LocalDateTime endDate = LocalDateTime.parse(updatedVersion,formatter);
                discount.setEndDate(endDate);
                return;
            case "maxAmount":
                discount.setMaxAmount(Double.parseDouble(updatedVersion));
                return;
        }
    }

    public void createDiscountCode (String code, LocalDateTime startDate, LocalDateTime endDate,
                                    int percentage, int usagePerCustomer, double maxAmount){
        storage.addDiscount(new Discount(code,startDate,endDate,percentage,usagePerCustomer,maxAmount));
    }

    public void removeDiscountCode (String code) throws Exception {
        if (storage.getDiscountByCode(code) == null)
            throw new Exception("There is not such Discount Code!!");
        else
        storage.deleteDiscount(storage.getDiscountByCode(code));
    }

    public Request viewRequest (String requestId) throws Exception {
        if (storage.getRequestById(Integer.parseInt(requestId)) == null)
            throw new Exception("There is not such request!!");
        else
        return storage.getRequestById(Integer.parseInt(requestId));
    }

    public void removeCategory (String name) throws Exception {
        if (storage.getCategoryByName(name) == null)
            throw new Exception("There is not a category with this name!!");
        else
        storage.deleteCategory(storage.getCategoryByName(name));
    }

    public void editCategoryByName (String oldName , String newName){
        storage.getCategoryByName(oldName).setCategoryName(newName);
    }

    public void editCategoryByProperties (Category category ,String property, String newValue){
        if(!category.getProperties().containsKey(property))
            category.addNewProperty(property,newValue);
        else
            category.setSingleValueInProperties(property,newValue);
    }

    public void acceptRequest (String requestId) throws Exception {
        if(storage.getRequestById(Integer.parseInt(requestId)) == null)
            throw new Exception("There is not a request with this Id!!");
        else {
            storage.getRequestById(Integer.parseInt(requestId)).acceptRequest();
            processAcceptedRequest(storage.getRequestById(Integer.parseInt(requestId)));
        }
    }

    public void declineRequest (String requestId) throws Exception {
        System.out.println("in adminManager accept request");
        if(storage.getRequestById(Integer.parseInt(requestId)) == null)
            throw new Exception("There is not a request with this Id!!");
        else
        storage.getRequestById(Integer.parseInt(requestId)).declineRequest();
    }

    public void processAcceptedRequest (Request request){
        switch (request.getTypeOfRequest()) {
            case REGISTER_SELLER:
                storage.addUser(new Seller(request.getInformation()));
                return;
            case ADD_PRODUCT:
                Seller seller = (Seller) storage.getUserByUsername(request.getInformation().get("seller"));
                if (request.getInformation().get("categoryName").equals("file")){
                    FileProduct fileProduct = new FileProduct(request.getInformation(),seller);
                    storage.addProduct(fileProduct);
                    seller.addProduct(fileProduct);
                }else {
                    Product product = new Product(request.getInformation(),seller);
                    storage.addProduct(product);
                    seller.addProduct(product);
                }
                return;
            case ADD_SALE:
                addSaleRequest(request);
                return;
            case EDIT_PRODUCT:
                String productField = request.getInformation().get("field");
                String productUpdatedVersion = request.getInformation().get("updatedVersion");
                String productId = request.getInformation().get("productId");
                editProduct(productId,productField,productUpdatedVersion);
                return;
            case EDIT_SALE:
                String saleField = request.getInformation().get("field");
                String saleUpdatedVersion = request.getInformation().get("updatedVersion");
                int saleId = Integer.parseInt(request.getInformation().get("offId"));
                editSale(saleId,saleField,saleUpdatedVersion);
                return;
            case REMOVE_PRODUCT:
                Product removedProduct = storage.getProductById(Integer.parseInt(request.getInformation().get("productId")));
                storage.deleteProduct(removedProduct);
                ((Seller)storage.getUserByUsername(request.getInformation().get("seller"))).removeProduct(removedProduct);
                Sale.removeProductFromItSale(storage.getAllSales(),removedProduct);
                return;
            case ADD_COMMENT:
                addCommentRequest(request);
                return;
            case ADD_PRODUCT_TO_SALE:
                addProductToSAleRequest(request);
                return;
            case REMOVE_PRODUCT_FROM_SALE:
                removeProductFromSaleRequest(request);
            case ADD_AUCTION:
                System.out.println("before adding");
                addAuction(request);
                System.out.println("helloooo");
        }
    }

    public void editProduct (String productId, String field, String updatedVersion){
        if (field.equalsIgnoreCase("name"))
            storage.getProductById(Integer.parseInt(productId)).setName(updatedVersion);
        else if (field.equalsIgnoreCase("brand"))
            storage.getProductById(Integer.parseInt(productId)).setBrand(updatedVersion);
        else if (field.equalsIgnoreCase("price"))
            storage.getProductById(Integer.parseInt(productId)).setPrice(Double.parseDouble(updatedVersion));
        else if (field.equalsIgnoreCase("explanation"))
            storage.getProductById(Integer.parseInt(productId)).setExplanation(updatedVersion);
        else if (field.equalsIgnoreCase("supply"))
            storage.getProductById(Integer.parseInt(productId)).setSupply(Integer.parseInt(updatedVersion));
        else if (field.equalsIgnoreCase("category"))
            storage.getProductById(Integer.parseInt(productId)).setCategory(Category.getCategoryByName(updatedVersion));
    }

    public void editSale (int offId, String field, String updatedVersion){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm");
        System.out.println(updatedVersion);
        if (field.equalsIgnoreCase("beginDate"))
            storage.getSaleById(offId).setBeginDate(LocalDateTime.parse(updatedVersion, formatter));
        if (field.equalsIgnoreCase("endDate"))
            storage.getSaleById(offId).setEndDate(LocalDateTime.parse(updatedVersion, formatter));
        if (field.equalsIgnoreCase("amountOfSale"))
            storage.getSaleById(offId).setAmountOfSale(Integer.parseInt(updatedVersion));
    }

    private void addSaleRequest (Request request){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm");
        LocalDateTime beginDate = LocalDateTime.parse(request.getInformation().get("beginDate"),formatter);
        LocalDateTime endDate = LocalDateTime.parse(request.getInformation().get("endDate"),formatter);
        int amountOfOff = Integer.parseInt(request.getInformation().get("amountOfSale"));
        int offId = Integer.parseInt(request.getInformation().get("offId"));
        Sale sale = new Sale(beginDate,endDate,amountOfOff,tempSellerManager.getSavedProductsInSale().get(offId));
        storage.addSale(sale);
        ((Seller)storage.getUserByUsername(request.getInformation().get("seller"))).addSale(sale);
        for (Product product : tempSellerManager.getSavedProductsInSale().get(offId)) {
            product.setSale(sale);
        }
    }

    private void addProductToSAleRequest (Request request){
        int addedProductToSAle = Integer.parseInt(request.getInformation().get("productId"));
        int saleIdToBeAdded = Integer.parseInt(request.getInformation().get("offId"));
        storage.getProductById(addedProductToSAle).setSale(storage.getSaleById(saleIdToBeAdded));
        storage.getSaleById(saleIdToBeAdded).addProductToThisSale(storage.getProductById(addedProductToSAle));
    }

    private void removeProductFromSaleRequest(Request request) {
        int removedProductFromSale = Integer.parseInt(request.getInformation().get("productId"));
        int saleIdToBeRemoved = Integer.parseInt(request.getInformation().get("offId"));
        if (storage.getProductById(removedProductFromSale).getSale() == storage.getSaleById(saleIdToBeRemoved)) {
            storage.getProductById(removedProductFromSale).setSale(null);
        }
        storage.getSaleById(saleIdToBeRemoved).removeProductFromThisSale(storage.getProductById(removedProductFromSale));
    }

    public void addCommentRequest (Request request){
        int productIdForComment = Integer.parseInt(request.getInformation().get("productId"));
        String title = request.getInformation().get("title");
        String content = request.getInformation().get("content");
        String username = request.getInformation().get("username");
        Comment comment = new Comment(username,storage.getProductById(productIdForComment),title,content);
        storage.addComment(comment);
        storage.getProductById(productIdForComment).addComment(comment);
    }

    public void getDiscountAwarded() throws Exception {
        LocalDateTime endDate = LocalDateTime.of(2021, 01, 01, 12, 30);
        Discount discount = new Discount(("Award" + lastAwardedIndex), LocalDateTime.now(), endDate, 10,
                3, 100);
        storage.addDiscount(discount);
        lastAwardedIndex++;
        addCustomerToDiscount(person.getUsername(), discount);
    }

    public void createRandomDiscounts() throws Exception {
        long minDay = LocalDate.of(2020, 5, 20).toEpochDay();
        long maxDay = LocalDate.of(2021, 5, 20).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        LocalDateTime beginDate = randomDate.atTime(8,00);
        LocalDateTime endDate = randomDate.atTime(20,00);
        Discount randomDiscount = new Discount(("Random" + lastRandomIndex),beginDate,endDate,20,
                2,100);
        storage.addDiscount(randomDiscount);
        addCustomerToDiscount(person.getUsername(), randomDiscount);
    }

    public void sendPurchase(String buyCode) throws Exception {
        if(storage.getBuyLogByCode(buyCode) == null)
            throw new Exception("There is not a buyLog with this Id!!");
        else
            storage.getBuyLogByCode(buyCode).sendPurchase();
    }

    public void addAuction(Request request){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm");
        LocalDateTime beginDate = LocalDateTime.parse(request.getInformation().get("beginDate"),formatter);
        System.out.println(beginDate);
        LocalDateTime endDate = LocalDateTime.parse(request.getInformation().get("endDate"),formatter);
        System.out.println(endDate);
        double initialPrice = Integer.parseInt(request.getInformation().get("price"));
        System.out.println(initialPrice);
        Product product1 = storage.getProductById(Integer.parseInt(request.getInformation().get("productId")));
        System.out.println(product1.getName() + " " + product1.getProductId());
        Seller seller = (Seller) storage.getUserByUsername(request.getInformation().get("seller"));
        System.out.println(seller.getUsername());
        Auction auction = new Auction(beginDate, endDate, product1, initialPrice, seller);
        storage.addAuction(auction);
        seller.addAuction(auction);
        auction.finishAuction();
    }
}
