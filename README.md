# Airport Billing Management System
## Project Report

---

## 📋 Table of Contents
1. [Executive Summary](#executive-summary)
2. [Project Overview](#project-overview)
3. [SOLID Principles Implementation](#solid-principles-implementation)
4. [System Architecture](#system-architecture)
5. [Class Descriptions](#class-descriptions)
6. [Features and Functionality](#features-and-functionality)
7. [User Interface Flow](#user-interface-flow)
8. [Technical Specifications](#technical-specifications)
9. [Testing Scenarios](#testing-scenarios)
10. [Future Enhancements](#future-enhancements)
11. [Conclusion](#conclusion)

---

## Executive Summary

The Airport Billing Management System is a Java-based console application designed to manage ticket sales, extra services, and bill generation for airport customers. The system demonstrates the practical implementation of three core SOLID principles: **Single Responsibility Principle (SRP)**, **Open-Closed Principle (OCP)**, and **Interface Segregation Principle (ISP)**.

### Key Highlights:
-  **14 classes/interfaces** implementing SOLID principles
-  **Interactive command-line interface** for dynamic data entry
-  **Extensible architecture** allowing easy addition of new services
-  **Robust input validation** preventing system crashes
-  **Professional bill formatting** with transaction tracking

---

## Project Overview

### Purpose
To create a maintainable, scalable, and well-structured billing system that handles:
- Flight ticket sales
- Extra service purchases (luggage, lounge access, priority boarding, car rental, insurance)
- Payment processing
- Bill generation and printing

### Target Users
- Airport staff
- Ticket counter operators
- Customer service representatives

### Development Environment
- **Language:** Java (Java 8+)
- **IDE:** Any Java IDE (Eclipse, IntelliJ IDEA, VS Code)
- **Build Tool:** Standard javac compiler
- **Dependencies:** Java Standard Library only

---

## SOLID Principles Implementation

### 1. Single Responsibility Principle (SRP)
**Definition:** Each class should have only one reason to change.

#### Implementation:

| Class | Single Responsibility |
|-------|----------------------|
| `Ticket` | Manages ticket information only |
| `Bill` | Aggregates items and calculates totals only |
| `PaymentProcessor` | Handles payment processing logic only |
| `BillPrinter` | Formats and prints bills only |
| `InputHandler` | Manages all user input operations only |

**Example:**
```java
class BillPrinter {
    // ONLY responsible for printing
    public void printBill(Bill bill, String transactionId) { ... }
    public void printItemSummary(BillableItem item) { ... }
}
```

**Benefits:**
- Easy to maintain and test
- Clear separation of concerns
- Changes in one area don't affect others

---

### 2. Open-Closed Principle (OCP)
**Definition:** Software entities should be open for extension but closed for modification.

#### Implementation:

**Abstract Base Class:**
```java
abstract class ExtraService implements BillableItem, Printable {
    protected String serviceName;
    protected double price;
    // Base implementation that doesn't need modification
}
```

**Extensions (without modifying base):**
- `LuggageService`
- `LoungeAccessService`
- `PriorityBoardingService`
- `CarRentalService` ⭐ (added later)
- `AirportInsuranceService` ⭐ (added later)

**Example of Extension:**
```java
// NEW service added without changing ExtraService
class CarRentalService extends ExtraService implements DiscountApplicable {
    private int days;
    
    public CarRentalService(int days, double pricePerDay) {
        super(String.format("Car Rental (%d days)", days), days * pricePerDay);
        this.days = days;
    }
}
```

**Benefits:**
- Easy to add new services
- Existing code remains stable
- No risk of breaking existing functionality

---

### 3. Interface Segregation Principle (ISP)
**Definition:** Clients should not be forced to depend on interfaces they don't use.

#### Implementation:

**Segregated Interfaces:**

```java
// Minimal core interface
interface BillableItem {
    String getDescription();
    double getPrice();
}

// Optional interface for printable items
interface Printable {
    String formatForPrint();
}

// Optional interface for discountable items
interface DiscountApplicable {
    void applyDiscount(double percentage);
    double getOriginalPrice();
}
```

## System Architecture

### Component Diagram

```
┌─────────────────────────────────────────────────────────┐
│                    Main Application                      │
│              (AirportBillingSystem)                      │
└────────────────────┬────────────────────────────────────┘
                     │
         ┌───────────┼───────────┐
         │           │           │
         ▼           ▼           ▼
    ┌────────┐  ┌─────────┐  ┌──────────┐
    │  Input │  │  Bill   │  │ Payment  │
    │Handler │  │ Printer │  │Processor │
    └────────┘  └─────────┘  └──────────┘
         │
         ▼
    ┌────────────────────────────────────┐
    │           Bill Object              │
    │   (Contains BillableItems)         │
    └────────────────────────────────────┘
         │
         └──────┬─────────────────┬─────────────┐
                │                 │             │
                ▼                 ▼             ▼
           ┌────────┐      ┌────────────┐  ┌──────────┐
           │ Ticket │      │ExtraService│  │  Other   │
           └────────┘      │  (abstract)│  │  Items   │
                           └────────────┘  └──────────┘
                                  │
                    ┌─────────────┼─────────────┐
                    │             │             │
                    ▼             ▼             ▼
              ┌─────────┐   ┌─────────┐   ┌─────────┐
              │ Luggage │   │ Lounge  │   │Priority │
              │ Service │   │ Access  │   │Boarding │
              └─────────┘   └─────────┘   └─────────┘
                                  │
                          ┌───────┴───────┐
                          ▼               ▼
                    ┌──────────┐    ┌──────────┐
                    │   Car    │    │Insurance │
                    │  Rental  │    │          │
                    └──────────┘    └──────────┘
```

---

## Class Descriptions

### Interface Layer

#### 1. BillableItem (Interface)
**Purpose:** Core contract for all billable items

**Methods:**
- `String getDescription()` - Returns item description
- `double getPrice()` - Returns item price

**Implementations:** Ticket, ExtraService (and all subclasses)

---

#### 2. Printable (Interface)
**Purpose:** Contract for items with custom print formatting

**Methods:**
- `String formatForPrint()` - Returns formatted string for printing

**Implementations:** Ticket, ExtraService (and all subclasses)

---

#### 3. DiscountApplicable (Interface)
**Purpose:** Contract for items that support discounts

**Methods:**
- `void applyDiscount(double percentage)` - Applies discount
- `double getOriginalPrice()` - Returns price before discount

**Implementations:** LoungeAccessService, CarRentalService

---

### Model Layer

#### 4. Ticket (Class)
**Purpose:** Represents a flight ticket

**Attributes:**
- `passengerName: String`
- `flightNumber: String`
- `destination: String`
- `departureDate: String`
- `price: double`

**Key Methods:**
- Constructor with all ticket details
- Implements BillableItem and Printable interfaces

**SOLID Principle:** SRP - Only manages ticket data

---

#### 5. Bill (Class)
**Purpose:** Aggregates billable items and calculates totals

**Attributes:**
- `items: List<BillableItem>`
- `billId: String`
- `createdAt: LocalDateTime`

**Key Methods:**
- `addItem(BillableItem item)` - Adds item to bill
- `calculateTotal()` - Calculates total amount
- `getItems()` - Returns list of items

**SOLID Principle:** SRP - Only manages bill aggregation and calculation

---

### Service Layer

#### 6. ExtraService (Abstract Class)
**Purpose:** Base class for all extra services

**Attributes:**
- `serviceName: String`
- `price: double`

**Key Methods:**
- Constructor for service initialization
- Implements BillableItem and Printable interfaces

**SOLID Principle:** OCP - Open for extension, closed for modification

---

#### 7. LuggageService (Class)
**Purpose:** Represents extra luggage service

**Additional Attributes:**
- `weightKg: int`

**Constructor:**
```java
public LuggageService(int weightKg, double pricePerKg)
```

---

#### 8. LoungeAccessService (Class)
**Purpose:** Represents VIP lounge access

**Features:**
- Fixed price: $45.00
- Supports discounts (implements DiscountApplicable)

**Additional Attributes:**
- `originalPrice: double`

---

#### 9. PriorityBoardingService (Class)
**Purpose:** Represents priority boarding service

**Features:**
- Fixed price: $25.00

---

#### 10. CarRentalService (Class)
**Purpose:** Represents car rental service

**Features:**
- Supports discounts (implements DiscountApplicable)
- Price based on days and daily rate

**Additional Attributes:**
- `days: int`
- `originalPrice: double`

**Constructor:**
```java
public CarRentalService(int days, double pricePerDay)
```

---

#### 11. AirportInsuranceService (Class)
**Purpose:** Represents travel insurance

**Features:**
- Fixed price: $15.00

---

### Processing Layer

#### 12. PaymentProcessor (Class)
**Purpose:** Handles payment processing

**Attributes:**
- `transactionCounter: static int` - Generates unique transaction IDs

**Key Methods:**
- `processPayment(Bill bill, String paymentMethod)` - Processes payment
- `validatePayment(String paymentMethod, double amount)` - Validates payment

**SOLID Principle:** SRP - Only handles payment processing

---

#### 13. BillPrinter (Class)
**Purpose:** Formats and prints bills

**Key Methods:**
- `printBill(Bill bill, String transactionId)` - Prints formatted bill
- `printItemSummary(BillableItem item)` - Prints item summary
- `centerText(String text, int width)` - Helper for text formatting

**SOLID Principle:** SRP - Only handles printing and formatting

---

#### 14. InputHandler (Class)
**Purpose:** Manages all user input operations

**Key Methods:**
- `readString(String prompt)` - Reads string input
- `readInt(String prompt)` - Reads integer with validation
- `readDouble(String prompt)` - Reads double with validation
- `readYesNo(String prompt)` - Reads yes/no response
- `readChoice(String prompt, int min, int max)` - Reads menu choice

**SOLID Principle:** SRP - Only handles user input

---

## Features and Functionality

### Core Features

#### 1. Ticket Creation
- **Input Fields:**
  - Passenger name
  - Flight number
  - Destination
  - Departure date
  - Ticket price

- **Validation:** All fields required, price must be positive

#### 2. Extra Services
- **Available Services:**
  1. Extra Luggage (customizable weight and price)
  2. VIP Lounge Access ($45, with optional discount)
  3. Priority Boarding ($25)
  4. Car Rental (customizable days and daily rate, with optional discount)
  5. Travel Insurance ($15)

- **Features:**
  - Add multiple services to one bill
  - Optional discount application for eligible services
  - Real-time price calculation

#### 3. Payment Processing
- **Payment Methods:**
  - Credit Card
  - Debit Card
  - Cash
  - Digital Wallet

- **Features:**
  - Unique transaction ID generation
  - Payment validation
  - Transaction status display

#### 4. Bill Generation
- **Bill Contents:**
  - Bill ID (auto-generated)
  - Timestamp
  - Transaction ID
  - Itemized list of purchases
  - Individual item prices
  - Discount information (where applicable)
  - Total amount

- **Format:** Professional receipt-style output with borders and formatting

#### 5. Session Management
- **Features:**
  - Create multiple bills in one session
  - Session statistics (total bills created)
  - Graceful exit option

---

## User Interface Flow

### Main Program Flow

```
START
  │
  ├─► Display Welcome Header
  │
  ├─► CREATE NEW BILL
  │   │
  │   ├─► Input Ticket Information
  │   │   ├─ Passenger Name
  │   │   ├─ Flight Number
  │   │   ├─ Destination
  │   │   ├─ Departure Date
  │   │   └─ Ticket Price
  │   │
  │   ├─► Add Extra Services (Loop)
  │   │   ├─ Display Service Menu
  │   │   ├─ Select Service
  │   │   ├─ Enter Service Details
  │   │   ├─ Apply Discount (if applicable)
  │   │   └─ Continue? (Y/N)
  │   │
  │   ├─► Display Bill Summary
  │   │
  │   ├─► Select Payment Method
  │   │
  │   ├─► Process Payment
  │   │
  │   └─► Print Final Bill
  │
  ├─► Create Another Bill? (Y/N)
  │   ├─ Yes → Go to CREATE NEW BILL
  │   └─ No → Continue to END
  │
END
  │
  └─► Display Session Statistics
```

### Sample User Interaction

```
==============================================================
   AIRPORT BILLING MANAGEMENT SYSTEM - INTERACTIVE MODE
==============================================================

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
📋 TICKET INFORMATION
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Enter passenger name: John Smith
Enter flight number: BA456
Enter destination: London Heathrow
Enter departure date (YYYY-MM-DD): 2025-11-15
Enter ticket price ($): 850.00
✓ Ticket added successfully!

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
🛍️  EXTRA SERVICES
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Available Services:
  1. Extra Luggage
  2. VIP Lounge Access
  3. Priority Boarding
  4. Car Rental
  5. Travel Insurance
  0. Done adding services

Select service (0-5): 1
Enter luggage weight (kg): 15
Enter price per kg ($): 3.00
  ✓ Extra Luggage (15 kg) - $45.00
✓ Service added!

Add another service? (y/n): y

Select service (0-5): 2
Apply discount to lounge access? (y/n): y
Enter discount percentage: 20
  ✓ VIP Lounge Access - $36.00 (was $45.00)
✓ Service added!

Add another service? (y/n): n

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
📊 BILL SUMMARY
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Bill ID: BILL-2025-001
Total Items: 3

Items:
  ✓ Flight Ticket - John Smith to London Heathrow... - $850.00
  ✓ Extra Luggage (15 kg) - $45.00
  ✓ VIP Lounge Access - $36.00 (was $45.00)

──────────────────────────────────────────────────────────────
TOTAL AMOUNT: $931.00
──────────────────────────────────────────────────────────────

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
💳 PAYMENT METHOD
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  1. Credit Card
  2. Debit Card
  3. Cash
  4. Digital Wallet

Select payment method (1-4): 1

============================================================
PROCESSING PAYMENT
============================================================
Payment Method: Credit Card
Amount: $931.00
Transaction ID: TXN1001
Status: APPROVED
============================================================

[Final Bill Printed Here]

Would you like to create another bill? (y/n): n

==============================================================
Thank you for using Airport Billing Management System!
Total bills created: 1
==============================================================
```

---

## Technical Specifications

### System Requirements

**Minimum Requirements:**
- Java Runtime Environment (JRE) 8 or higher
- 50 MB free disk space
- Console/Terminal access

**Recommended Requirements:**
- Java Development Kit (JDK) 11 or higher
- 100 MB free disk space
- Modern terminal with UTF-8 support


### Dependencies

**Standard Library Classes Used:**
- `java.util.Scanner` - User input
- `java.util.List` - Item collections
- `java.util.ArrayList` - Dynamic arrays
- `java.time.LocalDateTime` - Timestamps
- `java.time.format.DateTimeFormatter` - Date formatting

**No External Dependencies Required**

---

## Conclusion

### Project Success Metrics

 **SOLID Principles:** All three principles successfully demonstrated
 **Code Quality:** Clean, readable, well-documented code
 **Functionality:** All required features implemented
 **User Experience:** Interactive, error-handled, user-friendly
 **Extensibility:** Easy to add new services and features
 **Maintainability:** Clear separation of concerns

### Key Achievements

1. **Comprehensive Implementation:** 14 classes/interfaces working together seamlessly
2. **Robust Input Handling:** Full validation and error handling
3. **Professional Output:** Well-formatted bills and receipts
4. **Extensible Design:** New services can be added without modifying existing code
5. **Educational Value:** Clear demonstration of SOLID principles in action


### Final Thoughts

This Airport Billing Management System successfully demonstrates how SOLID principles can be applied to create maintainable, extensible, and robust software. The system is production-ready for educational purposes and can be extended for real-world applications.

The interactive command-line interface makes it easy to use while maintaining clean code architecture. Each component has a clear responsibility, new features can be added without risk, and interfaces are minimal and focused.

---
