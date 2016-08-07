import java.io.IOException;
import java.awt.AWTException;
import java.net.*;
import java.util.Enumeration;

import server.BroadcastThread;
import server.Server;

class Main
{ 
    public static void main(String args[]) throws IOException,AWTException,InterruptedException
    {
        Server s = new Server(1234);

        BroadcastThread broadcastThread = new BroadcastThread();
        Thread bThread = new Thread(broadcastThread);
        bThread.start();

        s.listen();

        broadcastThread.stopBroadcast();
        s.close();
    }
}