package com.pmk.demotransferfile;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    public static final String FILES_PATH = "src/main/resources/Server_files";

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        try {
            this.dataInputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            this.dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String action = dataInputStream.readUTF();
            if ("upload".equals(action)) {
                receiveFile();
            } else if ("download".equals(action)) {
                sendFilesMenu();
                int fileIndex = dataInputStream.readInt();
                sendSelectedFile(fileIndex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    private void receiveFile() throws IOException {
        String fileName = dataInputStream.readUTF();
        long fileSize = dataInputStream.readLong();

        File file = new File(FILES_PATH, fileName);
        try (FileOutputStream fos = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            byte[] buffer = new byte[8192];
            long totalRead = 0;
            int bytesRead;
            while (totalRead < fileSize && (bytesRead = dataInputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
                totalRead += bytesRead;
            }
            bos.flush();
        }
        System.out.println("File saved: " + fileName);
    }

    private void sendFilesMenu() throws IOException {
        File dir = new File(FILES_PATH);
        File[] files = dir.listFiles();
        if (files != null) {
            dataOutputStream.writeInt(files.length);
            for (int i = 0; i < files.length; i++) {
                dataOutputStream.writeUTF((i + 1) + ". " + files[i].getName());
            }
            dataOutputStream.flush();
        }
    }

    private void sendSelectedFile(int fileIndex) throws IOException {
        File dir = new File(FILES_PATH);
        File[] files = dir.listFiles();
        if (files != null && fileIndex > 0 && fileIndex <= files.length) {
            File file = files[fileIndex - 1];
            dataOutputStream.writeUTF(file.getName());
            dataOutputStream.writeLong(file.length());

            try (FileInputStream fis = new FileInputStream(file);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    dataOutputStream.write(buffer, 0, bytesRead);
                }
                dataOutputStream.flush();
            }
            System.out.println("file send: " + file.getName());
        }
    }

    private void closeConnections() {
        try {
            if (dataInputStream != null) dataInputStream.close();
            if (dataOutputStream != null) dataOutputStream.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
