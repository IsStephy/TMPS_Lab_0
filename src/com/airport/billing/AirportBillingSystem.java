package billing;


public class AirportBillingSystem {

    private static InputHandler input;
    private static BillPrinter printer;
    private static PaymentProcessor paymentProcessor;
    private static int billCounter = 0;

    public static void main(String[] args) {
        input = new InputHandler();
        printer = new BillPrinter();
        paymentProcessor = new PaymentProcessor();

        printHeader();

        boolean continueProgram = true;

        while (continueProgram) {
            try {
                // Create a new bill
                Bill bill = new Bill("BILL-2025-" + String.format("%03d", ++billCounter));

                // Get ticket information
                Ticket ticket = createTicketFromInput();
                bill.addItem(ticket);
                System.out.println("✓ Ticket added successfully!\n");

                // Add extra services
                addExtraServices(bill);

                // Display summary
                displayBillSummary(bill);

                // Process payment
                String paymentMethod = selectPaymentMethod();
                String transactionId = paymentProcessor.processPayment(bill, paymentMethod);

                // Print final bill
                printer.printBill(bill, transactionId);

                // Ask if user wants to create another bill
                System.out.println();
                continueProgram = input.readYesNo("Would you like to create another bill?");
                System.out.println();

            } catch (Exception e) {
                System.out.println("\n❌ An error occurred: " + e.getMessage());
                System.out.println("Let's try again.\n");
            }
        }

        System.out.println("\n" + "=".repeat(62));
        System.out.println("Thank you for using Airport Billing Management System!");
        System.out.println("Total bills created: " + billCounter);
        System.out.println("=".repeat(62));

        input.close();
    }

    private static void printHeader() {
        System.out.println("=".repeat(62));
        System.out.println("   AIRPORT BILLING MANAGEMENT SYSTEM - INTERACTIVE MODE");
        System.out.println("=".repeat(62));
        System.out.println();
    }

    private static Ticket createTicketFromInput() {
        System.out.println("━".repeat(62));
        System.out.println("📋 TICKET INFORMATION");
        System.out.println("━".repeat(62));

        String passengerName = input.readString("Enter passenger name: ");
        String flightNumber = input.readString("Enter flight number: ");
        String destination = input.readString("Enter destination: ");
        String departureDate = input.readString("Enter departure date (YYYY-MM-DD): ");
        double price = input.readDouble("Enter ticket price ($): ");

        return new Ticket(passengerName, flightNumber, destination, departureDate, price);
    }

    private static void addExtraServices(Bill bill) {
        System.out.println("\n━".repeat(62));
        System.out.println("🛍️  EXTRA SERVICES");
        System.out.println("━".repeat(62));

        boolean addMore = true;

        while (addMore) {
            System.out.println("\nAvailable Services:");
            System.out.println("  1. Extra Luggage");
            System.out.println("  2. VIP Lounge Access");
            System.out.println("  3. Priority Boarding");
            System.out.println("  4. Car Rental");
            System.out.println("  5. Travel Insurance");
            System.out.println("  0. Done adding services");

            int choice = input.readChoice("\nSelect service (0-5): ", 0, 5);

            if (choice == 0) {
                break;
            }

            BillableItem service = createServiceFromChoice(choice);
            if (service != null) {
                bill.addItem(service);
                printer.printItemSummary(service);
                System.out.println("✓ Service added!\n");
            }

            if (choice != 0) {
                addMore = input.readYesNo("Add another service?");
            }
        }
    }

    private static BillableItem createServiceFromChoice(int choice) {
        switch (choice) {
            case 1: // Luggage
                int weight = input.readInt("Enter luggage weight (kg): ");
                double pricePerKg = input.readDouble("Enter price per kg ($): ");
                return new LuggageService(weight, pricePerKg);

            case 2: // Lounge
                LoungeAccessService lounge = new LoungeAccessService();
                if (input.readYesNo("Apply discount to lounge access?")) {
                    double discount = input.readDouble("Enter discount percentage: ");
                    lounge.applyDiscount(discount);
                }
                return lounge;

            case 3: // Priority Boarding
                return new PriorityBoardingService();

            case 4: // Car Rental
                int days = input.readInt("Enter rental duration (days): ");
                double pricePerDay = input.readDouble("Enter price per day ($): ");
                CarRentalService carRental = new CarRentalService(days, pricePerDay);
                if (input.readYesNo("Apply discount to car rental?")) {
                    double discount = input.readDouble("Enter discount percentage: ");
                    carRental.applyDiscount(discount);
                }
                return carRental;

            case 5: // Insurance
                return new AirportInsuranceService();

            default:
                return null;
        }
    }

    private static void displayBillSummary(Bill bill) {
        System.out.println("\n━".repeat(62));
        System.out.println("📊 BILL SUMMARY");
        System.out.println("━".repeat(62));
        System.out.println("Bill ID: " + bill.getBillId());
        System.out.println("Total Items: " + bill.getItemCount());
        System.out.println("\nItems:");
        for (BillableItem item : bill.getItems()) {
            printer.printItemSummary(item);
        }
        System.out.println("\n" + "-".repeat(62));
        System.out.println("TOTAL AMOUNT: $" + String.format("%.2f", bill.calculateTotal()));
        System.out.println("-".repeat(62));
    }

    private static String selectPaymentMethod() {
        System.out.println("\n━".repeat(62));
        System.out.println("💳 PAYMENT METHOD");
        System.out.println("━".repeat(62));
        System.out.println("  1. Credit Card");
        System.out.println("  2. Debit Card");
        System.out.println("  3. Cash");
        System.out.println("  4. Digital Wallet");

        int choice = input.readChoice("\nSelect payment method (1-4): ", 1, 4);

        String[] methods = {"Credit Card", "Debit Card", "Cash", "Digital Wallet"};
        return methods[choice - 1];
    }
}