package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;
import java.awt.AWTException;

import mouse.MouseController;

public class Server {

    private boolean mStopFlag;
    private int mPort;
    private ServerSocket mServerSocket;
    private MouseController mMouseController;
    PrintWriter mOut;
    private NetworkState mState;

    public Server(NetworkState state, int port) throws IOException, AWTException {
        this.mState = state;
        mMouseController = new MouseController();
        mPort = port;
        mServerSocket = new ServerSocket(port);
        mServerSocket.setSoTimeout(200);
        mStopFlag = false;
    }
    
    public void confirm(Socket skt, boolean value) throws IOException {
        mOut = new PrintWriter(skt.getOutputStream(), true);
        if(value == true) {
            mOut.println("1");
        } else  mOut.println("0");
    }

    public void setStopFlag() { mStopFlag = true; }

    public int getTimeout() throws IOException { return mServerSocket.getSoTimeout(); }

    public void listen() throws IOException,InterruptedException {
        while(!mStopFlag) {
            try {
                Socket clientSocket = mServerSocket.accept();
                System.out.println(clientSocket.getInetAddress().getHostAddress() +
                        " wants to connect. Do you agree (0/1) ?");
                Scanner sc = new Scanner(System.in);
                int option = sc.nextInt();
                if (option == 1) {
                    confirm(clientSocket, true);
                    System.out.println("Connected to " + clientSocket.getInetAddress().getHostAddress());
                    ServerThread st = new ServerThread(mState, clientSocket, mMouseController);
                    Thread t = new Thread(st);
                    mState.add(clientSocket, st);
                    t.start();
                } else {
                    confirm(clientSocket, false);
                    clientSocket.close();
                }
            } catch (SocketTimeoutException e) {continue;}
        }
    }

    public void close() throws IOException {
        mServerSocket.close();
    }
}