package com.jnngl.system;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server implements Runnable {
    public static class Connection {
        public String name;
        public Socket socket;
        public PrintWriter writer;
        public BufferedReader reader;
        public Thread listenThread;

        public Connection name(String name) {
            this.name = name;
            return this;
        }

        public Connection socket(Socket socket) {
            this.socket = socket;
            return this;
        }

        public Connection writer(PrintWriter writer) {
            this.writer = writer;
            return this;
        }

        public Connection reader(BufferedReader reader) {
            this.reader = reader;
            return this;
        }
    }

    public static final int PORT = 7253;
    private final Set<Connection> connections = new HashSet<>();
    private boolean running = false;
    private String ip = "null";
    private Set<ConnectionListener> listeners = new HashSet<>();
    private ServerSocket serverSocket;

    public Set<Connection> connections() {
        return connections;
    }

    public void run() {
        running = true;
        try {
            serverSocket = new ServerSocket(PORT);
            ip = serverSocket.getInetAddress().getHostAddress();
            System.out.println("Started server on port "+PORT);

            while(running) {
                Socket socket = serverSocket.accept();
                Connection con = new Connection();
                con.socket = socket;
                con.writer = new PrintWriter(socket.getOutputStream(), true);
                con.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                con.name = con.reader.readLine();
                con.listenThread = new Thread(() -> {
                    while(running && !con.listenThread.isInterrupted()) {
                        if(con.socket.isClosed()) {
                            disconnect(con);
                            continue;
                        }
                        try {
                            String line = con.reader.readLine();
                            if(line == null) continue;
                            if(line.equals("packet->server://disconnect")) {
                                for(ConnectionListener listener : listeners)
                                    listener.onDisconnect(con);
                                disconnect(con);
                                continue;
                            }
                            for(ConnectionListener listener : listeners)
                                listener.onMessage(con, line);
                        } catch (IOException e) {
                            System.out.println("Server <-- Failed to read line from client");
                        }
                    }
                });
                con.listenThread.start();
                for(ConnectionListener listener : listeners)
                    listener.onConnect(con);
                connections.add(con);
                System.out.println("Server <-- Connected user "+con.name);
            }

        } catch (IOException e) {
            System.err.println("[TotalComputers: Chat] Server ERROR: "+e.getMessage());
        }
    }

    public void disconnect(Connection con) {
        con.writer.println("packet->server://disconnect");
        con.listenThread.interrupt();
        connections.remove(con);
        System.out.println("Server --> Disconnected user "+con.name);
    }

    public void removeListener(ConnectionListener listener) {
        listeners.remove(listener);
    }

    public void addListener(ConnectionListener listener) {
        listeners.add(listener);
    }

    public void close() {
        for(Connection con : connections) {
            con.writer.println("packet->server://disconnect");
            con.listenThread.interrupt();
        }
        connections.clear();
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Server ERROR -> I/O Error: "+e.getMessage());
        }

        listeners = null;
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public String getIP() {
        return ip;
    }

}
