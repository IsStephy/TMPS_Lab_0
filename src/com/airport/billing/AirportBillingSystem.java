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
        demonstrateDesignPatterns();

        boolean continueProgram = true;

        while (continueProgram) {
            try {
                System.out.println("\n" + "=".repeat(62));
                System.out.println("Choose billing mode:");
                System.out.println("  1. Manual Mode (enter each item)");
                System.out.println("  2. Package Mode (use predefined packages)");
                System.out.println("  3. Clone Previous Bill (Prototype Pattern)");
                System.out.println("=".repeat(62));

                int mode = input.readChoice("Select mode (1-3): ", 1, 3);

                Bill bill = null;

                if (mode == 1) {
                    bill = createBillManually();
                } else if (mode == 2) {
                    bill = createBillWithPackage();
                } else {
                    bill = createBillFromPrototype();
                }

                if (bill != null) {
                    displayBillSummary(bill);
                    String paymentMethod = selectPaymentMethod();
                    String transactionId = paymentProcessor.processPayment(bill, paymentMethod);
                    printer.printBill(bill, transactionId);
                }

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
        System.out.println("AIRPORT BILLING MANAGEMENT SYSTEM");
        System.out.println("Featuring: SOLID Principles & Design Patterns");
        System.out.println("=".repeat(62));
        System.out.println();
    }

    private static void demonstrateDesignPatterns() {
        System.out.println("🎨 DESIGN PATTERNS IMPLEMENTED:");
        System.out.println("━".repeat(62));
        System.out.println("1️⃣  BUILDER PATTERN:");
        System.out.println("   • BillBuilder for fluent bill creation");
        System.out.println("   • Chain methods: addTicket().addService().build()");
        System.out.println();
        System.out.println("2️⃣  PROTOTYPE PATTERN:");
        System.out.println("   • Clone existing tickets and services");
        System.out.println("   • Reuse configurations for similar bookings");
        System.out.println();
        System.out.println("3️⃣  ABSTRACT FACTORY PATTERN:");
        System.out.println("   • ServicePackageFactory for service bundles");
        System.out.println("   • Economy, Business, and Premium packages");
        System.out.println("━".repeat(62));
    }

    // Store last created bill for prototype pattern
    private static Bill lastBill = null;

    private static Bill createBillManually() {
        System.out.println("\n📝 MANUAL MODE - Build your bill step by step");

        String billId = "BILL-2025-" + String.format("%03d", ++billCounter);

        // BUILDER PATTERN: Using BillBuilder
        BillBuilder builder = BillBuilder.newBill(billId);

        Ticket ticket = createTicketFromInput();
        builder.addTicket(ticket);
        System.out.println("✓ Ticket added successfully!\n");

        addExtraServicesManually(builder);

        lastBill = builder.build();
        return lastBill;
    }

    private static Bill createBillWithPackage() {
        System.out.println("\n📦 PACKAGE MODE - Choose a service package");
        System.out.println("━".repeat(62));
        System.out.println("Available Packages (Abstract Factory Pattern):");
        System.out.println("  1. Economy Package");
        System.out.println("     • Extra Luggage (15kg)");
        System.out.println("     • Travel Insurance");
        System.out.println("     • 5% package discount");
        System.out.println();
        System.out.println("  2. Business Package");
        System.out.println("     • VIP Lounge Access (10% off)");
        System.out.println("     • Priority Boarding");
        System.out.println("     • 10% package discount");
        System.out.println();
        System.out.println("  3. Premium Package");
        System.out.println("     • VIP Lounge Access (15% off)");
        System.out.println("     • Car Rental 3 days (20% off)");
        System.out.println("     • 15% package discount");
        System.out.println("━".repeat(62));

        int choice = input.readChoice("Select package (1-3): ", 1, 3);

        // ABSTRACT FACTORY PATTERN: Select factory based on user choice
        ServicePackageFactory factory = null;
        switch (choice) {
            case 1:
                factory = new EconomyPackageFactory();
                break;
            case 2:
                factory = new BusinessPackageFactory();
                break;
            case 3:
                factory = new PremiumPackageFactory();
                break;
        }

        System.out.println("\n✓ Selected: " + factory.getPackageName());

        String billId = "BILL-2025-" + String.format("%03d", ++billCounter);

        // BUILDER PATTERN: Build bill with package services
        BillBuilder builder = BillBuilder.newBill(billId);

        Ticket ticket = createTicketFromInput();
        builder.addTicket(ticket);

        // Add services from factory
        ExtraService primary = factory.createPrimaryService();
        ExtraService secondary = factory.createSecondaryService();

        builder.addService(primary).addService(secondary);

        System.out.println("\n✓ Package services added:");
        printer.printItemSummary(primary);
        printer.printItemSummary(secondary);
        System.out.println("✓ Package discount: " + factory.getPackageDiscount() + "%");

        lastBill = builder.build();
        return lastBill;
    }

    private static Bill createBillFromPrototype() {
        if (lastBill == null) {
            System.out.println("\n❌ No previous bill to clone. Creating new bill...");
            return createBillManually();
        }

        System.out.println("\n🔄 PROTOTYPE MODE - Clone previous bill");
        System.out.println("━".repeat(62));
        System.out.println("Previous bill contains " + lastBill.getItemCount() + " items:");
        for (BillableItem item : lastBill.getItems()) {
            printer.printItemSummary(item);
        }
        System.out.println("━".repeat(62));

        if (!input.readYesNo("Clone this bill?")) {
            return createBillManually();
        }

        String billId = "BILL-2025-" + String.format("%03d", ++billCounter);
        BillBuilder builder = BillBuilder.newBill(billId);

        // PROTOTYPE PATTERN: Clone all items from previous bill
        System.out.println("\n🔄 Cloning items...");
        for (BillableItem item : lastBill.getItems()) {
            BillableItem cloned = item.clone();
            builder.addItem(cloned);

            // Allow modification of passenger name for tickets
            if (cloned instanceof Ticket) {
                if (input.readYesNo("Change passenger name?")) {
                    String newName = input.readString("Enter new passenger name: ");
                    ((Ticket) cloned).setPassengerName(newName);
                    System.out.println("✓ Passenger name updated");
                }
            }
        }

        System.out.println("✓ Bill cloned successfully!");

        if (input.readYesNo("Add more services?")) {
            addExtraServicesManually(builder);
        }

        lastBill = builder.build();
        return lastBill;
    }

    private static Ticket createTicketFromInput() {
        System.out.println("\n━".repeat(62));
        System.out.println("📋 TICKET INFORMATION");
        System.out.println("━".repeat(62));

        String passengerName = input.readString("Enter passenger name: ");
        String flightNumber = input.readString("Enter flight number: ");
        String destination = input.readString("Enter destination: ");
        String departureDate = input.readString("Enter departure date (YYYY-MM-DD): ");
        double price = input.readDouble("Enter ticket price ($): ");

        return new Ticket(passengerName, flightNumber, destination, departureDate, price);
    }

    private static void addExtraServicesManually(BillBuilder builder) {
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

            ExtraService service = createServiceFromChoice(choice);
            if (service != null) {
                builder.addService(service);
                printer.printItemSummary(service);
                System.out.println("✓ Service added!\n");
            }

            if (choice != 0) {
                addMore = input.readYesNo("Add another service?");
            }
        }
    }

    private static ExtraService createServiceFromChoice(int choice) {
        switch (choice) {
            case 1:
                int weight = input.readInt("Enter luggage weight (kg): ");
                double pricePerKg = input.readDouble("Enter price per kg ($): ");
                return new LuggageService(weight, pricePerKg);

            case 2:
                LoungeAccessService lounge = new LoungeAccessService();
                if (input.readYesNo("Apply discount to lounge access?")) {
                    double discount = input.readDouble("Enter discount percentage: ");
                    lounge.applyDiscount(discount);
                }
                return lounge;

            case 3:
                return new PriorityBoardingService();

            case 4:
                int days = input.readInt("Enter rental duration (days): ");
                double pricePerDay = input.readDouble("Enter price per day ($): ");
                CarRentalService carRental = new CarRentalService(days, pricePerDay);
                if (input.readYesNo("Apply discount to car rental?")) {
                    double discount = input.readDouble("Enter discount percentage: ");
                    carRental.applyDiscount(discount);
                }
                return carRental;

            case 5:
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