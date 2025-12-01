package com.fileviewer.chain;

import java.nio.file.Files;
import java.nio.file.Paths;


public class FileExistsHandler extends FileValidationHandler {

    @Override
    protected ValidationResult validate(String filePath) {
        System.out.println("Checking: Does file exist?");

        boolean exists = Files.exists(Paths.get(filePath));

        if (exists) {
            return new ValidationResult(
                    true,
                    "File exists at: " + filePath,
                    getHandlerName()
            );
        } else {
            return new ValidationResult(
                    false,
                    "File does not exist: " + filePath,
                    getHandlerName()
            );
        }
    }

    @Override
    public String getHandlerName() {
        return "File Existence Validator";
    }
}
