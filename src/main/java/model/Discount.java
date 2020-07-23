package model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Discount implements Idable<Discount> {

    private int discountId;
    private String discountCode;
    //temporarily commented and made transient
    private transient LocalDateTime beginDate;
    //temporarily commented and made transient
    private transient LocalDateTime endDate;
    private int usagePerCustomer;
    private double percentage;
    private double maxAmount;
    private HashMap<Customer, Integer> customersWithThisDiscount;
    private transient static ArrayList<Discount> allDiscounts = new ArrayList<>();

    public Discount() {
    }

    public static ArrayList<Discount> getAllDiscounts() {
        return allDiscounts;
    }
    public Discount(String discountCode, LocalDateTime beginDate, LocalDateTime endDate, double percentage,
                    int usagePerCustomer, double maxAmount) {
        this.discountId = idSetter();
        this.discountCode = discountCode;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.percentage = percentage;
        this.usagePerCustomer = usagePerCustomer;
        this.customersWithThisDiscount = new HashMap<>();
        this.maxAmount = maxAmount;
    }
    private int idSetter() {
        if (allDiscounts.size() == 0) {
            return 1;
        }
        int max = 0;
        for (Discount discount : allDiscounts) {
            if (discount.getDiscountId() > max)
                max = discount.getDiscountId();
        }
        return max + 1;
    }
    public int getDiscountId() {
        return discountId;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public double getPercentage() {
        return percentage;
    }

    public int getUsagePerCustomer() {
        return usagePerCustomer;
    }

    public HashMap<Customer, Integer> getCustomersWithThisDiscount() {
        return customersWithThisDiscount;
    }

    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public void setUsageCount(int usageCount) {
        this.usagePerCustomer = usageCount;
    }

    public void addCustomer(Customer newCustomer) {
        customersWithThisDiscount.put(newCustomer, this.usagePerCustomer);
    }

    public void removeCustomer(Customer specificCustomer, int usage) {
        customersWithThisDiscount.remove(specificCustomer, usage);
    }

    @Override
    public boolean equals(Object obj) {
        Discount first = (Discount) this;
        Discount second = (Discount) obj;
        boolean percentage = this.percentage == ((Discount) obj).percentage;
        boolean usagePerCustomer = this.usagePerCustomer == ((Discount) obj).usagePerCustomer;
        boolean maxAmount = this.maxAmount == ((Discount) obj).maxAmount;
        boolean beginDate = this.beginDate.equals(((Discount) obj).beginDate);
        boolean endDate = this.endDate.equals(((Discount) obj).endDate);
        boolean discountCode = this.discountCode.equals(((Discount) obj).discountCode);
        if (percentage && usagePerCustomer && maxAmount && beginDate && endDate && discountCode)
            return true;
        else return false;
    }

    @Override
    public int getId() {
        return this.discountId;
    }

    @Override
    public Discount getById(int id) {
        for (Discount discount : allDiscounts){
            if (discount.getDiscountId() == id)
                return discount;
        }
        return null;
    }
}
