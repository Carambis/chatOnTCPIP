package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket cs;

    public Client(String host, int port) throws IOException {
        cs = new Socket(host, port);
        new InputFromServer();
        new OutputToServer();
    }

    private class InputFromServer extends Thread {

        private DataInputStream input;

        public InputFromServer() throws IOException {
            input = new DataInputStream(cs.getInputStream());
            start();
        }

        public void run() {
            try {
                while (true) {
                    String data = input.readUTF();
                    receiveText(data);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        private void receiveText(String text) {
            System.out.println(text);
        }

    }

    private class OutputToServer extends Thread {

        private DataOutputStream output;
        private Scanner scan;

        public OutputToServer() throws IOException {
            output = new DataOutputStream(cs.getOutputStream());
            scan = new Scanner(System.in);
            start();
        }

        public void run() {
            try {
                while (true) {
                    String data = scan.nextLine();
                    if (data != null) {
                        output.writeUTF(data);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        int defaultPort = 8080;
        String defaultHost = "127.0.0.1";
        try {
            if(args.length != 2){
                System.out.println("Invalid count args");
                System.out.println("Starting client with default configuration");
                new Client(defaultHost,defaultPort);
            }else{
                new Client(args[0],Integer.parseInt(args[1]));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}