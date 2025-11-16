package com.fileviewer.decorators;

import com.fileviewer.interfaces.ReadableFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LoggingDecorator extends FileDecorator {
    private static final List<String> accessLog = new ArrayList<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LoggingDecorator(ReadableFile wrappedFile) {
        super(wrappedFile);
    }

    @Override
    public String readContent() {
        logAccess("READ");
        String content = wrappedFile.readContent();

        return "╔══════════════════════════════════════════╗\n" +
                "║           LOGGED FILE ACCESS             ║\n" +
                "║  Time: " + getCurrentTime() + "           ║\n" +
                "║  Action: READ                            ║\n" +
                "║  File: " + String.format("%-32s", wrappedFile.getFileName()) + "║\n" +
                "╚══════════════════════════════════════════╝\n\n" +
                content;
    }

    @Override
    public String getFileName() {
        logAccess("GET_NAME");
        return wrappedFile.getFileName();
    }

    @Override
    public String getFileType() {
        return wrappedFile.getFileType() + " (Logged)";
    }

    @Override
    public long getSize() {
        logAccess("GET_SIZE");
        return wrappedFile.getSize();
    }

    private void logAccess(String action) {
        String logEntry = String.format("[%s] %s - %s - %s (%d bytes)",
                getCurrentTime(),
                action,
                wrappedFile.getFileName(),
                wrappedFile.getFileType(),
                wrappedFile.getSize());
        accessLog.add(logEntry);
    }

    private String getCurrentTime() {
        return LocalDateTime.now().format(formatter);
    }

    public static List<String> getAccessLog() {
        return new ArrayList<>(accessLog);
    }

    public static void printAccessLog() {
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("                        ACCESS LOG                             ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        if (accessLog.isEmpty()) {
            System.out.println("No file access recorded.");
        } else {
            for (String log : accessLog) {
                System.out.println(log);
            }
        }
        System.out.println("═══════════════════════════════════════════════════════════════\n");
    }

    public static void clearAccessLog() {
        accessLog.clear();
    }

    @Override
    public String toString() {
        return String.format("Logged %s", wrappedFile.toString());
    }
}