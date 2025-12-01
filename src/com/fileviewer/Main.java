package com.fileviewer;

import com.fileviewer.strategy.*;
import com.fileviewer.template.*;
import com.fileviewer.chain.*;
import com.fileviewer.utils.FileGenerator;

import java.util.Scanner;


public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static String currentFile = "";

    public static void main(String[] args) {
        try {
            System.out.println("Generating sample test files...");
            FileGenerator.generateAllSampleFiles();
            System.out.println();

            boolean running = true;
            while (running) {
                displayMainMenu();
                int choice = getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1 -> demonstrateStrategyPattern();
                    case 2 -> demonstrateTemplateMethodPattern();
                    case 3 -> demonstrateChainOfResponsibility();
                    case 4 -> demonstrateAllPatterns();
                    case 5 -> selectFile();
                    case 0 -> {
                        System.out.println("\nThank you for using the Behavioral Patterns Demo!");
                        running = false;
                    }
                    default -> System.out.println("\n❌ Invalid choice. Please try again.\n");
                }
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void displayMainMenu() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║     BEHAVIORAL DESIGN PATTERNS DEMONSTRATION                 ║");
        System.out.println("║     Technical University of Moldova                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Current file: " + (currentFile.isEmpty() ? "[None selected]" : currentFile));
        System.out.println();
        System.out.println("┌──────────────────────────────────────────────────────────────┐");
        System.out.println("│  PATTERN DEMONSTRATIONS                                      │");
        System.out.println("├──────────────────────────────────────────────────────────────┤");
        System.out.println("│  1. Strategy Pattern - File Reading Strategies               │");
        System.out.println("│  2. Template Method Pattern - File Viewers                   │");
        System.out.println("│  3. Chain of Responsibility - File Validation                │");
        System.out.println("│  4. Demonstrate All Patterns Together                        │");
        System.out.println("├──────────────────────────────────────────────────────────────┤");
        System.out.println("│  5. Select File                                              │");
        System.out.println("│  0. Exit                                                     │");
        System.out.println("└──────────────────────────────────────────────────────────────┘");
        System.out.println();
    }

    private static void demonstrateStrategyPattern() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("STRATEGY PATTERN DEMONSTRATION");
        System.out.println("=".repeat(70));
        System.out.println("\nThe Strategy pattern defines a family of algorithms,");
        System.out.println("encapsulates each one, and makes them interchangeable.");
        System.out.println();

        if (currentFile.isEmpty()) {
            System.out.println("❌ No file selected. Using default: test-files/sample.txt");
            currentFile = "test-files/sample.txt";
        }

        try {
            // Create FileReader context
            FileReader reader = new FileReader();

            // Menu for strategy selection
            System.out.println("\nAvailable Reading Strategies:");
            System.out.println("1. Text Reading Strategy");
            System.out.println("2. JSON Reading Strategy");
            System.out.println("3. XML Reading Strategy");
            System.out.println("4. PDF Reading Strategy");
            System.out.println("5. Try all strategies");

            int choice = getIntInput("\nSelect strategy: ");

            FileReadStrategy[] strategies = {
                    new TextReadStrategy(),
                    new JsonReadStrategy(),
                    new XmlReadStrategy(),
                    new PdfReadStrategy()
            };

            if (choice >= 1 && choice <= 4) {
                // Use selected strategy
                reader.setStrategy(strategies[choice - 1]);
                System.out.println("\n" + "-".repeat(70));
                String content = reader.readFile(currentFile);
                System.out.println(content);
                System.out.println("-".repeat(70));

            } else if (choice == 5) {
                // Try all strategies
                for (FileReadStrategy strategy : strategies) {
                    reader.setStrategy(strategy);
                    System.out.println("\n" + "-".repeat(70));
                    try {
                        String content = reader.readFile(currentFile);
                        System.out.println(content);
                    } catch (Exception e) {
                        System.out.println("Error with " + strategy.getStrategyName() + ": " + e.getMessage());
                    }
                    System.out.println("-".repeat(70));
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }

        waitForEnter();
    }

    private static void demonstrateTemplateMethodPattern() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEMPLATE METHOD PATTERN DEMONSTRATION");
        System.out.println("=".repeat(70));
        System.out.println("\nThe Template Method pattern defines the skeleton of an algorithm");
        System.out.println("in a method, deferring some steps to subclasses.");
        System.out.println();

        if (currentFile.isEmpty()) {
            System.out.println("❌ No file selected. Using default: test-files/sample.txt");
            currentFile = "test-files/sample.txt";
        }

        System.out.println("\nAvailable File Viewers:");
        System.out.println("1. Text Viewer");
        System.out.println("2. JSON Viewer");
        System.out.println("3. PDF Viewer");
        System.out.println("4. Try all viewers");

        int choice = getIntInput("\nSelect viewer: ");

        AbstractFileViewer[] viewers = {
                new TxtViewer(),
                new JsonViewer(),
                new PdfViewer()
        };

        if (choice >= 1 && choice <= 3) {
            // Use selected viewer
            AbstractFileViewer viewer = viewers[choice - 1];
            System.out.println("\nUsing: " + viewer.getViewerType());
            viewer.viewFile(currentFile);

        } else if (choice == 4) {
            // Try all viewers
            for (AbstractFileViewer viewer : viewers) {
                System.out.println("\nUsing: " + viewer.getViewerType());
                viewer.viewFile(currentFile);
                System.out.println();
            }
        }

        waitForEnter();
    }


    private static void demonstrateChainOfResponsibility() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("CHAIN OF RESPONSIBILITY PATTERN DEMONSTRATION");
        System.out.println("=".repeat(70));
        System.out.println("\nThe Chain of Responsibility pattern passes requests along a chain");
        System.out.println("of handlers. Each handler decides to process or pass to the next.");
        System.out.println();

        if (currentFile.isEmpty()) {
            System.out.println("Please enter a file path to validate:");
            currentFile = scanner.nextLine().trim();
        }

        System.out.println("\nFile to validate: " + currentFile);
        System.out.println("\nBuilding validation chain...");

        // Create the chain of handlers
        FileValidationHandler chain = new FileExistsHandler();
        chain.setNext(new FileTypeHandler())
                .setNext(new FileSizeHandler(10)) // 10 MB limit
                .setNext(new FileReadableHandler());

        System.out.println("\nValidation Chain:");
        System.out.println("1. File Existence Check");
        System.out.println("2. File Type Check");
        System.out.println("3. File Size Check");
        System.out.println("4. File Readability Check");

        System.out.println("\n" + "-".repeat(70));
        System.out.println("Starting validation...");
        System.out.println("-".repeat(70));

        // Start the validation chain
        ValidationResult result = chain.handle(currentFile);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("VALIDATION RESULT");
        System.out.println("=".repeat(70));
        System.out.println(result);

        if (result.isSuccess()) {
            System.out.println("\n✅ All validations passed! File is ready for processing.");
        } else {
            System.out.println("\n❌ Validation failed! Chain stopped at: " + result.getHandlerName());
        }

        System.out.println("=".repeat(70));

        waitForEnter();
    }


    private static void demonstrateAllPatterns() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("INTEGRATED DEMONSTRATION - ALL PATTERNS");
        System.out.println("=".repeat(70));
        System.out.println("\nThis demonstration shows how all three behavioral patterns");
        System.out.println("can work together in a file processing system.");
        System.out.println();

        if (currentFile.isEmpty()) {
            System.out.println("❌ No file selected. Using default: test-files/sample.txt");
            currentFile = "test-files/sample.txt";
        }

        System.out.println("Processing file: " + currentFile);
        System.out.println();

        // STEP 1: Chain of Responsibility - Validate file
        System.out.println("═══ STEP 1: FILE VALIDATION (Chain of Responsibility) ═══");
        FileValidationHandler chain = new FileExistsHandler();
        chain.setNext(new FileTypeHandler())
                .setNext(new FileSizeHandler())
                .setNext(new FileReadableHandler());

        ValidationResult validationResult = chain.handle(currentFile);
        System.out.println("\n" + validationResult);

        if (!validationResult.isSuccess()) {
            System.out.println("\n❌ Cannot proceed - validation failed!");
            waitForEnter();
            return;
        }

        System.out.println("\n✅ Validation successful! Proceeding to next step...\n");

        System.out.println("═══ STEP 2: FILE READING (Strategy Pattern) ═══");

        String extension = currentFile.substring(currentFile.lastIndexOf('.') + 1).toLowerCase();
        FileReadStrategy strategy = switch (extension) {
            case "json" -> new JsonReadStrategy();
            case "xml" -> new XmlReadStrategy();
            case "pdf" -> new PdfReadStrategy();
            default -> new TextReadStrategy();
        };

        FileReader reader = new FileReader(strategy);

        try {
            String content = reader.readFile(currentFile);
            System.out.println("\n✅ File read successfully!\n");

            // STEP 3: Template Method - View file with appropriate viewer
            System.out.println("═══ STEP 3: FILE VIEWING (Template Method Pattern) ═══");

            AbstractFileViewer viewer = switch (extension) {
                case "json" -> new JsonViewer();
                case "pdf" -> new PdfViewer();
                default -> new TxtViewer();
            };

            viewer.viewFile(currentFile);

            System.out.println("\n" + "=".repeat(70));
            System.out.println("✅ ALL PATTERNS EXECUTED SUCCESSFULLY!");
            System.out.println("=".repeat(70));
            System.out.println("\nPattern Flow:");
            System.out.println("1. ✓ Chain of Responsibility validated the file");
            System.out.println("2. ✓ Strategy Pattern read the file with " + strategy.getStrategyName());
            System.out.println("3. ✓ Template Method displayed the file with " + viewer.getViewerType());

        } catch (Exception e) {
            System.err.println("❌ Error during processing: " + e.getMessage());
        }

        waitForEnter();
    }

    private static void selectFile() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("FILE SELECTION");
        System.out.println("=".repeat(70));
        System.out.println("\nAvailable sample files:");
        System.out.println("1. test-files/sample.txt");
        System.out.println("2. test-files/sample.json");
        System.out.println("3. test-files/sample.xml");
        System.out.println("4. test-files/sample.pdf");
        System.out.println("5. Enter custom file path");

        int choice = getIntInput("\nSelect file: ");

        currentFile = switch (choice) {
            case 1 -> "test-files/sample.txt";
            case 2 -> "test-files/sample.json";
            case 3 -> "test-files/sample.xml";
            case 4 -> "test-files/sample.pdf";
            case 5 -> {
                System.out.print("Enter file path: ");
                yield scanner.nextLine().trim();
            }
            default -> currentFile;
        };

        System.out.println("\n✅ Selected file: " + currentFile + "\n");
        waitForEnter();
    }


    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }


    private static void waitForEnter() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}