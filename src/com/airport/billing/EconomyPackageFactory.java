package billing;

class EconomyPackageFactory implements ServicePackageFactory {
    @Override
    public ExtraService createPrimaryService() {
        return new LuggageService(15, 2.00);
    }

    @Override
    public ExtraService createSecondaryService() {
        return new AirportInsuranceService();
    }

    @Override
    public String getPackageName() {
        return "Economy Package";
    }

    @Override
    public double getPackageDiscount() {
        return 5.0; // 5% discount
    }
}
