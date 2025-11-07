# Creational Design Patterns
# Theme: Airport Billing Management System

## Project Report - Enhanced with Design Patterns
## Author: Ștefan Istrati
---

##  Table of Contents
1. [Executive Summary](#executive-summary)
2. [Project Overview](#project-overview)
3. [SOLID Principles Implementation](#solid-principles-implementation)
4. [Creational Design Patterns](#creational-design-patterns)
5. [System Architecture](#system-architecture)
6. [Class Descriptions](#class-descriptions)
7. [Features and Functionality](#features-and-functionality)
8. [User Interface Flow](#user-interface-flow)
9. [Testing Scenarios](#testing-scenarios)
10. [Conclusion](#conclusion)

---

## Executive Summary

The Airport Billing Management System is a Java-based console application designed to manage ticket sales, extra services, and bill generation for airport customers. The system demonstrates the practical implementation of **three core SOLID principles** and **three creational design patterns**.

### Key Highlights:
-  **17+ classes/interfaces** implementing SOLID principles and Design Patterns
-  **Interactive command-line interface** for dynamic data entry
-  **Three SOLID Principles:** SRP, OCP, ISP
-  **Three Creational Patterns:** Builder, Prototype, Abstract Factory
-  **Extensible architecture** allowing easy addition of new services
-  **Robust input validation** preventing system crashes
-  **Professional bill formatting** with transaction tracking

---

## Project Overview

### Purpose
To create a maintainable, scalable, and well-structured billing system that handles:
- Flight ticket sales
- Extra service purchases (luggage, lounge access, priority boarding, car rental, insurance)
- Service packages (Economy, Business, Premium)
- Payment processing
- Bill generation and printing
- Bill cloning and reuse

### Target Users
- Airport staff
- Ticket counter operators
- Customer service representatives

---

## SOLID Principles Implementation

### 1. Single Responsibility Principle (SRP)
**Definition:** Each class should have only one reason to change.

#### Implementation:

| Class | Single Responsibility |
|-------|----------------------|
| `Ticket` | Manages ticket information only |
| `Bill` | Aggregates items and calculates totals only |
| `BillBuilder` | Constructs Bill objects only |
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
- `CarRentalService` 
- `AirportInsuranceService` 

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
    BillableItem clone(); // Prototype support
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


**Benefits:**
- Classes implement only what they need
- No unused method implementations
- More flexible and maintainable code

---

## Creational Design Patterns

### 1. Builder Pattern 

**Purpose:** Provides a fluent interface for constructing complex `Bill` objects step by step.

**Problem Solved:** 
- Constructing bills with multiple items can be complex
- Need readable and maintainable code for bill creation
- Want to separate bill construction from representation

**Implementation:**

```java
class BillBuilder {
    private Bill bill;
    
    public BillBuilder(String billId) {
        this.bill = new Bill(billId);
    }
    
    public BillBuilder addTicket(Ticket ticket) {
        bill.addItem(ticket);
        return this;
    }
    
    public BillBuilder addService(ExtraService service) {
        bill.addItem(service);
        return this;
    }
    
    public Bill build() {
        return bill;
    }
    
    public static BillBuilder newBill(String billId) {
        return new BillBuilder(billId);
    }
}
```

**Usage Example:**
```java
Bill bill = BillBuilder.newBill("BILL-001")
    .addTicket(ticket)
    .addService(luggage)
    .addService(lounge)
    .build();
```


**Real-World Use Cases:**
- Building complex bills with multiple items
- Creating bills in manual mode
- Constructing bills from packages

---

### 2. Prototype Pattern 

**Purpose:** Creates new objects by cloning existing objects, preserving their state and configuration.

**Problem Solved:**
- Frequent travelers may need similar tickets/services
- Creating identical objects from scratch is inefficient
- Need to preserve discount configurations

**Implementation:**

```java
interface BillableItem {
    String getDescription();
    double getPrice();
    BillableItem clone(); // Prototype method
}

class Ticket implements BillableItem {
    // ... fields ...
    
    @Override
    public Ticket clone() {
        return new Ticket(this.passengerName, this.flightNumber, 
                         this.destination, this.departureDate, this.price);
    }
}

class LoungeAccessService extends ExtraService {
    @Override
    public LoungeAccessService clone() {
        LoungeAccessService cloned = new LoungeAccessService();
        if (this.price < this.originalPrice) {
            cloned.applyDiscount((1 - this.price / this.originalPrice) * 100);
        }
        return cloned;
    }
}
```

**Usage Example:**
```java
// Clone entire bill for a group booking
for (BillableItem item : previousBill.getItems()) {
    BillableItem clonedItem = item.clone();
    newBill.addItem(clonedItem);
}
```


**Real-World Use Cases:**
- Group bookings (same flight, different passengers)
- Family travel packages
- Corporate booking templates
- Frequent flyer preferences

---

### 3. Abstract Factory Pattern 

**Purpose:** Creates families of related service objects without specifying their concrete classes.

**Problem Solved:**
- Need to offer predefined service packages
- Want to group related services together
- Need consistent service combinations

**Implementation:**

```java
// Abstract Factory Interface
interface ServicePackageFactory {
    ExtraService createPrimaryService();
    ExtraService createSecondaryService();
    String getPackageName();
    double getPackageDiscount();
}

// Concrete Factory 1: Economy
class EconomyPackageFactory implements ServicePackageFactory {
    @Override
    public ExtraService createPrimaryService() {
        return new LuggageService(15, 2.00);
    }
    
    @Override
    public ExtraService createSecondaryService() {
        return new AirportInsuranceService();
    }
    
    @Override
    public String getPackageName() {
        return "Economy Package";
    }
    
    @Override
    public double getPackageDiscount() {
        return 5.0; // 5% discount
    }
}

// Concrete Factory 2: Business
class BusinessPackageFactory implements ServicePackageFactory {
    @Override
    public ExtraService createPrimaryService() {
        LoungeAccessService lounge = new LoungeAccessService();
        lounge.applyDiscount(10);
        return lounge;
    }
    
    @Override
    public ExtraService createSecondaryService() {
        return new PriorityBoardingService();
    }
    
    @Override
    public String getPackageName() {
        return "Business Package";
    }
    
    @Override
    public double getPackageDiscount() {
        return 10.0;
    }
}

// Concrete Factory 3: Premium
class PremiumPackageFactory implements ServicePackageFactory {
    @Override
    public ExtraService createPrimaryService() {
        LoungeAccessService lounge = new LoungeAccessService();
        lounge.applyDiscount(15);
        return lounge;
    }
    
    @Override
    public ExtraService createSecondaryService() {
        CarRentalService carRental = new CarRentalService(3, 40.00);
        carRental.applyDiscount(20);
        return carRental;
    }
    
    @Override
    public String getPackageName() {
        return "Premium Package";
    }
    
    @Override
    public double getPackageDiscount() {
        return 15.0;
    }
}
```

**Package Comparison:**

| Package | Primary Service | Secondary Service | Discount |
|---------|----------------|-------------------|----------|
| **Economy** | Luggage (15kg) | Insurance | 5% |
| **Business** | Lounge (10% off) | Priority Boarding | 10% |
| **Premium** | Lounge (15% off) | Car Rental (20% off) | 15% |

**Usage Example:**
```java
ServicePackageFactory factory = new BusinessPackageFactory();
ExtraService primary = factory.createPrimaryService();
ExtraService secondary = factory.createSecondaryService();

builder.addService(primary)
       .addService(secondary);
```


**Real-World Use Cases:**
- Travel packages (Economy, Business, First Class)
- Seasonal promotions
- Corporate agreements
- Loyalty program tiers

---

## Design Patterns Summary

### Patterns Interaction Diagram

```
┌─────────────────────────────────────────────────────┐
│                  User Request                        │
└──────────────────────┬──────────────────────────────┘
                       │
                       ▼
         ┌─────────────────────────────┐
         │   Choose Billing Mode       │
         └─────────────┬───────────────┘
                       │
        ┌──────────────┼──────────────┐
        │              │              │
        ▼              ▼              ▼
   ┌────────┐    ┌──────────┐   ┌──────────┐
   │ Manual │    │ Package  │   │  Clone   │
   │  Mode  │    │   Mode   │   │   Mode   │
   └────┬───┘    └─────┬────┘   └─────┬────┘
        │              │              │
        │              │              │
        ▼              ▼              ▼
   ┌────────┐    ┌──────────┐   ┌──────────┐
   │BUILDER │    │ ABSTRACT │   │PROTOTYPE │
   │PATTERN │    │ FACTORY  │   │ PATTERN  │
   └────┬───┘    └─────┬────┘   └─────┬────┘
        │              │              │
        └──────────────┼──────────────┘
                       │
                       ▼
              ┌────────────────┐
              │  Bill Object   │
              └────────────────┘
```


## System Architecture

### Enhanced Component Diagram

```
┌─────────────────────────────────────────────────────────┐
│              Main Application Layer                      │
│         (AirportBillingSystem + InputHandler)           │
└────────────────────┬────────────────────────────────────┘
                     │
         ┌───────────┼───────────┬─────────────┐
         │           │           │             │
         ▼           ▼           ▼             ▼
    ┌────────┐  ┌─────────┐  ┌──────────┐ ┌──────────┐
    │ Builder│  │  Bill   │  │ Payment  │ │ Package  │
    │ Pattern│  │ Printer │  │Processor │ │Factories │
    └────┬───┘  └─────────┘  └──────────┘ └─────┬────┘
         │                                        │
         └──────────┬────────────────────────────┘
                    │
                    ▼
         ┌────────────────────┐
         │    Bill Object     │
         │ (Built by Builder) │
         └────────────────────┘
                    │
         ┌──────────┴──────────┐
         │                     │
         ▼                     ▼
    ┌────────┐         ┌────────────┐
    │ Ticket │         │ExtraService│
    │(Clone) │         │  (Clone)   │
    └────────┘         └────────────┘
                              │
                    ┌─────────┼─────────┐
                    │         │         │
                    ▼         ▼         ▼
              ┌─────────┐ ┌────────┐ ┌─────────┐
              │ Luggage │ │ Lounge │ │Priority │
              └─────────┘ └────────┘ └─────────┘
                              │
                    ┌─────────┴─────────┐
                    │                   │
                    ▼                   ▼
              ┌──────────┐        ┌──────────┐
              │   Car    │        │Insurance │
              │  Rental  │        │          │
              └──────────┘        └──────────┘
```

---

## Class Descriptions

### Interface Layer

#### 1. BillableItem (Interface)
**Purpose:** Core contract for all billable items

**Methods:**
- `String getDescription()` - Returns item description
- `double getPrice()` - Returns item price
- `BillableItem clone()` - **Prototype Pattern** support

**Implementations:** Ticket, ExtraService (and all subclasses)

**Design Pattern:** Prototype Pattern interface

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

#### 4. ServicePackageFactory (Interface)
**Purpose:** **Abstract Factory Pattern** - Creates families of services

**Methods:**
- `ExtraService createPrimaryService()` - Creates main service
- `ExtraService createSecondaryService()` - Creates additional service
- `String getPackageName()` - Returns package name
- `double getPackageDiscount()` - Returns package discount

**Implementations:** EconomyPackageFactory, BusinessPackageFactory, PremiumPackageFactory

**Design Pattern:** Abstract Factory Pattern interface

---

### Builder Layer

#### 5. BillBuilder (Class)
**Purpose:** **Builder Pattern** - Fluent interface for building Bills

**Attributes:**
- `bill: Bill` - The bill being constructed

**Key Methods:**
- `BillBuilder addTicket(Ticket ticket)` - Adds ticket, returns this
- `BillBuilder addService(ExtraService service)` - Adds service, returns this
- `BillBuilder addItem(BillableItem item)` - Adds any item, returns this
- `Bill build()` - Returns completed bill
- `static BillBuilder newBill(String billId)` - Factory method

**Design Pattern:** Builder Pattern

**Usage:**
```java
Bill bill = BillBuilder.newBill("BILL-001")
    .addTicket(ticket)
    .addService(luggage)
    .addService(lounge)
    .build();
```

---

### Model Layer

#### 6. Ticket (Class)
**Purpose:** Represents a flight ticket with cloning support

**Attributes:**
- `passengerName: String`
- `flightNumber: String`
- `destination: String`
- `departureDate: String`
- `price: double`

**Key Methods:**
- Constructor with all ticket details
- `Ticket clone()` - **Prototype Pattern** implementation
- `void setPassengerName(String name)` - Modify cloned ticket
- Implements BillableItem and Printable interfaces

**Design Pattern:** Prototype Pattern
**SOLID Principle:** SRP - Only manages ticket data

---

#### 7. Bill (Class)
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

#### 8. ExtraService (Abstract Class)
**Purpose:** Base class for all extra services with cloning support

**Attributes:**
- `serviceName: String`
- `price: double`

**Key Methods:**
- Constructor for service initialization
- `abstract ExtraService clone()` - **Prototype Pattern** (abstract)
- Implements BillableItem and Printable interfaces

**Design Pattern:** Prototype Pattern (abstract method)
**SOLID Principle:** OCP - Open for extension, closed for modification

---

#### 9-13. Service Subclasses
All service classes implement `clone()` method for Prototype Pattern:

- **LuggageService** - Clones with weight and price per kg
- **LoungeAccessService** - Clones with preserved discounts
- **PriorityBoardingService** - Simple cloning
- **CarRentalService** - Clones with days, rates, and discounts
- **AirportInsuranceService** - Simple cloning

**Design Pattern:** Prototype Pattern implementation

---

### Factory Layer

#### 14. EconomyPackageFactory (Class)
**Purpose:** Creates Economy package services

**Services Created:**
- Primary: Luggage (15kg @ $2/kg) = $30
- Secondary: Insurance = $15
- Package Discount: 5%

**Design Pattern:** Abstract Factory Pattern (Concrete Factory)

---

#### 15. BusinessPackageFactory (Class)
**Purpose:** Creates Business package services

**Services Created:**
- Primary: Lounge Access (10% off) = $40.50
- Secondary: Priority Boarding = $25
- Package Discount: 10%

**Design Pattern:** Abstract Factory Pattern (Concrete Factory)

---

#### 16. PremiumPackageFactory (Class)
**Purpose:** Creates Premium package services

**Services Created:**
- Primary: Lounge Access (15% off) = $38.25
- Secondary: Car Rental 3 days (20% off) = $96
- Package Discount: 15%

**Design Pattern:** Abstract Factory Pattern (Concrete Factory)

---

### Processing Layer

#### 17. PaymentProcessor (Class)
**Purpose:** Handles payment processing

**Attributes:**
- `transactionCounter: static int` - Generates unique transaction IDs

**Key Methods:**
- `processPayment(Bill bill, String paymentMethod)` - Processes payment
- `validatePayment(String paymentMethod, double amount)` - Validates payment

**SOLID Principle:** SRP - Only handles payment processing

---

#### 18. BillPrinter (Class)
**Purpose:** Formats and prints bills

**Key Methods:**
- `printBill(Bill bill, String transactionId)` - Prints formatted bill
- `printItemSummary(BillableItem item)` - Prints item summary
- `centerText(String text, int width)` - Helper for text formatting

**SOLID Principle:** SRP - Only handles printing and formatting

---

#### 19. InputHandler (Class)
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

#### 1. Three Billing Modes

**A. Manual Mode** (Builder Pattern)
- Enter ticket information manually
- Add services one by one
- Full customization of bill

**B. Package Mode** (Abstract Factory Pattern)
- Choose from predefined packages:
  - Economy Package (Budget-friendly)
  - Business Package (Professional)
  - Premium Package (Luxury)
- Services automatically configured
- Package discounts applied

**C. Clone Mode** (Prototype Pattern)
- Clone previous bill
- Modify passenger names
- Add additional services
- Reuse configurations

#### 2. Ticket Creation
- **Input Fields:**
  - Passenger name
  - Flight number
  - Destination
  - Departure date
  - Ticket price

- **Validation:** All fields required, price must be positive
- **Cloning Support:** Tickets can be cloned for group bookings

#### 3. Extra Services
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
  - **Cloning support** for all services

#### 4. Service Packages 

**Economy Package:**
- Extra Luggage (15kg)
- Travel Insurance
- 5% package discount
- **Total Value:** ~$42.75 (after discount)

**Business Package:**
- VIP Lounge Access (with 10% off)
- Priority Boarding
- 10% package discount
- **Total Value:** ~$59 (after discount)

**Premium Package:**
- VIP Lounge Access (with 15% off)
- Car Rental 3 days (with 20% off)
- 15% package discount
- **Total Value:** ~$114 (after discount)

#### 5. Payment Processing
- **Payment Methods:**
  - Credit Card
  - Debit Card
  - Cash
  - Digital Wallet

- **Features:**
  - Unique transaction ID generation
  - Payment validation
  - Transaction status display

#### 6. Bill Generation
- **Bill Contents:**
  - Bill ID (auto-generated)
  - Timestamp
  - Transaction ID
  - Itemized list of purchases
  - Individual item prices
  - Discount information (where applicable)
  - Package information (if applicable)
  - Total amount

- **Format:** Professional receipt-style output with borders and formatting

#### 7. Session Management
- **Features:**
  - Create multiple bills in one session
  - Session statistics (total bills created)
  - Graceful exit option
  - **Bill history** for cloning

---

## User Interface Flow

### Enhanced Main Program Flow

```
START
  │
  ├─► Display Welcome Header
  ├─► Display Design Patterns Info
  │
  ├─► SELECT BILLING MODE
  │   ├─ 1. Manual Mode (Builder Pattern)
  │   ├─ 2. Package Mode (Abstract Factory)
  │   └─ 3. Clone Mode (Prototype Pattern)
  │
  ├─► MODE 1: MANUAL
  │   ├─► Input Ticket Information
  │   ├─► Add Extra Services (Loop)
  │   └─► Build Bill with BillBuilder
  │
  ├─► MODE 2: PACKAGE
  │   ├─► Select Package (Economy/Business/Premium)
  │   ├─► Factory creates services
  │   ├─► Input Ticket Information
  │   └─► Build Bill with services from factory
  │
  ├─► MODE 3: CLONE
  │   ├─► Display previous bill
  │   ├─► Confirm cloning
  │   ├─► Clone all items (Prototype Pattern)
  │   ├─► Optionally modify passenger name
  │   └─► Optionally add more services
  │
  ├─► Display Bill Summary
  ├─► Select Payment Method
  ├─► Process Payment
  ├─► Print Final Bill
  │
  ├─► Create Another Bill? (Y/N)
  │   ├─ Yes → Go to SELECT BILLING MODE
  │   └─ No → Continue to END
  │
END
  │
  └─► Display Session Statistics
```

### Sample User Interactions

#### Mode 1: Manual Mode (Builder Pattern)
```
==============================================================
AIRPORT BILLING MANAGEMENT SYSTEM
Featuring: SOLID Principles & Design Patterns
==============================================================

 DESIGN PATTERNS IMPLEMENTED:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
1️  BUILDER PATTERN:
   • BillBuilder for fluent bill creation
   • Chain methods: addTicket().addService().build()

2️  PROTOTYPE PATTERN:
   • Clone existing tickets and services
   • Reuse configurations for similar bookings

3️  ABSTRACT FACTORY PATTERN:
   • ServicePackageFactory for service bundles
   • Economy, Business, and Premium packages
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

==============================================================
Choose billing mode:
  1. Manual Mode (enter each item)
  2. Package Mode (use predefined packages)
  3. Clone Previous Bill (Prototype Pattern)
==============================================================
Select mode (1-3): 1

 MANUAL MODE - Build your bill step by step

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 TICKET INFORMATION
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Enter passenger name: Sarah Johnson
Enter flight number: DL123
Enter destination: Paris CDG
Enter departure date (YYYY-MM-DD): 2025-12-01
Enter ticket price ($): 1200.00
✓ Ticket added successfully!

[Services menu and selection...]

✓ Bill built using Builder Pattern!
```

#### Mode 2: Package Mode (Abstract Factory)
```
Select mode (1-3): 2

 PACKAGE MODE - Choose a service package
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Available Packages (Abstract Factory Pattern):
  1. Economy Package
     • Extra Luggage (15kg)
     • Travel Insurance
     • 5% package discount

  2. Business Package
     • VIP Lounge Access (10% off)
     • Priority Boarding
     • 10% package discount

  3. Premium Package
     • VIP Lounge Access (15% off)
     • Car Rental 3 days (20% off)
     • 15% package discount
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Select package (1-3): 2

✓ Selected: Business Package

[Ticket information entry...]

✓ Package services added:
  ✓ VIP Lounge Access - $40.50 (was $45.00)
  ✓ Priority Boarding - $25.00
✓ Package discount: 10.0%
```

#### Mode 3: Clone Mode (Prototype Pattern)
```
Select mode (1-3): 3

 PROTOTYPE MODE - Clone previous bill
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Previous bill contains 4 items:
  ✓ Flight Ticket - Sarah Johnson to Paris CDG... - $1200.00
  ✓ VIP Lounge Access - $40.50 (was $45.00)
  ✓ Priority Boarding - $25.00
  ✓ Extra Luggage (10 kg) - $20.00
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Clone this bill? (y/n): y

 Cloning items...
Change passenger name? (y/n): y
Enter new passenger name: Michael Johnson
✓ Passenger name updated
✓ Bill cloned successfully!

Add more services? (y/n): n

✓ Clone complete! All items copied with Prototype Pattern
```

---





## Conclusion

### Project Success Metrics

 **SOLID Principles:** All three principles successfully demonstrated
 **Design Patterns:** Three creational patterns fully implemented
 **Code Quality:** Clean, readable, well-documented code
 **Functionality:** All required features + pattern features implemented
 **User Experience:** Interactive, error-handled, user-friendly
 **Extensibility:** Easy to add new services and features
 **Maintainability:** Clear separation of concerns
 **Reusability:** Patterns enable code reuse and flexibility

### Key Achievements

1. **Comprehensive Implementation:** 20+ classes/interfaces working together seamlessly
2. **Pattern Integration:** Three creational patterns working in harmony
3. **Robust Input Handling:** Full validation and error handling
4. **Professional Output:** Well-formatted bills and receipts
5. **Extensible Design:** New services and packages can be added without modifying existing code
6. **Educational Value:** Clear demonstration of SOLID principles AND design patterns in action
7. **Real-World Application:** Patterns solve actual business problems


### Final Thoughts

This Airport Billing Management System successfully demonstrates how SOLID principles and Design Patterns work together to create maintainable, extensible, and robust software. The system showcases:

1. **SOLID Foundation:** Three core principles ensure clean architecture
2. **Pattern Layer:** Three creational patterns add flexibility and reusability
3. **Business Value:** Patterns solve real business needs (packages, cloning, building)
4. **Educational Excellence:** Clear examples of when and why to use each pattern

The combination of SOLID principles and design patterns creates a production-ready system that is:
- **Easy to understand** - Clear responsibilities and patterns
- **Easy to extend** - New features don't require code changes
- **Easy to maintain** - Changes are isolated and safe
- **Easy to test** - Patterns enable better testing
- **Easy to scale** - Architecture supports growth

This project serves as an excellent reference for learning and applying both SOLID principles and design patterns in real-world scenarios.

---

## Appendix

### A. Complete Class Hierarchy with Patterns

```
Object
│
├── BillableItem (interface) [PROTOTYPE SUPPORT]
│   ├── Ticket [PROTOTYPE IMPL]
│   └── ExtraService (abstract) [PROTOTYPE SUPPORT]
│       ├── LuggageService [PROTOTYPE IMPL]
│       ├── LoungeAccessService [PROTOTYPE IMPL]
│       ├── PriorityBoardingService [PROTOTYPE IMPL]
│       ├── CarRentalService [PROTOTYPE IMPL]
│       └── AirportInsuranceService [PROTOTYPE IMPL]
│
├── Printable (interface)
│   └── [Implemented by Ticket and all ExtraService subclasses]
│
├── DiscountApplicable (interface)
│   ├── LoungeAccessService
│   └── CarRentalService
│
├── ServicePackageFactory (interface) [ABSTRACT FACTORY]
│   ├── EconomyPackageFactory [CONCRETE FACTORY]
│   ├── BusinessPackageFactory [CONCRETE FACTORY]
│   └── PremiumPackageFactory [CONCRETE FACTORY]
│
├── BillBuilder [BUILDER PATTERN]
├── Bill
├── PaymentProcessor
├── BillPrinter
├── InputHandler
└── AirportBillingSystem (main)
```



### B. Real-World Applications

**Builder Pattern:**
- Complex order systems (e-commerce)
- Document generation
- SQL query builders
- Report builders

**Prototype Pattern:**
- Game object cloning
- Document templates
- Configuration presets
- UI component libraries

**Abstract Factory Pattern:**
- UI themes (Light/Dark)
- Database drivers (MySQL/PostgreSQL)
- Platform-specific components (Windows/Mac/Linux)
- Payment gateways (Stripe/PayPal/Square)

### C . Glossary

**Design Pattern Terms:**
- **Creational Pattern:** Patterns that deal with object creation
- **Builder:** Constructs complex objects step by step
- **Prototype:** Creates objects by cloning existing ones
- **Abstract Factory:** Creates families of related objects
- **Fluent Interface:** Method chaining for readability
- **Factory Method:** Creates objects without specifying exact class
- **Concrete Factory:** Specific implementation of factory

**SOLID Terms:**
- **SRP:** Single Responsibility Principle
- **OCP:** Open-Closed Principle
- **LSP:** Liskov Substitution Principle
- **ISP:** Interface Segregation Principle
- **DIP:** Dependency Inversion Principle

**System Terms:**
- **Billable Item:** Any product or service that can be added to a bill
- **Extra Service:** Additional services beyond the basic flight ticket
- **Service Package:** Bundled services at discounted rates
- **Transaction ID:** Unique identifier for each payment transaction
- **Bill ID:** Unique identifier for each bill generated
- **Clone:** Duplicate copy of an object created via Prototype pattern

---

