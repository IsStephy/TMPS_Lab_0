package com.airport.billing.domain.models;

public interface DiscountApplicable {
    void applyDiscount(double percentage);
    double getOriginalPrice();
}