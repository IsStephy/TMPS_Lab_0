package billing;

class CarRentalService extends ExtraService implements DiscountApplicable {
    private int days;
    private double pricePerDay;
    private double originalPrice;

    public CarRentalService(int days, double pricePerDay) {
        super(String.format("Car Rental (%d days)", days), days * pricePerDay);
        this.days = days;
        this.pricePerDay = pricePerDay;
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

    @Override
    public CarRentalService clone() {
        CarRentalService cloned = new CarRentalService(this.days, this.pricePerDay);
        if (this.price < this.originalPrice) {
            cloned.applyDiscount((1 - this.price / this.originalPrice) * 100);
        }
        return cloned;
    }
}