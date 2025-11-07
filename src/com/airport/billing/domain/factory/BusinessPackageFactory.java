package com.airport.billing.domain.factory;

import com.airport.billing.domain.models.ExtraService;
import com.airport.billing.domain.services.LoungeAccessService;
import com.airport.billing.domain.services.PriorityBoardingService;

public class BusinessPackageFactory implements ServicePackageFactory {
    @Override
    public ExtraService createPrimaryService() {
        LoungeAccessService lounge = new LoungeAccessService();
        lounge.applyDiscount(10); // 10% discount on lounge
        return lounge;
    }

    @Override
    public ExtraService createSecondaryService() {
        return new PriorityBoardingService();
    }

    @Override
    public String getPackageName() {
        return "Business Package";
    }

    @Override
    public double getPackageDiscount() {
        return 10.0; // 10% discount
    }
}