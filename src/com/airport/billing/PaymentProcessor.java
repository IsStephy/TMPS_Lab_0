package billing;

class PaymentProcessor {
    private static int transactionCounter = 1000;

    public String processPayment(Bill bill, String paymentMethod) {
        String transactionId = "TXN" + (++transactionCounter);
        double amount = bill.calculateTotal();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("PROCESSING PAYMENT");
        System.out.println("=".repeat(60));
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("Amount: $" + String.format("%.2f", amount));
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Status: APPROVED");
        System.out.println("=".repeat(60));

        return transactionId;
    }

    public boolean validatePayment(String paymentMethod, double amount) {
        return paymentMethod != null && !paymentMethod.isEmpty() && amount > 0;
    }
}