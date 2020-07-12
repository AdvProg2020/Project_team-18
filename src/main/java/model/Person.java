package model;


import java.util.ArrayList;
import java.util.HashMap;

public class Person implements Idable<Person> {
    protected int id;
    private String username;
    private String password;
    private String name;
    private String familyName;
    private String email;
    private String number;
    private double balance;
    private Role role;
    private transient static ArrayList<Person>allPeople = new ArrayList<>();

    public static ArrayList<Person> getAllPeople() {
        return allPeople;
    }

    public Person() {
    }

    public Person(HashMap<String, String> information) {
        this.username = information.get("username");
        this.password = information.get("password");
        this.name = information.get("name");
        this.familyName = information.get("familyName");
        this.email = information.get("email");
        this.number = information.get("number");
        this.balance = 0;
        this.role = roleFinder(information.get("role"));
        this.id = idSetter();
        allPeople.add(this);
    }
    private int idSetter() {
        if (allPeople.size() == 0) {
            return 1;
        }
        int max = 0;
        for (Person person : allPeople) {
            if (person.id > max)
                max = person.id;
        }
        return max + 1;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public double getBalance() {
        return balance;
    }

    public Role getRole() {
        return role;
    }

    Role roleFinder(String roleName) {
        if ("customer".equals(roleName)) {
            return Role.CUSTOMER;
        } else if ("seller".equals(roleName)) {
            return Role.SELLER;
        } else if ("admin".equals(roleName)) {
            return Role.ADMIN;
        } else if("supporter".equals(roleName)){
            return Role.SUPPORTER;
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        Person first = (Person) this;
        Person second = (Person) obj;
        return first.getUsername().equals(second.getUsername());
    }

    @Override
    public String toString() {
        return "Person " +
                ", username = " + username + "\n" +
                ", password = " + password + "\n" +
                ", name = " + name + "\n" +
                ", familyName = " + familyName + "\n" +
                ", email = " + email + "\n" +
                ", number = " + number + "\n" +
                ", balance = " + balance +"\n" +
                ", role = " + role +
                '}';
    }


    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Person getById(int id) {
        for (Person person : allPeople){
            if (person.id == id)
                return person;
        }
        return null;
    }
}
