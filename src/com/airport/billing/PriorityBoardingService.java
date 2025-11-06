package billing;

class PriorityBoardingService extends ExtraService {
    public PriorityBoardingService() {
        super("Priority Boarding", 25.00);
    }

    @Override
    public PriorityBoardingService clone() {
        return new PriorityBoardingService();
    }
}