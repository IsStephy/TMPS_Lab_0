package com.fileviewer;

import com.fileviewer.adapters.*;
import com.fileviewer.composite.Directory;
import com.fileviewer.composite.File;
import com.fileviewer.decorators.*;
import com.fileviewer.interfaces.ReadableFile;

/**
 * Main application demonstrating the File Viewer system
 * Showcases Adapter, Composite, and Decorator patterns
 */
public class FileViewerApp {

    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        boolean running = true;

        printHeader();

        while (running) {
            printMenu();
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();
            System.out.println();

            switch (choice) {
                case "1":
                    demonstrateAdapterPattern();
                    break;
                case "2":
                    demonstrateCompositePattern();
                    break;
                case "3":
                    demonstrateDecoratorPattern();
                    break;
                case "4":
                    demonstrateAllPatternsTogether();
                    break;
                case "5":
                    LoggingDecorator.printAccessLog();
                    break;
                case "6":
                    LoggingDecorator.clearAccessLog();
                    System.out.println("✓ Access log cleared!");
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }

            if (running && !choice.equals("9")) {
                System.out.println("\n" + "═".repeat(70) + "\n");
                System.out.print("Press Enter to continue...");
                scanner.nextLine();
                System.out.println();
            }
        }

        scanner.close();
    }

    /**
     * Print application header
     */
    private static void printHeader() {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          FILE VIEWER - Structural Design Patterns Demo         ║");
        System.out.println("║                                                                ║");
        System.out.println("║  Patterns: Adapter, Composite, Decorator                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
    }

    /**
     * Print interactive menu
     */
    private static void printMenu() {
        System.out.println("┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│                        MAIN MENU                               │");
        System.out.println("├────────────────────────────────────────────────────────────────┤");
        System.out.println("│  Pattern Demonstrations:                                      │");
        System.out.println("│    1. Adapter Pattern Demo                                    │");
        System.out.println("│    2. Composite Pattern Demo                                  │");
        System.out.println("│    3. Decorator Pattern Demo                                  │");
        System.out.println("│    4. All Patterns Working Together                           │");
        System.out.println("│                                                                │");
        System.out.println("│  Utilities:                                                    │");
        System.out.println("│    5. View Access Log                                         │");
        System.out.println("│    6. Clear Access Log                                        │");
        System.out.println("│                                                                │");
        System.out.println("│    0. Exit                                                     │");
        System.out.println("└────────────────────────────────────────────────────────────────┘");
    }

    /**
     * Demonstrate the Adapter Pattern
     * Different file types adapted to a common ReadableFile interface
     */
    private static void demonstrateAdapterPattern() {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│             ADAPTER PATTERN DEMONSTRATION                   │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        // Create different file types using adapters
        ReadableFile txtFile = new TxtFileAdapter(
                "readme.txt",
                "This is a simple text file.\nIt contains plain text content.\nAdapter Pattern Example!"
        );

        ReadableFile jsonFile = new JsonFileAdapter(
                "config.json",
                "{\"app\":\"FileViewer\",\"version\":\"1.0\",\"features\":[\"Adapter\",\"Composite\",\"Decorator\"]}"
        );

        ReadableFile xmlFile = new XmlFileAdapter(
                "data.xml",
                "<root><app>FileViewer</app><version>1.0</version><features><feature>Adapter</feature><feature>Composite</feature><feature>Decorator</feature></features></root>"
        );

        ReadableFile csvFile = new CsvFileAdapter(
                "data.csv",
                "Name,Type,Size\nreadme.txt,TXT,45\nconfig.json,JSON,112\ndata.xml,XML,158"
        );

        System.out.println("📋 Files created using different adapters:");
        System.out.println("  • " + txtFile);
        System.out.println("  • " + jsonFile);
        System.out.println("  • " + xmlFile);
        System.out.println("  • " + csvFile);

        System.out.println("\n📄 Reading JSON file content:");
        System.out.println(jsonFile.readContent());
    }

    /**
     * Demonstrate the Composite Pattern
     * File system tree structure with directories and files
     */
    private static void demonstrateCompositePattern() {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│            COMPOSITE PATTERN DEMONSTRATION                  │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        // Create root directory
        Directory root = new Directory("root");

        // Create subdirectories
        Directory documents = new Directory("documents");
        Directory projects = new Directory("projects");
        Directory config = new Directory("config");

        // Create files
        File readme = new File(new TxtFileAdapter("README.md", "# File Viewer Project\nA demonstration of structural patterns."));
        File mainConfig = new File(new JsonFileAdapter("app.json", "{\"name\":\"FileViewer\",\"version\":\"1.0\"}"));
        File dataXml = new File(new XmlFileAdapter("data.xml", "<data><item>Test</item></data>"));
        File reportCsv = new File(new CsvFileAdapter("report.csv", "ID,Name,Value\n1,Test,100\n2,Demo,200"));

        File projectFile1 = new File(new TxtFileAdapter("notes.txt", "Project notes and ideas"));
        File projectFile2 = new File(new JsonFileAdapter("settings.json", "{\"theme\":\"dark\"}"));

        // Build the tree structure
        root.add(readme);
        root.add(documents);
        root.add(projects);
        root.add(config);

        documents.add(reportCsv);
        documents.add(dataXml);

        projects.add(projectFile1);
        projects.add(projectFile2);

        config.add(mainConfig);

        System.out.println("📂 File System Structure:");
        root.display("");

        System.out.println("\n📊 Statistics:");
        System.out.println("  Total Files: " + root.getFileCount());
        System.out.println("  Total Directories: " + root.getDirectoryCount());
        System.out.println("  Total Size: " + root.getSize() + " bytes");

        // Demonstrate file search
        System.out.println("\n🔍 Searching for 'app.json':");
        File found = root.findFile("app.json");
        if (found != null) {
            System.out.println("  Found: " + found.getName());
        }
    }

    /**
     * Demonstrate the Decorator Pattern
     * Adding dynamic features to files
     */
    private static void demonstrateDecoratorPattern() {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│            DECORATOR PATTERN DEMONSTRATION                  │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        // Create a base file
        ReadableFile baseFile = new TxtFileAdapter(
                "secret.txt",
                "This is confidential information.\nIt should be protected and compressed."
        );

        System.out.println("📄 Original file:");
        System.out.println("  " + baseFile);
        System.out.println("  Size: " + baseFile.getSize() + " bytes\n");

        // Apply compression decorator
        ReadableFile compressedFile = new CompressionDecorator(baseFile);
        System.out.println("🗜️  After Compression:");
        System.out.println("  " + compressedFile);
        System.out.println("  Size: " + compressedFile.getSize() + " bytes\n");

        // Apply encryption decorator
        ReadableFile encryptedFile = new EncryptionDecorator(baseFile, 7);
        System.out.println("🔒 After Encryption:");
        System.out.println("  " + encryptedFile);
        System.out.println("  Size: " + encryptedFile.getSize() + " bytes\n");

        // Apply logging decorator
        ReadableFile loggedFile = new LoggingDecorator(baseFile);
        System.out.println("📝 After Logging:");
        System.out.println("  " + loggedFile);

        // Apply read-only decorator
        ReadableFile readOnlyFile = new ReadOnlyDecorator(baseFile);
        System.out.println("\n🔐 After Read-Only Protection:");
        System.out.println("  " + readOnlyFile);

        // Stack multiple decorators
        System.out.println("\n🎯 Combining Multiple Decorators:");
        ReadableFile fullyDecorated = new LoggingDecorator(
                new CompressionDecorator(
                        new EncryptionDecorator(
                                new ReadOnlyDecorator(baseFile),
                                13
                        )
                )
        );
        System.out.println("  Decorators: ReadOnly → Encryption → Compression → Logging");
        System.out.println("  " + fullyDecorated);

        System.out.println("\n📖 Reading fully decorated file:");
        String content = fullyDecorated.readContent();
        System.out.println(content.substring(0, Math.min(300, content.length())) + "...");
    }

    /**
     * Demonstrate all patterns working together
     */
    private static void demonstrateAllPatternsTogether() {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│        ALL PATTERNS WORKING TOGETHER                        │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");

        // Create a complete file system with decorated files
        Directory root = new Directory("SecureFileSystem");
        Directory publicDir = new Directory("public");
        Directory privateDir = new Directory("private");

        // Public files - with logging
        File publicFile1 = new File(
                new LoggingDecorator(
                        new TxtFileAdapter("announcement.txt", "Public announcement for all users.")
                )
        );

        File publicFile2 = new File(
                new LoggingDecorator(
                        new JsonFileAdapter("public-config.json", "{\"access\":\"public\"}")
                )
        );

        // Private files - encrypted, compressed, and logged
        File privateFile1 = new File(
                new LoggingDecorator(
                        new CompressionDecorator(
                                new EncryptionDecorator(
                                        new ReadOnlyDecorator(
                                                new TxtFileAdapter("confidential.txt", "Top secret information!")
                                        ),
                                        17
                                )
                        )
                )
        );

        File privateFile2 = new File(
                new LoggingDecorator(
                        new EncryptionDecorator(
                                new XmlFileAdapter("secure-data.xml", "<data><secret>classified</secret></data>"),
                                23
                        )
                )
        );

        // Build structure
        publicDir.add(publicFile1);
        publicDir.add(publicFile2);
        privateDir.add(privateFile1);
        privateDir.add(privateFile2);
        root.add(publicDir);
        root.add(privateDir);

        System.out.println("📂 Complete File System:");
        root.display("");

        System.out.println("\n📖 Opening a private file:");
        System.out.println(privateFile1.open().substring(0, 400) + "...\n");

        System.out.println("✅ Demonstration complete! All three patterns working together:");
        System.out.println("  • ADAPTER: Different file types (TXT, JSON, XML) with common interface");
        System.out.println("  • COMPOSITE: Hierarchical file/directory tree structure");
        System.out.println("  • DECORATOR: Dynamic features (encryption, compression, logging)");
    }
}