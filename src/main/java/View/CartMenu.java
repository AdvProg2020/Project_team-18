package View;

import controller.CustomerManager;
import model.Customer;
import model.Product;

public class CartMenu extends Menu{
    private CustomerManager customerManager;
    public CartMenu(Menu previousMenu) {
        super("CartMenu", previousMenu);
        customerManager = new CustomerManager();
    }

    @Override
    public void commandProcess() {
        do {
            String command = scanner.nextLine().trim();
            if (command.equals("1"))
                showProducts();
            else if (command.equals("2"))
                viewSingleProduct();
            else if (command.equals("3"))
                increaseProduct();
            else if (command.equals("4"))
                decreaseProduct();
            else if (command.equals("5"))
                showTotalPrice();
            else if (command.equals("6"))
                purchase();
            else if (command.equals("7"))
                break;
            else if (command.equalsIgnoreCase("help"))
                show();
            else
                System.out.println("Invalid choice");

        }while (true);

    }

    private void purchase() {
        if (person instanceof Customer){
            PurchaseMenu purchaseMenu = new PurchaseMenu(this);
            purchaseMenu.run();
        }else{
            System.out.println("First login as customer then purchase.");
            LoginRegisterMenu loginRegisterMenu = new  LoginRegisterMenu(this);
            loginRegisterMenu.run();
        }

    }

    private void showTotalPrice() {
        System.out.println(customerManager.getCartTotalPrice());
    }

    private void decreaseProduct() {
        System.out.println("Enter product id :");
        String id = scanner.nextLine();
        try {
            customerManager.decreaseProduct(id);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void increaseProduct() {
        System.out.println("Enter product id :");
        String id = scanner.nextLine();
        try {
            customerManager.increaseProduct(id);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void viewSingleProduct() {
        System.out.println("Enter product id :");
        String id = scanner.nextLine();
        try {
            int productId = Integer.parseInt(id);
            Product product = customerManager.getProductInCart(productId);
            ProductMenu productMenu = new ProductMenu(product,this);
            productMenu.run();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void showProducts() {
        for (Product product : customerManager.getProductsInCart().keySet()) {
            System.out.println(product.getName() + "---" + product.getProductId() + "---" + product.getPrice());
            System.out.println("Numbers of this product in your cart : " + customerManager.getProductsInCart().get(product));
            System.out.println("---------------");
        }
    }

    @Override
    public void show() {
        System.out.println("Cart Menu :");
        System.out.println("Enter\n1.show products\n2.view a product\n3.increase a product\n4.decrease a product" +
                "\n5.show total price\n6.purchase\n7.back");

    }
}
