package billing;

class PremiumPackageFactory implements ServicePackageFactory {
    @Override
    public ExtraService createPrimaryService() {
        LoungeAccessService lounge = new LoungeAccessService();
        lounge.applyDiscount(15); // 15% discount on lounge
        return lounge;
    }

    @Override
    public ExtraService createSecondaryService() {
        CarRentalService carRental = new CarRentalService(3, 40.00);
        carRental.applyDiscount(20); // 20% discount on car rental
        return carRental;
    }

    @Override
    public String getPackageName() {
        return "Premium Package";
    }

    @Override
    public double getPackageDiscount() {
        return 15.0; // 15% discount
    }
}
