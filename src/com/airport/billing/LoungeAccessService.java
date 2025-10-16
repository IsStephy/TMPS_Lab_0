package billing;

class LoungeAccessService extends ExtraService implements DiscountApplicable {
    private double originalPrice;

    public LoungeAccessService() {
        super("VIP Lounge Access", 45.00);
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