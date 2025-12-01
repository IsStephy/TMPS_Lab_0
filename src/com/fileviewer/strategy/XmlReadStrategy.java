package com.fileviewer.strategy;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Concrete Strategy - XML File Reader
 * Reads and formats XML files with proper indentation
 */
public class XmlReadStrategy implements FileReadStrategy {

    @Override
    public String readFile(String filePath) throws Exception {
        StringBuilder content = new StringBuilder();
        content.append("=== XML FILE CONTENT ===\n");

        // Read the entire file
        String xmlContent = new String(Files.readAllBytes(Paths.get(filePath)));

        // Format XML with indentation
        content.append(formatXml(xmlContent));

        return content.toString();
    }

    /**
     * Simple XML formatter that adds indentation
     */
    private String formatXml(String xml) {
        StringBuilder formatted = new StringBuilder();
        int indentLevel = 0;
        boolean insideTag = false;
        boolean closingTag = false;

        for (int i = 0; i < xml.length(); i++) {
            char c = xml.charAt(i);

            if (c == '<') {
                if (i + 1 < xml.length() && xml.charAt(i + 1) == '/') {
                    closingTag = true;
                    indentLevel--;
                    if (formatted.length() > 0 && formatted.charAt(formatted.length() - 1) != '\n') {
                        formatted.append("\n");
                    }
                    formatted.append("  ".repeat(Math.max(0, indentLevel)));
                } else if (!insideTag) {
                    if (formatted.length() > 0 && formatted.charAt(formatted.length() - 1) != '\n') {
                        formatted.append("\n");
                    }
                    formatted.append("  ".repeat(indentLevel));
                }
                insideTag = true;
                formatted.append(c);
            } else if (c == '>') {
                formatted.append(c);
                insideTag = false;

                // Check if it's a self-closing tag
                if (i > 0 && xml.charAt(i - 1) == '/') {
                    // Self-closing tag, don't increase indent
                } else if (!closingTag) {
                    indentLevel++;
                }
                closingTag = false;
            } else {
                if (!Character.isWhitespace(c) || insideTag) {
                    formatted.append(c);
                }
            }
        }

        return formatted.toString();
    }

    @Override
    public String getStrategyName() {
        return "XML Reader Strategy";
    }
}
