package billing;

public interface BillableItem {
    String getDescription();
    double getPrice();
    BillableItem clone();
}