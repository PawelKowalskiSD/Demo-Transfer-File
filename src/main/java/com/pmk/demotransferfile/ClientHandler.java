package com.pmk.demotransferfile;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;
    public static final String FILES_PATH = "src/main/resources/Server_files";

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        try {
            this.in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            this.out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String action = in.readUTF();
            if ("upload".equals(action)) {
                receiveFile();
            } else if ("download".equals(action)) {
                sendFilesMenu();
                int fileIndex = in.readInt();
                sendSelectedFile(fileIndex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    private void receiveFile() throws IOException {
        String fileName = in.readUTF();
        long fileSize = in.readLong();

        File file = new File(FILES_PATH, fileName);
        try (FileOutputStream fos = new FileOutputStream(file);
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
        System.out.println("File saved: " + fileName);
    }

    private void sendFilesMenu() throws IOException {
        File dir = new File(FILES_PATH);
        File[] files = dir.listFiles();
        if (files != null) {
            out.writeInt(files.length);
            for (int i = 0; i < files.length; i++) {
                out.writeUTF((i + 1) + ". " + files[i].getName());
            }
            out.flush();
        }
    }

    private void sendSelectedFile(int fileIndex) throws IOException {
        File dir = new File(FILES_PATH);
        File[] files = dir.listFiles();
        if (files != null && fileIndex > 0 && fileIndex <= files.length) {
            File file = files[fileIndex - 1];
            out.writeUTF(file.getName());
            out.writeLong(file.length());

            try (FileInputStream fis = new FileInputStream(file);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            }
            System.out.println("file send: " + file.getName());
        }
    }

    private void closeConnections() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
