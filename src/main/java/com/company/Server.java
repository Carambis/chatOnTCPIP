package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static List<Connection> connections = new ArrayList<>();

    public Server(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started");
        while (true) {
            Socket socket = serverSocket.accept();
            connections.add(new Connection(socket));
            System.out.println("We have new client");
        }
    }

    private class Connection extends Thread {

        private DataInputStream input;
        private DataOutputStream output;
        private String name;

        public Connection(Socket socket) throws IOException {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            start();
        }

        public void run() {
            try {
                output.writeUTF("Enter your nick:");
                name = input.readUTF();

                output.writeUTF("Start communication");
                while (true) {
                    String data = input.readUTF();
                    receiveText(data);
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

        private void receiveText(String text) throws IOException {
            System.out.println(name + " send text: " + text);
            sendText(text);
        }

        private void sendText(String text) throws IOException {
            for (Connection connection : Server.connections) {
                connection.output.writeUTF(name + ": " + text);
            }
        }
    }

    public static void main(String[] args) {
        int defaultPort = 8080;
        try {
            if (args.length != 1) {
                System.out.println("Invalid count args");
                System.out.println("Starting server with default configuration");
                new Server(defaultPort);
            } else {
                new Server(Integer.parseInt(args[0]));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}