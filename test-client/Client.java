import java.net.Socket;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;

class Client {

    private int mPort;
    private Socket mSocket;
    private PrintWriter mOut;
    private BufferedReader mIn;

    public Client(String addr, int p) throws IOException {
        mPort = p;
        mSocket = new Socket(addr, p);
    }
    
    public Client(InetAddress addr, int p) throws IOException {
        mPort = p;
        mSocket = new Socket(addr, p);
    }
    
//     public void listen()
//     {
//         Socket cskt = skt.accept();
//         System.out.println(skt.getInetAddress());
//     }
    
    public boolean getConfirmation() throws IOException {
        mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        String s = mIn.readLine();
        return s.equals("1");
    }
    
    public void send(int k, int x, int y) throws IOException {
        mOut = new PrintWriter(mSocket.getOutputStream(), true);
        String s = k + " " + x + " " + y ;
        System.out.println(s);
        mOut.println(s);
    }
    
    public void close() throws IOException {
        mSocket.close();
    }
}

class ClientMain {

    public static void main(String args[]) throws IOException,InterruptedException {
        DatagramSocket datagramSocket = new DatagramSocket(1235);
        datagramSocket.setBroadcast(true);
        DatagramPacket datagramPacket = new DatagramPacket(new byte[32],32);
        
        datagramSocket.receive(datagramPacket);
//        byte[] b = p.getData();
//        String s = "";
//        for(int i=0;i<b.length;++i)
//            s = s + (char)b[i];
//        System.out.print(s);
        InetAddress addr = datagramPacket.getAddress();
//        System.out.println(InetAddress.getByName(s).isLoopbackAddress());
        System.out.println(datagramPacket.getAddress().toString());
//        System.out.println(InetAddress.getLocalHost().getHostAddress());
        Client c = new Client(addr, 1234);
        if(!c.getConfirmation()) {
            System.out.println("Declined by server. Aborting !!!");
        } else {
            Scanner sc = new Scanner(System.in);
            int x=0, y=0;

            while(true) {
                int k = sc.nextInt();
                if(k<0) {
                    c.send(-1, 0, 0);
                    break;
                } else {
                    c.send(k, x, y);
                    x+=50;
                    y+=50;
                }
            }
        }
        c.close();
    }
}