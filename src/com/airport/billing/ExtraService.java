package billing;

abstract class ExtraService implements BillableItem, Printable {
    protected String serviceName;
    protected double price;

    public ExtraService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    @Override
    public String getDescription() {
        return serviceName;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String formatForPrint() {
        return String.format("%-50s $%.2f", getDescription(), getPrice());
    }
}