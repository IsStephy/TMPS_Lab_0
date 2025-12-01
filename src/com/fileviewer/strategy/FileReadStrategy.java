package com.fileviewer.strategy;

public interface FileReadStrategy {
    String readFile(String filePath) throws Exception;
    String getStrategyName();
}