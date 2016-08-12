package server;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Sudipto Bhattacharjee on 11/8/16.
 */
public class NetworkManager {
    private Server server;
    private BroadcastThread broadcastThread;
    private Thread bThread;
    private NetworkState state;

    public NetworkManager() {
        state = new NetworkState();
        broadcastThread = new BroadcastThread();
    }

    public NetworkState getNetworkState() {
        return state;
    }

    public void startServer() throws IOException, InterruptedException, AWTException {
        server = new Server(state, 1234);
        bThread = new Thread(broadcastThread);
        bThread.start();
        System.out.println("Broadcast started ...");
        System.out.println("Server started ...");
        server.listen();
    }

    public void stopServer() throws IOException, InterruptedException {
        broadcastThread.stopBroadcast();
        bThread = null;
        System.out.println("Broadcast stopped ...");
        server.setStopFlag();
        Thread.sleep(server.getTimeout());
        server.close();
        System.out.println("Server stopped ...");
    }
}
