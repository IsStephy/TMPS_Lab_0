package com.airport.billing.utils;

import com.airport.billing.domain.models.Bill;
import com.airport.billing.domain.models.BillableItem;
import com.airport.billing.domain.models.Printable;
import java.time.format.DateTimeFormatter;

public class BillPrinter {

    public void printBill(Bill bill, String transactionId) {
        System.out.println("\n");
        System.out.println("╔" + "═".repeat(60) + "╗");
        System.out.println("║" + centerText("AIRPORT BILLING SYSTEM", 60) + "║");
        System.out.println("║" + centerText("Official Receipt", 60) + "║");
        System.out.println("╚" + "═".repeat(60) + "╝");
        System.out.println();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Bill ID: " + bill.getBillId());
        System.out.println("Date: " + bill.getCreatedAt().format(formatter));
        if (transactionId != null) {
            System.out.println("Transaction ID: " + transactionId);
        }
        System.out.println("-".repeat(62));
        System.out.println();

        System.out.println(String.format("%-50s %s", "ITEM DESCRIPTION", "PRICE"));
        System.out.println("-".repeat(62));

        for (BillableItem item : bill.getItems()) {
            if (item instanceof Printable) {
                System.out.println(((Printable) item).formatForPrint());
            } else {
                System.out.println(String.format("%-50s $%.2f",
                        item.getDescription(), item.getPrice()));
            }
        }

        System.out.println("-".repeat(62));
        System.out.println(String.format("%-50s $%.2f",
                "TOTAL AMOUNT", bill.calculateTotal()));
        System.out.println("=".repeat(62));
        System.out.println();
        System.out.println(centerText("Thank you for choosing our airport!", 62));
        System.out.println(centerText("Have a pleasant journey!", 62));
        System.out.println();
    }

    private String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - padding - text.length());
    }

    public void printItemSummary(BillableItem item) {
        System.out.println("  ✓ " + item.getDescription() + " - $" +
                String.format("%.2f", item.getPrice()));
    }
}