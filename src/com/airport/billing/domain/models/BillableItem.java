package com.airport.billing.domain.models;

public interface BillableItem {
    String getDescription();

    double getPrice();

    BillableItem clone();
}