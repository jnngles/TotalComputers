package com.jnngl.system;

public class Test {

    public static void main(String[] args) {
        if(args[0].equalsIgnoreCase("client")) {
            Client client = new Client("", args[2], new ConnectionListener() {
                @Override
                public void onConnect(Server.Connection con) {
                    System.out.println("CLIENT -> Connected: "+con.name);
                }

                @Override
                public void onDisconnect(Server.Connection con) {
                    System.out.println("CLIENT -> Disconnected: "+con.name);
                }

                @Override
                public void onMessage(Server.Connection con, String data) {
                    System.out.println("CLIENT -> Message: "+data);
                }
            }, args[1], Server.PORT);
            try {
                client.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            while(true) {
                String message = System.console().readLine();
                if(!client.isRunning()) break;
                client.writer.println(message);
            }
        } else if(args[0].equalsIgnoreCase("server")) {
            Server server = new Server();
            server.addListener(new ConnectionListener() {
                @Override
                public void onConnect(Server.Connection con) {
                    System.out.println("SERVER -> Connect: "+con.name);
                }

                @Override
                public void onDisconnect(Server.Connection con) {
                    System.out.println("SERVER -> Disconnect: "+con.name);
                }

                @Override
                public void onMessage(Server.Connection con, String data) {
                    System.out.println("SERVER -> Message from "+con.name+": "+data);
                }
            });
            new Thread(server).start();
            while(true) {
                String line = System.console().readLine();
                if(line.equals("server://close")) {
                    server.close();
                    break;
                }
                if(line.startsWith("msg->name=")) {
                    String[] parts = line.substring(10).split(";data-> ");
                    if(parts.length < 2) {
                        System.err.println("Server ERROR -> Failed to format message `"+line+'\'');
                        continue;
                    }
                    String dst = parts[0];
                    if(dst.contains(" ")) {
                        System.err.println("Server ERROR -> Failed to format message `"+line+'\'');
                        continue;
                    }
                    String msg = line.substring(18+dst.length());
                    for (Server.Connection con : server.connections()) {
                        if(!con.name.equals(dst)) continue;
                        con.writer.println(msg);
                        System.out.println("SERVER -> Send message to " + con.name + ": " + msg);
                    }
                }
            }
        }
    }

}
