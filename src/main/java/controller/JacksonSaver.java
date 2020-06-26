package controller;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class JacksonSaver {
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());

    private Storage storage = Storage.getStorage();
    public void dataSaver(){
        objectMapper.enableDefaultTyping();
        try {
            //writer.writeValue(System.out,storage.getAllAdmins().get(0));
            //writer.writeValue(new File("./dataBase/allUsers.json"),storage.getAllUsers());
            writer.writeValue(new File("./dataBase/allSellers.json"),storage.getAllSellers());
            writer.writeValue(new File("./dataBase/allCustomers.json"),storage.getAllCustomers());
            writer.writeValue(new File("./dataBase/allAdmins.json"),storage.getAllAdmins());
            writer.writeValue(new File("./dataBase/allLogs.json"),storage.getAllLogs());
            writer.writeValue(new File("./dataBase/allSellLogs.json"),storage.getAllSellLogs());
            writer.writeValue(new File("./dataBase/allBuyLogs.json"),storage.getAllBuyLogs());
            writer.writeValue(new File("./dataBase/allProducts.json"),storage.getAllProducts());
            writer.writeValue(new File("./dataBase/allCategories.json"),storage.getAllCategories());
            writer.writeValue(new File("./dataBase/allDiscounts.json"),storage.getAllDiscounts());
            writer.writeValue(new File("./dataBase/allRates.json"),storage.getAllRates());
            writer.writeValue(new File("./dataBase/allComments.json"),storage.getAllComments());
            writer.writeValue(new File("./dataBase/allSales.json"),storage.getAllSales());
            writer.writeValue(new File("./dataBase/allRequests.json"),storage.getAllRequests());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void dataReader(){
        objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);

        //reader(storage.getAllUsers(),"allUsers",Person[].class);
        //disguiseUser();
        reader(storage.getAllSellers(),"allSellers", Person.class);
        reader(storage.getAllCustomers(),"allCustomers",Person.class);
        reader(storage.getAllAdmins(),"allAdmins",Person.class);
        addAllPeople();
        reader(storage.getAllLogs(),"allLogs", Log.class);
        reader(storage.getAllSellLogs(),"allSellLogs",Log.class);
        reader(storage.getAllBuyLogs(),"allBuyLogs",Log.class);
        reader(storage.getAllProducts(),"allProducts", Product.class);
        reader(storage.getAllCategories(),"allCategories", Category.class);
        reader(storage.getAllDiscounts(),"allDiscounts",Discount.class);
        reader(storage.getAllRates(),"allRates",Rate.class);
        reader(storage.getAllComments(),"allComments",Comment.class);
        reader(storage.getAllSales(),"allSales",Sale.class);
        reader(storage.getAllRequests(),"allRequests",Request.class);


    }

    private void addAllPeople() {
        for (Person person : storage.getAllAdmins())
            storage.getAllUsers().add(person);
        for (Person person : storage.getAllCustomers())
            storage.getAllUsers().add(person);
        for (Person person : storage.getAllSellers())
            storage.getAllUsers().add(person);
    }

    private void disguiseUser() {
        for (Person person : storage.getAllUsers()){
            if (person instanceof Admin)
                storage.getAllAdmins().add(person);
            else if (person instanceof Seller)
                storage.getAllSellers().add(person);
            else storage.getAllCustomers().add(person);
        }
    }

    private <T> void reader(ArrayList<T> main,String path,Class<T> tClass){
        try {

            File file = new File("./dataBase/"+path+".json");
            if (!file.exists()) {
                file.getParentFile().mkdir();
                file.createNewFile();
                return;
            }
            T[] temp = objectMapper.readValue(file, TypeFactory.defaultInstance().constructArrayType(tClass));
            Collections.addAll(main,temp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}