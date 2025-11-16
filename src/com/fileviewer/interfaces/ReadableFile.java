package com.fileviewer.interfaces;

public interface ReadableFile {
    String readContent();

    String getFileName();

    String getFileType();

    long getSize();
}