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
        Client c = new Client("localhost", 1234);
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
                if(k<0)
                    break;
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