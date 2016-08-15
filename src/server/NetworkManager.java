package server;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

        HashMap<Socket, ServerThread> cMap = state.getConnectionMap();
        Iterator it = cMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Socket, ServerThread> entry = (Map.Entry<Socket, ServerThread>) it.next();
            ServerThread st = entry.getValue();
            st.setStopFlag();
            Thread.sleep(st.getTimeout());
        }

        System.out.println("Server stopped ...");
    }

    public void disconnect(InetAddress address) throws IOException, InterruptedException {
        ServerThread st = state.getServerThread(address);
        st.setStopFlag();
        Thread.sleep(st.getTimeout());
        System.out.println(address.getHostAddress() + " disconnected");
    }
}
