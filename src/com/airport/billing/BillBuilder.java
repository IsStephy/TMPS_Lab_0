package billing;

import java.util.List;

class BillBuilder {
    private Bill bill;

    public BillBuilder(String billId) {
        this.bill = new Bill(billId);
    }

    public BillBuilder addTicket(Ticket ticket) {
        bill.addItem(ticket);
        return this;
    }

    public BillBuilder addService(ExtraService service) {
        bill.addItem(service);
        return this;
    }

    public BillBuilder addItem(BillableItem item) {
        bill.addItem(item);
        return this;
    }

    public BillBuilder addMultipleItems(List<BillableItem> items) {
        for (BillableItem item : items) {
            bill.addItem(item);
        }
        return this;
    }

    public Bill build() {
        return bill;
    }

    // Static factory method for convenience
    public static BillBuilder newBill(String billId) {
        return new BillBuilder(billId);
    }
}