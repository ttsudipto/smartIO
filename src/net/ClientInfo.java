package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static server.NetworkManager.sPublicKey;

/**
 * @author Sayantan Majumdar.
 */
public class ClientInfo {

    public static final int UDP_PORT = 1236;

    public byte[] getClientPublicKey() {
        try {
            DatagramSocket datagramSocket = new DatagramSocket(UDP_PORT);
            datagramSocket.setBroadcast(true);
            DatagramPacket datagramPacket = new DatagramPacket(new byte[sPublicKey.length], sPublicKey.length);
            datagramSocket.receive(datagramPacket);
            datagramSocket.close();
            return datagramPacket.getData();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
