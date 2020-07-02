package controller;

import java.util.ArrayList;

import model.*;

public class Storage {
    private static Storage storage;
    private ArrayList<Person> allUsers;
    private ArrayList<Person> allCustomers;
    private ArrayList<Person> allSellers;
    private ArrayList<Person> allAdmins;
    private ArrayList<Product> allProducts;
    private ArrayList<Log> allLogs;
    private ArrayList<Log> allSellLogs;
    private ArrayList<Log> allBuyLogs;
    private ArrayList<Category> allCategories;
    private ArrayList<Discount> allDiscounts;
    private ArrayList<Rate> allRates;
    private ArrayList<Comment> allComments;
    private ArrayList<Sale> allSales;
    private ArrayList<Request> allRequests;
    private ArrayList<Filter> allFilters;
    private ArrayList<Sort> allSorts;
    private ArrayList<Cart> allCarts;

    public Storage() {
        newArrayLists();
    }

    public static Storage getStorage() {
        if (storage == null) {
            storage = new Storage();
            FileSaver fileSaver = new FileSaver(storage);
            fileSaver.dataReader();
        }
        return storage;
    }

    private void newArrayLists() {
        allUsers = Person.getAllPeople();
        allCustomers = Customer.getAllCustomers();
        allSellers = Seller.getAllSellers();
        allAdmins = Admin.getAllAdmins();
        allProducts = Product.getAllProducts();
        allLogs = Log.getAllLogs();
        allSellLogs = SellLog.getAllSellLogs();
        allBuyLogs = BuyLog.getAllBuyLogs();
        allCategories = Category.getAllCategories();
        allDiscounts = Discount.getAllDiscounts();
        allRates = Rate.getAllRates();
        allComments = Comment.getAllComments();
        allSales = Sale.getAllSales();
        allRequests = Request.getAllRequests();
        allFilters = Filter.getAllFilters();
        allSorts = Sort.getAllSorts();
        allCarts = new ArrayList<>();
    }

    public ArrayList<Person> getAllUsers() {
        return allUsers;
    }

    public ArrayList<Person> getAllCustomers() {
        return allCustomers;
    }

    public ArrayList<Person> getAllSellers() {
        return allSellers;
    }

    public ArrayList<Person> getAllAdmins() {
        return allAdmins;
    }

    public ArrayList<Product> getAllProducts() {
        return allProducts;
    }

    public ArrayList<Log> getAllLogs() {
        return allLogs;
    }

    public ArrayList<Log> getAllSellLogs() {
        return allSellLogs;
    }

    public ArrayList<Log> getAllBuyLogs() {
        return allBuyLogs;
    }

    public ArrayList<Category> getAllCategories() {
        return allCategories;
    }

    public ArrayList<Discount> getAllDiscounts() {
        return allDiscounts;
    }

    public ArrayList<Rate> getAllRates() {
        return allRates;
    }

    public ArrayList<Comment> getAllComments() {
        return allComments;
    }

    public ArrayList<Sale> getAllSales() {
        return allSales;
    }

    public ArrayList<Request> getAllRequests() {
        return allRequests;
    }

    public ArrayList<Cart> getAllCarts() {
        return allCarts;
    }

    public Person getUserByUsername(String username) {
        for (Person user : allUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public Discount getDiscountByCode(String code) {
        for (Discount discount : allDiscounts) {
            if (discount.getDiscountCode().equals(code)) {
                return discount;
            }
        }
        return null;
    }

    public BuyLog getBuyLogByCode(String code) {
        for (Log buyLog : allBuyLogs) {
            if (((BuyLog) buyLog).getBuyCode() == Integer.parseInt(code)) {
                return (BuyLog) buyLog;
            }
        }
        return null;
    }

    public SellLog getSellLogByCode(String code){
        for (Log sellLog : allSellLogs) {
            if (((SellLog) sellLog).getSellCode() == Integer.parseInt(code)){
                return (SellLog) sellLog;
            }
        }
        return null;
    }

    public Request getRequestById(int requestId) {
        for (Request request : allRequests) {
            if (request.getRequestId() == requestId) {
                return request;
            }
        }
        return null;
    }

    public Category getCategoryByName(String name) {
        for (Category category : allCategories) {
            if (category.getCategoryName().equals(name)) {
                return category;
            }
        }
        return null;
    }

    public Sale getSaleById(int saleId) {
        for (Sale sale : allSales) {
            if (sale.getSaleId() == saleId) {
                return sale;
            }
        }
        return null;
    }

    public Product getProductById(int productId) {
        for (Product product : allProducts) {
            if (product.getProductId() == productId) {
                return product;
            }
        }
        return null;
    }

    public void addUser(Person user) {
        //allUsers.add(user);
        switch (user.getRole()) {
            case CUSTOMER:
                allCustomers.add(user);
                return;
            case SELLER:
                allSellers.add(user);
                return;
            case ADMIN:
                allAdmins.add(user);
                return;
        }
    }

    public void addProduct(Product product) {
        allProducts.add(product);
    }

    public void addSellLog(SellLog log) {
        allSellLogs.add(log);
    }

    public void addBuyLog(BuyLog log) {
        allBuyLogs.add(log);
    }

    public void addCategory(Category category) {
        allCategories.add(category);
    }

    public void addDiscount(Discount discount) {
        allDiscounts.add(discount);
    }

    public void addRate(Rate rate) {
        allRates.add(rate);
    }

    public void addComment(Comment comment) {
        allComments.add(comment);
    }

    public void addSale(Sale sale) {
        allSales.add(sale);
    }

    public void addRequest(Request request) {
        allRequests.add(request);
    }

    public void addCart(Cart cart) {
        allCarts.add(cart);
    }

    public void addFilter(Filter filter) {
        allFilters.add(filter);
    }

    public void addSort(Sort sort) {
        allSorts.add(sort);
    }

    public void deleteUser(Person user) {
        allUsers.remove(user);
        switch (user.getRole()) {
            case CUSTOMER:
                allCustomers.remove(user);
                return;
            case SELLER:
                allSellers.remove(user);
                return;
            case ADMIN:
                allAdmins.remove(user);
                return;
        }
    }

    public void deleteProduct(Product product) {
        allProducts.remove(product);
    }

    public void deleteDiscount(Discount discount) {
        allDiscounts.remove(discount);
    }

    public void deleteCategory(Category category) {
        allCategories.remove(category);
    }
}
