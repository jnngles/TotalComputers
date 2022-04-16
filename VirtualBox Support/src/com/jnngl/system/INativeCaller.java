package com.jnngl.system;

import java.nio.ByteBuffer;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INativeCaller extends Remote {

    public String[] getMachineNames() throws RemoteException;
    public void launchVM(String name) throws RemoteException;
    public void closeVM() throws RemoteException;
    public int getWidth() throws RemoteException;
    public int getHeight() throws RemoteException;
    public byte[] getScreen() throws RemoteException;
    public void init(String applicationPath) throws RemoteException;
    public void click(int x, int y, boolean isLeft) throws RemoteException;
    public void key(int[] scancodes) throws RemoteException;

}
