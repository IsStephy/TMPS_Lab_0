package com.fileviewer.template;

import java.nio.file.Files;
import java.nio.file.Paths;


public abstract class AbstractFileViewer {


    public final void viewFile(String filePath) {
        try {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("Starting file viewing process...");
            System.out.println("=".repeat(60));

            // Step 1: Open file
            openFile(filePath);

            // Step 2: Read content
            String rawContent = readContent(filePath);

            // Step 3: Parse content
            String parsedContent = parseContent(rawContent);

            // Step 4: Display content
            display(parsedContent);

            // Hook method - can be overridden
            afterDisplay();

            System.out.println("=".repeat(60));
            System.out.println("File viewing completed successfully!");
            System.out.println("=".repeat(60) + "\n");

        } catch (Exception e) {
            handleError(e);
        }
    }


    protected void openFile(String filePath) throws Exception {
        System.out.println("[Step 1] Opening file: " + filePath);

        if (!Files.exists(Paths.get(filePath))) {
            throw new Exception("File does not exist: " + filePath);
        }

        if (!Files.isReadable(Paths.get(filePath))) {
            throw new Exception("File is not readable: " + filePath);
        }

        System.out.println("✓ File opened successfully");
    }

    protected abstract String readContent(String filePath) throws Exception;

    protected abstract String parseContent(String rawContent) throws Exception;


    protected void display(String content) {
        System.out.println("[Step 4] Displaying content:");
        System.out.println(content);
    }

    protected void afterDisplay() {
    }


    protected void handleError(Exception e) {
        System.err.println("=".repeat(60));
        System.err.println("ERROR during file viewing:");
        System.err.println(e.getMessage());
        System.err.println("=".repeat(60));
    }

    public abstract String getViewerType();
}
