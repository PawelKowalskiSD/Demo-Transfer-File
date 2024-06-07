package com.pmk.demotransferfile;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.pmk.demotransferfile.ClientHandler.FILES_PATH;
public class Server {
    public static final int PORT = 3030;
    private ServerSocket serverSocket;

    public Server() {
        File directory = new File(FILES_PATH);
        if (!directory.exists()) {
            directory.mkdir();
        }

        try {
            serverSocket = new ServerSocket(PORT);
            acceptConnections();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptConnections() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
