package controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import model.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FileSaver {
    private YaGson gson = new YaGsonBuilder().setPrettyPrinting().create();
    private Storage storage;

    public FileSaver(Storage storage) {
        this.storage = storage;
    }

    public void dataSaver(){
        writeArrayToFile(storage.getAllUsers(),"./dataBase/allUsers.json");
      //  writeArrayToFile(storage.getAllCustomers(),"./dataBase/allCustomers.json");
      //  writeArrayToFile(storage.getAllAdmins(),"./dataBase/allAdmins.json");
      //  writeArrayToFile(storage.getAllSellers(),"./dataBase/allSellers.json");
        writeArrayToFile(storage.getAllProducts(),"./dataBase/allProducts.json");
        writeArrayToFile(storage.getAllLogs(),"./dataBase/allLogs.json");
      //  writeArrayToFile(storage.getAllSellLogs(),"./dataBase/allSellLogs.json");
      //  writeArrayToFile(storage.getAllBuyLogs(),"./dataBase/allBuyLogs.json");
        writeArrayToFile(storage.getAllCategories(),"./dataBase/allCategories.json");
        writeArrayToFile(storage.getAllDiscounts(),"./dataBase/allDiscounts.json");
        writeArrayToFile(storage.getAllRates(),"./dataBase/allRates.json");
        writeArrayToFile(storage.getAllComments(),"./dataBase/allComments.json");
        writeArrayToFile(storage.getAllSales(),"./dataBase/allSales.json");
        writeArrayToFile(storage.getAllRequests(),"./dataBase/allRequests.json");


    }
    public void dataReader(){


        reader(storage.getAllUsers(),"allUsers",Person[].class);
       // reader(storage.getAllSellers(),"allSellers", Person[].class);
       // reader(storage.getAllCustomers(),"allCustomers",Person[].class);
       // reader(storage.getAllAdmins(),"allAdmins",Person[].class);
        reader(storage.getAllLogs(),"allLogs", Log[].class);
      //  reader(storage.getAllSellLogs(),"allSellLogs",Log[].class);
      //  reader(storage.getAllBuyLogs(),"allBuyLogs",Log[].class);
        reader(storage.getAllProducts(),"allProducts", Product[].class);
        reader(storage.getAllCategories(),"allCategories", Category[].class);
        reader(storage.getAllDiscounts(),"allDiscounts",Discount[].class);
        reader(storage.getAllRates(),"allRates",Rate[].class);
        reader(storage.getAllComments(),"allComments",Comment[].class);
        reader(storage.getAllSales(),"allSales",Sale[].class);
        reader(storage.getAllRequests(),"allRequests",Request[].class);
        modifyReferences();
       /* readUser();
        readCustomer();
        readAdmin();
        readSeller();
        readProduct();
        readLog();
        readBuyLog();
        readSellLog();
        readCategory();
        readDiscount();
        readRate();
        readComment();
        readSale();
        readRequest();*/
    }

    private void modifyReferences() {
        modifyPerson();
        modifyLog();

        modifySeller();
        modifyCustomer();
        modifyAdmin();

        modifyBuyLog();
        modifySellLog();
        modifyProduct();
        modifyCategory();
        modifyDiscount();
        modifyRate();
        modifyComment();
        modifySale();
        modifyRequest();
    }

    private void modifyRequest() {
    }

    private void modifySale() {
    }

    private void modifyComment() {
    }

    private void modifyRate() {
    }

    private void modifyDiscount() {
    }

    private void modifyCategory() {
    }

    private void modifyProduct() {
    }

    private void modifySellLog() {
    }

    private void modifyBuyLog() {
    }

    private void modifyLog() {
        for (Log log : storage.getAllLogs()){
            if (log instanceof BuyLog)
                storage.getAllBuyLogs().add(log);
            else storage.getAllSellLogs().add(log);
        }
    }

    private void modifyAdmin() {
    }

    private void modifyCustomer() {
    }

    private void modifySeller() {
       for (Person temp : storage.getAllSellers()){
           Seller seller = (Seller)temp;
           replace(seller.getProductsToSell(),seller);
       }
    }
    private <T extends Idable,K extends Idable> void replace(ArrayList<T> dest,K instance){
        ArrayList<Integer> id = new ArrayList<>();
        for (T t : dest)
            id.add(t.getId());
        dest.clear();
        for (Integer x : id){
            dest.add((T) instance.getById(x.intValue()));
        }
    }

    private void modifyPerson() {
        for (Person person : storage.getAllUsers()){
            if (person instanceof Admin)
                storage.getAllAdmins().add(person);
            else if (person instanceof Seller)
                storage.getAllSellers().add(person);
            else storage.getAllCustomers().add(person);
        }
    }

    private <T> void reader(ArrayList<T> main, String path, Class<T[]> tClass){
        File file = new File("./dataBase/"+path+".json");
        if (!file.exists()) {
            file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        try(FileReader fileReader = new FileReader(file)) {
            T [] fromFile = gson.fromJson(fileReader,tClass);
            Collections.addAll(main,fromFile);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void writeArrayToFile(ArrayList arrayList,String filePath){
        try(FileWriter fileWriter = new FileWriter(filePath)) {
            gson.toJson(arrayList,fileWriter);
            fileWriter.flush();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

   /* private void readUser (){
        try(FileReader fileReader = new FileReader("./dataBase/allUsers.json")) {
            Person [] fromFile = gson.fromJson(fileReader,Person[].class);
            Collections.addAll(storage.getAllUsers(),fromFile);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void readCustomer(){
        try(FileReader fileReader = new FileReader("./dataBase/allCustomers.json")) {
            Customer[] fromFile = gson.fromJson(fileReader,Customer[].class);
            Collections.addAll(storage.getAllCustomers(),fromFile);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void readAdmin(){
        try(FileReader fileReader = new FileReader("./dataBase/allAdmins.json")) {
            Admin[] fromFile = gson.fromJson(fileReader, Admin[].class);
            Collections.addAll(storage.getAllAdmins(),fromFile);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void readSeller(){
        try(FileReader fileReader = new FileReader("./dataBase/allSellers.json")) {
            Seller[] fromFile = gson.fromJson(fileReader, Seller[].class);
            Collections.addAll(storage.getAllSellers(),fromFile);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void readProduct(){
        try(FileReader fileReader = new FileReader("./dataBase/allProducts.json")) {
            Product[] fromFile = gson.fromJson(fileReader,Product[].class);
            Collections.addAll(storage.getAllProducts(),fromFile);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void readSale(){
        try(FileReader fileReader = new FileReader("./dataBase/allSales.json")) {
            Sale[] fromFile = gson.fromJson(fileReader,Sale[].class);
            Collections.addAll(storage.getAllSales(),fromFile);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void readCategory(){
        try(FileReader fileReader = new FileReader("./dataBase/allCategories.json")) {
            Category[] fromFile = gson.fromJson(fileReader,Category[].class);
            Collections.addAll(storage.getAllCategories(),fromFile);
            boolean uncatExist = false;
            for (Category category : storage.getAllCategories()){
                if (category.getCategoryName().equals("uncategorized"))
                    uncatExist = true;
            }
            if (!uncatExist) {
                storage.getAllCategories().add(new Category("uncategorized","file:src/main/java/graphics/fxml/images/shoes.jpg"));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void readDiscount(){
        try(FileReader fileReader = new FileReader("./dataBase/allDiscounts.json")) {
            Discount[] fromFile = gson.fromJson(fileReader,Discount[].class);
            Collections.addAll(storage.getAllDiscounts(),fromFile);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void readRate(){
        try(FileReader fileReader = new FileReader("./dataBase/allRates.json")) {
            Rate[] fromFile = gson.fromJson(fileReader,Rate[].class);
            Collections.addAll(storage.getAllRates(),fromFile);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void readComment(){
        try(FileReader fileReader = new FileReader("./dataBase/allComments.json")) {
            Comment[] fromFile = gson.fromJson(fileReader,Comment[].class);
            Collections.addAll(storage.getAllComments(),fromFile);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void readRequest(){
        try(FileReader fileReader = new FileReader("./dataBase/allRequests.json")) {
            Request[] fromFile = gson.fromJson(fileReader,Request[].class);
            Collections.addAll(storage.getAllRequests(),fromFile);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void readLog(){
        try(FileReader fileReader = new FileReader("./dataBase/allLogs.json")) {
            Log[] fromFile = gson.fromJson(fileReader,Log[].class);
            Collections.addAll(storage.getAllLogs(),fromFile);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void readBuyLog(){
        try(FileReader fileReader = new FileReader("./dataBase/allBuyLogs.json")) {
            BuyLog[] fromFile = gson.fromJson(fileReader,BuyLog[].class);
            Collections.addAll(storage.getAllBuyLogs(),fromFile);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void readSellLog(){
        try(FileReader fileReader = new FileReader("./dataBase/allSellLogs.json")) {
            SellLog[] fromFile = gson.fromJson(fileReader,SellLog[].class);
            Collections.addAll(storage.getAllSellLogs(),fromFile);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }*/
}
