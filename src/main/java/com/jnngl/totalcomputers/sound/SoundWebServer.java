package com.jnngl.totalcomputers.sound;

import com.jnngl.totalcomputers.TotalComputers;
import org.apache.commons.io.IOUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class SoundWebServer {
    private static Server server;

    public static void run() {
        server = new Server(7254);
        ResourceHandler handler = new ResourceHandler();
        handler.setDirectoriesListed(true);
        handler.setWelcomeFiles(new String[] { "plugins/TotalComputers/httdocs/index.html" });

        File dataFolder = JavaPlugin.getPlugin(TotalComputers.class).getDataFolder();
        File httdocs = new File(dataFolder, "httdocs");
        httdocs.mkdirs();
        handler.setResourceBase(httdocs.getAbsolutePath());

        createFiles(httdocs);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { handler, new DefaultHandler() });
        server.setHandler(handlers);

        try {
            server.start();
        } catch (Exception e) {
            System.err.println("[TotalComputers: Sound WebServer] Failed to start web server :(");
            System.err.println("Server::start -> "+e.getMessage());
        }
    }

    public static void shutdown() throws Exception {
        server.stop();
    }

    private static String readResource(String name) {
        InputStream is = SoundWebServer.class.getClassLoader().getResourceAsStream(name);
        try {
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Failed to read file from resources");
            return null;
        }
    }

    private static void createFiles(File httdocs) {
        try {
            Files.writeString(new File(httdocs, "index.html").toPath(),
                    readResource("index.html"));
            Files.writeString(new File(httdocs, "howler.core.js").toPath(),
                    readResource("howler.core.js"));
            Files.writeString(new File(httdocs, "websocket.js").toPath(),
                    readResource("websocket.js"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
