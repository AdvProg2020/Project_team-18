package model;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class Admin extends Person{
    private transient ArrayList<Request> allRequests;
    private static ArrayList<Person> allAdmins = new ArrayList<>();

    public static ArrayList<Person> getAllAdmins() {
        return allAdmins;
    }

    public Admin(HashMap<String, String> information) {
        super(information);
        this.allRequests = new ArrayList<>();
    }

    public ArrayList<Request> getAllRequests() {
        return allRequests;
    }


    public ArrayList<Integer> getAllRequestIds(){
        ArrayList<Integer>allRequestIds = new ArrayList<>();
        for (Request request : allRequests){
            allRequestIds.add(request.getRequestId());
        }
        return allRequestIds;
    }

}


