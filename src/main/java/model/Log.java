package model;


import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Log implements Idable<Log> {
    protected LocalDateTime date;
    protected HashMap<Product, Integer> products;
    private transient static ArrayList<Log>allLogs = new ArrayList<>();
    protected int id;

    public Log() {
    }

    public static ArrayList<Log> getAllLogs() {
        return allLogs;
    }
    public Log(LocalDateTime date) {
        this.date = date;
        this.products = new HashMap<>();
        this.id = idSetter();
        allLogs.add(this);
    }
    private int idSetter() {
        if (allLogs.size() == 0) {
            return 1;
        }
        int max = 0;
        for (Log sellLog : allLogs) {
            if (((BuyLog)sellLog).getBuyCode() > max)
                max = ((BuyLog)sellLog).getBuyCode();
        }
        return max + 1;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    @Override
    public boolean equals(Object obj) {
        Log first = (Log) this;
        Log second = (Log) obj;
        boolean date = this.date.equals(((Log) obj).date);
        boolean products = this.products.equals(((Log) obj).products);
        return date && products;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Log getById(int id) {
        for (Log log : allLogs){
            if (log.id == id)
                return log;
        }
        return null;
    }
}
