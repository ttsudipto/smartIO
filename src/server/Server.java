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
    
    public void confirm(Socket skt, boolean value) throws IOException
    {
        out = new PrintWriter(skt.getOutputStream(), true);
        if(value == true)
            out.println("1");
        else
            out.println("0");
    }
    
    public void listen() throws IOException,InterruptedException
    {
        while(true)
        {
            Socket cskt = skt.accept();
            System.out.println(cskt.getInetAddress().getHostAddress() + " wants to connect. Do you agree (0/1) ?");
            Scanner sc = new Scanner(System.in);
            int option = sc.nextInt();
            if(option == 1)
            {
                confirm(cskt, true);
                System.out.println("Connected to " + cskt.getInetAddress().getHostAddress());
                Thread t = new Thread(new ServerThread(cskt, mc));
                t.start();
            }
            else
            {
                confirm(cskt, false);
                cskt.close();
            }
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
    PrintWriter out;
}