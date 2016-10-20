package server;

import java.io.PrintWriter;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

import device.KeyboardController;
import device.MouseController;

/**
 * @author Sudipto Bhattacharjee
 * @author Sayantan Majumdar
 */

class ServerThread implements Runnable {

    private Socket mClientSocket;
    private MouseController mMouseController;
    private KeyboardController mKeyboardController;
    private boolean mStopFlag;
    private NetworkState mState;

    ServerThread(NetworkState state, Socket skt, MouseController mc, KeyboardController kc) throws IOException {
        mClientSocket = skt;
        mMouseController = mc;
        mKeyboardController = kc;
        mStopFlag = false;
        this.mState = state;
        mClientSocket.setSoTimeout(100);
    }

    @Override
    public void run() {
        try {
            while(!mStopFlag) {
                receiveData();
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
    
    private void receiveData() throws InterruptedException {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
            if(!in.ready()) return;
            String s = in.readLine();
            Scanner sc = new Scanner(s);
            switch (sc.next()) {
                case "Stop":
                    setStopFlag();
                    break;

                case "Mouse":
                    mMouseController.move(sc.nextInt(), sc.nextInt());
                    break;

                case "Key":
                    //length of "Key" = 3 and next char is space. Hence beginIndex = 4
                    mKeyboardController.doKeyOperation(s.substring(4));
                sc.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setStopFlag() { mStopFlag = true; }

    int getTimeout() throws IOException { return mClientSocket.getSoTimeout(); }
}