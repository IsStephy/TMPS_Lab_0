package billing;

public interface DiscountApplicable {
    void applyDiscount(double percentage);
    double getOriginalPrice();
}