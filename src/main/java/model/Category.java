package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.ArrayList;
import java.util.HashMap;
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Category {
    private int id;
    private String categoryName;
    private String imagePath;
    private ArrayList<Product> thisCategoryProducts;
    private HashMap<String, String> properties;
    private static ArrayList<Category> allCategories = new ArrayList<>();
    @JsonCreator
    public Category(String categoryName, String imageOption) {
        this.categoryName = categoryName;
        this.thisCategoryProducts = new ArrayList<>();
        this.properties = new HashMap<>();
        switch (imageOption){
            case "1":
                this.imagePath = "file:src/main/java/graphics/fxml/images/clothing.jpg";
                return;
            case "2":
                this.imagePath = "file:src/main/java/graphics/fxml/images/electronics.jpg";
                return;
            case "3":
                this.imagePath = "file:src/main/java/graphics/fxml/images/food.jpg";
                return;
            case "4":
                this.imagePath = "file:src/main/java/graphics/fxml/images/gardening.jpg";
                return;
            case "5":
                this.imagePath = "file:src/main/java/graphics/fxml/images/health.jpg";
                return;
            case "6":
                this.imagePath = "file:src/main/java/graphics/fxml/images/homeProducts.jpg";
                return;
            case "7" :
                this.imagePath = "file:src/main/java/graphics/fxml/images/sports.jpg";
                return;
            case "8" :
                this.imagePath = "file:src/main/java/graphics/fxml/images/toys.jpg";
                return;
            case "9" :
                this.imagePath = "file:src/main/java/graphics/fxml/images/books.jpg";
                return;
            case "10" :
                this.imagePath = "file:src/main/java/graphics/fxml/images/shoes.jpg";
        }
        this.id = idSetter();
    }
    private int idSetter() {
        if (allCategories.size() == 0) {
            return 1;
        }
        int max = 0;
        for (Category category : allCategories) {
            if (category.id > max)
                max = category.id;
        }
        return max + 1;
    }

    public static ArrayList<Category> getAllCategories() {
        return allCategories;
    }

    public String getImagePath() {
        return imagePath;
    }

    public static boolean doesCategoryExist(String name) {
        for (Category category : allCategories) {
            if (category.categoryName.equals(name))
                return true;
        }
        return false;
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public void addProductToCategory(Product product) {
        this.thisCategoryProducts.add(product);
    }

    public static Category getCategoryByName(String name) {
        for (Category category : allCategories) {
            if (category.getCategoryName().equals(name))
                return category;
        }
        return null;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public ArrayList<Product> getThisCategoryProducts() {
        return thisCategoryProducts;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setSingleValueInProperties(String property, String newValue) {
        this.properties.replace(property, newValue);
    }

    public void addNewProperty(String property, String value) {
        this.properties.put(property, value);
    }

    public void addToThisCategoryProducts(Product newProduct) {
        thisCategoryProducts.add(newProduct);
    }

    @Override
    public String toString() {
        return "CategoryName ='" + categoryName + '\'' + "\n" +
                ", thisCategoryProducts =" + thisCategoryProducts;
    }
}
