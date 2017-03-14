package net;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.InterfaceAddress;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.Enumeration;
import java.util.List;

/**
 * Implementation of the {@link Runnable} for the thread responsible
 * for making UDP broadcasts.
 *
 * <p>
 *     This thread continuously sends UDP broadcasts through all
 *     available network interfaces of the PC except point-to-point
 *     and loopback interface. The content of the broadcast data is
 *     a JSon-string of a {@link ServerInfo} object. The JSon string
 *     is created using the Google API -
 *     <a href="https://sites.google.com/site/gson/gson-user-guide">
 *     GSON</a>.
 * </p>
 * <p>
 *     It is started during server start by the method
 *     {@link NetworkManager#startServer()}. It is stopped during
 *     server stop by the method {@link NetworkManager#stopServer()}.
 *     The interval between two successive broadcast is {@value TIMEOUT}
 *     ms.
 * </p>
 * <p>
 *     This UDP broadcast is used for device discovery from the Android
 *     devices.
 * </p>
 *
 * @see Runnable
 * @see ServerInfo
 * @see Gson
 * @see NetworkManager#startServer()
 * @see NetworkManager#stopServer()
 */

class BroadcastThread implements Runnable {

    private ServerInfo mServerInfo;
    private boolean mStopFlag;
    private static final int BROADCAST_PORT = 1235;
    private static final int TIMEOUT = 1000;

    /**
     * Stops the broadcast. <br/>
     * Used to exit from the {@link #run()}.
     */
    void stopBroadcast() {
        mStopFlag = true;
        mServerInfo.setStopFlag();
        byte[] data = new Gson().toJson(mServerInfo).getBytes();
        new Thread(() -> {
           try {
              broadcastData(data);
           } catch (IOException ignored) {}
        }).start();
    }

    /**
     * Returns the status of the broadcast.
     *
     * @return {@code true} is broadcast is active, <br/>{@code false} otherwise.
     */
    boolean getBroadcastFlag() { return mStopFlag; }

    /**
     * @return the interval between two successive broadcasts in milliseconds.
     */
    long getTimeout() { return TIMEOUT; }

    /**
     * Constructor. <br/>
     * Initializes this thread.
     *
     * @param serverInfo the {@link ServerInfo} object containing the data to
     *                   be broadcast.
     * @see ServerInfo
     */
    BroadcastThread(ServerInfo serverInfo) { mServerInfo = serverInfo; }

    /**
     * Operations performed by this thread.
     *
     * <p>
     *     It continuously sends UDP broadcasts through all
     *     available network interfaces of the PC except point-to-point
     *     and loopback interface until {@link #stopBroadcast()} is
     *     invoked.
     * </p>
     */
    @Override
    public void run() {
        mStopFlag = false;
        try {
            byte[] data;
            while(!mStopFlag) {
                mServerInfo.clearStopFlag();
                data = new Gson().toJson(mServerInfo).getBytes();
                broadcastData(data);
                Thread.sleep(TIMEOUT);
            }
        } catch(InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcastData(byte[] data) throws IOException {
        Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        while (nis.hasMoreElements()) {
            NetworkInterface ni = nis.nextElement();
            if (!ni.isLoopback()) {
                List<InterfaceAddress> ias = ni.getInterfaceAddresses();
                for(InterfaceAddress addr: ias) {
                    InetAddress broadcastIA = addr.getBroadcast();
                    if(broadcastIA != null) {
                        DatagramSocket datagramSocket = new DatagramSocket();
                        datagramSocket.setBroadcast(true);
                        DatagramPacket datagramPacket = new DatagramPacket(data, data.length,
                                broadcastIA, BROADCAST_PORT);
                        datagramSocket.send(datagramPacket);
                        datagramSocket.close();
                    }
                }
            }
        }
    }
}