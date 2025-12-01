package com.fileviewer.chain;

public class ValidationResult {
    private final boolean success;
    private final String message;
    private final String handlerName;

    public ValidationResult(boolean success, String message, String handlerName) {
        this.success = success;
        this.message = message;
        this.handlerName = handlerName;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getHandlerName() {
        return handlerName;
    }

    @Override
    public String toString() {
        String status = success ? "✓ PASS" : "✗ FAIL";
        return String.format("[%s] %s: %s", status, handlerName, message);
    }
}
