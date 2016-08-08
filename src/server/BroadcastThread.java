package server;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.List;

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
                    NetworkInterface ni = nis.nextElement();
                    if (!ni.isLoopback()) {
                        List<InterfaceAddress> ias = ni.getInterfaceAddresses();

                        for(int i=0; i<ias.size(); ++i) {
                            InetAddress broadcastIA = ias.get(i).getBroadcast();
                            if(broadcastIA != null) {
//                                System.out.println(ia + " " + ia.isLinkLocalAddress()+ " " + ia.isAnyLocalAddress()+ " " + ia.isSiteLocalAddress());
                                DatagramSocket ds = new DatagramSocket();
                                ds.setBroadcast(true);
                                DatagramPacket p = new DatagramPacket("".getBytes(), "".getBytes().length, broadcastIA, 1235);
                                ds.send(p);
                                ds.close();
                            }
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