package com.pmk.demotransferfile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class GenerateFile {
    private final long size;
    private final String name;
    public static final String RESOURCES = "src/main/resources/";

    public GenerateFile(long size, String name) {
        this.size = size;
        this.name = name;
    }

    public String createFile() throws IOException {
        try (FileOutputStream createFile = new FileOutputStream(name)) {
            byte[] buffer = new byte[64 * 1024];
            long bytesWritten = 0;

            while (bytesWritten < size) {
                new Random().nextBytes(buffer);
                createFile.write(buffer);
                bytesWritten += buffer.length;
            }
            return "generate file: " + name + " with size: " + size + " bytes";
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IOException();
    }

    public long getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) throws IOException {
        GenerateFile generateFile = new GenerateFile(1_073_741_824, RESOURCES + "test2.txt");
        generateFile.createFile();
    }
}
