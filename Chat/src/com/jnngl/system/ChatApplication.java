package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jnngl.totalcomputers.system.overlays.Information;
import com.jnngl.totalcomputers.system.ui.ElementList;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.Field;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

public class ChatApplication extends WindowApplication implements ConnectionListener {
    private static record Message(String msg, boolean incoming) {}

    private Set<Client> clients;
    private Font font;
    private int fontHeight;
    private Server server;
    private Map<String, List<Message>> history;
    private ElementList contacts;
    private String name;
    private Button sendBtn;
    private Field msgField;
    private Button addBtn;

    private final ConnectionListener clientListener = new ConnectionListener() {
        @Override
        public void onConnect(Server.Connection con) {
        }

        @Override
        public void onDisconnect(Server.Connection con) {
        }

        @Override
        public void onMessage(Server.Connection con, String data) {
            System.out.println("CLIENT: "+data);
            if(!history.containsKey(con.name)) {
                history.put(con.name, new ArrayList<>());
                contacts.addEntry(con.name);
            }
            history.get(con.name).add(new Message(data, true));
        }
    };

    public static void main(String[] args) {
        ApplicationHandler.open(ChatApplication.class, args[0]);
    }

    public ChatApplication(TotalOS os, String path) {
        super(os, "Chat", os.screenWidth/3*2, os.screenHeight/3*2, path);
    }

    private void loadHistory() {
        try {
            File file = new File(applicationPath, ".history");
            if (!file.exists()) {
                file.createNewFile();
            }
            String data = Files.readString(file.toPath());
            String[] lines = data.split("\n");
            List<Message> tmpMsgs = null;
            String tmpName = null;
            for(String line : lines) {
                if(line.startsWith("=")) {
                    if(tmpMsgs != null) {
                        history.put(tmpName, tmpMsgs);
                        contacts.addEntry(tmpName);
                    }
                    tmpName = line.substring(1);
                    tmpMsgs = new ArrayList<>();
                } else if(line.startsWith(">")) {
                    if(tmpMsgs != null)
                        tmpMsgs.add(new Message(line.substring(1), false));
                } else if(line.startsWith("<")) {
                    if(tmpMsgs != null)
                        tmpMsgs.add(new Message(line.substring(1), true));
                } else System.err.println("Failed to process line `"+line+'\'');
            }
        } catch (IOException e) {
            System.err.println("Failed to load history");
        }
    }

    @Override
    protected void onStart() {
        history = new HashMap<>();
        clients = new HashSet<>();
        font = os.baseFont.deriveFont((float)os.screenHeight/128*3);
        fontHeight = Utils.getFontMetrics(font).getHeight();
        addBtn = new Button(Button.ButtonColor.BLUE, 0, 0, getWidth()/4, fontHeight, font, "Add");
        addBtn.registerClickEvent(() -> {
            history.put("0.0.0.0 test_computer", new ArrayList<>());
            contacts.addEntry("0.0.0.0 test_computer");
            try {
                Client client = new Client("0.0.0.0 test_computer", name, clientListener, "localhost", Server.PORT);
                client.run();
                clients.add(client);
            } catch (Throwable e) {
                System.err.println("Failed to create client: "+e.getMessage());
            }
        });
        sendBtn = new Button(Button.ButtonColor.BLUE, getWidth()-getWidth()/6, getHeight()-fontHeight,
                getWidth()/6, fontHeight, font, "Send");
        sendBtn.registerClickEvent(() -> {
            for(Client c : clients) {
                if(c.dst.startsWith(contacts.getSelectedElement().split(" ")[0])) {
                    c.writer.println("msg->name="+c.dst.split(" ")[1]+";data-> "+msgField.getText());
                    msgField.setText("");
                    break;
                }
            }
        });
        msgField = new Field(getWidth()/4, getHeight()-fontHeight, getWidth()-sendBtn.getWidth()-getWidth()/4
                , fontHeight, font, "", "Message", os.keyboard);
        contacts = new ElementList(0, addBtn.getHeight(), getWidth()/4, getHeight(), font);
        Object isLocked = os.storage.get(ChatApplication.class, "running");
        if(isLocked instanceof Boolean && (Boolean) isLocked) {
            os.information.displayMessage(Information.Type.ERROR, "This application is already " +
                    "running on this computer", this::close);
            return;
        }
//        os.storage.put(ChatApplication.class, "running", true);
        server = (Server) os.storage.get(ChatApplication.class, "server");
        if(server == null) {
            server = new Server();
            new Thread(server).start();
            os.storage.put(ChatApplication.class, "server", server);
        }
        name = server.getIP() + " " + os.name;
        server.addListener(this);
        loadHistory();
        for(String name : history.keySet()) {
            contacts.addEntry(name);
            try {
                Client client = new Client(name, this.name, clientListener, name.split(" ")[0], Server.PORT);
                client.run();
                clients.add(client);
            } catch (Exception ignored) {}
        }
    }

    @Override
    protected boolean onClose() {
        for(Client client : clients)
            client.writer.println("packet->server://disconnect");
        if(server != null) {
            server.removeListener(this);
            os.storage.put(ChatApplication.class, "running", false);
        }
        return true;
    }

    @Override
    protected void update() {
        renderCanvas();
    }

    @Override
    protected void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        addBtn.render(g);
        contacts.render(g);
        if(contacts.getSelectedIndex() < 0) return;
        sendBtn.render(g);
        msgField.render(g);
        g.setColor(Color.BLACK);
        g.setFont(font);
        int y = 0;
        for(Message msg : history.get(contacts.getSelectedElement())) {
            g.drawString(msg.incoming?"< ":"> "+msg.msg, 0, y+=fontHeight);
        }
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(contacts.getSelectedIndex() >= 0) {
            sendBtn.processInput(x, y, type);
            msgField.processInput(x, y, type);
        }
        addBtn.processInput(x, y, type);
        contacts.processInput(x, y, type);
    }

    @Override
    public void onConnect(Server.Connection con) {
    }

    @Override
    public void onDisconnect(Server.Connection con) {

    }

    @Override
    public void onMessage(Server.Connection c, String data) {
        System.out.println("SERVER: "+data);
        if(!history.containsKey(c.name)) {
            history.put(c.name, new ArrayList<>());
            contacts.addEntry(c.name);
            try {
                Client client = new Client(c.name, name, clientListener, c.name.split(" ")[0], Server.PORT);
                client.run();
                clients.add(client);
            } catch (Throwable e) {
                System.err.println("Failed to create client: "+e.getMessage());
            }
        }
        if(data.startsWith("msg->name=")) {
            String[] parts = data.substring(10).split(";data-> ");
            if(parts.length < 2) {
                System.err.println("Server ERROR -> Failed to format message `"+data+'\'');
                return;
            }
            String dst = parts[0];
            if(dst.contains(" ")) {
                System.err.println("Server ERROR -> Failed to format message `"+data+'\'');
                return;
            }
            String msg = data.substring(18+dst.length());
//            for (Server.Connection con : server.connections()) {
//                if(!con.name.equals(dst)) continue;
//                con.writer.println(msg);
                if(name.split(" ")[1].equals(dst))
                    history.get(0).add(new Message(msg, true));
//                System.out.println("SERVER -> Send message to " + con.name + ": " + msg);
//            }
        }
    }
}
