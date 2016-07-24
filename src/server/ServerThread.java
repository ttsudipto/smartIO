package server;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import mouse.MouseController;

public class ServerThread implements Runnable
{
    public ServerThread(Socket skt, MouseController mc_, int d)
    {
        cskt = skt;
        mc = mc_;
        delay = d;
    }
    
    public void receive() throws IOException,InterruptedException
    {
        in = new BufferedReader(new InputStreamReader(cskt.getInputStream()));
        while(!in.ready());
        String s = in.readLine();
        System.out.println(s);
        Scanner sc = new Scanner(s);
        int x = sc.nextInt();
        int y = sc.nextInt();
        mc.move(x, y);
//         mc.wait();
    }
    
    public void run()
    {
        try
        {
            for(int i=0; i<20; ++i)
            {
                receive();
                System.out.println(i);
                //Thread.sleep(delay);
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception in run()");
        }
    }
    
    private Socket cskt;
    private MouseController mc;
    private BufferedReader in;
    private int delay;
}