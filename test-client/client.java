import java.net.*;
import java.io.*;
import java.util.Scanner;

class Client
{
    public Client(String addr, int p) throws IOException
    {
        port = p;
        skt = new Socket(addr, p);
    }
    
    public Client(InetAddress addr, int p) throws IOException
    {
        port = p;
        skt = new Socket(addr, p);
    }
    
//     public void listen()
//     {
//         Socket cskt = skt.accept();
//         System.out.println(skt.getInetAddress());
//     }
    
    public boolean getConfirmation() throws IOException
    {
        in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
        String s = in.readLine();
        return s.equals("1");
    }
    
    public void send(int k, int x, int y) throws IOException
    {
        out = new PrintWriter(skt.getOutputStream(), true);
        String s = k + " " + x + " " + y ;
        System.out.println(s);
        out.println(s);
    }
    
    public void close() throws IOException
    {
        skt.close();
    }
    
    private int port; 
    private Socket skt;
    private PrintWriter out;
    private BufferedReader in;
}

class ClientMain
{
    public static void main(String args[]) throws IOException,InterruptedException
    {
        DatagramSocket ds = new DatagramSocket(1235);
        ds.setBroadcast(true);
        DatagramPacket p = new DatagramPacket(new byte[32],32);
        
        ds.receive(p);
//        byte[] b = p.getData();
//        String s = "";
//        for(int i=0;i<b.length;++i)
//            s = s + (char)b[i];
//        System.out.print(s);
        InetAddress addr = p.getAddress();
//        System.out.println(InetAddress.getByName(s).isLoopbackAddress());
        System.out.println(p.getAddress().toString());
//        System.out.println(InetAddress.getLocalHost().getHostAddress());
        Client c = new Client(addr, 1234);
        if(!c.getConfirmation())
        {
            System.out.println("Declined by server. Aborting !!!");
        }
        else
        {
            Scanner sc = new Scanner(System.in);
            int x=0, y=0;

            while(true)
            {
                int k = sc.nextInt();
                if(k<0) {
                    c.send(-1, 0, 0);
                    break;
                }
                else
                {
                    c.send(k, x, y);
                    x+=50;
                    y+=50;
                }
            }
        }
        c.close();
    }
}