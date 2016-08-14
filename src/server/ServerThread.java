package server;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import mouse.MouseController;

public class ServerThread implements Runnable
{
    private Socket cskt;
    private MouseController mc;
    private BufferedReader in;
    private boolean stopFlag;
    private NetworkState state;

    public ServerThread(NetworkState state, Socket skt, MouseController mc_) throws IOException
    {
        cskt = skt;
        mc = mc_;
        stopFlag = false;
        this.state = state;
        cskt.setSoTimeout(100);
    }
    
    public void receive() throws InterruptedException
    {
        try {
            in = new BufferedReader(new InputStreamReader(cskt.getInputStream()));
//            while (!in.ready()) ;
            String s = in.readLine();
            System.out.println(s);
            Scanner sc = new Scanner(s);
            int k = sc.nextInt();
            if (k < 0)
                stopFlag = true;
            else {
                int x = sc.nextInt();
                int y = sc.nextInt();
                mc.move(x, y);
            }
        } catch (IOException e) {
            return;
        }
//         mc.wait();
    }

    public void setStopFlag() { stopFlag = true; }

    public int getTimeout() throws IOException { return cskt.getSoTimeout(); }
    
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
            state.remove(cskt);
            cskt.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception in run()");
        }
    }
}