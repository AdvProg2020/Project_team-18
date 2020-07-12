package controller;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.gilecode.yagson.com.google.gson.*;
import model.*;

import javax.security.auth.login.AccountExpiredException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileSaver {
    private YaGson gson = new YaGsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer()).registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer()).excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
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
        //nothing
    }

    private void modifySale() {
        for (Sale sale : storage.getAllSales()){
            replace(sale.getProductsWithThisSale(),new Product());
        }
    }

    private void modifyComment() {
        Product instance = new Product();
        for (Comment comment : storage.getAllComments()){
            Product product = instance.getById(comment.getProduct().getProductId());
            comment.setProduct(product);
        }
    }

    private void modifyRate() {
        Product instance = new Product();
        for (Rate rate : storage.getAllRates()){
            Product product = instance.getById(rate.getProduct().getProductId());
            rate.setProduct(product);
        }
    }

    private void modifyDiscount() {
        for (Discount discount : storage.getAllDiscounts()){
            hashmapReplace(discount.getCustomersWithThisDiscount(),new Customer());
        }
    }

    private void modifyCategory() {
        for (Category category : storage.getAllCategories()){
            replace(category.getThisCategoryProducts(),new Product());
        }
    }

    private void modifyProduct() {
        Person instance = new Person();
        Category cat = new Category();
        for (Product product : storage.getAllProducts()){
            Person seller = instance.getById(product.getSeller().getId());
            product.setSeller((Seller)seller);
            Category category = cat.getById(product.getCategory().getId());
            product.setCategory(category);
            replace(product.getComments(),new Comment());
            replace(product.getRates(),new Rate());
            replace(product.getThisProductsBuyers(),new Customer());
        }
    }

    private void modifySellLog() {
        Person instance = new Person();
        for (Log temp : storage.getAllSellLogs()){
            SellLog sellLog = (SellLog)temp;
            Person customer = instance.getById(sellLog.getCustomer().getId());
            sellLog.setCustomer((Customer)customer);
            hashmapReplace(sellLog.getSellerProductsInCart(),new Product());
        }
    }

    private void modifyBuyLog() {
        for (Log temp : storage.getAllBuyLogs()){
            BuyLog buyLog = (BuyLog)temp;
            replace(buyLog.getSeller(),new Seller(null));
        }
    }

    private void modifyLog() {
        for (Log log : storage.getAllLogs()){
            if (log instanceof BuyLog)
                storage.getAllBuyLogs().add(log);
            else storage.getAllSellLogs().add(log);
        }
    }

    private void modifyAdmin() {
        for(Person temp : storage.getAllAdmins()){
            Admin admin = (Admin)temp;
            replace(admin.getAllRequests(),new Request());
        }
    }

    private void modifyCustomer() {
        for (Person temp : storage.getAllCustomers()){
            Customer customer = (Customer)temp;
            replace(customer.getAllDiscounts(),new Discount());
            replace(customer.getBuyHistory(),new BuyLog());
        }
    }

    private void modifySeller() {
       for (Person temp : storage.getAllSellers()){
           Seller seller = (Seller)temp;
           replace(seller.getProductsToSell(),new Product());
           replace(seller.getSaleList(),new Sale());
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

    private <T extends Idable> void replace(ArrayList<T> dest,T instance){
        ArrayList<Integer> id = new ArrayList<>();
        for (T t : dest)
            id.add(t.getId());
        dest.clear();
        for (Integer x : id){
            dest.add((T) instance.getById(x.intValue()));
        }
    }
    private <K extends Idable> void hashmapReplace(HashMap<K,Integer> hashMap,K instance){
        HashMap<K,Integer> temp = new HashMap<>();
        for (Map.Entry<K,Integer> entry : hashMap.entrySet()){
            K k = (K) instance.getById(entry.getKey().getId());
            temp.put(k,entry.getValue());
        }
        hashMap.clear();
        hashMap.putAll(temp);

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
class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm");

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(formatter.format(localDateTime));
    }

    @Override
    public boolean isSimple() {
        return false;
    }
}
class LocalDateTimeDeserializer implements JsonDeserializer< LocalDateTime > {
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        System.out.println(json.getAsString());
        String[] dateTime = json.getAsString().split(",");
        LocalDateTime localDateTime = LocalDateTime.of(Integer.parseInt(dateTime[0]), Integer.parseInt(dateTime[1])
                , Integer.parseInt(dateTime[2]), Integer.parseInt(dateTime[3]), Integer.parseInt(dateTime[4]));
//        return LocalDateTime.parse(json.getAsString(),
//                DateTimeFormatter.ofPattern("yyyy,MM,dd,HH,mm").withLocale(Locale.ENGLISH));
        return localDateTime;
    }

    @Override
    public boolean isSimple() {
        return false;
    }
}
