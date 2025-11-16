package com.fileviewer.composite;

import com.fileviewer.interfaces.FileSystemComponent;
import com.fileviewer.interfaces.ReadableFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Leaf component in the Composite Pattern
 * Represents a single file in the file system
 */
public class File implements FileSystemComponent {
    private final ReadableFile readableFile;

    public File(ReadableFile readableFile) {
        this.readableFile = readableFile;
    }

    @Override
    public String getName() {
        return readableFile.getFileName();
    }

    @Override
    public long getSize() {
        return readableFile.getSize();
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + "📄 " + getName() +
                " [" + readableFile.getFileType() + "] " +
                "(" + getSize() + " bytes)");
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public void add(FileSystemComponent component) {
        throw new UnsupportedOperationException("Cannot add to a file");
    }

    @Override
    public void remove(FileSystemComponent component) {
        throw new UnsupportedOperationException("Cannot remove from a file");
    }

    @Override
    public List<FileSystemComponent> getChildren() {
        return new ArrayList<>(); // Files have no children
    }

    public ReadableFile getReadableFile() {
        return readableFile;
    }

    public String open() {
        return readableFile.readContent();
    }
}