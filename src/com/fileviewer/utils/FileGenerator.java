package com.fileviewer.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileGenerator {

    private static final String TEST_FILES_DIR = "test-files";


    public static void generateAllSampleFiles() throws IOException {
        // Create test files directory
        Path dir = Paths.get(TEST_FILES_DIR);
        if (!Files.exists(dir)) {
            Files.createDirectory(dir);
        }

        generateTextFile();
        generateJsonFile();
        generateXmlFile();
        generatePdfFile();

        System.out.println("✓ All sample files generated in '" + TEST_FILES_DIR + "/' directory");
    }


    private static void generateTextFile() throws IOException {
        String content = """
                Behavioral Design Patterns Laboratory
                =====================================
                
                This is a sample text file for testing the behavioral design patterns.
                
                Patterns Implemented:
                1. Strategy Pattern - Different file reading strategies
                2. Template Method Pattern - File viewer algorithm skeleton
                3. Chain of Responsibility - File validation handlers
                
                The Strategy pattern defines a family of algorithms, encapsulates each one,
                and makes them interchangeable. Strategy lets the algorithm vary independently
                from clients that use it.
                
                The Template Method pattern defines the skeleton of an algorithm in a method,
                deferring some steps to subclasses. Template Method lets subclasses redefine
                certain steps of an algorithm without changing the algorithm's structure.
                
                The Chain of Responsibility pattern avoids coupling the sender of a request
                to its receiver by giving more than one object a chance to handle the request.
                Chain the receiving objects and pass the request along the chain until an
                object handles it.
                
                These patterns promote loose coupling and flexibility in software design.
                """;

        writeFile(TEST_FILES_DIR + "/sample.txt", content);
    }

    /**
     * Generate sample JSON file
     */
    private static void generateJsonFile() throws IOException {
        String content = """
                {
                  "course": "Software Design Patterns",
                  "university": "Technical University of Moldova",
                  "laboratory": {
                    "number": 3,
                    "topic": "Behavioral Design Patterns",
                    "patterns": [
                      {
                        "name": "Strategy",
                        "category": "Behavioral",
                        "purpose": "Define a family of algorithms, encapsulate each one, and make them interchangeable"
                      },
                      {
                        "name": "Template Method",
                        "category": "Behavioral",
                        "purpose": "Define the skeleton of an algorithm, deferring some steps to subclasses"
                      },
                      {
                        "name": "Chain of Responsibility",
                        "category": "Behavioral",
                        "purpose": "Avoid coupling the sender to receiver by giving multiple objects a chance to handle request"
                      }
                    ]
                  },
                  "student": {
                    "focus": "Computer Science",
                    "year": 2024
                  }
                }
                """;

        writeFile(TEST_FILES_DIR + "/sample.json", content);
    }

    /**
     * Generate sample XML file
     */
    private static void generateXmlFile() throws IOException {
        String content = """
                <?xml version="1.0" encoding="UTF-8"?>
                <laboratory>
                    <course>Software Design Patterns</course>
                    <university>Technical University of Moldova</university>
                    <topic>Behavioral Design Patterns</topic>
                    <patterns>
                        <pattern>
                            <name>Strategy</name>
                            <type>Behavioral</type>
                            <description>Defines a family of algorithms and makes them interchangeable</description>
                        </pattern>
                        <pattern>
                            <name>Template Method</name>
                            <type>Behavioral</type>
                            <description>Defines the skeleton of an algorithm in a method</description>
                        </pattern>
                        <pattern>
                            <name>Chain of Responsibility</name>
                            <type>Behavioral</type>
                            <description>Chains objects to handle requests sequentially</description>
                        </pattern>
                    </patterns>
                    <implementation>
                        <language>Java</language>
                        <version>17</version>
                    </implementation>
                </laboratory>
                """;

        writeFile(TEST_FILES_DIR + "/sample.xml", content);
    }

    /**
     * Generate sample PDF file (simplified)
     */
    private static void generatePdfFile() throws IOException {
        // Create a minimal PDF file with text content
        String content = "%PDF-1.4\n" +
                "1 0 obj\n" +
                "<< /Type /Catalog /Pages 2 0 R >>\n" +
                "endobj\n" +
                "2 0 obj\n" +
                "<< /Type /Pages /Kids [3 0 R] /Count 1 >>\n" +
                "endobj\n" +
                "3 0 obj\n" +
                "<< /Type /Page /Parent 2 0 R /Resources 4 0 R /MediaBox [0 0 612 792] /Contents 5 0 R >>\n" +
                "endobj\n" +
                "4 0 obj\n" +
                "<< /Font << /F1 << /Type /Font /Subtype /Type1 /BaseFont /Helvetica >> >> >>\n" +
                "endobj\n" +
                "5 0 obj\n" +
                "<< /Length 150 >>\n" +
                "stream\n" +
                "BT\n" +
                "/F1 12 Tf\n" +
                "50 750 Td\n" +
                "(Behavioral Design Patterns Laboratory) Tj\n" +
                "0 -20 Td\n" +
                "(Technical University of Moldova) Tj\n" +
                "0 -20 Td\n" +
                "(Sample PDF Document) Tj\n" +
                "ET\n" +
                "endstream\n" +
                "endobj\n" +
                "xref\n" +
                "0 6\n" +
                "0000000000 65535 f\n" +
                "0000000009 00000 n\n" +
                "0000000058 00000 n\n" +
                "0000000115 00000 n\n" +
                "0000000214 00000 n\n" +
                "0000000304 00000 n\n" +
                "trailer\n" +
                "<< /Size 6 /Root 1 0 R >>\n" +
                "startxref\n" +
                "502\n" +
                "%%EOF\n";

        writeFile(TEST_FILES_DIR + "/sample.pdf", content);
    }

    /**
     * Helper method to write content to file
     */
    private static void writeFile(String path, String content) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        }
    }

    /**
     * Get the test files directory path
     */
    public static String getTestFilesDir() {
        return TEST_FILES_DIR;
    }
}
