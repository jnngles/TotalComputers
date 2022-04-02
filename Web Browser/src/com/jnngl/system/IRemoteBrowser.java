package com.jnngl.system;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteBrowser extends Remote {

    public String getURL() throws RemoteException;
    public String getTitle() throws RemoteException;

    public void onStart(int width, int height) throws RemoteException;
    public void close() throws RemoteException;
    public byte[] render() throws RemoteException;
    public void processInput(int x, int y, boolean isLeft) throws RemoteException;
    public void keyTyped(String key, String name) throws RemoteException;

    public void setSize(int width, int height) throws RemoteException;

    public void goBack() throws RemoteException;
    public void goForward() throws RemoteException;
    public void loadURL(String url) throws RemoteException;
    public boolean canGoBack() throws RemoteException;
    public boolean canGoForward() throws RemoteException;

}
