package server;

import gui.MainWindow;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;


public class NetworkManager {

    private Server server;
    private BroadcastThread broadcastThread;
    private static HashSet<InetAddress> addressSet = new HashSet<>();


    public void init() throws IOException, AWTException, InterruptedException {
        server = new Server(1234);
        broadcastThread = new BroadcastThread();
        Thread bThread = new Thread(broadcastThread);
        bThread.start();
        server.listen();
    }

    public String[] getAddresses() {
        InetAddress[] addressArray = (InetAddress[]) addressSet.toArray();
        String[] addressStringArray = new String[addressArray.length];
        for(int i=0; i<addressArray.length; ++i) {
            addressStringArray[i] =addressArray[i].getHostAddress();
        }
        return addressStringArray;
    }

    public void add(InetAddress ia) {
        addressSet.add(ia);
    }

    public void remove(InetAddress ia) {
        if(addressSet.contains(ia))
            addressSet.remove(ia);
    }
}