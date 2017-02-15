package server;

import java.awt.AWTException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Sudipto Bhattacharjee
 */
public class NetworkManager {

    private Server mServer;
    private BroadcastThread mBroadcastThread;
    private Thread mThread;
    private NetworkState mState;

    static byte[] sPublicKey;

    public NetworkManager(byte[] publicKey) {
        mState = new NetworkState();
        sPublicKey = publicKey;
        mBroadcastThread = new BroadcastThread();
        System.out.println("Server public key: " + new String(sPublicKey));
    }

    public NetworkState getNetworkState() { return mState; }

    void startServer() throws IOException, InterruptedException, AWTException {
        mThread = new Thread(mBroadcastThread);
        mServer = new Server(mState, mBroadcastThread);
        mThread.start();
        System.out.println("Broadcast started ...");
        System.out.println("Server started ...");
        mServer.listen();
    }

    public void stopServer() throws IOException, InterruptedException {
        mBroadcastThread.stopBroadcast();
        mThread = null;
        System.out.println("Broadcast stopped ...");
        
        if(mServer != null) {
            mServer.setStopFlag();
            Thread.sleep(mServer.getTimeout());
            mServer.close();
            mServer = null;

            HashMap<InetAddress, Socket> aMap = mState.getsAddressMap();
            Iterator it = aMap.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<InetAddress, Socket> entry = (Map.Entry<InetAddress, Socket>) it.next();
                disconnect(entry.getKey());
            }

            System.out.println("Server stopped ...");
        }
    }

    public void disconnect(InetAddress address) throws IOException, InterruptedException {
        ServerThread st = mState.getServerThread(address);
        try {
            st.setStopFlag();
            Thread.sleep(st.getTimeout());
            System.out.println(address.getHostAddress() + " disconnected");
        } catch (NullPointerException e) {System.out.println("No connected devices. Skipping ...");}
    }
}
