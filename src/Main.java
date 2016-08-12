import java.io.IOException;
import java.awt.AWTException;

import server.BroadcastThread;
import server.NetworkManager;
import server.NetworkState;
import server.Server;
import gui.*;

class Main
{ 
    public static void main(String args[]) throws IOException,AWTException,InterruptedException
    {
//        NetworkGUIAdapter adapter = new NetworkGUIAdapter(win);
//        NetworkState state = new NetworkState(/*adapter*/);

        NetworkManager manager = new NetworkManager();
        MainWindow win = new MainWindow(manager);
//        win.display();

//        Server s = new Server(state, 1234);
//
//        BroadcastThread broadcastThread = new BroadcastThread();
//        Thread bThread = new Thread(broadcastThread);
//        bThread.start();
//
//        s.listen();
//
//        broadcastThread.stopBroadcast();
//        s.close();
    }
}