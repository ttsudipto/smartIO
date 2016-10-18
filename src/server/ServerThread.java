package server;

import java.io.PrintWriter;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

import mouse.MouseController;

/**
 * @author Sudipto Bhattacharjee
 * @author Sayantan Majumdar
 */

class ServerThread implements Runnable {

    private Socket mClientSocket;
    private MouseController mMouseController;
    private boolean mStopFlag;
    private NetworkState mState;

    ServerThread(NetworkState state, Socket skt, MouseController mc) throws IOException {
        mClientSocket = skt;
        mMouseController = mc;
        mStopFlag = false;
        this.mState = state;
        mClientSocket.setSoTimeout(100);
    }

    @Override
    public void run() {
        try {
            while(!mStopFlag) {
                receiveMouseData();
            }
            mState.remove(mClientSocket);
            System.out.println(mClientSocket.getInetAddress() + " is now disconnected!");
            PrintWriter printWriter = new PrintWriter(mClientSocket.getOutputStream(), true);
            printWriter.println(-1);
            mClientSocket.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void receiveMouseData() throws InterruptedException {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
            if(!in.ready()) return;
            String s = in.readLine();
            System.out.println(s);
            Scanner sc = new Scanner(s);
            if (sc.nextInt() == -1) setStopFlag();
            int x = sc.nextInt();
            int y = sc.nextInt();
            mMouseController.move(x, y);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setStopFlag() { mStopFlag = true; }

    int getTimeout() throws IOException { return mClientSocket.getSoTimeout(); }
}