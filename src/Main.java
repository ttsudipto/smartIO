import java.io.IOException;
import java.awt.AWTException;
import java.net.*;
import java.util.Enumeration;

import server.Server;

class Main
{ 
    public static void main(String args[]) throws IOException,AWTException,InterruptedException
    {
        Server s = new Server(1234);
//        System.out.println(InetAddress.getAllByName("sudiptol").length);
        
        int bport = 1236;
        Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        
        while(nis.hasMoreElements()) {
            Enumeration<InetAddress> ias = nis.nextElement().getInetAddresses();
            
            while(ias.hasMoreElements()) {
                InetAddress ia = ias.nextElement();
                if(!ia.isLoopbackAddress() && ia.getAddress().length == 4)
                {
//                    System.out.println(ia + " " + ia.getAddress().length);
                    DatagramSocket ds = new DatagramSocket(bport++, ia);
                    ds.setBroadcast(true);
                    DatagramPacket p = new DatagramPacket(new byte[1], 1, InetAddress.getByName("255.255.255.255"), 1235);
                    ds.send(p);
                }
            }
        }

        s.listen();

        s.close();
    }
}