package View;

import model.Person;

public abstract class AccountMenu extends Menu {
    protected void viewInfo() {
        viewPersonalInfo(person);
        while (true){
            System.out.println("Enter\n1.editFields\n2.back");
            String choice = scanner.nextLine().trim();
            if (choice.equals("1"))
                editField();
            else if (choice.equals("2"))
                break;
            else System.out.println("Invalid choice");
        }
    }
    protected void viewPersonalInfo(Person p){
        System.out.println("role : "+p.getRole());
        System.out.println("username : "+p.getUserName());
        System.out.println("password : "+p.getPassword());
        System.out.println("name : "+p.getName());
        System.out.println("family name : "+p.getFamilyName());
        System.out.println("email : "+p.getEmail());
        System.out.println("number : "+p.getNumber());
    }
    protected void editField (){
        System.out.println("Enter field to change:\n1.password\n2.name\n3.family name\n4.email\n5.number");
        String choice = scanner.nextLine().trim();
        String field;
        if (choice.equals("1"))
            field = "password";
        else if (choice.equals("2"))
            field = "name";
        else if (choice.equals("3"))
            field = "familyName";
        else if (choice.equals("4"))
            field = "email";
        else if (choice.equals("5"))
            field = "number";
        else{
            System.out.println("Invalid choice");
            return;
        }
        System.out.println("Enter new value :");
        String newValue = scanner.nextLine().trim();
        try {
            manager.editField(field,newValue);
            System.out.println("Field successfully changed!");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}