package View;

import controller.SellerManager;
import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class SellerMenu extends AccountMenu {
    SellerManager sellerManager;

    public SellerMenu(Menu previousMenu) {
        super("SellerMenu", previousMenu);
    }

    @Override
    public void commandProcess() {
        while (true) {
            String command = scanner.nextLine().trim();
            if (command.equals("1"))
                viewInfo();
            else if (command.equals("2"))
                viewCompanyInfo();
            else if (command.equals("3"))
                viewSalesHistory(); //bahar
            else if (command.equals("4"))
                manageProductsMenu();//bahar
            else if (command.equals("5"))
                addProduct(); //bahar
            else if (command.equals("6"))
                removeProduct();
            else if (command.equals("7"))
                showCategories();
            else if (command.equals("8"))
                viewOffsMenu();
            else if (command.equals("9"))
                viewBalance();
            else if (command.equals("10"))
                break;
            else if (command.equalsIgnoreCase("help"))
                show();
            else
                System.out.println("Invalid choice");

        }

    }

    private void viewBalance() {
        System.out.println("Your balance is : "+person.getBalance());
    }

    private void viewOffsMenu() {
        ArrayList<Sale> allOffs = sellerManager.viewSellerOffs();
        if (allOffs.isEmpty()){
            System.out.println("no offs yet.");
            return;
        }
        for (Sale off : allOffs){
            System.out.println(off.getSaleId());
        }
        while (true){
            System.out.println("Enter\n1.view off\n2.edit off\n3.add off\n4.back");
            int command = scanner.nextInt();
            if (command == 1)
                viewSingleOff();
            else if (command == 2)
                editOff();
            else if (command == 3)
                addOff();
            else if (command == 4)
                break;
            else
                System.out.println("Invalid choice");

        }
    }

    private void viewSingleOff (){
        System.out.println("Enter offId :");
        int offId = scanner.nextInt();
        try {
            Sale sale = sellerManager.viewSingleOff(offId);
            System.out.println(sale.getSaleId());
            System.out.println("Amount of sale : " + sale.getAmountOfSale());
            System.out.println("Begin Date : " + sale.getBeginDate());
            System.out.println("End Date : " + sale.getEndDate());
            System.out.println("List of products in this off : ");
            for (Product product : sale.getProductsWithThisSale()) {
                System.out.println(product.getProductId() + "------" + product.getName());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void addOff(){
        HashMap<String,String> information = new HashMap<>();
        ArrayList<Product> productsInThisSale = new ArrayList<>();
        int productId = 0;
        System.out.println("start date and time (yyyy,MM,dd,HH,mm) :");
        String startDate = scanner.nextLine().trim();
        information.put ("beginDate",startDate);

        System.out.println("end date and time (yyyy,MM,dd,HH,mm) :");
        String endDate = scanner.nextLine().trim();
        information.put("endDate",endDate);
        try {
            System.out.println("Enter amount of sale :");
            int amountOfSale = scanner.nextInt();
            information.put("amountOfSale",Integer.toString(amountOfSale));
            System.out.println("Enter number of products in this off:");
            int numberOfProducts = scanner.nextInt();
            for (int i = 0; i < numberOfProducts; i++){
                System.out.println("Enter the username of " + i + "th product");
                productId = scanner.nextInt();
                if (!sellerManager.doesProductExist(productId)){
                    System.out.println("There is not a product with this Id! Try again :)");
                    i--;
                }
                else if (!sellerManager.doesSellerHaveProduct(productId)){
                    System.out.println("You don't have this product in your available products! Try again :)");
                    i--;
                }
                else {
                    productsInThisSale.add(sellerManager.getSellerProductById(productId));
                }
            }
            sellerManager.addOff(information,productsInThisSale);
            System.out.println("created successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("unsuccessful creation!");
        }
    }

    private void editOff(){
        System.out.println("Enter Off Id :");
        int offId = scanner.nextInt();
        if (!sellerManager.doesSellerHaveThisOff(offId)) {
            System.out.println("Invalid offId.");
            return;
        }
        while (true){
            System.out.println("Enter field to edit :\n1.start date and time\n2.end date and time\n3.amount of sale\n4." +
                    "add product to off" + "\n5.remove product from off\n6.back");
            int command = scanner.nextInt();
            if (!(0 < command && command < 7)) {
                System.out.println("Invalid command");
                continue;
            } else if (command == 6)
                break;
            System.out.println("Enter new value :");
            String newValue = scanner.nextLine();
            if (command == 1) {
                try {
                    sellerManager.editOff(offId, "beginDate", newValue);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (command == 2) {
                try {
                    sellerManager.editOff(offId, "endDate", newValue);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (command == 3) {
                try {
                    sellerManager.editOff(offId, "amountOfSale", newValue);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (command == 4) {
                System.out.println("Enter productId : ");
                int productId = scanner.nextInt();
                try {
                    sellerManager.addProductToOff(offId,productId);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (command == 5) {
                System.out.println("Enter productId : ");
                int productId = scanner.nextInt();
                try {
                    sellerManager.removeProductFromOff(offId,productId);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void showCategories() {
        if (sellerManager.viewAllCategories().isEmpty()){
            System.out.println("no categories yet.");
            return;
        }
        for (Category category: sellerManager.viewAllCategories()) {
            System.out.println(category.getCategoryName());
            System.out.println(category.getProperties());
            System.out.println("-------");
        }
    }

    private void removeProduct() {
        System.out.println("Enter product Id");
        String id = scanner.nextLine();
        try {
            sellerManager.removeProduct(Integer.parseInt(id));
            System.out.println("product removed successfully!");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void addProduct() {
    }

    private void manageProductsMenu() {
        showSellerProducts(); //just names
        while (true){
            System.out.println("Enter\n1.view a product\n2.view buyers of a product\n3.edit a product\n4.back :");
            int command = scanner.nextInt();
            //to be continued
        }

    }

    private void showSellerProducts() {
    }

    private void viewSalesHistory() {
    }

    private void viewCompanyInfo() {
        System.out.println(((Seller)person).getCompany());
        System.out.println("Enter\n1.edit\n2.back");
        int command = scanner.nextInt();
        if(command == 1){
            System.out.println("Enter new value");
            String newValue = scanner.nextLine();
            ((Seller)person).setCompany(newValue);
        }else if (command == 2)
            return;
        else
            System.out.println("Invalid choice");
    }

    private LocalDateTime dateMaker(String date) throws Exception {
        String[] stringDateArray = date.split(",");
        int[] intDateArray = new int[stringDateArray.length];
        for (int i = 0; i < stringDateArray.length; i++) intDateArray[i] = Integer.parseInt(stringDateArray[i]);
        LocalDateTime localDateTime = LocalDateTime.of(intDateArray[0], intDateArray[1], intDateArray[2], intDateArray[3], intDateArray[4]);
        return localDateTime;

    }

    @Override
    public void show() {
        System.out.println("Seller Menu :");
        System.out.println("1.view personal info\n2.view company information\n3.view sales history\n" +
                "4.manage products\n5.add product\n6.remove product\n7.show categories\n8.view offs\n" +
                "9.view balance\n10.back");
    }
}