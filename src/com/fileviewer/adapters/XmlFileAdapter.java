package com.fileviewer.adapters;

import com.fileviewer.interfaces.ReadableFile;

public class XmlFileAdapter implements ReadableFile {
    private final String fileName;
    private final String xmlContent;

    public XmlFileAdapter(String fileName, String xmlContent) {
        this.fileName = fileName;
        this.xmlContent = xmlContent;
    }

    @Override
    public String readContent() {
        // Format XML for better readability
        return formatXml(xmlContent);
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getFileType() {
        return "XML";
    }

    @Override
    public long getSize() {
        return xmlContent.getBytes().length;
    }

    private String formatXml(String xml) {
        StringBuilder formatted = new StringBuilder();
        int indentLevel = 0;
        boolean inTag = false;
        boolean closingTag = false;

        for (int i = 0; i < xml.length(); i++) {
            char c = xml.charAt(i);

            if (c == '<') {
                if (i + 1 < xml.length() && xml.charAt(i + 1) == '/') {
                    closingTag = true;
                    indentLevel--;
                    formatted.append('\n').append("  ".repeat(Math.max(0, indentLevel)));
                } else if (i + 1 < xml.length() && xml.charAt(i + 1) != '?') {
                    if (!inTag) {
                        formatted.append('\n').append("  ".repeat(indentLevel));
                    }
                }
                inTag = true;
                formatted.append(c);
            } else if (c == '>') {
                formatted.append(c);
                inTag = false;
                if (!closingTag && i > 0 && xml.charAt(i - 1) != '/') {
                    indentLevel++;
                }
                closingTag = false;
            } else if (!Character.isWhitespace(c) || inTag) {
                formatted.append(c);
            }
        }

        return formatted.toString();
    }

    @Override
    public String toString() {
        return String.format("XML File: %s (%d bytes)", fileName, getSize());
    }
}