package com.fileviewer.chain;

import java.nio.file.Files;
import java.nio.file.Paths;


public class FileReadableHandler extends FileValidationHandler {

    @Override
    protected ValidationResult validate(String filePath) {
        System.out.println("Checking: Is file readable?");

        try {
            boolean isReadable = Files.isReadable(Paths.get(filePath));

            if (isReadable) {
                return new ValidationResult(
                        true,
                        "File is readable",
                        getHandlerName()
                );
            } else {
                return new ValidationResult(
                        false,
                        "File is not readable (check permissions)",
                        getHandlerName()
                );
            }
        } catch (Exception e) {
            return new ValidationResult(
                    false,
                    "Error checking file readability: " + e.getMessage(),
                    getHandlerName()
            );
        }
    }

    @Override
    public String getHandlerName() {
        return "File Readability Validator";
    }
}

