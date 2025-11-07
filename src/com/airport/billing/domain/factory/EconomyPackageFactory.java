package com.airport.billing.domain.factory;

import com.airport.billing.domain.models.ExtraService;
import com.airport.billing.domain.services.LuggageService;
import com.airport.billing.domain.services.AirportInsuranceService;

public class EconomyPackageFactory implements ServicePackageFactory {
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