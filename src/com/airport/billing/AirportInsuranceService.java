package billing;

class AirportInsuranceService extends ExtraService {
    public AirportInsuranceService() {
        super("Travel Insurance", 15.00);
    }

    @Override
    public AirportInsuranceService clone() {
        return new AirportInsuranceService();
    }
}

