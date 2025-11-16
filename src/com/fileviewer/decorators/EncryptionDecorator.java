package com.fileviewer.decorators;

import com.fileviewer.interfaces.ReadableFile;

import java.util.Base64;

public class EncryptionDecorator extends FileDecorator {
    private final int encryptionKey;

    public EncryptionDecorator(ReadableFile wrappedFile) {
        this(wrappedFile, 13); // Default to ROT13
    }

    public EncryptionDecorator(ReadableFile wrappedFile, int key) {
        super(wrappedFile);
        this.encryptionKey = key;
    }

    @Override
    public String readContent() {
        String originalContent = wrappedFile.readContent();
        String encrypted = encrypt(originalContent);
        String decrypted = decrypt(encrypted);

        return "╔══════════════════════════════════════════╗\n" +
                "║        ENCRYPTED FILE (Decrypted)        ║\n" +
                "║  Encryption: Caesar Cipher (Key: " + String.format("%-2d", encryptionKey) + ")    ║\n" +
                "║  Status: Successfully Decrypted          ║\n" +
                "╚══════════════════════════════════════════╝\n\n" +
                decrypted;
    }

    @Override
    public String getFileName() {
        return wrappedFile.getFileName() + ".encrypted";
    }

    @Override
    public long getSize() {
        // Encryption adds some overhead
        return wrappedFile.getSize() + 50; // Add header overhead
    }

    private String encrypt(String content) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : content.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                encrypted.append((char) ((c - base + encryptionKey) % 26 + base));
            } else {
                encrypted.append(c);
            }
        }
        return Base64.getEncoder().encodeToString(encrypted.toString().getBytes());
    }

    private String decrypt(String encrypted) {
        try {
            String decoded = new String(Base64.getDecoder().decode(encrypted));
            StringBuilder decrypted = new StringBuilder();
            for (char c : decoded.toCharArray()) {
                if (Character.isLetter(c)) {
                    char base = Character.isUpperCase(c) ? 'A' : 'a';
                    decrypted.append((char) ((c - base - encryptionKey + 26) % 26 + base));
                } else {
                    decrypted.append(c);
                }
            }
            return decrypted.toString();
        } catch (Exception e) {
            return "[Decryption failed]";
        }
    }

    @Override
    public String toString() {
        return String.format("Encrypted %s (Key: %d)",
                wrappedFile.getFileType(),
                encryptionKey);
    }
}