package server;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import mouse.MouseController;

public class ServerThread implements Runnable
{
    public ServerThread(NetworkState state, Socket skt, MouseController mc_)
    {
        cskt = skt;
        mc = mc_;
        stopFlag = false;
        this.state = state;
    }
    
    public void receive() throws IOException,InterruptedException
    {
        in = new BufferedReader(new InputStreamReader(cskt.getInputStream()));
        while(!in.ready());
        String s = in.readLine();
        System.out.println(s);
        Scanner sc = new Scanner(s);
        int k = sc.nextInt();
        if(k<0)
            stopFlag = true;
        else
        {
            int x = sc.nextInt();
            int y = sc.nextInt();
            mc.move(x, y);
        }
//         mc.wait();
    }
    
    public void run()
    {
        try
        {
            while(true)
            {
                receive();
                if(stopFlag)
                    break;
                //Thread.sleep(delay);
            }
            state.remove(cskt.getInetAddress());
            cskt.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception in run()");
        }
    }

    private Socket cskt;
    private MouseController mc;
    private BufferedReader in;
    private boolean stopFlag;
    private NetworkState state;
}