package com.fileviewer.template;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TxtViewer extends AbstractFileViewer {

    private int lineCount = 0;

    @Override
    protected String readContent(String filePath) throws Exception {
        System.out.println("[Step 2] Reading TXT file content...");

        // Read all lines from the file
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        lineCount = lines.size();

        System.out.println("✓ Read " + lineCount + " lines");

        return String.join("\n", lines);
    }

    @Override
    protected String parseContent(String rawContent) throws Exception {
        System.out.println("[Step 3] Parsing TXT content...");

        // For text files, parsing is minimal - just add line numbers
        String[] lines = rawContent.split("\n");
        StringBuilder parsed = new StringBuilder();

        parsed.append("--- TEXT FILE CONTENT ---\n\n");

        for (int i = 0; i < lines.length; i++) {
            parsed.append(String.format("%4d | %s\n", i + 1, lines[i]));
        }

        System.out.println("✓ Content parsed successfully");

        return parsed.toString();
    }

    @Override
    protected void afterDisplay() {
        // Additional statistics after displaying
        System.out.println("\n--- Statistics ---");
        System.out.println("Total lines: " + lineCount);
    }

    @Override
    public String getViewerType() {
        return "Text File Viewer";
    }
}
