
# Project Report - Enhanced with Design Patterns
## File Viewer - Structural Design Patterns Implementation
#### Author: Ștefan Istrati
##  Table of Contents
- [Overview](#overview)
- [Design Patterns](#design-patterns)
- [Project Structure](#project-structure)
- [Features](#features)
- [How to Run](#how-to-run)
- [Pattern Explanations](#pattern-explanations)
- [Code Examples](#code-examples)

##  Overview

This project implements a File Viewer system that can handle different file types, organize them in a hierarchical structure, and add dynamic features at runtime. It demonstrates how structural design patterns can work together to create a flexible and extensible system.

##  Design Patterns

### 1. Adapter Pattern
**Purpose**: Convert different file formats into a common `ReadableFile` interface.

**Implementation**:
- **Interface**: `ReadableFile` - Common interface for all file types
- **Adapters**:
  - `TxtFileAdapter` - Handles plain text files
  - `JsonFileAdapter` - Handles JSON files with formatting
  - `XmlFileAdapter` - Handles XML files with formatting
  - `CsvFileAdapter` - Handles CSV files with table formatting

**Why it's useful**: 
- Allows the system to work with different file formats without changing the core logic
- Easy to add new file types by creating new adapters
- Each adapter can have specific formatting logic for its file type

### 2. Composite Pattern
**Purpose**: Represent files and directories in a tree structure.

**Implementation**:
- **Component Interface**: `FileSystemComponent` - Common interface for files and directories
- **Leaf**: `File` - Represents individual files (cannot contain children)
- **Composite**: `Directory` - Represents directories (can contain files and subdirectories)

**Why it's useful**:
- Treats individual files and directories uniformly
- Supports recursive operations (calculate total size, display tree, search)
- Easy to add new levels of hierarchy

### 3. Decorator Pattern
**Purpose**: Add dynamic features to files without modifying their structure.

**Implementation**:
- **Base Decorator**: `FileDecorator` - Abstract decorator class
- **Concrete Decorators**:
  - `CompressionDecorator` - Simulates file compression
  - `EncryptionDecorator` - Adds encryption with Caesar cipher
  - `LoggingDecorator` - Logs file access operations
  - `ReadOnlyDecorator` - Marks files as read-only

**Why it's useful**:
- Add features dynamically at runtime
- Stack multiple decorators for combined functionality
- No need to modify original file classes

##  Project Structure

```
FileViewer/
├── src/main/java/com/fileviewer/
│   ├── FileViewerApp.java              # Main application
│   ├── interfaces/
│   │   ├── ReadableFile.java           # Adapter pattern interface
│   │   └── FileSystemComponent.java    # Composite pattern interface
│   ├── adapters/                       # Adapter Pattern
│   │   ├── TxtFileAdapter.java
│   │   ├── JsonFileAdapter.java
│   │   ├── XmlFileAdapter.java
│   │   └── CsvFileAdapter.java
│   ├── composite/                      # Composite Pattern
│   │   ├── File.java                   # Leaf component
│   │   └── Directory.java              # Composite component
│   └── decorators/                     # Decorator Pattern
│       ├── FileDecorator.java          # Base decorator
│       ├── CompressionDecorator.java
│       ├── EncryptionDecorator.java
│       ├── LoggingDecorator.java
│       └── ReadOnlyDecorator.java
├── README.md
└── REPORT.md
```

##  Features

### File Type Support (Adapter Pattern)
- **TXT**: Plain text display
- **JSON**: Formatted with proper indentation
- **XML**: Formatted with proper tag structure
- **CSV**: Displayed as a formatted table

### File System Operations (Composite Pattern)
- Create hierarchical directory structures
- Add/remove files and directories
- Calculate total size recursively
- Search for files in the tree
- Display tree structure visually
- Get file and directory counts

### Dynamic Features (Decorator Pattern)
- **Compression**: Simulates file compression with size reduction
- **Encryption**: Uses Caesar cipher for demonstration
- **Logging**: Tracks all file access operations with timestamps
- **Read-Only**: Marks files as protected

### Combined Features
- Stack multiple decorators on a single file
- Use decorated files in the composite structure
- Different file types can have different decorators


##  Pattern Explanations

### Adapter Pattern in Detail

The Adapter pattern allows incompatible interfaces to work together. In this project:

```java

ReadableFile txtFile = new TxtFileAdapter("file.txt", "content");
ReadableFile jsonFile = new JsonFileAdapter("file.json", "{...}");
ReadableFile xmlFile = new XmlFileAdapter("file.xml", "<root>...</root>");


String content = txtFile.readContent(); 
```

### Composite Pattern in Detail

The Composite pattern allows you to compose objects into tree structures:

```java
Directory root = new Directory("root");
Directory subDir = new Directory("docs");
File file = new File(new TxtFileAdapter("readme.txt", "content"));

root.add(subDir); 
subDir.add(file);    

root.display("");    
long totalSize = root.getSize(); 
```

### Decorator Pattern in Detail

The Decorator pattern adds responsibilities to objects dynamically:

```java
ReadableFile file = new TxtFileAdapter("data.txt", "sensitive data");

ReadableFile encrypted = new EncryptionDecorator(file);

ReadableFile secure = new LoggingDecorator(
    new CompressionDecorator(
        new EncryptionDecorator(
            new ReadOnlyDecorator(file)
        )
    )
);
```

##  Code Examples

### Example 1: Creating Files with Different Types

```java
ReadableFile txtFile = new TxtFileAdapter("notes.txt", "Meeting notes...");
ReadableFile jsonFile = new JsonFileAdapter("config.json", "{\"version\":\"1.0\"}");
ReadableFile xmlFile = new XmlFileAdapter("data.xml", "<data><item>Test</item></data>");

System.out.println(txtFile.readContent());
System.out.println(jsonFile.readContent());
System.out.println(xmlFile.readContent());
```

### Example 2: Building a File System

```java
Directory root = new Directory("project");
Directory src = new Directory("src");
Directory test = new Directory("test");

File mainFile = new File(new TxtFileAdapter("Main.java", "public class Main {}"));
File testFile = new File(new TxtFileAdapter("Test.java", "public class Test {}"));

root.add(src);
root.add(test);
src.add(mainFile);
test.add(testFile);

root.display("");
System.out.println("Total size: " + root.getSize());
```

### Example 3: Decorating Files

```java
ReadableFile baseFile = new TxtFileAdapter("secret.txt", "confidential");

ReadableFile compressed = new CompressionDecorator(baseFile);
System.out.println("Compressed size: " + compressed.getSize());

ReadableFile encrypted = new EncryptionDecorator(compressed, 13);

ReadableFile logged = new LoggingDecorator(encrypted);

String content = logged.readContent();
```

### Example 4: All Patterns Together

```java
Directory secureDir = new Directory("secure");

File confidentialFile = new File(
    new LoggingDecorator(
        new CompressionDecorator(
            new EncryptionDecorator(
                new TxtFileAdapter("classified.txt", "Top Secret")
            )
        )
    )
);

secureDir.add(confidentialFile);

secureDir.display("");
String content = confidentialFile.open();
```