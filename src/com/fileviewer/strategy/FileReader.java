package com.fileviewer.strategy;

public class FileReader {
    private FileReadStrategy strategy;

    public FileReader(FileReadStrategy strategy) {
        this.strategy = strategy;
    }

    public FileReader() {
        this.strategy = null;
    }

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


    public String getCurrentStrategy() {
        return strategy != null ? strategy.getStrategyName() : "No strategy set";
    }
}
