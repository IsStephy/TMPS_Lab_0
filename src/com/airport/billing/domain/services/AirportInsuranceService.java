package com.airport.billing.domain.services;

import com.airport.billing.domain.models.ExtraService;

public class AirportInsuranceService extends ExtraService {
    public AirportInsuranceService() {
        super("Travel Insurance", 15.00);
    }

    @Override
    public AirportInsuranceService clone() {
        return new AirportInsuranceService();
    }
}