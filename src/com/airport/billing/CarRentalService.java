package billing;

class CarRentalService extends ExtraService implements DiscountApplicable {
    private int days;
    private double originalPrice;

    public CarRentalService(int days, double pricePerDay) {
        super(String.format("Car Rental (%d days)", days), days * pricePerDay);
        this.days = days;
        this.originalPrice = this.price;
    }

    @Override
    public void applyDiscount(double percentage) {
        this.price = originalPrice * (1 - percentage / 100);
    }

    @Override
    public double getOriginalPrice() {
        return originalPrice;
    }

    @Override
    public String formatForPrint() {
        if (price < originalPrice) {
            return String.format("%-50s $%.2f (was $%.2f)",
                    getDescription(), getPrice(), getOriginalPrice());
        }
        return super.formatForPrint();
    }
}