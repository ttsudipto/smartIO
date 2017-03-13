package net;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.io.IOException;
import java.awt.AWTException;

import device.KeyboardController;
import device.MouseController;

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

    private boolean mStopFlag;
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

    void setStopFlag() { mStopFlag = true; }

    int getTimeout() throws IOException { return mServerSocket.getSoTimeout(); }

    void listen() throws IOException,InterruptedException {
        while(!mStopFlag) {
            try {
                Socket clientSocket = mServerSocket.accept();

                ServerThread st = new ServerThread(mState, clientSocket, mMouseController,
                        mKeyboardController, mBroadcastThread);
                Thread t = new Thread(st);
                t.start();
            } catch (SocketTimeoutException | SocketException ignored) {}
        }
    }

    void close() throws IOException {
        mServerSocket.close();
    }
}