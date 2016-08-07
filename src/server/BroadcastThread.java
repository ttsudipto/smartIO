package server;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class BroadcastThread implements Runnable {

    private int broadcastPort;
    private boolean stopFlag;

    public BroadcastThread() {
        stopFlag = false;
    }

    public void stopBroadcast() {
        stopFlag = true;
    }

    @Override
    public void run() {

        try {
            while(!stopFlag) {
                broadcastPort = 1236;
                Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();

                while (nis.hasMoreElements()) {
                    Enumeration<InetAddress> ias = nis.nextElement().getInetAddresses();

                    while (ias.hasMoreElements()) {
                        InetAddress ia = ias.nextElement();
                        if (!ia.isLoopbackAddress() && ia.getAddress().length == 4) {
//                    System.out.println(ia + " " + ia.getAddress());
                            DatagramSocket ds = new DatagramSocket(broadcastPort++, ia);
                            ds.setBroadcast(true);
                            DatagramPacket p = new DatagramPacket(new byte[1], 1, InetAddress.getByName("255.255.255.255"), 1235);
                            ds.send(p);
                            ds.close();
                        }
                    }
                }
                Thread.sleep(1000);
            }
        } catch(InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}