package server;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

import mouse.MouseController;

/**
 * @author Sudipto Bhattacharjee
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
    
    private void receive() throws InterruptedException {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
            if(!in.ready())    return;
            //while (!in.ready()) ;
            String s = in.readLine();
            System.out.println(s);
            Scanner sc = new Scanner(s);
            int k = sc.nextInt();
            if (k < 0) {
                mStopFlag = true;
            } else {
                int x = sc.nextInt();
                int y = sc.nextInt();
                mMouseController.move(x, y);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //mc.wait();
    }

    void setStopFlag() { mStopFlag = true; }

    int getTimeout() throws IOException { return mClientSocket.getSoTimeout(); }
    
    @Override
    public void run() {
        try {
            while(true) {
                receive();
                if(mStopFlag)   break;
                //Thread.sleep(delay);
            }
            mState.remove(mClientSocket);
            mClientSocket.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}