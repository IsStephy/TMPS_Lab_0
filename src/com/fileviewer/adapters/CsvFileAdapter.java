package com.fileviewer.adapters;

import com.fileviewer.interfaces.ReadableFile;

public class CsvFileAdapter implements ReadableFile {
    private final String fileName;
    private final String csvContent;

    public CsvFileAdapter(String fileName, String csvContent) {
        this.fileName = fileName;
        this.csvContent = csvContent;
    }

    @Override
    public String readContent() {
        // Format CSV as a table
        return formatCsv(csvContent);
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getFileType() {
        return "CSV";
    }

    @Override
    public long getSize() {
        return csvContent.getBytes().length;
    }

    private String formatCsv(String csv) {
        String[] lines = csv.split("\n");
        StringBuilder formatted = new StringBuilder();

        formatted.append("CSV Table View:\n");
        formatted.append("=".repeat(50)).append("\n");

        for (int i = 0; i < lines.length; i++) {
            String[] cells = lines[i].split(",");
            formatted.append("| ");
            for (String cell : cells) {
                formatted.append(cell.trim()).append(" | ");
            }
            formatted.append("\n");

            if (i == 0) {
                formatted.append("-".repeat(50)).append("\n");
            }
        }

        return formatted.toString();
    }

    @Override
    public String toString() {
        return String.format("CSV File: %s (%d bytes)", fileName, getSize());
    }
}