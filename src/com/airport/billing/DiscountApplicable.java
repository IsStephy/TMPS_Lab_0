package billing;

interface DiscountApplicable {
    void applyDiscount(double percentage);
    double getOriginalPrice();
}
