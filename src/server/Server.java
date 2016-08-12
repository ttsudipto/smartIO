package server;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.awt.AWTException;
import mouse.MouseController;

public class Server
{
    private final NetworkState state;

    public Server(NetworkState state, int port_) throws IOException, AWTException
    {
        this.state = state;
        mc = new MouseController();
        port = port_;
        skt = new ServerSocket(port);
        skt.setSoTimeout(200);
        stopFlag = false;
    }
    
    public void confirm(Socket skt, boolean value) throws IOException
    {
        out = new PrintWriter(skt.getOutputStream(), true);
        if(value == true)
            out.println("1");
        else
            out.println("0");
    }

    public void setStopFlag() { stopFlag = true; }

    public int getTimeout() throws IOException { return skt.getSoTimeout(); }

    public void listen() throws IOException,InterruptedException
    {
        while(stopFlag == false)
        {
            try {
                Socket cskt = skt.accept();
                System.out.println(cskt.getInetAddress().getHostAddress() + " wants to connect. Do you agree (0/1) ?");
                Scanner sc = new Scanner(System.in);
                int option = sc.nextInt();
                if (option == 1) {
                    confirm(cskt, true);
                    System.out.println("Connected to " + cskt.getInetAddress().getHostAddress());
                    Thread t = new Thread(new ServerThread(state, cskt, mc));
                    state.add(cskt.getInetAddress());
                    t.start();
                } else {
                    confirm(cskt, false);
                    cskt.close();
                }
            } catch (SocketTimeoutException e) {continue;}
        }
    }

    public void close() throws IOException
    {
//         cskt.close();
        skt.close();
    }

    private boolean stopFlag;
    private int port; 
    private ServerSocket skt;
    private MouseController mc;
    PrintWriter out;
}