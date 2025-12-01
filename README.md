# Behavioral Design Patterns Laboratory Report

**Laboratory:** Laboratory Work #3  
**Topic:** Behavioral Design Patterns Implementation  
**Student:** Ștefan Istrati

---

## Table of Contents

1. [Introduction](#introduction)
2. [Strategy Pattern](#strategy-pattern)
3. [Template Method Pattern](#template-method-pattern)
4. [Chain of Responsibility Pattern](#chain-of-responsibility-pattern)
5. [Implementation Details](#implementation-details)
6. [Conclusion](#conclusion)

---

## Introduction

### Overview

This laboratory work demonstrates the implementation of three fundamental behavioral design patterns in Java. Behavioral patterns are concerned with algorithms and the assignment of responsibilities between objects. They describe not just patterns of objects or classes but also the patterns of communication between them.


### Patterns Implemented

This laboratory implements three behavioral design patterns:

1. **Strategy Pattern** - Defines a family of algorithms for file reading
2. **Template Method Pattern** - Defines the skeleton of file viewing algorithms
3. **Chain of Responsibility Pattern** - Creates a chain of file validation handlers

---

## Strategy Pattern

### Definition

The Strategy pattern defines a family of algorithms, encapsulates each one, and makes them interchangeable. Strategy lets the algorithm vary independently from clients that use it. This pattern is particularly useful when you need to use different variants of an algorithm within an object and want to switch from one algorithm to another at runtime.


### Implementation

#### Class Structure

| Component | Type | Description |
|-----------|------|-------------|
| `FileReadStrategy` | Interface | Defines the contract for all reading strategies with `readFile()` and `getStrategyName()` methods |
| `TextReadStrategy` | Concrete Strategy | Reads plain text files line by line |
| `JsonReadStrategy` | Concrete Strategy | Reads and formats JSON files with proper indentation |
| `XmlReadStrategy` | Concrete Strategy | Reads and formats XML files with hierarchical structure |
| `PdfReadStrategy` | Concrete Strategy | Reads PDF files and extracts text content |
| `FileReader` | Context | Maintains reference to strategy and delegates work to it |

####  Code Example

**FileReadStrategy Interface:**
```java
public interface FileReadStrategy {
    String readFile(String filePath) throws Exception;
    String getStrategyName();
}
```

**TextReadStrategy Implementation:**
```java
public class TextReadStrategy implements FileReadStrategy {
    @Override
    public String readFile(String filePath) throws Exception {
        StringBuilder content = new StringBuilder();
        content.append("=== TEXT FILE CONTENT ===\n");
        Files.lines(Paths.get(filePath)).forEach(line -> {
            content.append(line).append("\n");
        });
        return content.toString();
    }
    
    @Override
    public String getStrategyName() {
        return "Text Reader Strategy";
    }
}
```

**FileReader Context:**
```java
public class FileReader {
    private FileReadStrategy strategy;
    
    public void setStrategy(FileReadStrategy strategy) {
        this.strategy = strategy;
    }
    
    public String readFile(String filePath) throws Exception {
        if (strategy == null) {
            throw new IllegalStateException("No strategy set!");
        }
        System.out.println("Using: " + strategy.getStrategyName());
        return strategy.readFile(filePath);
    }
}
```

**Usage Example:**
```java
FileReader reader = new FileReader();

reader.setStrategy(new TextReadStrategy());
String textContent = reader.readFile("sample.txt");

reader.setStrategy(new JsonReadStrategy());
String jsonContent = reader.readFile("sample.json");
```

### Key Features

- **Runtime Strategy Selection**: Choose appropriate strategy based on file type
- **Easy Extension**: Add new strategies without modifying existing code
- **Encapsulation**: Each strategy encapsulates its own algorithm
- **No Conditionals**: Eliminates complex if-else chains


---

## Template Method Pattern

###  Definition

The Template Method pattern defines the skeleton of an algorithm in the superclass but lets subclasses override specific steps of the algorithm without changing its structure. This pattern is based on inheritance and promotes code reuse through a well-defined algorithm structure.


### Implementation

#### Class Structure

| Component | Type | Description |
|-----------|------|-------------|
| `AbstractFileViewer` | Abstract Class | Defines the template method and algorithm skeleton |
| `TxtViewer` | Concrete Class | Implements text file viewing with line numbers |
| `JsonViewer` | Concrete Class | Implements JSON file viewing with formatting statistics |
| `PdfViewer` | Concrete Class | Implements PDF file viewing with metadata extraction |


#### Code Example

**Abstract Base Class:**
```java
public abstract class AbstractFileViewer {
    
    public final void viewFile(String filePath) {
        try {
            System.out.println("Starting file viewing process...");

            openFile(filePath);

            String rawContent = readContent(filePath);

            String parsedContent = parseContent(rawContent);
            
            display(parsedContent);
            
            afterDisplay();
            
            System.out.println("File viewing completed!");
        } catch (Exception e) {
            handleError(e);
        }
    }
    protected void openFile(String filePath) throws Exception {
        System.out.println("[Step 1] Opening file: " + filePath);
        if (!Files.exists(Paths.get(filePath))) {
            throw new Exception("File does not exist");
        }
        if (!Files.isReadable(Paths.get(filePath))) {
            throw new Exception("File is not readable");
        }
        System.out.println("✓ File opened successfully");
    }
    protected abstract String readContent(String filePath) throws Exception;
    protected abstract String parseContent(String rawContent) throws Exception;
    
    protected void display(String content) {
        System.out.println("[Step 4] Displaying content:");
        System.out.println(content);
    }
    
    protected void afterDisplay() {
    }
    
    protected void handleError(Exception e) {
        System.err.println("ERROR: " + e.getMessage());
    }
    
    public abstract String getViewerType();
}
```

**Concrete Implementation Example:**
```java
public class TxtViewer extends AbstractFileViewer {
    private int lineCount = 0;
    
    @Override
    protected String readContent(String filePath) throws Exception {
        System.out.println("[Step 2] Reading TXT file content...");
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        lineCount = lines.size();
        System.out.println("✓ Read " + lineCount + " lines");
        return String.join("\n", lines);
    }
    
    @Override
    protected String parseContent(String rawContent) throws Exception {
        System.out.println("[Step 3] Parsing TXT content...");
        String[] lines = rawContent.split("\n");
        StringBuilder parsed = new StringBuilder();
        parsed.append("--- TEXT FILE CONTENT ---\n\n");
        
        for (int i = 0; i < lines.length; i++) {
            parsed.append(String.format("%4d | %s\n", i + 1, lines[i]));
        }
        
        System.out.println("✓ Content parsed successfully");
        return parsed.toString();
    }
    
    @Override
    protected void afterDisplay() {
        System.out.println("\n--- Statistics ---");
        System.out.println("Total lines: " + lineCount);
    }
    
    @Override
    public String getViewerType() {
        return "Text File Viewer";
    }
}
```

**Usage Example:**
```java
AbstractFileViewer viewer = new TxtViewer();
viewer.viewFile("sample.txt");  

viewer = new JsonViewer();
viewer.viewFile("sample.json"); 
```

###  Key Features

- **Consistent Algorithm Structure**: All viewers follow the same sequence of steps
- **Code Reuse**: Common functionality implemented once in base class
- **Flexibility**: Subclasses customize specific steps without changing overall flow
- **Inversion of Control**: Base class calls subclass methods (Hollywood Principle)
- **Hook Methods**: Optional extension points for additional behavior

---

## Chain of Responsibility Pattern

### Definition

The Chain of Responsibility pattern avoids coupling the sender of a request to its receiver by giving more than one object a chance to handle the request. The pattern chains the receiving objects and passes the request along the chain until an object handles it or the chain ends.


### Implementation

#### Class Structure

| Component | Type | Description |
|-----------|------|-------------|
| `FileValidationHandler` | Abstract Handler | Base class for all validators with chain management |
| `ValidationResult` | Result Object | Encapsulates validation outcome (success/failure + message) |
| `FileExistsHandler` | Concrete Handler | Validates that file exists in filesystem |
| `FileTypeHandler` | Concrete Handler | Validates file extension against supported types |
| `FileSizeHandler` | Concrete Handler | Validates file size is within acceptable limits |
| `FileReadableHandler` | Concrete Handler | Validates file has read permissions |


#### Code Example

**Abstract Handler:**
```java
public abstract class FileValidationHandler {
    protected FileValidationHandler nextHandler;
    
    public FileValidationHandler setNext(FileValidationHandler handler) {
        this.nextHandler = handler;
        return handler;
    }
    
    public ValidationResult handle(String filePath) {
        ValidationResult result = validate(filePath);

        if (!result.isSuccess()) {
            return result;
        }
        if (nextHandler != null) {
            return nextHandler.handle(filePath);
        }
        return result;
    }
    
    protected abstract ValidationResult validate(String filePath);
    public abstract String getHandlerName();
}
```

**Concrete Handler Example:**
```java
public class FileExistsHandler extends FileValidationHandler {
    
    @Override
    protected ValidationResult validate(String filePath) {
        System.out.println("Checking: Does file exist?");
        
        boolean exists = Files.exists(Paths.get(filePath));
        
        if (exists) {
            return new ValidationResult(
                true, 
                "File exists at: " + filePath,
                getHandlerName()
            );
        } else {
            return new ValidationResult(
                false, 
                "File does not exist: " + filePath,
                getHandlerName()
            );
        }
    }
    
    @Override
    public String getHandlerName() {
        return "File Existence Validator";
    }
}
```

**Validation Result:**
```java
public class ValidationResult {
    private final boolean success;
    private final String message;
    private final String handlerName;
    
    public ValidationResult(boolean success, String message, String handlerName) {
        this.success = success;
        this.message = message;
        this.handlerName = handlerName;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getHandlerName() { return handlerName; }
    
    @Override
    public String toString() {
        String status = success ? "✓ PASS" : "✗ FAIL";
        return String.format("[%s] %s: %s", status, handlerName, message);
    }
}
```

**Usage Example:**
```java
FileValidationHandler chain = new FileExistsHandler();
chain.setNext(new FileTypeHandler())
     .setNext(new FileSizeHandler(10))  
     .setNext(new FileReadableHandler());

ValidationResult result = chain.handle("sample.txt");

if (result.isSuccess()) {
    System.out.println("✅ All validations passed!");
} else {
    System.out.println("❌ Validation failed: " + result.getMessage());
    System.out.println("Failed at: " + result.getHandlerName());
}
```

###  Key Features

- **Decoupled Handlers**: Each validator is independent
- **Dynamic Chain**: Handlers can be added/removed/reordered at runtime
- **Fail-Fast**: Chain stops at first failure
- **Single Responsibility**: Each handler validates one aspect
- **Flexible Composition**: Mix and match validators as needed

---

##  Implementation Details

###  Project Structure

```
behavioral-patterns-lab/
├── src/
│   ├── Main.java                      
│   ├── strategy/
│   │   ├── FileReadStrategy.java     
│   │   ├── TextReadStrategy.java      
│   │   ├── JsonReadStrategy.java      
│   │   ├── XmlReadStrategy.java       
│   │   ├── PdfReadStrategy.java       
│   │   └── FileReader.java            
│   ├── template/
│   │   ├── AbstractFileViewer.java    
│   │   ├── TxtViewer.java             
│   │   ├── JsonViewer.java            
│   │   └── PdfViewer.java            
│   ├── chain/
│   │   ├── FileValidationHandler.java 
│   │   ├── ValidationResult.java      
│   │   ├── FileExistsHandler.java     
│   │   ├── FileTypeHandler.java       
│   │   ├── FileSizeHandler.java       
│   │   └── FileReadableHandler.java  
│   └── utils/
│       └── FileGenerator.java         
├── test-files/                       
│   ├── sample.txt
│   ├── sample.json
│   ├── sample.xml
│   └── sample.pdf
└── README.md                        
```

###  Design Principles Applied

###  Pattern Integration

The three patterns work together in a complete file processing pipeline:

```
1. CHAIN OF RESPONSIBILITY
   ├─ Validate file exists
   ├─ Validate file type
   ├─ Validate file size
   └─ Validate file readable
       │
       ▼ (if all pass)
2. STRATEGY PATTERN
   ├─ Select appropriate reading strategy
   ├─ Read file with chosen strategy
   └─ Return parsed content
       │
       ▼
3. TEMPLATE METHOD
   ├─ Execute viewing algorithm
   ├─ Display formatted content
   └─ Show additional statistics
```

---


##  Conclusion


The behavioral design patterns demonstrated in this laboratory provide powerful tools for managing algorithms, workflows, and object interactions. By understanding when and how to apply these patterns, developers can create more maintainable, flexible, and robust software systems. The practical implementation reinforces theoretical knowledge and demonstrates the real-world value of design patterns in software engineering.
