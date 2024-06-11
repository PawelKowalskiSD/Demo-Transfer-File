package com.pmk.demotransferfile;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class UploadClient {
    private Socket socket;
    private DataOutputStream dataOutputStream;

    public UploadClient(String host, int port) {
        try {
            socket = new Socket(host, port);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            sendFile();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    private void sendFile() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("provide the path to the file: ");
        String filePath = scanner.nextLine();
        File file = new File(filePath);

        if (file.exists()) {
            dataOutputStream.writeUTF("upload");
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
            System.out.println("The file has been sent.");
        } else {
            System.out.println("file does not exist.");
        }
    }

    private void closeConnections() {
        try {
            if (dataOutputStream != null) dataOutputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new UploadClient("127.0.0.1", Server.PORT);
    }
}
