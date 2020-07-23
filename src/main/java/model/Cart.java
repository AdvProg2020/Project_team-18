package model;

import java.util.HashMap;

public class Cart {
    private static Cart cart;
    private HashMap<Product, Integer> productsInCart;
    private double totalPrice;
    private Customer customer;
    private boolean isPurchased = false;

    private Cart(Customer customer) {
        this.productsInCart = new HashMap<>();
        this.customer = customer;
        this.totalPrice = 0;
    }

    public static Cart getCart(){
        if (cart == null)
            cart = new Cart(null);
        return cart;
    }

    public HashMap<Product, Integer> getProductsInCart() {
        return productsInCart;
    }

    public void setProductsInCart(HashMap<Product, Integer> productsInCart) {
        this.productsInCart = productsInCart;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void addProductToCart(Product newProduct) {
        if(newProduct.getSupply() != 0) {
            productsInCart.put(newProduct, 1);
            newProduct.setSupply(newProduct.getSupply() - 1);
            totalPrice += newProduct.getPrice();
            newProduct.setNumberInCart(1);
        }
    }

    public void addNumberOfProductInTheCart(Product product) {
        for (Product productInCart : productsInCart.keySet()) {
            if (productInCart.getId() == product.getId()) {
                if(productInCart.getSupply() != 0) {
                    productsInCart.replace(productInCart, productsInCart.get(productInCart) + 1);
                    productInCart.setSupply(productInCart.getSupply() - 1);
                    totalPrice += productInCart.getPrice();
                    productInCart.setNumberInCart(productsInCart.get(productInCart));
                }
            }
        }
    }

    public void decreaseProduct(Product specificProduct) {
        for (Product product : productsInCart.keySet()) {
            if (product.getId() == specificProduct.getId()){
                if (productsInCart.get(product) != 0) {
                    productsInCart.replace(product, productsInCart.get(product) - 1);
                    product.setSupply(product.getSupply() + 1);
                    totalPrice -= product.getPrice();
                    product.setNumberInCart(productsInCart.get(product));
                }
                if (productsInCart.get(product) == 0) {
                    //System.out.println(1);
                    product.setNumberInCart(productsInCart.get(product));
                    //System.out.println(2);
                    removeProduct(product);
                    //System.out.println(3);
                }
            }
        }

    }

    public void removeProduct(Product specificProduct) {
        productsInCart.remove(specificProduct, 0);
    }

    public void emptyCart(){
        if (!cart.isPurchased){
            for (Product product : productsInCart.keySet()) {
                product.setSupply(product.getSupply() + productsInCart.get(product));
            }
        }
        this.totalPrice = 0;
        this.customer = null;
        this.productsInCart.clear();
    }

    public double getTotalPriceWithSale(){
        double finalPrice = 0.0;
        for (Product product : this.getProductsInCart().keySet()) {
            finalPrice += product.getPriceWithSale() * this.getProductsInCart().get(product);
        }
        return finalPrice;
    }

    public void isPurchased(){
        this.isPurchased = true;
    }

    @Override
    public String toString() {
        return "****"+productsInCart.size();
    }
}
