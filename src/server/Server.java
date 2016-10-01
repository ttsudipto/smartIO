package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;
import java.awt.AWTException;

import mouse.MouseController;
import net.ClientInfo;

/**
 * @author Sudipto Bhattacharjee
 */

class Server {

    private boolean mStopFlag;
    private ServerSocket mServerSocket;
    private MouseController mMouseController;
    private NetworkState mState;

    Server(NetworkState state, int port) throws IOException, AWTException {
        this.mState = state;
        mMouseController = new MouseController();
        mServerSocket = new ServerSocket(port);
        mServerSocket.setSoTimeout(200);
        mStopFlag = false;
    }
    
    private void confirm(Socket skt, boolean value) throws IOException {
        PrintWriter printWriter = new PrintWriter(skt.getOutputStream(), true);
        if(value) {
            printWriter.println("1");
        } else  printWriter.println("0");
    }

    void setStopFlag() { mStopFlag = true; }

    int getTimeout() throws IOException { return mServerSocket.getSoTimeout(); }

    void listen() throws IOException,InterruptedException {
        while(!mStopFlag) {
            try {
                Socket clientSocket = mServerSocket.accept();
                System.out.println(clientSocket.getInetAddress().getHostAddress() +
                        " wants to connect. Do you agree (0/1) ?");
                String clientPubKey = new String(new ClientInfo().getBase64EncodedPubKey());
                Scanner sc = new Scanner(System.in);
                int option = sc.nextInt();
                if (option == 1) {
                    confirm(clientSocket, true);
                    System.out.println("Connected to " + clientSocket.getInetAddress().getHostAddress());
                    System.out.println("Client public key: " + clientPubKey);
                    ServerThread st = new ServerThread(mState, clientSocket, mMouseController);
                    Thread t = new Thread(st);
                    mState.add(clientSocket, st);
                    t.start();
                } else {
                    confirm(clientSocket, false);
                    clientSocket.close();
                }
            } catch (SocketTimeoutException e) {}
        }
    }

    void close() throws IOException {
        mServerSocket.close();
    }
}