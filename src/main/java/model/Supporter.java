package model;

import javax.annotation.processing.SupportedOptions;
import java.util.ArrayList;
import java.util.HashMap;

public class Supporter extends Person {
    private static ArrayList<Person> allSupporters = new ArrayList<>();
    public Supporter(HashMap<String, String> information){
        super(information);
    }
    public static ArrayList<Person> getAllSupporters(){
        return allSupporters;
    }
}
