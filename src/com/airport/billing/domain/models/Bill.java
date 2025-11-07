package com.airport.billing.domain.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Bill {
    private List<BillableItem> items;
    private String billId;
    private LocalDateTime createdAt;

    public Bill(String billId) {
        this.billId = billId;
        this.items = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    public void addItem(BillableItem item) {
        items.add(item);
    }

    public List<BillableItem> getItems() {
        return new ArrayList<>(items);
    }

    public double calculateTotal() {
        return items.stream()
                .mapToDouble(BillableItem::getPrice)
                .sum();
    }

    public String getBillId() {
        return billId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getItemCount() {
        return items.size();
    }
}