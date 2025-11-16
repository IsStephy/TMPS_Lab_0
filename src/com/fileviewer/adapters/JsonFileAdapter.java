package com.fileviewer.adapters;

import com.fileviewer.interfaces.ReadableFile;

public class JsonFileAdapter implements ReadableFile {
    private final String fileName;
    private final String jsonContent;

    public JsonFileAdapter(String fileName, String jsonContent) {
        this.fileName = fileName;
        this.jsonContent = jsonContent;
    }

    @Override
    public String readContent() {
        return formatJson(jsonContent);
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getFileType() {
        return "JSON";
    }

    @Override
    public long getSize() {
        return jsonContent.getBytes().length;
    }

    private String formatJson(String json) {
        StringBuilder formatted = new StringBuilder();
        int indentLevel = 0;
        boolean inString = false;

        for (char c : json.toCharArray()) {
            if (c == '"' && formatted.length() > 0 && formatted.charAt(formatted.length() - 1) != '\\') {
                inString = !inString;
            }

            if (!inString) {
                if (c == '{' || c == '[') {
                    formatted.append(c).append('\n');
                    indentLevel++;
                    formatted.append("  ".repeat(indentLevel));
                } else if (c == '}' || c == ']') {
                    formatted.append('\n');
                    indentLevel--;
                    formatted.append("  ".repeat(indentLevel));
                    formatted.append(c);
                } else if (c == ',') {
                    formatted.append(c).append('\n');
                    formatted.append("  ".repeat(indentLevel));
                } else if (c == ':') {
                    formatted.append(c).append(' ');
                } else if (!Character.isWhitespace(c)) {
                    formatted.append(c);
                }
            } else {
                formatted.append(c);
            }
        }

        return formatted.toString();
    }

    @Override
    public String toString() {
        return String.format("JSON File: %s (%d bytes)", fileName, getSize());
    }
}