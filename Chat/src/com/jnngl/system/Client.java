package com.jnngl.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private final String hostname;
    private final int port;
    private final ConnectionListener listener;
    private Socket socket;
    private Thread read;
    public PrintWriter writer;
    private Server.Connection connection;
    private boolean running = false;
    public final String name;
    public final String dst;

    public Client(String dst, String name, ConnectionListener listener, String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        this.listener = listener;
        this.name = name;
        this.dst = dst;
    }

    public void run() throws UnknownHostException, IOException {
        try {
            socket = new Socket(hostname, port);
            writer = new PrintWriter(socket.getOutputStream(), true);
            connection = new Server.Connection().socket(socket).name(name).writer(writer);
            writer.println(name);
            System.out.println("Connected to the server");
            read = new Thread(() -> {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    connection.reader = reader;
                    listener.onConnect(connection);

                    while(running && !read.isInterrupted()) {
                        try {
                            String response = reader.readLine();
                            if(response.equals("packet->server://disconnect")) {
                                listener.onDisconnect(connection);
                                read.interrupt();
                                socket.close();
                                running = false;
                                break;
                            }
                            listener.onMessage(connection, response);
                        } catch (IOException e) {
                            System.err.println("Client ERROR -> Failed to read data");
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Client ERROR -> I/O Error: "+e.getMessage());
                }
            });
            running = true;
            read.start();
        } catch (UnknownHostException e) {
            System.err.println("Client ERROR -> Server not found: "+e.getMessage());
            throw e;
        } catch (IOException e) {
            System.err.println("Client ERROR -> I/O Error: "+e.getMessage());
            throw e;
        }
    }

    public boolean isRunning() {
        return running;
    }
}
