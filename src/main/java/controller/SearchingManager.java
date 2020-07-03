package controller;

import model.Filter;
import model.Product;
import model.Sort;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchingManager extends Manager {

    private ProductManager productManager = new ProductManager();

    public SearchingManager() {

    }

    public ArrayList<Filter> getCurrentFilters() {
        return currentFilters;
    }

    public ArrayList<Sort> getCurrentSorts() {
        return currentSorts;
    }

    public ArrayList<Product> viewAllProducts(){
        ArrayList<Product> temp = new ArrayList<>();
        temp.addAll(storage.getAllProducts());
        return processOfViewProduct(temp);
    }

    private ArrayList<Product> processOfViewProduct (ArrayList <Product> selectedProducts){
        return sortProducts(filterProducts(selectedProducts));
    }

    private ArrayList<Product> filterProducts(ArrayList<Product> products){
        if (currentFilters.isEmpty()){
            return products;
        }
        else {
            ArrayList<Product> temp = new ArrayList<>();
            ArrayList<Product> tempContainer;
            tempContainer = products;
            for (Filter filter : currentFilters) {
                if (filter.getFilterName().equals("category")) {
                    tempContainer = filter.filterByCategory(storage.getCategoryByName(filter.getFilterInfo()), tempContainer);
                    for (Product product : tempContainer) {
                        if(!temp.contains(product))
                            temp.add(product);
                    }
                }
                if (filter.getFilterName().equals("name")) {
                    tempContainer = filter.filterByName(filter.getFilterInfo(), tempContainer);
                    for (Product product : tempContainer) {
                        if(!temp.contains(product))
                            temp.add(product);
                    }
                }
                if (filter.getFilterName().equals("price")) {
                    tempContainer = filter.filterByPrice(Double.parseDouble(filter.getFilterInfo()), tempContainer);
                    for (Product product : tempContainer) {
                        if(!temp.contains(product))
                            temp.add(product);
                    }
                }
            }
            return tempContainer;
        }
    }

    private ArrayList<Product> sortProducts (ArrayList<Product> products){
        if (currentSorts.isEmpty()){
            Sort sort = new Sort("");
            return sort.defaultSort(products);
        }
        else{
            ArrayList<Product> temp = new ArrayList<>();
            for (Sort sort : currentSorts) {
                if (sort.getSortName().equals("price"))
                    temp.addAll(sort.sortByPrice(products));
                if (sort.getSortName().equals("average rate"))
                    temp.addAll(sort.sortByAverageRate(products));
            }
            return temp;
        }
    }

    public ArrayList<Product> performFilter(String filterTag, String info) throws Exception {
        for (Filter filter : currentFilters) {
            if (filter.getFilterName().equals(filterTag) && filter.getFilterInfo().equals(info))
                throw new Exception("The " + filterTag +" filter is already selected!");
        }
            Filter filter = new Filter(filterTag,info);
            storage.addFilter(filter);
            currentFilters.add(filter);
            return this.viewAllProducts();
    }

    public ArrayList<Product> performSort(String sortTag) throws Exception {
        for (Sort sort : currentSorts) {
            if (sort.getSortName().equals(sortTag))
                throw new Exception("This sort is already selected!");
        }
        Sort sort = new Sort(sortTag);
        storage.addSort(sort);
        currentSorts.add(sort);
        return this.viewAllProducts();
    }

    public ArrayList<Product> disableFilter(String filterTag, String info) throws Exception {
        Filter removedFilter = null;
        for (Filter filter : currentFilters) {
            if(filter.getFilterName().equals("price")) {
                if (filter.getFilterName().equals(filterTag) && Double.parseDouble(filter.getFilterInfo()) == Double.parseDouble(info)) {
                    removedFilter = filter;
                }
            } else{
                if (filter.getFilterName().equals(filterTag) && filter.getFilterInfo().equals(info)) {
                    removedFilter = filter;
                }
            }
        }
        if(removedFilter == null){
            throw new Exception("You did not select this filter");
        }
        else {
            currentFilters.remove(removedFilter);
            return this.viewAllProducts();
        }
    }


    public ArrayList<Product> disableSort(String sortTag) throws Exception {
        Sort removedSort = null;
        for (Sort sort : currentSorts) {
            if(sort.getSortName().equals(sortTag))
                removedSort = sort;
        }
        if (removedSort == null){
            throw new Exception("You did not select this sort");
        }
        else {
            currentSorts.remove(removedSort);
            System.out.println(currentSorts);
            return this.viewAllProducts();
        }
    }

    public ArrayList<Product> performDefaultSort(ArrayList<Product> products){
        Sort sort = new Sort("name");
        return sort.defaultSort(products);
    }
}
