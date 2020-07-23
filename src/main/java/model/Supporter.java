package model;

import javax.annotation.processing.SupportedOptions;
import java.util.ArrayList;
import java.util.HashMap;

public class Supporter extends Person {
    ArrayList<String> senders = new ArrayList<>();
    ArrayList<String> inbox = new ArrayList<>();
    private static ArrayList<Person> allSupporters = new ArrayList<>();

    public Supporter(HashMap<String, String> information) {
        super(information);
    }

    public static ArrayList<Person> getAllSupporters() {
        return allSupporters;
    }

    public void addToInBox(String sender, String receivedMessage) {
        senders.add(sender);
        inbox.add(receivedMessage);
    }

    public ArrayList<String> getInbox() {
        return this.inbox;
    }
}
