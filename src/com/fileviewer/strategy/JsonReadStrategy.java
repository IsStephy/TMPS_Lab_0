package com.fileviewer.strategy;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Concrete Strategy - JSON File Reader
 * Reads and formats JSON files with indentation
 */
public class JsonReadStrategy implements FileReadStrategy {

    @Override
    public String readFile(String filePath) throws Exception {
        StringBuilder content = new StringBuilder();
        content.append("=== JSON FILE CONTENT ===\n");

        // Read the entire file
        String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));

        // Simple JSON formatting (indentation)
        content.append(formatJson(jsonContent));

        return content.toString();
    }

    /**
     * Simple JSON formatter that adds indentation
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

    @Override
    public String getStrategyName() {
        return "JSON Reader Strategy";
    }
}

