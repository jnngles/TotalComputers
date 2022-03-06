/*
    Plugin for computers in vanilla minecraft!
    Copyright (C) 2022  JNNGL

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.jnngl.system;

import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.ConsoleApplication;

import java.io.File;
import java.io.FilenameFilter;

public class Terminal extends ConsoleApplication {

    private File path;

    public static void main(String[] args) {
        ApplicationHandler.open(Terminal.class, args[0]);
    }

    public Terminal(TotalOS os, String path) {
        super(os, "Terminal", os.screenWidth/3*2, os.screenHeight/3*2, path);
    }

    @Override
    protected void onStart() {
        path = new File(os.fs.root()+"/usr");
        putString(os.name).putString("$").putString(path.getPath().replace(os.fs.root(), "")).putString(": ");
    }

    @Override
    protected boolean onClose() {
        return true;
    }

    private void processCommand(String name, String[] args) {
        switch (name) {
            case "cd": {
                File probe = new File(path.getPath()+"/"+args[0]);
                if(args[0].startsWith("/")) probe = new File(os.fs.root()+args[0]);
                if(!probe.exists()) {
                    putString("Directory '").putString(args[0]).putString("' does not exists.\n");
                    break;
                }
                if(!probe.isDirectory()) {
                    putString("'").putString(args[0]).putString("' is a file.\n");
                    break;
                }
                path = probe;
                break;
            }

            case "ls": {
                boolean A = false;
                boolean a = false;

                String directory = path.getPath();

                for(String arg : args) {
                    if(arg.equals("")) continue;
                    if(arg.startsWith("-") && !arg.startsWith("--")) {
                        if(arg.contains("a")) {
                            a = true;
                        }
                        if(arg.contains("A")) {
                            a = true;
                            A = true;
                        }
                    } else if(arg.startsWith("--")) {

                    } else {
                        if(arg.startsWith("/")) directory = os.fs.root()+arg;
                        else directory = path.getPath()+"/"+arg;
                    }
                }

                boolean finalA = a;
                File[] files = new File(directory).listFiles((dir, name1) -> {
                    if(name1.startsWith(".")) {
                        if(!finalA) return false;
                    }

                    return true;
                });

                if(files == null) {
                    putString("Failed to access directory '").putString(directory).putString("'.\n");
                    break;
                }
                if(a && !A) putString(".\n..");
                for(File file : files) {
                    putString(file.getName()).putString("\n");
                }

                break;
            }

            default: {
                putString(name).putString(": command not found\n");
                break;
            }
        }
    }

    @Override
    public void update() {
        if(hasNext()) {
            String cmd = next();
            String[] parts = cmd.split(" ");
            String[] args = new String[parts.length-1];
            System.arraycopy(parts, 1, args, 0, args.length);
            processCommand(parts[0], args);
            putString(os.name).putString("$").putString(path.getPath().replace(os.fs.root(), "")).putString(": ").renderCanvas();
        }
    }
}
