package server;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.InterfaceAddress;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.Enumeration;
import java.util.List;

import static server.NetworkManager.sPublicKey;

/**
 * @author Sudipto Bhattacharjee
 * @author Sayantan Majumdar
 */

class BroadcastThread implements Runnable {

    private boolean mStopFlag;
    private static final int BROADCAST_PORT = 1235;

    void stopBroadcast() { mStopFlag = true; }
    boolean getBroadcastFlag() { return mStopFlag; }
    long getTimeout() {
        return (long) 1000; }


    @Override
    public void run() {
        mStopFlag = false;
        try {
            while(!mStopFlag) {
                broadcastData(sPublicKey);
                Thread.sleep(1000);
            }
            broadcastData("Stop".getBytes());
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
                        if(new String(data).equals("Stop")) System.out.println("Stop broadcasted");
                        datagramSocket.send(datagramPacket);
                        datagramSocket.close();
                    }
                }
            }
        }
    }
}