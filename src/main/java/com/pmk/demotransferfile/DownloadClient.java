package com.pmk.demotransferfile;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class DownloadClient {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    public static final String FILES_PATH = "src/main/resources/Client_files";

    public DownloadClient(String host, int port) {
        File directory = new File(FILES_PATH);
        if (!directory.exists()) {
            directory.mkdir();
        }

        try {
            socket = new Socket(host, port);
            this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            this.out = new DataOutputStream(socket.getOutputStream());
            receiveFile();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    private void receiveFile() throws IOException {
        out.writeUTF("download");
        int filesCount = in.readInt();

        for (int i = 0; i < filesCount; i++) {
            System.out.println(in.readUTF());
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Select file number: ");
        int fileIndex = scanner.nextInt();
        out.writeInt(fileIndex);
        out.flush();
        String fileName = in.readUTF();
        long fileSize = in.readLong();
        File outputFile = new File(FILES_PATH, fileName);

        try (FileOutputStream fos = new FileOutputStream(outputFile);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            byte[] buffer = new byte[8192];
            long totalRead = 0;
            int bytesRead;
            while (totalRead < fileSize && (bytesRead = in.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
                totalRead += bytesRead;
            }
            bos.flush();
        }
        System.out.println("The file " + fileName + "has been downloaded.");
    }

    private void closeConnections() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new DownloadClient("127.0.0.1", Server.PORT);
    }
}
