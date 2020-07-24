package bank;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.gilecode.yagson.com.google.gson.JsonElement;
import com.gilecode.yagson.com.google.gson.JsonParser;
import com.gilecode.yagson.com.google.gson.reflect.TypeToken;
import model.Person;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Pattern;

public class BankFileSavor {

    private HashMap allAccounts;
    private ArrayList allAccountIDs;
    private BankAccount bankAccount = new BankAccount("temp","temp","temp","temp");
    private Receipt receipt = new Receipt("temp","temp","temp","temp","temp","temp");
    private YaGson gson = new YaGsonBuilder().setPrettyPrinting().create();
    private static final Type TT_mapStringString = new TypeToken<HashMap<String,String>>(){}.getType();

    public BankFileSavor(HashMap<String, String> allAccounts, ArrayList<Integer> allAccountIDs) {
        this.allAccounts = allAccounts;
        this.allAccountIDs = allAccountIDs;
    }

    public void dataSavor () {
       createFile("./bankDataBase/allAccounts.json");
       createFile("./bankDataBase/allAccountIds.json");
       createFile("./bankDataBase/allBankAccounts.json");
       createFile("./bankDataBase/allReceipts.json");
        writeHashMapToFile(allAccounts , "./bankDataBase/allAccounts.json");
        writeArrayToFile(allAccountIDs , "./bankDataBase/allAccountIds.json");
        writeArrayToFile(bankAccount.getAllAccounts(),"./bankDataBase/allBankAccounts.json");
        writeArrayToFile(receipt.getAllReceipts(),"./bankDataBase/allReceipts.json");
    }

    private void createFile (String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void dataReader() {
        reader(bankAccount.getAllAccounts(),"allBankAccounts",BankAccount[].class);
        reader(receipt.getAllReceipts(),"allReceipts",Receipt[].class);
        for (BankAccount account : bankAccount.getAllAccounts()) {
            System.out.println(account.getUserName());
        }
    }

    private <T> void reader(ArrayList<T> main, String path, Class<T[]> tClass){
        File file = new File("./bankDataBase/"+path+".json");
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

    public HashMap<String,String> readAllAccounts () {
        return jsonToMapStringString("./bankDataBase/allAccounts.json");
    }

    public ArrayList<Integer> readAllAccountIds () {
        return readArrayListData();
    }

    private void writeArrayToFile(ArrayList arrayList,String filePath){
        try(FileWriter fileWriter = new FileWriter(filePath)) {
            gson.toJson(arrayList,fileWriter);
            fileWriter.flush();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void writeHashMapToFile (HashMap hashMap, String filePath) {
        try(FileWriter fileWriter = new FileWriter(filePath)) {
            gson.toJson(hashMap,fileWriter);
            fileWriter.flush();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Integer> readArrayListData(){
        File file = new File("./bankDataBase/allAccountIds.json");
        if (!file.exists()) {
            file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
            ArrayList<Integer> patterns = new ArrayList<>();
            JsonParser jsonParser = new JsonParser();
            try {
                BufferedReader br = new BufferedReader(new FileReader("./bankDataBase/allAccountIds.json"));
                JsonElement jsonElement = jsonParser.parse(br);
                Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
                return gson.fromJson(jsonElement, type);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return patterns;
        }

    public HashMap<String, String> jsonToMapStringString(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        try {

            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            reader.close();
            String content = stringBuilder.toString();
            HashMap<String, String> ret = new HashMap<String, String>();
            if (content == null || content.isEmpty())
                return ret;
            return gson.fromJson(content, TT_mapStringString);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
