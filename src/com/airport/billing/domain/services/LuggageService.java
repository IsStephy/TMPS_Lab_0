package com.airport.billing.domain.services;

import com.airport.billing.domain.models.ExtraService;

public class LuggageService extends ExtraService {
    private int weightKg;
    private double pricePerKg;

    public LuggageService(int weightKg, double pricePerKg) {
        super(String.format("Extra Luggage (%d kg)", weightKg), weightKg * pricePerKg);
        this.weightKg = weightKg;
        this.pricePerKg = pricePerKg;
    }

    @Override
    public LuggageService clone() {
        return new LuggageService(this.weightKg, this.pricePerKg);
    }
}