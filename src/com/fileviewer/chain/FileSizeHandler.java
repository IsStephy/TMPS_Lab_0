package com.fileviewer.chain;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FileSizeHandler extends FileValidationHandler {

    private final long maxSizeBytes;


    public FileSizeHandler(long maxSizeMB) {
        this.maxSizeBytes = maxSizeMB * 1024 * 1024;
    }


    public FileSizeHandler() {
        this(10);
    }

    @Override
    protected ValidationResult validate(String filePath) {
        System.out.println("Checking: Is file size acceptable?");

        try {
            long fileSize = Files.size(Paths.get(filePath));
            double fileSizeMB = fileSize / (1024.0 * 1024.0);

            if (fileSize <= maxSizeBytes) {
                return new ValidationResult(
                        true,
                        String.format("File size OK: %.2f MB (limit: %.2f MB)",
                                fileSizeMB, maxSizeBytes / (1024.0 * 1024.0)),
                        getHandlerName()
                );
            } else {
                return new ValidationResult(
                        false,
                        String.format("File too large: %.2f MB (limit: %.2f MB)",
                                fileSizeMB, maxSizeBytes / (1024.0 * 1024.0)),
                        getHandlerName()
                );
            }
        } catch (Exception e) {
            return new ValidationResult(
                    false,
                    "Error checking file size: " + e.getMessage(),
                    getHandlerName()
            );
        }
    }

    @Override
    public String getHandlerName() {
        return "File Size Validator";
    }
}
