import java.io.IOException;
import java.awt.AWTException;
import java.net.*;

import server.Server;

class Main
{ 
    public static void main(String args[]) throws IOException,AWTException,InterruptedException
    {
        Server s = new Server(1234);
//        System.out.println(InetAddress.getAllByName("sudiptol").length);

        DatagramSocket ds = new DatagramSocket();
        ds.setBroadcast(true);
//        ds.connect(InetAddress.getByName("255.255.255.255"), 1234);
        
        byte[] b = InetAddress.getLocalHost().getHostAddress().getBytes();
        DatagramPacket p = new DatagramPacket(b, b.length, InetAddress.getByName("255.255.255.255"), 1235);
        
        ds.send(p);
        
        s.listen();

        s.close();
    }
}