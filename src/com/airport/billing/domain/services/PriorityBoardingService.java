package com.airport.billing.domain.services;

import com.airport.billing.domain.models.ExtraService;

public class PriorityBoardingService extends ExtraService {
    public PriorityBoardingService() {
        super("Priority Boarding", 25.00);
    }

    @Override
    public PriorityBoardingService clone() {
        return new PriorityBoardingService();
    }
}