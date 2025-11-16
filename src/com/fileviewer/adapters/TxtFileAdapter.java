package com.fileviewer.adapters;

import com.fileviewer.interfaces.ReadableFile;

public class TxtFileAdapter implements ReadableFile {
    private final String fileName;
    private final String content;

    public TxtFileAdapter(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }

    @Override
    public String readContent() {
        return content;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getFileType() {
        return "TXT";
    }

    @Override
    public long getSize() {
        return content.getBytes().length;
    }

    @Override
    public String toString() {
        return String.format("TXT File: %s (%d bytes)", fileName, getSize());
    }
}