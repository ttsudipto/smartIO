package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.io.PrintWriter;
import java.io.IOException;
import java.awt.AWTException;

import device.KeyboardController;
import device.MouseController;
import security.EKEProvider;

/**
 * @author Sudipto Bhattacharjee
 * @author Sayantan Majumdar
 */

class Server {

    private ServerSocket mServerSocket;
    private BroadcastThread mBroadcastThread;
    private MouseController mMouseController;
    private KeyboardController mKeyboardController;
    private NetworkState mState;
    private EKEProvider mEKEProvider;

    private boolean mStopFlag;
    private String mPairingKey;
    private String mReceivedKey;
    private static final int TCP_PORT = 1234;

    Server(NetworkState state, BroadcastThread broadcastThread) throws IOException, AWTException {
        mState = state;
        mBroadcastThread = broadcastThread;
        mMouseController = new MouseController();
        mKeyboardController = new KeyboardController();
        mServerSocket = new ServerSocket(TCP_PORT);
        mServerSocket.setSoTimeout(200);
        mStopFlag = false;
    }
    
    private boolean isValidClient(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        mReceivedKey = mEKEProvider.decryptString(in.readLine());
        return mPairingKey.equals(mReceivedKey);
    }

    void setStopFlag() {
        mStopFlag = true;
    }

    int getTimeout() throws IOException { return mServerSocket.getSoTimeout(); }

    void listen() throws IOException,InterruptedException {
        while(!mStopFlag) {
            try {
                Socket clientSocket = mServerSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                byte[] clientPublicKey = in.readLine().getBytes();
                System.out.println(clientSocket.getInetAddress().getHostAddress() +
                        " wants to connect.");


                mPairingKey = EKEProvider.getPairingKey();
                mEKEProvider = new EKEProvider(mPairingKey, clientPublicKey);
                System.out.println("Type the following pairing key to connect your phone: " + mPairingKey);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                if(!mBroadcastThread.getBroadcastFlag()) {
                    if (isValidClient(clientSocket)) {
                        out.println(mEKEProvider.encryptString("1"));
                        System.out.println("Connected to " + clientSocket.getInetAddress().getHostAddress());
                        System.out.println("Client public key: " + new String(clientPublicKey));
                        ServerThread st = new ServerThread(mState, clientSocket, mMouseController,
                                mKeyboardController, clientPublicKey, mPairingKey);
                        Thread t = new Thread(st);
                        mState.add(clientSocket, st);
                        t.start();
                    } else {
                        if(mReceivedKey != null) {
                            System.out.println("Incorrect Pairing Key!");
                        }
                        out.println(mEKEProvider.encryptString("0"));
                        clientSocket.close();
                    }
                } else {
                    if(!clientSocket.isClosed())    clientSocket.close();
                }
            } catch (SocketTimeoutException | SocketException e) {}
        }
    }

    void close() throws IOException {
        mServerSocket.close();
    }
}