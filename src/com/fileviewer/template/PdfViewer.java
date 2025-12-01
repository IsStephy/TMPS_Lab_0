package com.fileviewer.template;

import java.nio.file.Files;
import java.nio.file.Paths;

public class PdfViewer extends AbstractFileViewer {

    private long fileSize = 0;
    private String pdfVersion = "";

    @Override
    protected String readContent(String filePath) throws Exception {
        System.out.println("[Step 2] Reading PDF file content...");

        // Read file as bytes
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        fileSize = bytes.length;

        // Check PDF header
        if (bytes.length < 8 || bytes[0] != '%' || bytes[1] != 'P' ||
                bytes[2] != 'D' || bytes[3] != 'F') {
            throw new Exception("Not a valid PDF file");
        }

        // Extract PDF version
        pdfVersion = new String(bytes, 0, Math.min(8, bytes.length));

        System.out.println("✓ PDF file read (" + fileSize + " bytes)");

        return new String(bytes);
    }

    @Override
    protected String parseContent(String rawContent) throws Exception {
        System.out.println("[Step 3] Parsing PDF content...");

        StringBuilder parsed = new StringBuilder();
        parsed.append("--- PDF FILE CONTENT ---\n\n");

        // Extract metadata
        parsed.append("PDF Version: ").append(pdfVersion).append("\n");
        parsed.append("File Size: ").append(fileSize).append(" bytes\n\n");

        // Extract text content (simplified)
        String extractedText = extractTextFromPdf(rawContent);

        if (extractedText.isEmpty()) {
            parsed.append("--- Document Structure ---\n");
            parsed.append("[Binary PDF content - text extraction limited]\n");
            parsed.append("[This is a simplified PDF viewer]\n");
        } else {
            parsed.append("--- Extracted Text ---\n");
            parsed.append(extractedText);
        }

        System.out.println("✓ PDF content parsed");

        return parsed.toString();
    }

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
                text.append(" ");
                continue;
            }
            if (inText && pdfContent.charAt(i) >= 32 && pdfContent.charAt(i) <= 126) {
                text.append(pdfContent.charAt(i));
            }
        }

        return text.toString().trim();
    }

    @Override
    protected void afterDisplay() {
        System.out.println("\n--- PDF Information ---");
        System.out.println("Version: " + pdfVersion);
        System.out.println("Size: " + String.format("%.2f KB", fileSize / 1024.0));
    }

    @Override
    public String getViewerType() {
        return "PDF File Viewer";
    }
}
