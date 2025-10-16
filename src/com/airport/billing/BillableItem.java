package billing;

interface BillableItem {
    String getDescription();
    double getPrice();
}