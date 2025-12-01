package com.fileviewer.chain;

public abstract class FileValidationHandler {

    protected FileValidationHandler nextHandler;


    public FileValidationHandler setNext(FileValidationHandler handler) {
        this.nextHandler = handler;
        return handler;
    }

    public ValidationResult handle(String filePath) {
        // Perform this handler's validation
        ValidationResult result = validate(filePath);

        // If validation failed, stop the chain
        if (!result.isSuccess()) {
            return result;
        }

        // If there's a next handler, pass the request along
        if (nextHandler != null) {
            return nextHandler.handle(filePath);
        }

        // All validations passed
        return result;
    }


    protected abstract ValidationResult validate(String filePath);


    public abstract String getHandlerName();
}

