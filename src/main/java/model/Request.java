package model;


import java.util.ArrayList;
import java.util.HashMap;

public class Request implements Idable<Request> {
    private int requestId;
    private RequestType typeOfRequest;
    private StateType stateOfRequest;
    private HashMap<String, String> information;
    private transient static ArrayList<Request>allRequests = new ArrayList<>();

    public static ArrayList<Request> getAllRequests() {
        return allRequests;
    }

    public Request() {
    }

    public Request(String typeOfRequest, HashMap<String, String> information) {
        this.requestId = idSetter();
        this.typeOfRequest = requestTypeFinder(typeOfRequest);
        this.information = information;
        this.stateOfRequest = StateType.PROCESSING;
   }
    private int idSetter (){
        if (allRequests.size() == 0){
            return 1;
        }
        int max = 0;
        for (Request request : allRequests){
            if (request.requestId>max)
                max = request.requestId;
        }
        return max+1;
    }

    public int getRequestId() {
        return requestId;
    }


    public RequestType getTypeOfRequest() {
        return typeOfRequest;
    }

    public StateType getStateOfRequest() {
        return stateOfRequest;
    }

    public HashMap<String, String> getInformation() {
        return information;
    }

    RequestType requestTypeFinder(String typeName) {
        switch (typeName) {
            case "register seller":
                return RequestType.REGISTER_SELLER;
            case "add product":
                return RequestType.ADD_PRODUCT;
            case "edit product":
                return RequestType.EDIT_PRODUCT;
            case "remove product":
                return RequestType.REMOVE_PRODUCT;
            case "add sale":
                return RequestType.ADD_SALE;
            case "edit sale":
                return RequestType.EDIT_SALE;
            case "add product to sale":
                return RequestType.ADD_PRODUCT_TO_SALE;
            case "remove product from sale":
                return RequestType.REMOVE_PRODUCT_FROM_SALE;
            case "add comment":
                return RequestType.ADD_COMMENT;
            case "add auction":
                return RequestType.ADD_AUCTION;
        }
        return null;
    }

    public void acceptRequest() {
        this.stateOfRequest = StateType.ACCEPTED;
    }

    public void declineRequest() {
        this.stateOfRequest = StateType.DECLINED;
    }

    @Override
    public int getId() {
        return this.requestId;
    }

    @Override
    public Request getById(int id) {
        for (Request request : allRequests)
            if (request.requestId == id)
                return request;
        return null;
    }
}
