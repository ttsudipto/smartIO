package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;
import java.awt.AWTException;

import mouse.MouseController;
import net.ClientInfo;
import security.EKEProvider;

/**
 * @author Sudipto Bhattacharjee
 * @author Sayantan Majumdar
 */

class Server {

    private boolean mStopFlag;
    private ServerSocket mServerSocket;
    private MouseController mMouseController;
    private NetworkState mState;
    private String mPairingKey;

    Server(NetworkState state) throws IOException, AWTException {
        this.mState = state;
        mMouseController = new MouseController();
        mServerSocket = new ServerSocket(NetworkManager.TCP_PORT);
        mServerSocket.setSoTimeout(200);
        mStopFlag = false;
    }
    
    private boolean isValidClient(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        if(!in.ready()) return false;
        String pairingKey = in.readLine();
        System.out.println("Received pairing key: " + pairingKey);
        return pairingKey != null && mPairingKey.equals(pairingKey);
    }

    void setStopFlag() { mStopFlag = true; }

    int getTimeout() throws IOException { return mServerSocket.getSoTimeout(); }

    void listen() throws IOException,InterruptedException {
        boolean keyGenerated = false;
        while(!mStopFlag) {
            try {
                if(!keyGenerated) {
                    mPairingKey = new EKEProvider().getPairingKey();
                    System.out.println("Type the following pairing key to connect your phone: " + mPairingKey);
                    keyGenerated = true;
                }
                Socket clientSocket = mServerSocket.accept();
                PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                System.out.println(clientSocket.getInetAddress().getHostAddress() +
                        " wants to connect.");
                String clientPubKey = new String(new ClientInfo().getBase64EncodedPubKey());

                if(isValidClient(clientSocket)) {
                    printWriter.println(1);
                    System.out.println("Connected to " + clientSocket.getInetAddress().getHostAddress());
                    System.out.println("Client public key: " + clientPubKey);
                    ServerThread st = new ServerThread(mState, clientSocket, mMouseController);
                    Thread t = new Thread(st);
                    mState.add(clientSocket, st);
                    t.start();
                } else {
                    System.out.println("Incorrect Pairing Key!");
                    printWriter.println(0);
                    clientSocket.close();
                }
                keyGenerated = false;
            } catch (SocketTimeoutException | SocketException e) {}
        }
    }

    void close() throws IOException {
        mServerSocket.close();
    }
}