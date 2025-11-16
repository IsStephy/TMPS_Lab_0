package com.fileviewer.interfaces;

import java.util.List;


public interface FileSystemComponent {

    String getName();
    long getSize();


    void display(String indent);


    boolean isDirectory();

    void add(FileSystemComponent component);

    void remove(FileSystemComponent component);

    List<FileSystemComponent> getChildren();
}