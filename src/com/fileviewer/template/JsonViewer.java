package com.fileviewer.template;

import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonViewer extends AbstractFileViewer {

    private int objectCount = 0;
    private int arrayCount = 0;

    @Override
    protected String readContent(String filePath) throws Exception {
        System.out.println("[Step 2] Reading JSON file content...");

        // Read entire file as string
        String content = new String(Files.readAllBytes(Paths.get(filePath)));

        System.out.println("✓ JSON file read (" + content.length() + " characters)");

        return content;
    }

    @Override
    protected String parseContent(String rawContent) throws Exception {
        System.out.println("[Step 3] Parsing JSON content...");

        objectCount = countOccurrences(rawContent, '{');
        arrayCount = countOccurrences(rawContent, '[');

        String formatted = formatJson(rawContent);

        System.out.println("✓ JSON parsed successfully");

        StringBuilder result = new StringBuilder();
        result.append("--- JSON FILE CONTENT ---\n\n");
        result.append(formatted);

        return result.toString();
    }

    /**
     * Format JSON with proper indentation
     */
    private String formatJson(String json) {
        StringBuilder formatted = new StringBuilder();
        int indentLevel = 0;
        boolean inQuotes = false;

        for (char c : json.toCharArray()) {
            switch (c) {
                case '"':
                    formatted.append(c);
                    inQuotes = !inQuotes;
                    break;
                case '{':
                case '[':
                    formatted.append(c);
                    if (!inQuotes) {
                        formatted.append("\n");
                        indentLevel++;
                        formatted.append("  ".repeat(indentLevel));
                    }
                    break;
                case '}':
                case ']':
                    if (!inQuotes) {
                        formatted.append("\n");
                        indentLevel--;
                        formatted.append("  ".repeat(indentLevel));
                    }
                    formatted.append(c);
                    break;
                case ',':
                    formatted.append(c);
                    if (!inQuotes) {
                        formatted.append("\n");
                        formatted.append("  ".repeat(indentLevel));
                    }
                    break;
                case ':':
                    formatted.append(c);
                    if (!inQuotes) {
                        formatted.append(" ");
                    }
                    break;
                default:
                    if (!Character.isWhitespace(c) || inQuotes) {
                        formatted.append(c);
                    }
            }
        }

        return formatted.toString();
    }

    private int countOccurrences(String str, char ch) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == ch) count++;
        }
        return count;
    }

    @Override
    protected void afterDisplay() {
        System.out.println("\n--- JSON Statistics ---");
        System.out.println("Objects: " + objectCount);
        System.out.println("Arrays: " + arrayCount);
    }

    @Override
    public String getViewerType() {
        return "JSON File Viewer";
    }
}
