package server;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.InterfaceAddress;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.Enumeration;
import java.util.List;

public class BroadcastThread implements Runnable {

    private int mBroadcastPort;
    private boolean mStopFlag;

    public void stopBroadcast() {
        mStopFlag = true;
    }

    @Override
    public void run() {
        mStopFlag = false;
        try {
            while(!mStopFlag) {
                mBroadcastPort = 1236;
                Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();

                while (nis.hasMoreElements()) {
                    NetworkInterface ni = nis.nextElement();
                    if (!ni.isLoopback()) {
                        List<InterfaceAddress> ias = ni.getInterfaceAddresses();

                        for(int i=0; i<ias.size(); ++i) {
                            InetAddress broadcastIA = ias.get(i).getBroadcast();
                            if(broadcastIA != null) {
//                                System.out.println(ia + " " + ia.isLinkLocalAddress()+ " "
// + ia.isAnyLocalAddress()+ " " + ia.isSiteLocalAddress());
                                DatagramSocket datagramSocket = new DatagramSocket();
                                datagramSocket.setBroadcast(true);
                                DatagramPacket datagramPacket = new DatagramPacket("".getBytes(), "".getBytes().length,
                                        broadcastIA, 1235);
                                datagramSocket.send(datagramPacket);
                                datagramSocket.close();
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