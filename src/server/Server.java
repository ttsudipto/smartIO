package server;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.awt.AWTException;
import mouse.MouseController;

public class Server
{
    public Server(int port_) throws IOException, AWTException
    {
        mc = new MouseController();
        port = port_;
        skt = new ServerSocket(port);
    }
    
    public void listen() throws IOException,InterruptedException
    {
        int delay[] = {5000, 7000};
        int i=0;
        while(true)
        {
            Socket cskt = skt.accept();
            System.out.println(cskt.getInetAddress().getHostAddress());
            Thread t = new Thread(new ServerThread(cskt, mc, i));
            i = (i+1)%2;
            t.start();
//             t.join();
//             if(Thread.currentThread().activeCount() == 0)
//                 break;
        }
    }
    
    public void close() throws IOException
    {
//         cskt.close();
        skt.close();
    }
    
    private int port; 
    private ServerSocket skt;
    private MouseController mc;
}