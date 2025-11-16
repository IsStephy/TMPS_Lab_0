package com.fileviewer.decorators;

import com.fileviewer.interfaces.ReadableFile;

/**
 * Decorator that marks files as read-only
 * Adds a read-only indicator to file display
 */
public class ReadOnlyDecorator extends FileDecorator {

    public ReadOnlyDecorator(ReadableFile wrappedFile) {
        super(wrappedFile);
    }

    @Override
    public String readContent() {
        return "╔══════════════════════════════════════════╗\n" +
                "║            READ-ONLY FILE                ║\n" +
                "║  This file is protected from changes.   ║\n" +
                "╚══════════════════════════════════════════╝\n\n" +
                wrappedFile.readContent();
    }

    @Override
    public String getFileName() {
        return wrappedFile.getFileName() + " [READ-ONLY]";
    }

    @Override
    public String toString() {
        return String.format("ReadOnly %s", wrappedFile.toString());
    }
}