package billing;

public interface ServicePackageFactory {
    ExtraService createPrimaryService();
    ExtraService createSecondaryService();
    String getPackageName();
    double getPackageDiscount();
}
