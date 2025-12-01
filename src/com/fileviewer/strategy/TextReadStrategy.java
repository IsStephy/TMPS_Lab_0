package com.fileviewer.strategy;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Concrete Strategy - Text File Reader
 * Reads plain text files line by line
 */
public class TextReadStrategy implements FileReadStrategy {

    @Override
    public String readFile(String filePath) throws Exception {
        StringBuilder content = new StringBuilder();
        content.append("=== TEXT FILE CONTENT ===\n");

        // Read all lines from the text file
        Files.lines(Paths.get(filePath)).forEach(line -> {
            content.append(line).append("\n");
        });

        return content.toString();
    }

    @Override
    public String getStrategyName() {
        return "Text Reader Strategy";
    }
}
