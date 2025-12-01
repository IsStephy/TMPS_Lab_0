package com.fileviewer.strategy;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Concrete Strategy - PDF File Reader
 * Simulates reading PDF files (simplified version without external libraries)
 */
public class PdfReadStrategy implements FileReadStrategy {

    @Override
    public String readFile(String filePath) throws Exception {
        StringBuilder content = new StringBuilder();
        content.append("=== PDF FILE CONTENT ===\n");

        // Read file bytes
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));

        // Check if it's a PDF file
        if (fileBytes.length < 4 || fileBytes[0] != '%' || fileBytes[1] != 'P' ||
                fileBytes[2] != 'D' || fileBytes[3] != 'F') {
            throw new Exception("Not a valid PDF file");
        }

        content.append("PDF File Information:\n");
        content.append("- File size: ").append(fileBytes.length).append(" bytes\n");
        content.append("- PDF Header: ").append(new String(fileBytes, 0, Math.min(8, fileBytes.length))).append("\n");

        // Extract text content (simplified - looks for text between parentheses)
        content.append("\nExtracted Text Content:\n");
        String pdfContent = new String(fileBytes);
        content.append(extractTextFromPdf(pdfContent));

        return content.toString();
    }

    /**
     * Simplified PDF text extraction
     * Extracts text found between parentheses in PDF structure
     */
    private String extractTextFromPdf(String pdfContent) {
        StringBuilder text = new StringBuilder();
        boolean inText = false;

        for (int i = 0; i < pdfContent.length() - 1; i++) {
            if (pdfContent.charAt(i) == '(' && (i == 0 || pdfContent.charAt(i - 1) != '\\')) {
                inText = true;
                continue;
            }
            if (pdfContent.charAt(i) == ')' && pdfContent.charAt(i - 1) != '\\') {
                inText = false;
                text.append("\n");
                continue;
            }
            if (inText && pdfContent.charAt(i) >= 32 && pdfContent.charAt(i) <= 126) {
                text.append(pdfContent.charAt(i));
            }
        }

        String extracted = text.toString().trim();
        return extracted.isEmpty() ? "[No extractable text found]" : extracted;
    }

    @Override
    public String getStrategyName() {
        return "PDF Reader Strategy";
    }
}

