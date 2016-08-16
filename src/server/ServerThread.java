package server;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

import mouse.MouseController;

public class ServerThread implements Runnable {

    private Socket mClientSocket;
    private MouseController mMouseController;
    private BufferedReader mIn;
    private boolean mStopFlag;
    private NetworkState mState;

    public ServerThread(NetworkState state, Socket skt, MouseController mc) throws IOException {
        mClientSocket = skt;
        mMouseController = mc;
        mStopFlag = false;
        this.mState = state;
        mClientSocket.setSoTimeout(100);
    }
    
    public void receive() throws InterruptedException {
        try {
            mIn = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
            if(!mIn.ready())    return;
            //while (!in.ready()) ;
            String s = mIn.readLine();
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

    public void setStopFlag() { mStopFlag = true; }

    public int getTimeout() throws IOException { return mClientSocket.getSoTimeout(); }
    
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