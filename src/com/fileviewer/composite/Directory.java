package com.fileviewer.composite;

import com.fileviewer.interfaces.FileSystemComponent;

import java.util.ArrayList;
import java.util.List;

public class Directory implements FileSystemComponent {
    private final String name;
    private final List<FileSystemComponent> children;

    public Directory(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSize() {
        long totalSize = 0;
        for (FileSystemComponent child : children) {
            totalSize += child.getSize();
        }
        return totalSize;
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + "📁 " + getName() + "/ (" + getSize() + " bytes total)");
        for (FileSystemComponent child : children) {
            child.display(indent + "  ");
        }
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public void add(FileSystemComponent component) {
        children.add(component);
    }

    @Override
    public void remove(FileSystemComponent component) {
        children.remove(component);
    }

    @Override
    public List<FileSystemComponent> getChildren() {
        return new ArrayList<>(children);
    }

    public File findFile(String fileName) {
        for (FileSystemComponent child : children) {
            if (!child.isDirectory() && child.getName().equals(fileName)) {
                return (File) child;
            } else if (child.isDirectory()) {
                File found = ((Directory) child).findFile(fileName);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    public int getFileCount() {
        int count = 0;
        for (FileSystemComponent child : children) {
            if (child.isDirectory()) {
                count += ((Directory) child).getFileCount();
            } else {
                count++;
            }
        }
        return count;
    }

    public int getDirectoryCount() {
        int count = 0;
        for (FileSystemComponent child : children) {
            if (child.isDirectory()) {
                count += 1 + ((Directory) child).getDirectoryCount();
            }
        }
        return count;
    }
}