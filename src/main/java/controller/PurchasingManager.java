package controller;

import model.BuyLog;
import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class PurchasingManager extends Manager {

    private int buyLogCode = 0;
    private int wage;
    private AdminManager adminManager = new AdminManager();

    public PurchasingManager() {
    }

    public Cart performPayment(HashMap<String, String> receiverInformation, double totalPrice, double discountPercentage, String discountUsed)
            throws Exception {
        double moneyToTransfer = totalPrice - totalPrice * (1.0 * discountPercentage / 100);
        person.setBalance(person.getBalance() - moneyToTransfer);
        ((Customer) person).addAmountOfAllPurchasing(moneyToTransfer);
        if (((Customer) person).getAmountOfAllPurchasing() > 100) {
            adminManager.getDiscountAwarded();
            ((Customer) person).setAmountOfAllPurchasing(0);
        }
        adminManager.createRandomDiscounts();
        createBuyLog(receiverInformation, totalPrice, discountPercentage, discountUsed);
        addCustomerToProductsBuyers();
        for (Seller seller : findDistinctSellers(super.cart)) {
            double totalPricePerSeller = calculateEachSellerMoneyTransfer(sellerProductsInCart(super.cart, seller));
            double w = (double) wage/100;
            seller.addBalance(totalPricePerSeller * (1-w));
            createSellLog(seller, totalPricePerSeller, discountPercentage);
        }
        refineFileProducts();
        cart.isPurchased();
        cart.emptyCart();
        return cart;
    }

    public void performPaymentWithBankAccount(HashMap<String, String> receiverInformation, double totalPrice,
                                              double discountPercentage, String discountUsed) throws Exception {
        double moneyToTransfer = totalPrice - totalPrice * (1.0 * discountPercentage / 100);
        ((Customer) person).addAmountOfAllPurchasing(moneyToTransfer);
        if (((Customer) person).getAmountOfAllPurchasing() > 100) {
            adminManager.getDiscountAwarded();
            ((Customer) person).setAmountOfAllPurchasing(0);
        }
        adminManager.createRandomDiscounts();
        createBuyLog(receiverInformation, totalPrice, discountPercentage, discountUsed);
        addCustomerToProductsBuyers();
        for (Seller seller : findDistinctSellers(super.cart)) {
            double totalPricePerSeller = calculateEachSellerMoneyTransfer(sellerProductsInCart(super.cart, seller));
            double w = (double) wage/100;
            seller.addBalance(totalPricePerSeller * (1 - w));
            createSellLog(seller, totalPricePerSeller, discountPercentage);
        }
        refineFileProducts();
        cart.isPurchased();
        cart.emptyCart();
    }

    private void refineFileProducts() {
        for (Product product : cart.getProductsInCart().keySet()) {
            if(product.getCategory().getCategoryName().equalsIgnoreCase("File")){
                FileProduct fileProduct = (FileProduct)product;
                fileProduct.setFileState(FileState.READY_TO_DOWNLOAD);
                ((Customer)person).addToFileProducts(fileProduct);
                Seller seller = (Seller)getPersonByUsername(fileProduct.getSellerName());
                seller.addToFileProducts(fileProduct);
            }
        }
    }

    private void addCustomerToProductsBuyers() {
        for (Product product : cart.getProductsInCart().keySet()) {
            product.addBuyer((Customer) person);
        }
    }

    public ArrayList<Seller> findDistinctSellers(Cart cart) {
        ArrayList<Seller> allSellers = new ArrayList<>();
        for (Product product : cart.getProductsInCart().keySet()) {
            if (!allSellers.contains(product.getSeller())) {
                allSellers.add((Seller) storage.getUserByUsername(product.getSeller().getUsername()));
            }
        }
        return allSellers;
    }

    public void createBuyLog(HashMap<String, String> receiverInformation, double totalPrice, double saleAmount, String discountUsed) {
        BuyLog buyLog = new BuyLog(LocalDateTime.now(), totalPrice, saleAmount, findDistinctSellers(super.cart),
                receiverInformation, cart.getProductsInCart(), discountUsed);
        storage.addBuyLog(buyLog);
        ((Customer) person).addToBuyLogs(buyLog);
        this.buyLogCode = buyLog.getBuyCode();
    }

    public void createSellLog(Seller seller, double totalPrice, double saleAmount) {
        SellLog sellLog = new SellLog(LocalDateTime.now(), totalPrice, saleAmount, (Customer) person, sellerProductsInCart(super.cart, seller));
        storage.addSellLog(sellLog);
        seller.addToSellLogs(sellLog);
    }

    public HashMap<Product, Integer> sellerProductsInCart(Cart cart, Seller seller) {
        HashMap<Product, Integer> aimedProducts = new HashMap<>();
        for (Product product : cart.getProductsInCart().keySet()) {
            if (product.getSeller().equals(seller))
                aimedProducts.put(product, cart.getProductsInCart().get(product));
        }
        return aimedProducts;
    }

    public double calculateEachSellerMoneyTransfer(HashMap<Product, Integer> products) {
        double totalMoney = 0;
        for (Product product : products.keySet()) {
            totalMoney += product.getPrice() * products.get(product);
        }
        return totalMoney;
    }

    public void checkDiscountValidity(String discountCode) throws Exception {
        if (storage.getDiscountByCode(discountCode).getEndDate().isBefore(LocalDateTime.now()))
            throw new Exception("This discount is expired!");
        else if (storage.getDiscountByCode(discountCode).getBeginDate().isAfter(LocalDateTime.now()))
            throw new Exception("You can't use a discount which is not available yet!");
        else if (storage.getDiscountByCode(discountCode).getUsagePerCustomer() == 0)
            throw new Exception("You used this discount before and it's not available anymore!");
    }

    public boolean doesCustomerHaveEnoughMoney(double price) {
        return !(person.getBalance() < price);
    }

    public double calculateTotalPriceWithDiscount(String discountCode) {
        double totalPriceWithoutDiscount = getTotalPriceWithoutDiscount();
        double discountPercentage = storage.getDiscountByCode(discountCode).getPercentage();
        return (1.0 * (100 - discountPercentage) * totalPriceWithoutDiscount) / 100;
    }

    public double getDiscountPercentage(String discountCode) {
        if (discountCode.equals("")) {
            return 0.0;
        }
        return storage.getDiscountByCode(discountCode).getPercentage();
    }

    public double getTotalPriceWithoutDiscount() {
        double totalPrice = 0;
        for (Product product : super.cart.getProductsInCart().keySet()) {
            totalPrice += (super.cart.getProductsInCart().get(product) * product.getPriceWithSale());
        }
        return totalPrice;
    }

    public void updateDiscountUsagePerPerson(String discountCode) {
        storage.getDiscountByCode(discountCode).setUsageCount(storage.getDiscountByCode(discountCode).getUsagePerCustomer() - 1);
        if (storage.getDiscountByCode(discountCode).getUsagePerCustomer() == 0) {
            ((Customer) person).getAllDiscounts().remove(storage.getDiscountByCode(discountCode));
        }
    }

    public boolean doesCustomerHaveDiscountCode(String discountCode) {
        for (Discount discount : ((Customer) person).getAllDiscounts()) {
            if (discount.getDiscountCode().equals(discountCode))
                return true;
        }
        return false;
    }

    public int getWage() {
        return wage;
    }

    public void setWage(int wage) {
        this.wage = wage;
    }
}

