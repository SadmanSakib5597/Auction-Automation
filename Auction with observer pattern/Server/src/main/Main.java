package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static final int PORT = 9898;
    public static ServerSocket server;

    public static int ammount = 0;

    public static void main(String[] args) {

        try {
            server = new ServerSocket(PORT);
            System.out.println("Server Started");

            while (true) {
                Socket client = null;
                System.out.println("Waiting for connection...");
                client = server.accept();

                Thread t1 = new ClientHandler(client);
                t1.start();

            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}

class ClientHandler extends Thread {

    Socket client;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());

            String data = (String) objectInputStream.readObject();
            String line[] = data.split("=");

            if (line[0].equals("set")) {
                int clientAmmount = Integer.parseInt(line[1]);
                if (clientAmmount >= Main.ammount)
                    Main.ammount = Integer.parseInt(line[1]);

                System.out.println(Main.ammount);

                objectInputStream.close();
                client.close();
            } else {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                objectOutputStream.writeObject(Integer.toString(Main.ammount));
                objectOutputStream.close();
                client.close();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
