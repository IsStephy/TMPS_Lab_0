package billing;

class LuggageService extends ExtraService {
    private int weightKg;

    public LuggageService(int weightKg, double pricePerKg) {
        super(String.format("Extra Luggage (%d kg)", weightKg), weightKg * pricePerKg);
        this.weightKg = weightKg;
    }
}
