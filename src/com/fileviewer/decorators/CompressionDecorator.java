package com.fileviewer.decorators;

import com.fileviewer.interfaces.ReadableFile;

import java.util.Base64;
import java.util.zip.Deflater;

public class CompressionDecorator extends FileDecorator {
    private static final double COMPRESSION_RATIO = 0.6; // Simulated 40% compression

    public CompressionDecorator(ReadableFile wrappedFile) {
        super(wrappedFile);
    }

    @Override
    public String readContent() {
        String originalContent = wrappedFile.readContent();
        return decompress(compress(originalContent));
    }

    @Override
    public String getFileName() {
        return wrappedFile.getFileName() + ".compressed";
    }

    @Override
    public long getSize() {
        // Return simulated compressed size
        return (long) (wrappedFile.getSize() * COMPRESSION_RATIO);
    }

    private String compress(String content) {
        try {
            byte[] input = content.getBytes("UTF-8");
            Deflater deflater = new Deflater();
            deflater.setInput(input);
            deflater.finish();

            byte[] buffer = new byte[input.length * 2];
            int compressedSize = deflater.deflate(buffer);
            deflater.end();

            byte[] output = new byte[compressedSize];
            System.arraycopy(buffer, 0, output, 0, compressedSize);

            return Base64.getEncoder().encodeToString(output);
        } catch (Exception e) {
            return content; // Fallback to original if compression fails
        }
    }

    private String decompress(String compressed) {
        try {
            byte[] input = Base64.getDecoder().decode(compressed);
            java.util.zip.Inflater inflater = new java.util.zip.Inflater();
            inflater.setInput(input);

            byte[] buffer = new byte[input.length * 10];
            int decompressedSize = inflater.inflate(buffer);
            inflater.end();

            String decompressed = new String(buffer, 0, decompressedSize, "UTF-8");

            return "╔══════════════════════════════════════════╗\n" +
                    "║      COMPRESSED FILE (Decompressed)      ║\n" +
                    "║  Original Size: " + String.format("%-6d", wrappedFile.getSize()) + " bytes           ║\n" +
                    "║  Compressed Size: " + String.format("%-6d", getSize()) + " bytes         ║\n" +
                    "║  Compression Ratio: " + String.format("%.1f", (1 - COMPRESSION_RATIO) * 100) + "%              ║\n" +
                    "╚══════════════════════════════════════════╝\n\n" +
                    decompressed;
        } catch (Exception e) {
            return "[Decompression failed]\n" + compressed;
        }
    }

    @Override
    public String toString() {
        return String.format("Compressed %s (%d bytes -> %d bytes)",
                wrappedFile.getFileType(),
                wrappedFile.getSize(),
                getSize());
    }
}