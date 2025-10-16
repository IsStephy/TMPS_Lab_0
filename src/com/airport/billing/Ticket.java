package billing;

class Ticket implements BillableItem, Printable {
    private String passengerName;
    private String flightNumber;
    private String destination;
    private String departureDate;
    private double price;

    public Ticket(String passengerName, String flightNumber, String destination,
                  String departureDate, double price) {
        this.passengerName = passengerName;
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.departureDate = departureDate;
        this.price = price;
    }

    @Override
    public String getDescription() {
        return String.format("Flight Ticket - %s to %s (Flight: %s, Date: %s)",
                passengerName, destination, flightNumber, departureDate);
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String formatForPrint() {
        return String.format("%-50s $%.2f", getDescription(), getPrice());
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getFlightNumber() {
        return flightNumber;
    }
}