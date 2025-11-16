package com.fileviewer.decorators;

import com.fileviewer.interfaces.ReadableFile;

public abstract class FileDecorator implements ReadableFile {
    protected ReadableFile wrappedFile;

    public FileDecorator(ReadableFile wrappedFile) {
        this.wrappedFile = wrappedFile;
    }

    @Override
    public String readContent() {
        return wrappedFile.readContent();
    }

    @Override
    public String getFileName() {
        return wrappedFile.getFileName();
    }

    @Override
    public String getFileType() {
        return wrappedFile.getFileType();
    }

    @Override
    public long getSize() {
        return wrappedFile.getSize();
    }
}