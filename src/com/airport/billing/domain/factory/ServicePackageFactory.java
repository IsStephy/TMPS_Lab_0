package com.airport.billing.domain.factory;

import com.airport.billing.domain.models.ExtraService;

public interface ServicePackageFactory {
    ExtraService createPrimaryService();
    ExtraService createSecondaryService();
    String getPackageName();
    double getPackageDiscount();
}