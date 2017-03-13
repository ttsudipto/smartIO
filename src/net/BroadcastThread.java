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
 * @author Sudipto Bhattacharjee
 * @author Sayantan Majumdar
 */

class BroadcastThread implements Runnable {

    private ServerInfo mServerInfo;
    private boolean mStopFlag;
    private static final int BROADCAST_PORT = 1235;
    private final int TIMEOUT = 1000;

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
    boolean getBroadcastFlag() { return mStopFlag; }
    long getTimeout() { return TIMEOUT; }

    BroadcastThread(ServerInfo serverInfo) { mServerInfo = serverInfo; }

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