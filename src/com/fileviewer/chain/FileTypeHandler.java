package com.fileviewer.chain;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FileTypeHandler extends FileValidationHandler {

    private final String[] supportedExtensions;

    public FileTypeHandler(String... extensions) {
        this.supportedExtensions = extensions;
    }


    public FileTypeHandler() {
        this("txt", "json", "xml", "pdf");
    }

    @Override
    protected ValidationResult validate(String filePath) {
        System.out.println("Checking: Is file type supported?");

        try {
            String fileName = Paths.get(filePath).getFileName().toString();
            String extension = "";

            int lastDot = fileName.lastIndexOf('.');
            if (lastDot > 0) {
                extension = fileName.substring(lastDot + 1).toLowerCase();
            }

            // Check if extension is supported
            for (String supported : supportedExtensions) {
                if (extension.equals(supported.toLowerCase())) {
                    return new ValidationResult(
                            true,
                            "File type supported: ." + extension,
                            getHandlerName()
                    );
                }
            }

            // File type not supported
            return new ValidationResult(
                    false,
                    "File type '." + extension + "' not supported. Supported types: " +
                            String.join(", ", supportedExtensions),
                    getHandlerName()
            );

        } catch (Exception e) {
            return new ValidationResult(
                    false,
                    "Error checking file type: " + e.getMessage(),
                    getHandlerName()
            );
        }
    }

    @Override
    public String getHandlerName() {
        return "File Type Validator";
    }
}

