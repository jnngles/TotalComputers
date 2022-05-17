package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.Web;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jnngl.totalcomputers.system.overlays.Information;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.Field;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class VBoxManagerApplication extends WindowApplication {

    private boolean isTaskRunning = false;

    private Field password;
    private Button unlock, install, create;
    private Field name, ram, vram, hdd, iso, ostype;

    private String pswd = "";

    public static void main(String[] args) {
        ApplicationHandler.open(VBoxManagerApplication.class, args[0]);
    }

    public VBoxManagerApplication(TotalOS os, String path) {
        super(os, "VBox Manager", os.screenWidth/4*3, os.screenHeight/4*3, path);
    }

    private void run(String cmd) {
        System.out.println("Run command NOT as root: "+cmd);
        try {
            String[] commandLine = new String[]{"/bin/bash","-c",cmd};
            System.out.println("Command line: "+ Arrays.toString(commandLine));
            Process process = Runtime.getRuntime().exec(commandLine);

            String line;
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();
            process.waitFor();
        } catch (IOException e) {
            System.err.println("I/O Error -> "+e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void sudoRun(String cmd) {
        sudoRun(cmd, false);
    }

    private void sudoRun(String cmd, boolean pressReturnToFinishVirtualBoxInstallationIfNeededWhenPressReturnBlahBlahBlahMessageIsBeingPrinted) {
        System.out.println("Run command as root: "+cmd);
        try {
            String[] commandLine = (!pswd.isEmpty())?
                    new String[]{"/bin/bash","-c","echo \""+pswd+"\" | sudo -S "+cmd}
                    : new String[]{"/bin/bash","-c","sudo "+cmd};
            System.out.println("Command line: "+ Arrays.toString(commandLine));
            Process process = Runtime.getRuntime().exec(commandLine);

            String line;
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            OutputStreamWriter output = new OutputStreamWriter(process.getOutputStream());
            while (process.isAlive()) {
                line = input.readLine();
                if(line != null) {
                    if (pressReturnToFinishVirtualBoxInstallationIfNeededWhenPressReturnBlahBlahBlahMessageIsBeingPrinted
                            && line.contains("Return")) {
                        output.write('\n');
                        output.flush();
                    }
                    System.out.println(line);
                }
            }
            input.close();
            process.waitFor();
        } catch (IOException e) {
            System.err.println("I/O Error -> "+e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onStart() {
        if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            os.information.displayMessage(Information.Type.ERROR,
                    "This application does not support Windows.\nSupport: https://discord.gg/6fezjgfK7F",
                    null);
            close();
            return;
        }

        os.information.displayMessage(Information.Type.WARNING, "This application WILL modify system files. Use it at your own risk!", null);

        Font font = os.baseFont.deriveFont((float)os.screenHeight/128*3);
        FontMetrics metrics = Utils.getFontMetrics(font);

        password = new Field(0, 0, getWidth()/2, metrics.getHeight(), font, "",
                "Sudo password (Optional)", os.keyboard);
        unlock = new Button(Button.ButtonColor.BLUE, getWidth()/2, 0, getWidth()/2, metrics.getHeight(),
                font, "Unlock");

        install = new Button(Button.ButtonColor.BLUE, 0, metrics.getHeight(), getWidth(), metrics.getHeight(), font,
                "Install (Never run if already installed)");
        install.setLocked(true);

        create = new Button(Button.ButtonColor.BLUE, 0, metrics.getHeight()*2, getWidth(), metrics.getHeight(),
                font, "Create new VM");
        name = new Field(0, metrics.getHeight()*3, getWidth(), metrics.getHeight(), font, "", "New VM name",
                os.keyboard);
        ram = new Field(0, metrics.getHeight()*4, getWidth(), metrics.getHeight(), font, "",
                "RAM (megabytes)", os.keyboard);
        vram = new Field(0, metrics.getHeight()*5, getWidth(), metrics.getHeight(), font, "",
                "VRAM (megabytes) (Max: 128)", os.keyboard);
        hdd = new Field(0, metrics.getHeight()*6, getWidth(), metrics.getHeight(), font, "",
                "HHD size (megabytes)", os.keyboard);
        iso = new Field(0, metrics.getHeight()*7, getWidth(), metrics.getHeight(), font,
                "/usr/Desktop/YourISO.iso", "Path to .iso", os.keyboard);
        ostype = new Field(0, metrics.getHeight()*8, getWidth(), metrics.getHeight(), font,
                "", "OS Type", os.keyboard);

        unlock.registerClickEvent(() -> {
            pswd = password.getText();
            install.setLocked(new File(applicationPath + "/.tmp/install.run").exists());
        });

        install.registerClickEvent(() -> {
            isTaskRunning = true;
            install.setText("Check console for info");
            install.setLocked(true);
            create.setLocked(true);
            new Thread(() -> {
                String url = "https://download.virtualbox.org/virtualbox/6.1.32/VirtualBox-6.1.32-149290-Linux_amd64.run";
                try {
                    AtomicInteger done = new AtomicInteger();
                    new File(applicationPath + "/.tmp/").mkdirs();
                    new File(applicationPath + "/.tmp/install.run").createNewFile();
                    Web.downloadFileFromURL(url, applicationPath + "/.tmp/install.run",
                            () -> {
                                if ((done.get() / 1000) != (done.incrementAndGet() / 1000))
                                    System.out.println((done.get() / 1000) + "MB done");
                            });
                    done.set(0);
                    new File(applicationPath+"/.tmp/vboxjxpcom.jar").createNewFile();
                    Web.downloadFileFromURL(
                            "https://github.com/JNNGL/TotalComputers/raw/appbase/VBox%20Manager.app/vboxjxpcom.jar",
                            applicationPath+"/.tmp/vboxjxpcom.jar",
                            () -> {
                                if((done.get() / 1000) != (done.incrementAndGet() / 1000))
                                    System.out.println((done.get() / 1000) + "MB done");
                            });
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    return;
                }
                String applicationPath = this.applicationPath.replace(" ", "\\ ");
                sudoRun("chmod +x " + applicationPath + "/.tmp/install.run", true);
                sudoRun(applicationPath + "/.tmp/install.run");
                sudoRun("apt install -y gcc make perl");
                sudoRun("/sbin/vboxconfig");
                sudoRun("cp -v "+applicationPath+"/.tmp/vboxjxpcom.jar /opt/VirtualBox");
                isTaskRunning = false;
                create.setLocked(false);
            }).start();
        });

        create.registerClickEvent(() -> {
            new Thread(() -> {
                create.setText("Check console for info");
                if(name.isEmpty()) {
                    create.setText("Name cannot be empty");
                    return;
                }
                if(ostype.isEmpty()) {
                    create.setText("`OS Type' field cannot be empty");
                    return;
                }
                try {
                    Integer.parseInt(ram.getText());
                    Integer.parseInt(vram.getText());
                    Integer.parseInt(hdd.getText());
                } catch (Throwable e) {
                    create.setText(e.getMessage());
                    return;
                }
                if(!iso.getText().endsWith(".iso")) {
                    create.setText(iso.getText()+": invalid file extension");
                    return;
                }
                File fiso = os.fs.toFile(iso.getText());
                if(fiso == null) {
                    create.setText("ISO file not found");
                    return;
                }

                isTaskRunning = true;
                create.setLocked(true);

                String baseFolder = System.getProperty("user.home")+"/.tcmp_vms";
                new File(baseFolder).mkdirs();
                baseFolder = baseFolder.replace(" ", "\\ ");
                run("VBoxManage createvm --name \""+name.getText()+"\" --ostype \""+ostype.getText()+"\" --register --basefolder "+baseFolder);
                run("VBoxManage modifyvm \""+name.getText()+"\" --ioapic on");
                run("VBoxManage modifyvm \""+name.getText()+"\" --memory "+Integer.parseInt(ram.getText())+" --vram "+Integer.parseInt(vram.getText()));
                run("VBoxManage modifyvm \""+name.getText()+"\" --nic1 nat");
                run("VBoxManage createhd --filename "+(baseFolder+"/"+name.getText()).replace(" ", "\\ ")+".vdi --size "+Integer.parseInt(hdd.getText())+" --format VDI");
                run("VBoxManage storagectl \""+name.getText()+"\" --name \"SATA Controller\" --add sata --controller IntelAhci");
                run("VBoxManage storageattach \""+name.getText()+"\" --storagectl \"SATA Controller\" --port 0 --device 0 --type hdd --medium "+(baseFolder+"/"+name.getText()).replace(" ", "\\ ")+".vdi");
                run("VBoxManage storagectl \""+name.getText()+"\" --name \"IDE Controller\" --add ide --controller PIIX4");
                run("VBoxManage storageattach \""+name.getText()+"\" --storagectl \"IDE Controller\" --port 1 --device 0 --type dvddrive --medium "+fiso.getAbsolutePath().replace(" ", "\\ "));
                create.setLocked(false);
                isTaskRunning = false;
            }).start();
        });
    }

    @Override
    protected boolean onClose() {
        if(isTaskRunning) return false;
        return true;
    }

    @Override
    protected void update() {
        renderCanvas();
    }

    @Override
    protected void render(Graphics2D g) {
        password.render(g);
        unlock.render(g);
        install.render(g);
        create.render(g);
        name.render(g);
        ram.render(g);
        vram.render(g);
        hdd.render(g);
        iso.render(g);
        ostype.render(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(!os.requestAdminRights()) return;
        password.processInput(x, y, type);
        unlock.processInput(x, y, type);
        install.processInput(x, y, type);
        create.processInput(x, y, type);
        name.processInput(x, y, type);
        ram.processInput(x, y, type);
        vram.processInput(x, y, type);
        hdd.processInput(x, y, type);
        iso.processInput(x, y, type);
        ostype.processInput(x, y, type);
    }
}
